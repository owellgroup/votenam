package com.example.votenam.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utility class to create the database if it doesn't exist.
 * Run this as a standalone Java application before starting Spring Boot,
 * or integrate it into your deployment process.
 * 
 * Usage:
 * java -cp your-classpath com.example.votenam.util.DatabaseCreator
 */
public class DatabaseCreator {
    
    private static final String HOST = "37.27.56.188";
    private static final String PORT = "5432";
    private static final String DATABASE_NAME = "votenam";
    private static final String USERNAME = "joelchipoya";
    private static final String PASSWORD = "Kalimbwejoel";
    
    public static void main(String[] args) {
        createDatabaseIfNotExists();
    }
    
    public static void createDatabaseIfNotExists() {
        String baseUrl = "jdbc:postgresql://" + HOST + ":" + PORT;
        String defaultDbUrl = baseUrl + "/postgres";
        
        try {
            System.out.println("Connecting to PostgreSQL server...");
            System.out.println("Host: " + HOST);
            System.out.println("Port: " + PORT);
            System.out.println("Checking if database '" + DATABASE_NAME + "' exists...");
            
            try (Connection conn = DriverManager.getConnection(defaultDbUrl, USERNAME, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                // Check if database exists
                String checkDbQuery = "SELECT 1 FROM pg_database WHERE datname = '" + DATABASE_NAME + "'";
                ResultSet rs = stmt.executeQuery(checkDbQuery);
                
                if (!rs.next()) {
                    // Database doesn't exist, create it
                    System.out.println("Database '" + DATABASE_NAME + "' does not exist. Creating it...");
                    
                    // CREATE DATABASE cannot be executed in a transaction
                    conn.setAutoCommit(true);
                    String createDbQuery = "CREATE DATABASE \"" + DATABASE_NAME + "\"";
                    stmt.executeUpdate(createDbQuery);
                    System.out.println("✓ Database '" + DATABASE_NAME + "' created successfully!");
                } else {
                    System.out.println("✓ Database '" + DATABASE_NAME + "' already exists.");
                }
            }
            
            System.out.println("\nYou can now start the Spring Boot application.");
            
        } catch (Exception e) {
            System.err.println("✗ Error creating database: " + e.getMessage());
            System.err.println("\nPlease create the database manually using:");
            System.err.println("  CREATE DATABASE " + DATABASE_NAME + ";");
            System.err.println("\nOr run this command:");
            System.err.println("  psql -U " + USERNAME + " -h " + HOST + " -p " + PORT + " -d postgres -c \"CREATE DATABASE " + DATABASE_NAME + ";\"");
            System.exit(1);
        }
    }
}

