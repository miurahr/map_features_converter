import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class MapFeaturesConverter {

    public static final void main(String[] args) {

        String filename;
        String locale;
        File inputFile;
        File outputFile;

        if (args.length > 1) {
            filename = args[0];
            locale = args[1];
        } else if (args.length > 0) {
            filename = args[0];
            locale = "en_US";
        } else {
            filename = "map_features.xml";
            locale = "en_US";
        }

        try {
            inputFile = new File("data/" + filename);
            new File("out/" + locale).mkdir();
            outputFile = new File("out/" + locale + "/" +filename);

            InputStream propInputStream = new FileInputStream(new File("data/" + locale + "/map_features.properties"));
            Properties configuration = new Properties();
            configuration.load(new InputStreamReader(propInputStream, "UTF-8"));

            CustomizedXMLFilter filter = new CustomizedXMLFilter(XMLReaderFactory.createXMLReader(), configuration);
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


