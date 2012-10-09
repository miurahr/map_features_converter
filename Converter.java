import java.io.File;
import java.io.FileInputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class Converter {
    public static final void main(String[] args) {

        File inputFile = new File("map_features.xml");
        File outputFile = new File("output.xml");
        try {
            CustomizedXMLFilter filter = new CustomizedXMLFilter(XMLReaderFactory.createXMLReader());
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            Source source = new SAXSource(filter, new InputSource(new FileInputStream(inputFile)));
            Result result = new StreamResult(outputFile);
            transformer.transform(source, result);
        }
        catch (javax.xml.transform.TransformerFactoryConfigurationError e) {
            System.err.println("TransformerFactoryConfigurationError : " + e.getMessage());
        }
        catch (javax.xml.transform.TransformerConfigurationException e) {
            System.err.println("TransformerConfigurationException : " + e.getMessage());
        }
        catch (javax.xml.transform.TransformerException e) {
            System.err.println("TransformerException : " + e.getMessage());
        }
        catch (SAXException e) { 
            System.err.println("SAXException : " + e.getMessage());
        }
        catch (java.io.IOException e) { 
            System.err.println("IOException : " + e.getMessage());
        }
    }
}


