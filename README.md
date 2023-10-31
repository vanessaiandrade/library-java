# LIBRARY

I created this management library program as a way to improve my understanding and practice of Java programming and SQL.


## Database Schema

![schema](https://github.com/vanessaiandrade/library-java/assets/104696266/f12a64d3-acd9-4b06-8359-93816ce5b99e)

[View queries for creating the database](#mysql-database)

## Tech stack

- Java
- Java Swing (GUI Designer in IntelliJ IDEA)
- MySQL


## Screenshots

![books](https://github.com/vanessaiandrade/library-java/assets/104696266/9499f525-c537-449c-9ea0-816602670249)
![readers](https://github.com/vanessaiandrade/library-java/assets/104696266/920a74ca-7092-4b8e-a9bd-ee29e1434bfb)
![loans](https://github.com/vanessaiandrade/library-java/assets/104696266/2d2b9ae5-09a3-4e4c-a508-af2386118846)


## MySQL Database

```sql
CREATE DATABASE library;

USE library;

CREATE TABLE `books` (
  `books_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `amount` int(11) NOT NULL,
  `available_for_loan` int(11) DEFAULT NULL,
  PRIMARY KEY (`books_id`),
  UNIQUE KEY `books_id` (`books_id`)
);

CREATE TABLE `readers` (
  `readers_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `birth_date` date NOT NULL,
  `loan_enabled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`readers_id`),
  UNIQUE KEY `readers_id` (`readers_id`)
);

CREATE TABLE `loans` (
  `loan_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` int(11) NOT NULL,
  `reader_id` int(11) NOT NULL,
  `loan_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  PRIMARY KEY (`loan_id`),
  KEY `book_id` (`book_id`),
  KEY `reader_id` (`reader_id`),
  CONSTRAINT `fk_books_loans` FOREIGN KEY (`book_id`) REFERENCES `books` (`books_id`),
  CONSTRAINT `fk_readers_loans` FOREIGN KEY (`reader_id`) REFERENCES `readers` (`readers_id`)
);
```
