CREATE DATABASE IF NOT EXISTS siddhuai;

USE siddhuai;

-- Create a dedicated user for the application
-- We use a specific user instead of root to avoid password conflicts with your existing MySQL installation
CREATE USER IF NOT EXISTS 'siddhu_user'@'localhost' IDENTIFIED BY 'password';
-- Ensure the password is set correctly even if the user already existed
ALTER USER 'siddhu_user'@'localhost' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON siddhuai.* TO 'siddhu_user'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
