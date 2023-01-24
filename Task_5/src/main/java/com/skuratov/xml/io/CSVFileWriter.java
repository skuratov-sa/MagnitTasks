package com.skuratov.xml.io;

import com.opencsv.CSVWriter;
import com.skuratov.xml.model.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileWriter {

    public static void write(String filename, String[] headers, List<Product> products) {
        String[] headersCopy =  Arrays.stream(headers).skip(1).toArray(String[]::new);
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            writer.writeNext(headersCopy);
            writer.writeAll(parseProduct(products));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static List<String[]> parseProduct(List<Product> products) {
        List<String[]> list = new ArrayList<>();
        for (Product product : products) {
            list.add(product.toString().split(","));
        }
        return list;
    }
}
