package pl.norton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import groovy.util.NodeList;
import org.apache.commons.cli.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import pl.norton.parser.CurrencyRate;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import static pl.norton.parser.EuroRatesParser.*;


public class Main {
    // WCZYTAC PLIK
    // sparsować do listy obiektow CurrencyRate
    // przejśc po liscie CurrencyRate i wyprintować wartosci v * rate (zrobić obiekt ktory to trzyma
    // i uzyć stream api) (lista.stream().map(mapuj).collect(Collectors.toList);
    // wyprintować na ekran System.outem
    // TESTY JUNIT

    public static void main(String[] args) {

        Options options = getOptions();
        CommandLine line = null;
        List<CurrencyRate> currRateList = new ArrayList<>();
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        String spec = "resources/eurofxref-daily.xml";

        CommandLineParser parser = new DefaultParser();
        try {
            line = parser.parse(options, args);
            System.out.println(line.getOptionValue("value"));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            String xPathString = CUBE_NODE;
            XPathExpression expr = xpath.compile(xPathString);
            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                NamedNodeMap attribs = node.getAttributes();
                if (attribs.getLength() > 0) {
                    Node currencyAttrib = attribs.getNamedItem(CURRENCY);
                    if (currencyAttrib != null) {
                        String currencyTxt = currencyAttrib.getNodeValue();
                        String rateTxt = attribs.getNamedItem(RATE).getNodeValue();
                        currRateList.add(new CurrencyRate(currencyTxt, rateTxt));
                    }
                }
            }
        } catch (ParseException | XPathExpressionException ex) {
            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            System.exit(1);
            // WYPISZ HELP
        }
    }


    private static Options getOptions() {
        Options options = new Options();
        options.addOption("v", "value", true, "Amount of Euros to convert");
        options.addOption("c", "currency", true, "Currency to parse to");
        return options;
    }

}
