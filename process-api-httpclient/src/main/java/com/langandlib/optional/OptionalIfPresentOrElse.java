package com.langandlib.optional;

import com.langandlib.Book;

import java.util.Optional;

public class OptionalIfPresentOrElse {

    public static void main(String... args) {
        Optional<Book> full = Optional.of(Book.getBook());

        // Before ifPresentOrElse
        full.ifPresent(System.out::println);

        if (full.isPresent()) {
            System.out.println(full.get() + " full.isPresent()");
        } else {
            System.out.println("Nothing here full.isPresent()");
        }

        full.ifPresentOrElse(System.out::println,
                () -> System.out.println("Nothing here!"));

        Optional.empty().ifPresentOrElse(System.out::println,
                () -> System.out.println("Nothing here!"));

    }

}
