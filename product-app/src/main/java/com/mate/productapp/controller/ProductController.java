package com.mate.productapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getAll() {
        return List.of(new Product(1L, "Bread"), new Product(2L, "Cheese"));
    }

    @PostMapping("/products")
    public String save(@RequestBody Product product) {
        return "Products was saved, Id: %s, name: %s".formatted(product.id(), product.name());
    }
}
