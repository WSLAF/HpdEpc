package org.example;

import java.io.IOException;
import java.io.PrintWriter;

public class FileWriter {

    private PrintWriter writer;

    public FileWriter(String fileName) throws IOException {
        this(fileName, false);
    }

    public FileWriter(String fileName, boolean append) throws IOException {
        this.writer = new PrintWriter(new java.io.FileWriter(fileName, append));
    }

    public void println(String content){
        this.writer.println(content);
    }

    public void print(String content){
        this.writer.print(content);
    }

    public void close(){
        this.writer.close();
    }
}

