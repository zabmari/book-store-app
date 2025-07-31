
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
                                                                                             (10, 'First test book', 'First test author', '978-0-123456-47-10', 39.90, 'first test description', 'img_test1.jpg', 0),
                                                                                             (11, 'Second test book', 'Second test author', '978-0-123456-47-11', 29.90, 'second test description', 'img_test2.jpg', 0),
                                                                                             (12, 'Third test book', 'Third test author', '978-0-123456-47-12', 19.90, 'third test description', 'img_test3.jpg', 0);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address) VALUES
    (10, 'anna.kowalska@example.com', '$2a$10$xCH3VTUPwI8pj/NFAaKbxOMbM3r6rbjSnzh3CzazIUs9Gi5WqoVgq', 'Test', 'User', 'ul. Testowa 99, Warszawa, Polska');

INSERT INTO roles (id, name) VALUES
    (10, 'USER');

INSERT INTO user_roles (user_id, role_id) VALUES
    (10, 10);

INSERT INTO shopping_carts (id, user_id) VALUES
    (10, 10);

INSERT INTO cart_items (id, cart_id, book_id, quantity) VALUES
                                                            (10, 10, 10, 2),
                                                            (11, 10, 11, 1);