INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
                                                                                             (1, 'First book', 'First author', '978-0-123456-47-1', 39.90, 'first description', 'img1.jpg', 0),
                                                                                             (2, 'Second book', 'Second author', '978-0-123456-47-2', 29.90, 'second description', 'img2.jpg', 0),
                                                                                             (3, 'Third book', 'Third author', '978-0-123456-47-3', 49.90, 'third description', 'img3.jpg', 0);
INSERT INTO users (id, email, password, first_name, last_name, shipping_address) VALUES
                                                                                     (1, 'anna.kowalska@example.com', '$2a$10$xCH3VTUPwI8pj/NFAaKbxOMbM3r6rbjSnzh3CzazIUs9Gi5WqoVgq', 'Anna', 'Kowalska', 'ul. Zielona 15, Warszawa, Polska');

INSERT INTO roles (id, name) VALUES (1, 'USER');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);

INSERT INTO cart_items (id, cart_id, book_id, quantity) VALUES
                                                            (1, 1, 1, 2),
                                                            (2, 1, 2, 1);

