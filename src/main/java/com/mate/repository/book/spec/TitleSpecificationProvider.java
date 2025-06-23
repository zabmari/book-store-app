package com.mate.repository.book.spec;

import com.mate.model.Book;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder) -> root.get("title").in(Arrays.asList(params));
    }
}
