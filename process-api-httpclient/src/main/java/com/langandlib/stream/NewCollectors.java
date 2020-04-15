package com.langandlib.stream;

import com.langandlib.Book;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class NewCollectors {
    public static void main(String... args) {
        Stream<Book> books = Book.getBooks();

        // Collect all books costing more than 10, grouped by author(s)
        Map<Set<String>, Set<Book>> booksByAuthors =
          books.collect(
                  groupingBy(Book::getAuthors,
                             filtering(b -> b.getPrice() > 10,      // filtering removes entries
                                       toSet())
                  )
          );
        System.out.println(booksByAuthors + " booksByAuthors example");

        books = Book.getBooks();
        // Collect all authors that sell books at a given price
        Map<Double, Set<String>> authorsSellingForPrice =
          books.collect(
                  groupingBy(Book::getPrice,
                             flatMapping(b -> b.getAuthors().stream(),
                                         toSet())
                  )
          );
        System.out.println(authorsSellingForPrice + " authorsSellingForPrice example");
    }
}
