package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private String fileName;

    public FileReader(String fileName) {
        this.fileName = fileName;
    }

    public List<String> readLines() throws FileNotFoundException {

        List<String> list = new ArrayList<>();

        Scanner scanner = new Scanner(new java.io.FileReader(this.fileName));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            list.add(line);
        }
        scanner.close();

        return list;
    }

    public String readAll() throws IOException {

        StringBuilder sb = new StringBuilder();

        java.io.FileReader reader = new java.io.FileReader(this.fileName);

        int len = 0;
        char[] data = new char[1024];
        while ((len = reader.read(data)) != -1) {
            sb.append(data, 0, len);
        }

        reader.close();

        return sb.toString();
    }
}

