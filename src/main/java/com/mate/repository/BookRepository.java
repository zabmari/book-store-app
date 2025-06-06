package com.mate.repository;

import com.mate.model.Book;

import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
