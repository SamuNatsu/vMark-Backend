/*
vMark Database Setup Script For MySQL
Version: 0.0.1

*/

# Clean up
DROP DATABASE IF EXISTS vmark;

# Create database
CREATE DATABASE vmark;
USE vmark;

# ===== User table =====
# Create table
CREATE TABLE users(
    uid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `account` VARCHAR(255) NOT NULL,
    `name` VARCHAR(255),
    `password` VARCHAR(255) NOT NULL,
    privilege TINYINT NOT NULL DEFAULT 0 CHECK ( privilege = 0 OR privilege = 1 )
);
# Add administrator account, default password is "admin"
INSERT INTO users(`account`, `name`, `password`, privilege)
VALUES ("administrator", "Admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918", 1);

# ===== Item table =====
# Create item table
CREATE TABLE items(
    iid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    sale FLOAT,
    `description` MEDIUMTEXT,
    remain INT NOT NULL DEFAULT 0
);
# Create item picture table
CREATE TABLE item_pics(
    ipid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    iid INT NOT NULL,
    `data` MEDIUMBLOB
);

# ===== Order table =====
# Create order table
CREATE TABLE orders(
    oid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uid INT NOT NULL,
    `timestamp` TIMESTAMP NOT NULL
);
# Create order item table
CREATE TABLE order_items(
    oid INT NOT NULL,
    iid INT NOT NULL,
    price INT NOT NULL,
    sale INT NOT NULL,
    `count` INT NOT NULL
);

# ===== Option table =====
# Create option table
CREATE TABLE options(
    `name` VARCHAR(255) PRIMARY KEY NOT NULL,
    `data` MEDIUMBLOB
);
# Insert options
INSERT INTO options(`name`) VALUES ("skin");
