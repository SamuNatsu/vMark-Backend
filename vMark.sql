/*

vMark Database Setup Script For MySQL
Version: 0.0.2

*/

# Clean up
DROP DATABASE IF EXISTS vmark;

# Create database
CREATE DATABASE vmark;
USE vmark;

# ===== User table =====
# Create table
CREATE TABLE users(
    # User ID
    uid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,

    # Account (6 <= length <= 30, composed of alphas, digits and underlines, unique)
    `account` CHAR(30) UNIQUE NOT NULL,

    # Name (leng <= 30)
    `name` CHAR(30),

    # Password (SHA256 string)
    `password` CHAR(64) NOT NULL,

    # Privilege (0 -> user, 1 -> admin, 2 -> super admin)
    privilege TINYINT NOT NULL DEFAULT 0 CHECK (privilege IN (0, 1, 2))
);

# Add super admin account, password is "super_admin", encrypted as SHA256
INSERT INTO users(`account`, `password`, privilege)
VALUES (
           'super_admin',
           '35b1e72c51ac17b1cfc8d79e2b24fd22bd5797e4c8461e7e8561818eec28715d',
           2
       );

# Add test account, password is "test", encrypted as SHA256
INSERT INTO users(`account`, `password`)
VALUES (
           'test',
           '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08'
       );

# ===== Item table =====
# Create item table
CREATE TABLE items(
    iid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    picture INT,
    price FLOAT NOT NULL,
    sale FLOAT,
    `description` MEDIUMTEXT,
    remain INT NOT NULL DEFAULT 0
);
# Add test item
INSERT INTO items(`name`, price)
VALUES (
    'Test item',
    0.00
);

# ===== Attachment table =====


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
INSERT INTO options(`name`) VALUES ('skin');
