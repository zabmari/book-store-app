package com.mate.repository.book;

import com.mate.model.Book;
import com.mate.repository.SpecificationProvider;
import com.mate.repository.SpecificationProviderManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final Map<String, SpecificationProvider<Book>> providerMap;

    @Autowired
    public BookSpecificationProviderManager(
            List<SpecificationProvider<Book>> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        SpecificationProvider::getKey,
                        Function.identity()
                ));
    }

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return Optional.ofNullable(providerMap.get(key))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No SpecificationProvider found for key " + key));
    }
}
