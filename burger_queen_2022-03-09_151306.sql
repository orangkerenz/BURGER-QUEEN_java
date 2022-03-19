CREATE TABLE users(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('waiter', 'chef', 'manager', 'customer') NOT NULL,

    PRIMARY KEY(id)
);

CREATE TABLE tables(
    tables_num INT NOT NULL,
    tables_capacity INT NOT NULL,
    avaliable TINYINT NOT NULL,
    PRIMARY KEY(tables_num)
);

CREATE TABLE menus(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    price DECIMAL(19,4),
    avaliable TINYINT NOT NULL DEFAULT 1,

    PRIMARY KEY(id)
);

CREATE TABLE ingredients(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    quantity_in_grams DECIMAL(10,4),

    PRIMARY KEY(id)
);

CREATE TABLE orders(
    id INT NOT NULL AUTO_INCREMENT,
    order_date TIMESTAMP NOT NULL,
    paid TINYINT NOT NULL,
    users_id INT,
    customers_id INT NOT NULL,
    tables_num INT NOT NULL,
    canceled TINYINT NOT NULL,
    served TINYINT NOT NULL DEFAULT 0,
    completed TINYINT NOT NULL DEFAULT 0,
    total_price DECIMAL(19,4),

    PRIMARY KEY(id),
    FOREIGN KEY (tables_num) REFERENCES tables(tables_num),
    FOREIGN KEY (users_id) REFERENCES users(id)
);

CREATE TABLE recipes(
    menus_id INT NOT NULL,
    ingredients_id INT NOT NULL,
    quantity_in_grams DECIMAL(10,4),

    PRIMARY KEY(menus_id, ingredients_id),
    FOREIGN KEY (menus_id) REFERENCES menus(id),
    FOREIGN KEY (ingredients_id) REFERENCES ingredients(id)
);

CREATE TABLE menus_has_order(
    menus_id INT NOT NULL,
    orders_id INT NOT NULL,
    quantity INT NOT NULL,

    PRIMARY KEY(menus_id, orders_id),
    FOREIGN KEY (menus_id) REFERENCES menus(id),
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);

CREATE TABLE transactions(
    id INT NOT NULL AUTO_INCREMENT,
    transaction_date TIMESTAMP NOT NULL NOT NULL,
    price  DECIMAL(19,4) NOT NULL,
    orders_id INT,
    type ENUM('debit', 'kredit'),

    PRIMARY KEY(id),
    FOREIGN KEY (orders_id) REFERENCES orders(id)
);

CREATE TABLE transactions_has_ingredients(
    transactions_id INT NOT NULL,
    ingredients_id INT NOT NULL,
    quantity_in_grams DECIMAL(10,4) NOT NULL,

    PRIMARY KEY(transactions_id, ingredients_id),
    FOREIGN KEY (transactions_id) REFERENCES transactions(id),
    FOREIGN KEY (ingredients_id) REFERENCES ingredients(id)
);



INSERT INTO tables (tables_num, tables_capacity, avaliable) VALUES(1,2,1),(2,4,1),(3,6,1),(4,2,1),(5,4,1),(6,7,1),(7,2,1),(8,2,1);

INSERT INTO menus (name, price) VALUES('Cheese Burger', 35.00), ('Egg Burger', 50.00), ('Fried Burger', 15.00);

INSERT INTO users  (username, password, role) VALUES ('customer1', '123', 'customer'), ('zian', '123', 'waiter'), ('rafael', '123', 'chef'), ('eja', '123', 'manager');

INSERT INTO ingredients (name, quantity_in_grams) VALUES('Bread',1000), ('Cheese',1500), ('Egg',2000), ('Meat',10000), ('Lettuce',1000), ('Tomato',500);

INSERT INTO recipes (menus_id, ingredients_id, quantity_in_grams) VALUES(1, 1, 100), (1, 2, 100), (1, 3, 100), (1, 4, 100), (1, 5, 100), (1, 6, 100), (2, 1, 100), (2, 2, 100), (2, 3, 100), (2, 4, 100), (2, 5, 100), (2, 6, 100), (3, 1, 100), (3, 2, 100), (3, 3, 100), (3, 4, 100), (3, 5, 100), (3, 6, 100);
