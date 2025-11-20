-- Database Initialization Script for Namibia Voting System
-- Run this script in PostgreSQL to create the database if it doesn't exist

-- Connect to PostgreSQL (you can run this from psql or pgAdmin)
-- psql -U joelchipoya -h 37.27.56.188 -p 5432 -d postgres

-- Create database if it doesn't exist
SELECT 'CREATE DATABASE votenam'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'votenam')\gexec

-- Note: After creating the database, the Spring Boot application will automatically
-- create all tables using JPA/Hibernate with the ddl-auto=update setting

