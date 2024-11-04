package org.example;

import java.io.IOException;
import java.util.List;

public class TestFileUtil {
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("data.txt", true);
        writer.println("hello");
        writer.println("world");
        writer.close();

        FileReader reader = new FileReader("data.txt");
        String content = reader.readAll();
        System.out.print(content);
        // hello
        // world

        List<String> list = reader.readLines();
        System.out.println(list);
        // [hello, world]

    }
}

