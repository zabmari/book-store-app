package com.mate.service;

import com.mate.model.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
