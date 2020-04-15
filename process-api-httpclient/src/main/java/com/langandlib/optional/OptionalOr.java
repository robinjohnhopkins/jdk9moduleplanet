package com.langandlib.optional;

import com.langandlib.Book;
import java.util.Optional;
import java.util.Set;

public class OptionalOr {
    public static void main(String... args) {
        Optional<Book> localFallback = Optional.of(Book.getBook());

        // Before Optional.or
        Book bestBookBefore = getBestOffer()
                .orElse(getExternalOffer().orElse(localFallback.get()));  // .get() is BAD!

        Optional<Book> bestBook =
                getBestOffer()
                .or(() -> getExternalOffer())
                .or(() -> localFallback);
        System.out.println(bestBook);
    }

    static Optional<Book> getBestOffer() {
        return Optional.empty();
    }

    static Optional<Book> getExternalOffer() {
        return Optional.of(new Book("External Book", Set.of(), 11.99));
    }
}