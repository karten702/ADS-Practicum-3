/**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */

import utils.XMLParser;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class Product {
    private String code;            // a unique product code; identical codes designate identical products
    private String description;     // the product description, useful for reporting
    private double price;           // the product's price

    public Product(String code, String description, double price) {
        this.code = code;
        this.description = description;
        this.price = price;
    }

    // TODO implement relevant overrides and/or local classes to be able to
    //  print Products and/or use them in sets, maps and/or priority queues.

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    /**
     * read a series of products from the xml stream
     * and add them to the provided products list
     * @param xmlParser
     * @param products
     * @return
     * @throws XMLStreamException
     */
    public static Set<Product> importProductsFromXML(XMLParser xmlParser, Set<Product> products) throws XMLStreamException {
        if (xmlParser.nextBeginTag("products")) {
            xmlParser.nextTag();
            if (products != null) {
                Product product;
                while ((product = importFromXML(xmlParser)) != null) {
                    products.add(product);
                }
            }

            xmlParser.findAndAcceptEndTag("products");
            return products;
        }
        return null;
    }

    /**
     * read a single product from the xml stream
     * @param xmlParser
     * @return
     * @throws XMLStreamException
     */
    public static Product importFromXML(XMLParser xmlParser) throws XMLStreamException {
        if (xmlParser.nextBeginTag("product")) {
            String code = xmlParser.getAttributeValue(null, "code");
            String description = xmlParser.getAttributeValue(null, "description");
            double price = xmlParser.getDoubleAttributeValue(null, "price", 0);

            Product product = new Product(code, description, price);

            xmlParser.findAndAcceptEndTag("product");
            return product;
        }
        return null;
    }

    /**
     * write a single product to the xml stream
     * @param xmlWriter
     * @throws XMLStreamException
     */
    public void exportToXML(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("product");
        xmlWriter.writeAttribute("code", this.code);
        xmlWriter.writeAttribute("description", this.description);
        xmlWriter.writeAttribute("price", String.format(Locale.US, "%.2f", this.price));
        xmlWriter.writeEndElement();
    }
}
