package com.mate.dto;

public record BookSearchParameters(String[] titles, String[] authors,
                                   String[] isbn, String[] categories) {
}
