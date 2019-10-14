public class SupermarketMain {
    public static void main(String[] args) {

        // load the simulation configuration with open and closing times
        // and products and customers
        Supermarket supermarket =
                Supermarket.importFromXML("jambi250_8.xml");
        supermarket.printCustomerStatistics();

        // configure the cashiers for the base scenario
        supermarket.getCashiers().clear();
        // TODO Add a FIFO cashiers
        //  (uncomment these lines once the FIFOCashier class has been implemented)
        //supermarket.getCashiers().add(new FIFOCashier("FIFO"));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();

        // configure the cashiers for the priority scenario
        supermarket.getCashiers().clear();
        // TODO Add a PRIO cashier respecting 5 priority items
        //  (uncomment these lines once the PriorityCashier class has been implemented)
        //supermarket.getCashiers().add(new PriorityCashier("PRIO",5));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();

        // configure the cashiers for the self-service scenario
        supermarket.getCashiers().clear();
        // TODO Add 1 FIFO cashiers and 1 PRIO cashier
        //  (uncomment these lines once the FIFO- and PriorityCashier class have been implemented)
        //supermarket.getCashiers().add(new FIFOCashier("FIFO"));
        //supermarket.getCashiers().add(new FIFOCashier("PRIO"));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();
    }
}
