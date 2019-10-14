import java.time.LocalTime;

public class SupermarketGenerate {
    public static void main(String[] args) {

        // load a base configuration with products only
        Supermarket supermarket =
                Supermarket.importFromXML("supermarket25.xml");

        // set the time window for the simulation dataset
        supermarket.setOpenTime(LocalTime.NOON);
        supermarket.setClosingTime(LocalTime.NOON.plusMinutes(30));

        // generate random visiting customers that shop on average 10 items
        supermarket.addRandomCustomers(50, 8);

        // export the configuration to XML
        supermarket.exportXML("src/main/resources/jambi50_7.xml");

        // print customer purchase statistics
        supermarket.printCustomerStatistics();
    }
}
