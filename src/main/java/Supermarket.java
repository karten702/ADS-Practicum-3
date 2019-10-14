/**
 * Supermarket Customer check-out and Cashier simulation
 * @author  hbo-ict@hva.nl
 */
import utils.SLF4J;
import utils.XMLParser;
import utils.XMLWriter;
import javax.xml.stream.XMLStreamConstants;
import java.time.LocalTime;
import java.util.*;

public class Supermarket {
    public String name;                 // name of the case for reporting purposes
    private Set<Product> products;      // a set of products that is being sold in the supermarket
    private List<Customer> customers;   // a list of customers that have visited the supermarket
    private List<Cashier> cashiers;     // the cashiers which have been configured to handle the customers

    private LocalTime openTime;         // start time of the simulation
    private LocalTime closingTime;      // end time of the simulation

    public Supermarket(String name, LocalTime openTime, LocalTime closingTime) {
        this.name = name;
        this.setOpenTime(openTime);
        this.setClosingTime(closingTime);
        this.cashiers = new ArrayList<>();
        // TODO create empty data structures for products and customers
    }

    public int getTotalNumberOfItems() {
        int totalItems = 0;

        // TODO: calculate the total number of shopped items

        return totalItems;
    }

    /**
     * report statistics of the input data and results of the simulation
     */
    public void printCustomerStatistics() {
        System.out.printf("\nCustomer Statistics of '%s' between %s and %s\n",
                this.name, this.openTime, this.closingTime);
        if (this.customers == null || this.products == null ||
                this.customers.size() == 0 || this.products.size() == 0) {
            System.out.println("No products or customers have been set up...");
            return;
        }

        System.out.printf("%d customers have shopped %d items out of %d different products\n",
                this.customers.size(), this.getTotalNumberOfItems(), this.products.size());

        System.out.printf("Revenues and most bought product per zip-code:");
        Map<String, Double> revenues = this.revenueByZipCode();
        Map<String, Product> populars = this.mostBoughtProductByZipCode();

        double totalRevenue = 0.0;
        // TODO: display the calculated revenues and most bought products.
        // TODO: calculate the total revenue.

        System.out.printf("\nTotal Revenue=%.2f\n", totalRevenue);
    }

    /**
     * reports results of the cashier simulation
     */
    public void printSimulationResults() {

        System.out.printf("\nSimulation scenario results:\n");
        System.out.printf("Cashiers:     n-customers:  avg-wait-time: max-wait-time: max-queue-length: avg-check-out-time: idle-time:\n");
        // TODO: report simulation results per cashier:
        //  a) number of customers
        //  b) average waiting time per customer
        //  c) maximum waiting time by any customer at the given cashier
        //  d) maximum queue length of waiting customers including the customer being served
        //  e) average check-out time of customers at the given cashier
        //  f) total idle time of the cashier
        //     (a self-service area is idle already if at least one terminal is idle)


        // TODO: report the same overall simulation results across all cashiers
        //  as customer weighted averages or sums of totals.

    }

    /**
     * calculates a map of aggregated revenues per zip code that is also ordered by zip code
     * @return
     */
    public Map<String, Double> revenueByZipCode() {
        Map<String, Double> revenues = null;

        // TODO create an appropriate data structure for the revenues
        //  and calculate its contents

        return revenues;
    }

    /**
     * (DIFFICULT!!!)
     * calculates a map of most bought products per zip code that is also ordered by zip code
     * if multiple products have the same maximum count, just pick one.
     * @return
     */
    public Map<String, Product> mostBoughtProductByZipCode() {
        Map<String, Product> mostBought = null;

        // TODO create an appropriate data structure for the mostBought
        //  and calculate its contents

        return mostBought;
    }

