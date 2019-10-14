public class SupermarketSmall {
    public static void main(String[] args) {

        // load the simulation configuration with open and closing times
        // and a small set of 5 products and customers
        Supermarket supermarket =
                Supermarket.importFromXML("jambi5.xml");
        supermarket.printCustomerStatistics();

        // configure the cashiers for a test scenario
        supermarket.getCashiers().clear();
        // TODO Add 2 FIFO cashiers
        //  (uncomment these lines once the FIFOCashier class has been implemented)
        //supermarket.getCashiers().add(new FIFOCashier("FIFO-1"));
        //supermarket.getCashiers().add(new FIFOCashier("FIFO-2"));

        // simulate the configuration and print the result
        supermarket.simulateCashiers();
        supermarket.printSimulationResults();
    }
}
