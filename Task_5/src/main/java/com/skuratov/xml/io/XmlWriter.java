package com.skuratov.xml.io;

import com.skuratov.xml.constants.TypeViewXml;
import com.skuratov.xml.model.Product;
import lombok.SneakyThrows;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.util.List;

public class XmlWriter {

    /**
     * Method creates xml file
     *
     * @param version     - version of this file
     * @param localName   - Local name for header
     * @param products    - List of products to fill out the file
     * @param typeViewXml - data display
     *                    DATA_ATTRIBUTES as <tag param = "value">
     *                    DATA_TAGS - as <tag>value</tag>)
     */
    @SneakyThrows
    public static void createXML(String filename,
                                 String version,
                                 String localName,
                                 List<Product> products,
                                 String[] headers,
                                 TypeViewXml typeViewXml) {
        XMLStreamWriter writer = getXmlStreamWriter(filename);

        writer.writeStartDocument(version);
        writer.writeStartElement(localName);

        switch (typeViewXml) {
            case DATA_ATTRIBUTES -> products.forEach(
                    product -> createItemXMLWithAttributes(writer, headers, product)
            );
            case DATA_TAGS -> products.forEach(
                    product -> createItemXmlByTags(writer, headers, product)
            );
        }

        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
    }

    @SneakyThrows
    private static void createItemXMLWithAttributes(XMLStreamWriter writer,
                                                    String[] headers,
                                                    Product product) {
        writer.writeStartElement(headers[0]);
        writer.writeAttribute(headers[1], product.idArt());
        writer.writeAttribute(headers[2], product.name());
        writer.writeAttribute(headers[3], product.code());
        writer.writeAttribute(headers[4], product.guid());
        writer.writeEndElement();
    }

    @SneakyThrows
    private static void createItemXmlByTags(XMLStreamWriter writer,
                                            String[] headers,
                                            Product product) {
        writer.writeStartElement(headers[0]);
        addFieldItemByTags(writer, headers[1], product.idArt());
        addFieldItemByTags(writer, headers[2], product.name());
        addFieldItemByTags(writer, headers[3], product.code());
        addFieldItemByTags(writer, headers[4], product.guid());
        writer.writeEndElement();

    }

    @SneakyThrows
    private static void addFieldItemByTags(XMLStreamWriter writer,
                                           String tag,
                                           String value) {
        writer.writeStartElement(tag);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }


    @SneakyThrows
    private static XMLStreamWriter getXmlStreamWriter(String filename) {
        return XMLOutputFactory.newInstance().
                createXMLStreamWriter(new FileWriter(filename));
    }
}
