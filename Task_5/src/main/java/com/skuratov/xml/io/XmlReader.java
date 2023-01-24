package com.skuratov.xml.io;

import com.skuratov.xml.model.Product;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlReader {

    @SneakyThrows
    public static List<Product> readXMlFile(String filename, String[] headers) {
        Document document = getDocument(filename);
        Map<String, String> mapFields = getMapKeysByHeaders(headers);
        NodeList list = getNodesByExpression("/articles/article", document);

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            var product = getProduct(mapFields, list.item(i).getChildNodes());
            products.add(product);
        }
        return products;
    }

    private static HashMap<String, String> getMapKeysByHeaders(String[] headers) {
        return new HashMap<>() {
            {
                put(headers[0], "");
                put(headers[1], "");
                put(headers[2], "");
                put(headers[3], "");
            }
        };
    }

    @SneakyThrows
    private static Document getDocument(String filename) {
        return DocumentBuilderFactory.newInstance().
                newDocumentBuilder().parse(new File(filename));
    }

    @SneakyThrows
    private static NodeList getNodesByExpression(String expression, Document document) {
        XPath path = XPathFactory.newInstance().newXPath();
        return (NodeList) path.compile(expression).evaluate(document, XPathConstants.NODESET);
    }

    private static Product getProduct(Map<String, String> mapFields, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item instanceof Element) {
                mapFields.replace(item.getNodeName(), item.getTextContent());
            }
        }
        return parceProduct(mapFields);
    }

    private static Product parceProduct(Map<String, String> fields) {
        return new Product(
                fields.get("id_art"),
                fields.get("name"),
                fields.get("code"),
                fields.get("guid")
        );
    }
}
