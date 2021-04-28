package com.testingAPI.backend.utils;

import io.cucumber.java.bs.A;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class XmlHelper {
    public String format(String unformattedXml){
        try{
            final Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat();
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out,format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXmlFile (String in){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw  new RuntimeException(e);
        }
    }

    public static String getElementByXpath(String expression, String xml,String... namespacesToIgnore) throws ParserConfigurationException, XPathExpressionException, SAXException, IOException {
        List<String> result = getElementsByXpath(expression,xml,namespacesToIgnore);
        if(result.size() > 0) { return result.get(0); }
        throw  new XPathExpressionException("No Such element");
    }

    public static List<String> getElementsByXpath(String expression, String xml, String... namespacesToIgnore) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        for(String namespace: namespacesToIgnore){
            xml = xml.replaceAll(namespace + ":", "");
        }

        Document doc = builder.parse((new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))));
        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList elements = (NodeList)xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
        List<String> matches = new ArrayList<>();
        for(int i = 0 ; i < elements.getLength(); i++){
            matches.add(elements.item(i).getTextContent());
        }
        return matches;
    }
}
