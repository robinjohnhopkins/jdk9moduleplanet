package com.langandlib.stream;

import com.langandlib.Book;
import java.util.stream.Stream;

public class OfNullable {

    public static void main(String... args ) {

        long zero = Stream.ofNullable(null).count();
        long one = Stream.ofNullable(Book.getBook()).count();

        System.out.printf("zero: %d, one: %d", zero, one);

        // Before ofNullable
        Book book = getPossiblyNull(true);
        Stream<String> authors =
                book == null ? Stream.empty() : book.authors.stream();
        authors.forEach(System.out::println);

        System.out.println("ofNullable 1");
        // With ofNullable
        Stream.ofNullable(getPossiblyNull(false))
                .flatMap(b -> b.authors.stream())
                .forEach(System.out::println);
        System.out.println("ofNullable 2");
        Stream.ofNullable(getPossiblyNull(true))
                .flatMap(b -> b.authors.stream())
                .forEach(System.out::println);
    }

    private static Book getPossiblyNull(boolean isNull) {
        return isNull ? null : Book.getBook();
    }


}
