package com.langandlib.stream;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class FindGitConflict {

    public static void main(String... args) {

        try {
            // Files.lines -> Stream<String>
            Files.lines(Paths.get("./process-api-httpclient/src/main/resources/index.html"))
                    .dropWhile(l -> !l.contains("<<<<<<<"))
                    .skip(1)
                    .takeWhile(l -> !l.contains(">>>>>>>"))
                    .forEach(System.out::println);
        } catch (Exception ex){
            System.out.println("Exception " + ex.getMessage() + " workspace/jdk9moduleplanet is expected run location - it's only a test!");
        }
    }
}
