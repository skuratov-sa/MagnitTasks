package com.skuratov.xml;

import com.skuratov.xml.io.CSVFileWriter;
import com.skuratov.xml.io.XmlReader;
import com.skuratov.xml.io.XmlWriter;
import com.skuratov.xml.model.Product;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static com.skuratov.xml.constants.TypeViewXml.DATA_ATTRIBUTES;
import static com.skuratov.xml.constants.TypeViewXml.DATA_TAGS;

public class App {

    private final static List<Product> productsInit;

    private final static String[] headers;

    static {
        headers = new String[]{"article", "id_art", "name", "code", "guid"};

        productsInit = new ArrayList<>() {{
            add(new Product("1", "Морковь", "32498534", "FSF23dweds322e34"));
            add(new Product("2", "Морковь", "54498534", "FSF23dweds322e34"));
            add(new Product("3", "Селедка", "32698534", "FSF23dweds322e34"));
            add(new Product("4", "Икра", "32498534", "FSF23dweds322e34"));
            add(new Product("5", "Творог", "32498534", "FSF23dweds322e34"));
            add(new Product("6", "Рыба", "32498534", "FSF23dweds322e34"));
            add(new Product("7", "Свекла", "32498534", "FSF23dweds322e34"));
            add(new Product("8", "Морковь", "32498534", "FSF23dweds322e34"));
        }};
    }

    @SneakyThrows
    public static void main(String[] args) {
        //Заполнить xml файл данными в виде <tag param="value"></tag>
        XmlWriter.createXML(
                "FileAttributes.xml",
                "1.0",
                "articles",
                productsInit,
                headers,
                DATA_ATTRIBUTES
        );

        //Заполнить xml файл данными в виде <tag>value</tag>
        XmlWriter.createXML(
                "FileTags.xml",
                "1.0",
                "articles",
                productsInit,
                headers,
                DATA_TAGS
        );

        //Читает xml файл
        List<Product> productsReading = XmlReader.readXMlFile("FileTags.xml", headers);
        CSVFileWriter.write("FileTags.csv", headers, productsReading);
    }

}
