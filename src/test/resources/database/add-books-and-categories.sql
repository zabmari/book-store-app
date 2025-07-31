INSERT INTO categories (id, name, description, is_deleted) VALUES
                           (10, 'first' , 'first category', 0 ),
                           (11, 'second', 'second category', 0 );
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted) VALUES
                      (10, 'First book', 'First author', '978-0-123456-47-1', 39.90, 'first description', 'img1.jpg', 0 ),
                      (12, 'Second book', 'Second author', '978-0-123456-47-2', 29.90, 'second description', 'img2.jpg', 0 ),
                      (13, 'Third book', 'Third author', '978-0-123456-47-3', 19.90, 'third description', 'img3.jpg', 0 );
iNSERT INTO books_categories(book_id, category_id) VALUES
                                 (10,10),
                                 (12,11),
                                 (13, 10);

