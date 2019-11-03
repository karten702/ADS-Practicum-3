public class SupermarketMain {
    public static void main(String[] args) {

        // load the simulation configuration with open and closing times
        // and products and customers
        Supermarket supermarket =
                Supermarket.importFromXML("jambi250_8.xml");
        supermarket.printCustomerStatistics();

        // configure the cashiers for the base scenario
        supermarket.getCashiers().clear();
        supermarket.getCashiers().add(new FIFOCashier("FIFO"));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();

        // configure the cashiers for the priority scenario
        supermarket.getCashiers().clear();
        supermarket.getCashiers().add(new PriorityCashier("PRIO",5));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();

        // configure the cashiers for the self-service scenario
        supermarket.getCashiers().clear();
        supermarket.getCashiers().add(new FIFOCashier("FIFO"));
        supermarket.getCashiers().add(new FIFOCashier("PRIO"));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();
    }
}
