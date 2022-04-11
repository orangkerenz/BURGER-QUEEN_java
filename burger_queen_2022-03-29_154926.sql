CREATE DATABASE burger_queen;

use burger_queen;

-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: burger_queen
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `quantity_in_grams` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` decimal(19,4) DEFAULT NULL,
  `available` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `menus_has_orders`
--

DROP TABLE IF EXISTS `menus_has_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus_has_orders` (
  `menu_id` int NOT NULL,
  `order_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`menu_id`,`order_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `menus_has_orders_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`),
  CONSTRAINT `menus_has_orders_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_date` timestamp NOT NULL,
  `paid` tinyint NOT NULL,
  `waiter_id` int DEFAULT NULL,
  `customer_id` int NOT NULL,
  `table_number` int NOT NULL,
  `total_price` decimal(19,4) DEFAULT NULL,
  `status` enum('pending','ready','served','completed','canceled') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `table_number` (`table_number`),
  KEY `customer_id` (`customer_id`),
  KEY `waiter_id` (`waiter_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`table_number`) REFERENCES `tables` (`table_number`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`waiter_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipes` (
  `menu_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity_in_grams` decimal(10,4) DEFAULT NULL,
  PRIMARY KEY (`menu_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`),
  CONSTRAINT `recipes_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tables`
--

DROP TABLE IF EXISTS `tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tables` (
  `table_number` int NOT NULL,
  `table_capacity` int NOT NULL,
  `available` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`table_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `transaction_date` timestamp NOT NULL,
  `price` decimal(19,4) NOT NULL,
  `order_id` int DEFAULT NULL,
  `type` enum('debit','kredit') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactions_has_ingredients`
--

DROP TABLE IF EXISTS `transactions_has_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions_has_ingredients` (
  `transaction_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity_in_grams` decimal(10,4) NOT NULL,
  PRIMARY KEY (`transaction_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `transactions_has_ingredients_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`id`),
  CONSTRAINT `transactions_has_ingredients_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` enum('waiter','chef','manager','customer') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-29 15:49:28


INSERT INTO users (username, password, role) VALUES ('manager', '123', 'manager'), ('chef', '123' , 'chef'),  ('waiter', '123' , 'waiter'), ('customer', '123' , 'customer'), ('customer1', '123' , 'customer'),  ('customer2', '123' , 'customer');

INSERT INTO ingredients (name, quantity_in_grams) VALUES('Buns', 0), ('Lettuce' , 0), ('Mayonnaise', 0), ('Tomato', 0), ('Egg', 0), ('Ketchup', 0), ('Beef', 0), ('Chicken', 0);

INSERT INTO menus(name, price, available) VALUES ('Cheese Burger', '15', 1), ('Teriyaki Burger', '15', 1), ('Chilli Burger', '15', 1), ('Double Cheese Burger', '25', 1), ('Bacon Burger', '25', 0); 

INSERT INTO recipes (menu_id, ingredient_id, quantity_in_grams) VALUES (1, 1, 100), (1,2, 100), (1,3,100), (1,4,100),
                                (2, 1, 100), (2,5, 100), (2,6,100), (2,7,100),
                                (3, 1, 100), (3,4, 100), (3,8,100), (3,7,100),
                                (4, 1, 100), (4,2, 100), (4,8,100), (4,3,100),
                                (5, 1, 100), (5,4, 100), (5,8,100), (5,7,100);

INSERT INTO tables(table_number, table_capacity, available) VALUES(1,2,1),(2,2,1),(3,2,1),(4,2,1),(5,2,1),(6,2,1),(7,2,1),(8,2,1),(9,2,1),(10,2,1);


UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 1;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(1,1,10000);



UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 2;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(2,2,10000);
  



UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 3;


INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(3,3,10000);

UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 4;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(4,4,10000);

UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 5;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(5,5,10000);

UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 6;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(6,6,10000);

UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 7;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(7,7,10000);

UPDATE ingredients SET quantity_in_grams = 10000 WHERE id = 8;

INSERT INTO transactions (transaction_date, price, type) VALUES ('2021-04-19 15:49:28', 30, 'kredit');

INSERT INTO transactions_has_ingredients(transaction_id, ingredient_id, quantity_in_grams) VALUES(8,8,10000);


INSERT INTO orders(order_date, paid, waiter_id, customer_id, table_number, total_price, status) 
VALUES ('2021-04-20 15:49:28', 1, 3, 4, 1, 30, 'completed');

INSERT INTO menus_has_orders(menu_id, order_id, quantity) VALUES(1,1, 1), (2,1, 1);

INSERT INTO transactions (transaction_date, price, order_id, type) VALUES ('2021-04-20 15:49:28', 30, 1, 'debit');


INSERT INTO orders(order_date, paid, waiter_id, customer_id, table_number, total_price, status) 
VALUES ('2021-04-20 15:49:28', 0, 3, 4, 1, 30, 'pending');

INSERT INTO menus_has_orders(menu_id, order_id, quantity) VALUES(3,2, 1), (4,2, 1);




