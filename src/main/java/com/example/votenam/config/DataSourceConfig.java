package com.example.votenam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        // First, ensure the database exists
        ensureDatabaseExists();
        
        // Then create and return the DataSource
        return DataSourceBuilder.create()
                .url(datasourceUrl)
                .username(username)
                .password(password)
                .build();
    }
    
    private void ensureDatabaseExists() {
        try {
            // Extract database name from URL
            String dbName = extractDatabaseName(datasourceUrl);
            String baseUrl = datasourceUrl.substring(0, datasourceUrl.lastIndexOf("/"));
            
            // Connect to default 'postgres' database
            String defaultDbUrl = baseUrl + "/postgres";
            
            System.out.println("Checking if database '" + dbName + "' exists...");
            
            try (Connection conn = DriverManager.getConnection(defaultDbUrl, username, password);
                 Statement stmt = conn.createStatement()) {
                
                // Check if database exists
                String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
                ResultSet rs = stmt.executeQuery(checkDbQuery);
                
                if (!rs.next()) {
                    // Database doesn't exist, create it
                    System.out.println("Database '" + dbName + "' does not exist. Creating it...");
                    conn.setAutoCommit(true);
                    String createDbQuery = "CREATE DATABASE \"" + dbName + "\"";
                    stmt.executeUpdate(createDbQuery);
                    System.out.println("✓ Database '" + dbName + "' created successfully!");
                } else {
                    System.out.println("✓ Database '" + dbName + "' already exists.");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠ Warning: Could not automatically create database: " + e.getMessage());
            System.err.println("Please ensure the database exists before starting the application.");
            // Don't throw - let the DataSource creation fail with a clearer error if database truly doesn't exist
        }
    }
    
    private String extractDatabaseName(String url) {
        int lastSlash = url.lastIndexOf("/");
        if (lastSlash != -1 && lastSlash < url.length() - 1) {
            String dbPart = url.substring(lastSlash + 1);
            int questionMark = dbPart.indexOf("?");
            if (questionMark != -1) {
                dbPart = dbPart.substring(0, questionMark);
            }
            return dbPart;
        }
        return "votenam";
    }
}

