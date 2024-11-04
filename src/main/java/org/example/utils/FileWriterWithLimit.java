package org.example.utils;


import cn.hutool.json.JSONUtil;
import org.elasticsearch.search.aggregations.metrics.ParsedSingleValueNumericMetricsAggregation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterWithLimit {

    private static final Long MAX_FILE_SIZE = 10737418240L; // 10G
    private static int fileCount = 1;
    private static BufferedWriter writer;
    private static File currentFile;

//    public static void main(String[] args) {
//        try {
//            initializeNewFile();
//
//            // 示例用法: 写入大量数据到文件中
//            for (int i = 0; i < 10000000; i++) { // 假设写入大量数据
//                writeToFile("这是第 " + i + " 行数据\n");
//            }
//
//            closeWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void initializeNewFile(String catalogCode) throws IOException {
        currentFile = new File("E:\\hpdepc\\output_" + catalogCode + "_" + fileCount + ".txt");
        writer = new BufferedWriter(new FileWriter(currentFile), 32 * 1024);
    }

//    public static void initializeNewFile(String catalogCode) throws IOException {
//        currentFile = new File("E:\\hpdepc\\output_" + catalogCode + "_" + fileCount + ".txt");
//        writer = new BufferedWriter(new FileWriter(currentFile));
//    }

    public static void writeToFile(String data, String catalogCode) throws IOException {
        // 检查当前文件的大小，如果加上新数据超过最大大小，则创建新文件
//        System.out.println("mmmm" + MAX_FILE_SIZE);
        if (currentFile.length() + data.getBytes().length > MAX_FILE_SIZE) {
//            System.out.println("mmm");
            closeWriter();
            fileCount++;
            initializeNewFile(catalogCode);
        }
        writer.write(data);
    }

    public static void closeWriter() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}

