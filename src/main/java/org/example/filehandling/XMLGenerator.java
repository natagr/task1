package org.example.filehandling;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Map;

/**
 * This class is designed for generating XML documents from statistical data.
 */
public class XMLGenerator {

    /**
     * Generates an XML document containing statistics for a specific attribute.
     *
     * @param statistics A Map containing the statistical data to be included in the XML. The keys represent attribute values, and the values represent their frequencies.
     * @param attribute The name of the attribute these statistics are for. This is used to name the resulting XML file.
     *
     * The generated XML document has a root element named "statistics" and contains child elements for each entry in the statistics map.
     */
    public static void generateXML(Map<String, Integer> statistics, String attribute) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element rootElement = doc.createElement("statistics");
        doc.appendChild(rootElement);

        statistics.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> {
                    Element item = doc.createElement("item");
                    rootElement.appendChild(item);

                    Element value = doc.createElement("value");
                    value.appendChild(doc.createTextNode(e.getKey()));
                    item.appendChild(value);

                    Element count = doc.createElement("count");
                    count.appendChild(doc.createTextNode(e.getValue().toString()));
                    item.appendChild(count);
                });

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("statistics_by_" + attribute + ".xml"));

        transformer.transform(source, result);
    }
}
