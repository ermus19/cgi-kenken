
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Akiyoshi
 */
public class ValidadorXML {

    private final static String SCHEMANAME = "/kenkenSchema.xsd";  //RUTA, CAMBIAR POR FILE HARDCODED
    private static String xmlName;
    private static String schemaPath;
    private static String xmlPath;

    ValidadorXML(String fileName, String currentPath) {
        schemaPath = currentPath + SCHEMANAME;
        xmlName = "/" + fileName;
        xmlPath = currentPath + xmlName;
    }

    public int parseTam() {
        try {
            File xml = new File(xmlPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList root = doc.getElementsByTagName("kenken");

            Node rootElem = root.item(0);
            Element eElement = (Element) rootElem;

            return Integer.valueOf(eElement.getAttribute("filas"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int validate() throws IOException {

        Source schemaFile = new StreamSource(new File(schemaPath));
        Source xmlFile = new StreamSource(new File(xmlPath));
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(schemaFile);
        } catch (SAXException ex) {
            System.err.printf(ex.toString());
        }
        Validator validator = schema.newValidator();
        try {

            validator.validate(xmlFile);
            return 0;

            //System.out.println(xmlFile.getSystemId() + " is valid");
        } catch (SAXException e) {
            return 1;
            //System.out.println(xmlFile.getSystemId() + " is NOT valid");
            //System.out.println("Reason: " + e.getLocalizedMessage());
        }

    }

}
