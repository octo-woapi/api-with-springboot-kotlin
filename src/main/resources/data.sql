-- Creation des données "products"
INSERT INTO products (id, name, price, weight) values (1, 'Cement bag 50kg', 25.0, 50.0);
INSERT INTO products (id, name, price, weight) values (2, 'Cement bag 25kg', 15.0, 25.0);
INSERT INTO products (id, name, price, weight) values (3, 'Red bricks small pallet 250 units', 149.99, 500.0);
INSERT INTO products (id, name, price, weight) values (4, 'Concrete reinforcing rod 1 unit', 0.50, 0.80);
INSERT INTO products (id, name, price, weight) values (5, 'Concrete reinforcing rod 25 units', 12.0, 20.0);
INSERT INTO products (id, name, price, weight) values (6, 'Concrete blockwork 1 unit', 1.50, 19.0);
INSERT INTO products (id, name, price, weight) values (7, 'Concrete blockwork small pallet 45 units', 45.0, 855.0);
INSERT INTO products (id, name, price, weight) values (8, 'Screw box 25mm 100 units', 5.0, 1.0);
INSERT INTO products (id, name, price, weight) values (9, 'Screw box 25mm 200 units', 10.0, 2.0);
INSERT INTO products (id, name, price, weight) values (10, 'Screw box 75mm 50 units', 15.0, 2.5);
INSERT INTO products (id, name, price, weight) values (11, 'Screw box 75mm 100 units', 30.0, 5.0);
INSERT INTO products (id, name, price, weight) values (12, 'Screw box 100mm 50 units', 25.0, 3.0);

-- Creation des données "orders"
INSERT INTO orders (id, status) values (1, 'PENDING');
INSERT INTO orders (id, status) values (2, 'PAID');

-- Creation des données "order_content"
INSERT INTO orders_content (order_id, product_id) values (1, 1);
INSERT INTO orders_content (order_id, product_id) values (2, 2);