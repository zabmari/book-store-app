package com.mate.dto.book;

public record BookSearchParameters(String[] titles, String[] authors,
                                   String[] isbn, String[] categories) {
}
