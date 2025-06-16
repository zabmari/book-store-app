package com.mate.repository.book;

import com.mate.dto.BookSearchParameters;
import com.mate.model.Book;
import com.mate.repository.SpecificationBuilder;
import com.mate.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = null;

        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            Specification<Book> authorSpec = specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(bookSearchParameters.authors());
            specification = authorSpec;
        }

        if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
            Specification<Book> titleSpec = specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(bookSearchParameters.titles());
            specification = (specification == null) ? titleSpec : specification.and(titleSpec);
        }

        if (bookSearchParameters.isbn() != null && bookSearchParameters.isbn().length > 0) {
            Specification<Book> isbnSpec = specificationProviderManager
                    .getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParameters.isbn());
            specification = (specification == null) ? isbnSpec : specification.and(isbnSpec);
        }

        if (bookSearchParameters.categories() != null
                && bookSearchParameters.categories().length > 0) {
            Specification<Book> categoriesSpec = specificationProviderManager
                    .getSpecificationProvider("categories")
                    .getSpecification(bookSearchParameters.categories());
            specification = (specification == null) ? categoriesSpec
                    : specification.and(categoriesSpec);
        }

        return specification;
    }
}
