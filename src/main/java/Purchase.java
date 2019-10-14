/**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */

import utils.XMLParser;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Set;

/**
 * represents a purchase of a product made by a customer
 * it registers the number of items of the same product being bought
 */
public class Purchase {
    private Product product;        // the product that the customer has bought
    private int amount;             // the amount of items that the customer has bought of the given product

    public Purchase(Product p, int n) {
        this.product = p;
        this.amount = n;
    }

    // TODO implement relevant overrides and/or local classes to be able to
    //  print Purchases and/or use them in sets, maps and/or priority queues.

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * read a single purchase from the xml stream
     * @param xmlParser
     * @return
     * @throws XMLStreamException
     */
    public static Purchase importFromXML(XMLParser xmlParser, Set<Product> products) throws XMLStreamException {
        if (xmlParser.nextBeginTag("purchase")) {
            String productCode = xmlParser.getAttributeValue(null, "product");
            int numItems = xmlParser.getIntegerAttributeValue(null, "amount", 0);
            xmlParser.findAndAcceptEndTag("purchase");

            if (products != null) {
                for (Product product : products) {
                    if (productCode.equals(product.getCode())) {
                        return new Purchase(product, numItems);
                    }
                }
            }
        }
        return null;
    }

    /**
     * write a single purchase to the xml stream
     * @param xmlWriter
     * @throws XMLStreamException
     */
    public void exportToXML(XMLStreamWriter xmlWriter) throws XMLStreamException {
        xmlWriter.writeStartElement("purchase");
        xmlWriter.writeAttribute("product", this.product.getCode());
        xmlWriter.writeAttribute("amount", String.valueOf(this.amount));
        xmlWriter.writeEndElement();
    }
}
