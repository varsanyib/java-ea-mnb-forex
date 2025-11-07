package hu.gamf.springlectureproject.tools;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CustomXMLParser {
    public static Document parse(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(xml.getBytes()));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Hiba történt az XML feldolgozása közben! További információ: " + e.getMessage());
            return null;
        }

    }
}
