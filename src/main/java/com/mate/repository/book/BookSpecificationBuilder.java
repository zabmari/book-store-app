package com.mate.repository.book;

import com.mate.dto.book.BookSearchParameters;
import com.mate.model.Book;
import com.mate.repository.book.spec.SpecificationBuilder;
import com.mate.repository.book.spec.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {

        Specification<Book> spec = Specification.where(null);

        spec = addToSpec(spec, "title", bookSearchParameters.titles());
        spec = addToSpec(spec, "author", bookSearchParameters.authors());
        spec = addToSpec(spec, "isbn", bookSearchParameters.isbn());
        spec = addToSpec(spec, "category", bookSearchParameters.categories());

        return spec;
    }

    private Specification<Book> addToSpec(Specification<Book> spec, String key, String[] values) {
        if (values != null && values.length > 0) {
            Specification<Book> part = specificationProviderManager
                    .getSpecificationProvider(key)
                    .getSpecification(values);
            spec = spec.and(part);
        }
        return spec;
    }
}
