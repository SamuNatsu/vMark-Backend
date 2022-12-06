/*

vMark Database Setup Script For MySQL
Version: 0.0.2

*/

# Clean up
DROP DATABASE IF EXISTS vmark;

# Create database
CREATE DATABASE vmark;
USE vmark;


# ===== Attachment table =====
# Create attachment table
CREATE TABLE attachments(
    # Attachment ID
    aid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,

    # Attachment name
    `name` VARCHAR(255) NOT NULL,

    # Attachment path
    `path` VARCHAR(255) NOT NULL,

    # Attachment timestamp
    `timestamp` LONG NOT NULL
);


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
           'test_0',
           '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08'
       );


# ===== Category table =====
# Create category table
CREATE TABLE categories(
    # Category ID
    cid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,

    # Category name (length <= 10)
    `name` CHAR(10) NOT NULL,

    # Category parent
    parent INT
);

# Add test categories
INSERT INTO categories(`name`)
VALUES ('Test');

INSERT INTO categories(`name`, parent)
VALUES ('Sub Test', 1);


# ===== Item table =====
# Create item table
CREATE TABLE items(
    # Item ID
    iid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,

    # Item name
    `name` VARCHAR(255) NOT NULL,

    # Item category ID
    cid INT NOT NULL,

    # Item price (in cents)
    price INT NOT NULL,

    # Item remains
    remain INT NOT NULL DEFAULT 0,

    # Item showcase attachment ID
    aid INT,

    # Item sale price (in cents)
    sale INT,

    # Item description (HTML)
    `description` MEDIUMTEXT
);

# Add test item
INSERT INTO items(`name`, cid, price, sale, `description`)
VALUES ('Test item', 2, 0, 0, 'Test item description');

# ===== Order table =====
# Create order table
CREATE TABLE orders(
    oid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uid INT NOT NULL,
    `timestamp` LONG NOT NULL
);
# Create order item table
CREATE TABLE order_items(
    oid INT NOT NULL,
    iid INT NOT NULL,
    price INT NOT NULL,
    `count` INT NOT NULL,
    PRIMARY KEY (oid, iid)
);

# ===== Option table =====
# Create option table
CREATE TABLE options(
    `name` VARCHAR(255) PRIMARY KEY NOT NULL,
    `data` MEDIUMBLOB
);
# Insert options
INSERT INTO options(`name`) VALUES ('skin');