    /**
     * simulate the cashiers while handling all customers that enter their queues
     */
    public void simulateCashiers() {
        Queue<Customer> shoppingQueue = null;

        // TODO: create an appropriate data structure for the shoppingQueue
        //  and add all customers in the supermarket

        // all cashiers restart at open time
        for (Cashier c : this.cashiers) {
            c.reStart(this.openTime);
        }

        // poll the customers from the queue one by one
        // and redirect them to the cashier of their choice

        // TODO: get the first customer from the shoppingQueue;
        Customer nextCustomer = null;

        while (nextCustomer != null) {

            // let all cashiers finish up their work before the given arrival time of the customer
            for (Cashier c : this.cashiers) {
                c.doTheWorkUntil(nextCustomer.getQueuedAt());
            }
            // ask the customer about his preferred cashier for the check-out
            Cashier selectedCashier = nextCustomer.selectCashier(this.cashiers);
            // redirect the customer to the selected cashier
            selectedCashier.add(nextCustomer);

            // TODO: next customer is arriving, get the next customer from the shoppingQueue
        }

        // all customers have been handled;
        // cashiers finish their work until closing time + some overtime
        final int overtime = 15*60;
        for (Cashier c : this.cashiers) {
            c.doTheWorkUntil(this.closingTime.plusSeconds(overtime));
            // remove the overtime from the current time and the idle time of the cashier
            c.setCurrentTime(c.getCurrentTime().minusSeconds(overtime));
            c.setTotalIdleTime(c.getTotalIdleTime()-overtime);
        }
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    /**
     * Loads a complete supermarket configuration from an XML file
     * @param resourceName  the XML file name to be found in the resources folder
     * @return
     */
    public static Supermarket importFromXML(String resourceName) {
        XMLParser xmlParser = new XMLParser(resourceName);

        try {
            xmlParser.nextTag();
            xmlParser.require(XMLStreamConstants.START_ELEMENT, null, "supermarket");
            LocalTime openTime = LocalTime.parse(xmlParser.getAttributeValue(null, "openTime"));
            LocalTime closingTime = LocalTime.parse(xmlParser.getAttributeValue(null, "closingTime"));
            xmlParser.nextTag();

            Supermarket supermarket = new Supermarket(resourceName, openTime, closingTime);

            Product.importProductsFromXML(xmlParser, supermarket.products);
            Customer.importCustomersFromXML(xmlParser, supermarket.customers, supermarket.products);

            return supermarket;

        } catch (Exception ex) {
            SLF4J.logException("XML error in '" + resourceName + "'", ex);
        }

        return null;
    }

    /**
     * Exports the supermarket configuration to an xml configuration file
     * that can be shared and read in by a main
     * @param resourceName
     */
    public void exportXML(String resourceName) {
        XMLWriter xmlWriter = new XMLWriter(resourceName);

        try {
            xmlWriter.writeStartDocument();
            xmlWriter.writeStartElement("supermarket");
            xmlWriter.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlWriter.writeAttribute("\n\txsi:noNamespaceSchemaLocation", "supermarket.xsd");
            xmlWriter.writeAttribute("\n\topenTime", this.openTime.toString().concat(":00").substring(0, 8));
            xmlWriter.writeAttribute("closingTime", this.closingTime.toString().concat(":00").substring(0, 8));
            if (this.products instanceof Collection && this.products.size() > 0) {
                xmlWriter.writeStartElement("products");
                for (Product p : this.products) {
                    p.exportToXML(xmlWriter);
                }
                xmlWriter.writeEndElement();
            }
            if (this.products instanceof Collection && this.customers.size() > 0) {
                xmlWriter.writeStartElement("customers");
                for (Customer c : this.customers) {
                    c.exportToXML(xmlWriter);
                }
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndDocument();
        } catch (Exception ex) {
            SLF4J.logException("XML writing error in '" + resourceName + "'", ex);
        }

        // update the name of the supermarket
        this.name = resourceName;
    }

    /**
     * adds a collection of random customers to the configuration with a random number of items
     * between 1 and 4 * averageNrItems.
     * the distribution ensures that on average each customer buys averageNrItems
     * arrival times are chosen well in advance of closingTime of the supermarket,
     * such that cashiers can be expected to be able to finish all work
     * (unless an extreme workload has been configured)
     * @param nCustomers
     * @param averageNrItems
     */
    public void addRandomCustomers(int nCustomers, int averageNrItems) {
        if (!(this.products instanceof Collection) ||
                !(this.customers instanceof Collection)
        )   return;

        // copy the product to an array for easy random selection
        Product[] prods = new Product[this.products.size()];
        prods = this.products.toArray(prods);

        // compute an arrival interval range of at least 60 seconds that ends one minute before closing time if possible
        int maxArrivalSeconds = Math.max(60, closingTime.toSecondOfDay() - openTime.toSecondOfDay() - 60);

        for (int i = 0; i < nCustomers; i++) {
            // create a random customer with random arrival time and zip code
            Customer c = new Customer(
                    this.openTime.plusSeconds(randomizer.nextInt(maxArrivalSeconds)),
                    generateRandomZIPCode());

            // select a random number of bought items
            int remainingNumberOfItems = selectRandomNrItems(averageNrItems);

            // build a random distribution of these items across available products
            int upper = prods.length;
            while (remainingNumberOfItems > 0) {
                int count = 1 + randomizer.nextInt(remainingNumberOfItems);
                // pick a random product that has not been used yet by this customer
                int pIdx = randomizer.nextInt(upper);
                Purchase pu = new Purchase(prods[pIdx], count);
                c.getItems().add(pu);
                // System.out.println(c.toString() + pu.toString());
                remainingNumberOfItems -= count;
                // move the product out of the range of available products for this customer
                upper--;
                Product pt = prods[upper];
                prods[upper] = prods[pIdx];
                prods[pIdx] = pt;
            }

            this.customers.add(c);
        }
    }

    private static Random randomizer = new Random();

    private static int selectRandomNrItems(int averageNrItems) {
        return 1 + (int) ((4 * averageNrItems - 1) * randomizer.nextDouble() * randomizer.nextDouble());
    }

    private static String generateRandomZIPCode() {
        int randomDigit = randomizer.nextInt(5);
        int randomChar1 = randomizer.nextInt(2);
        int randomChar2 = randomizer.nextInt(2);
        return String.valueOf(1013 + randomDigit) +
                (char) (randomDigit + 9 * randomChar1 + randomChar2 + 'A') +
                (char) (randomDigit + 3 * randomChar1 + 7 * randomChar2 + 'D');
    }
}
