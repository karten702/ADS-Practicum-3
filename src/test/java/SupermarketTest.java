import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class SupermarketTest {

    private Supermarket supermarket1;
    private Supermarket supermarket2;
    private Supermarket supermarket5;

    private Cashier fifoCashier1 = null;
    private Cashier fifoCashier2 = null;
    private Cashier priorityCashier = null;

    @BeforeEach
    void setup() {
        supermarket1 = Supermarket.importFromXML("jambi1.xml");
        supermarket2 = Supermarket.importFromXML("jambi2.xml");
        supermarket5 = Supermarket.importFromXML("jambi5.xml");

        try {
            this.fifoCashier1 = (Cashier) Class.forName("FIFOCashier")
                    .getConstructor(new Class[]{String.class}).newInstance("FIFO-1");
            this.fifoCashier2 = (Cashier) Class.forName("FIFOCashier")
                    .getConstructor(new Class[]{String.class}).newInstance("FIFO-2");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            // class has not been implemented correctly
        }
        try {
            this.priorityCashier = (Cashier) Class.forName("PriorityCashier")
                    .getConstructor(new Class[]{String.class, int.class}).newInstance("PRIO-1", 5);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            // class has not been implemented correctly
        }
    }

    @Test
    void t041_customersAndProductsAreLoadedFromXML() {
        t041_customersAndProductsAreLoadedFromXML(this.supermarket1, 5, 1, 10);
        t041_customersAndProductsAreLoadedFromXML(this.supermarket2, 5, 2, 3);
        t041_customersAndProductsAreLoadedFromXML(this.supermarket5, 5, 6, 25);
    }
    private void t041_customersAndProductsAreLoadedFromXML(Supermarket supermarket,
            int nProducts, int nCustomers, int nItems) {
        assertEquals(nProducts, supermarket.getProducts().size());
        assertEquals(nCustomers, supermarket.getCustomers().size());
        assertEquals(nItems, supermarket.getTotalNumberOfItems());
    }

    @Test
    void t042_revenueByZipCodeIsCorrect() {
        assertEquals(supermarket1.revenueByZipCode().keySet(), Set.of("1015MF"));
        assertEquals(supermarket2.revenueByZipCode().keySet(), Set.of("1013MF"));
        assertThat(supermarket5.revenueByZipCode().keySet().toString(), is(Set.of("1014DA, 1015DK, 1015DP, 1016DK").toString()));
        assertThat(supermarket1.revenueByZipCode().get("1015MF"), is(closeTo(33.85,0.0001)));
        assertThat(supermarket2.revenueByZipCode().get("1013MF"), is(closeTo(5.25,0.0001)));
        assertThat(supermarket5.revenueByZipCode().get("1014DA"), is(closeTo(56.79,0.0001)));
        assertThat(supermarket5.revenueByZipCode().get("1015DK"), is(closeTo(38.10,0.0001)));
        assertThat(supermarket5.revenueByZipCode().get("1015DP"), is(closeTo(7.85,0.0001)));
        assertThat(supermarket5.revenueByZipCode().get("1016DK"), is(closeTo(0.0,0.0001)));
    }

    @Test
    void t042_mostBoughtProduceByZipCodeIsCorrect() {
        assertThat(supermarket1.mostBoughtProductByZipCode().get("1015MF").toString(), is("Verse scharreleieren 4 stuks"));
        assertThat(supermarket2.mostBoughtProductByZipCode().get("1013MF").toString(), is("Croissant"));
        assertThat(supermarket5.mostBoughtProductByZipCode().get("1014DA").toString(), is("Verse scharreleieren 4 stuks"));
        assertThat(supermarket5.mostBoughtProductByZipCode().get("1015DK").toString(), is("Calve Pindakaas 650g"));
        assertThat(supermarket5.mostBoughtProductByZipCode().get("1015DP").toString(), is("Croissant"));
        assertNull(supermarket5.mostBoughtProductByZipCode().get("1016DK"));
    }

    @Test
    void t061_oneFIFOCashierSimulation() {
        t060_oneCashierSimulation(this.supermarket1, this.fifoCashier1, 260, 1);
        t060_oneCashierSimulation(this.supermarket2, this.fifoCashier1, 254, 2);
        t060_oneCashierSimulation(this.supermarket5, this.fifoCashier1, 150, 4);
    }

    @Test
    void t062_twoFIFOCashierSimulation() {
        t060_twoCashierSimulation(this.supermarket1, this.fifoCashier1, this.fifoCashier2, 560, 260, 300, 0, 1);
        t060_twoCashierSimulation(this.supermarket2, this.fifoCashier1, this.fifoCashier2, 554, 276, 278, 1, 1);
        t060_twoCashierSimulation(this.supermarket5, this.fifoCashier1, this.fifoCashier2, 450, 202, 248, 2, 2);
    }

    @Test
    void t111_onePriorityCashierSimulation() {
        t060_oneCashierSimulation(this.supermarket1, this.priorityCashier, 260, 1);
        t060_oneCashierSimulation(this.supermarket2, this.priorityCashier, 254, 2);
        t060_oneCashierSimulation(this.supermarket5, this.priorityCashier, 150, 4);
    }

    @Test
    void t112_fifoPlusPriorityCashierSimulation() {
        t060_twoCashierSimulation(this.supermarket1, this.fifoCashier1, this.priorityCashier, 560, 260, 300, 0, 1);
        t060_twoCashierSimulation(this.supermarket2, this.fifoCashier1, this.priorityCashier, 554, 276, 278, 1, 1);
        t060_twoCashierSimulation(this.supermarket5, this.fifoCashier1, this.priorityCashier, 450, 202, 248, 2, 2);
    }

    private void t060_oneCashierSimulation(Supermarket supermarket, Cashier cashier,
                                               int totalIdleTime, int maxQueueLength) {
        if (cashier == null) return;
        supermarket.getCashiers().clear();
        supermarket.getCashiers().add(cashier);
        supermarket.simulateCashiers();
        assertEquals(totalIdleTime, cashier.getTotalIdleTime());
        assertEquals(maxQueueLength, cashier.getMaxQueueLength());
    }
    private void t060_twoCashierSimulation(Supermarket supermarket, Cashier cashier1, Cashier cashier2,
                  int idleTimeSum, int idleTimeLow, int idleTimeHigh, int maxQueueLengthLow, int maxQueueLengthHigh) {
        if (cashier1 == null || cashier2 == null) return;
        supermarket.getCashiers().clear();
        supermarket.getCashiers().add(cashier1);
        supermarket.getCashiers().add(cashier2);
        supermarket.simulateCashiers();
        assertThat(cashier1.getTotalIdleTime()+cashier2.getTotalIdleTime(), is(idleTimeSum));
        assertThat(cashier1.getTotalIdleTime(), is(greaterThanOrEqualTo(idleTimeLow)));
        assertThat(cashier1.getTotalIdleTime(), is(lessThanOrEqualTo(idleTimeHigh)));
        assertEquals((cashier1.getTotalIdleTime()>cashier2.getTotalIdleTime() ? maxQueueLengthLow : maxQueueLengthHigh),
                cashier1.getMaxQueueLength());
        assertEquals((cashier1.getTotalIdleTime()>cashier2.getTotalIdleTime() ? maxQueueLengthHigh : maxQueueLengthLow),
                cashier2.getMaxQueueLength());
    }

}