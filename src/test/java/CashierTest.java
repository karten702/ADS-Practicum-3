import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class CashierTest {

    private Cashier fifoCashier = null;
    private Cashier priorityCashier = null;

    private Product prod1 = new Product("A001", "Any-1", 1.0);
    private Product prod2 = new Product("A002", "Any-2", 2.0);
    private Product prod3 = new Product("A003", "Any-3", 3.0);
    private Customer customer0, customer1, customer2, customer9;

    @BeforeEach
    void setup() {
        try {
            this.fifoCashier = (Cashier) Class.forName("FIFOCashier")
                    .getConstructor(new Class[]{String.class}).newInstance("FIFO-1");
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

        this.customer0 = new Customer(LocalTime.NOON, "1000AA");
        this.customer1 = new Customer(LocalTime.NOON, "1000AB");
        this.customer1.getItems().add(new Purchase(this.prod1, 1));
        this.customer2 = new Customer(LocalTime.NOON, "1000AB");
        this.customer2.getItems().add(new Purchase(this.prod2, 2));
        this.customer9 = new Customer(LocalTime.NOON, "1000AC");
        this.customer9.getItems().add(new Purchase(this.prod1, 5));
        this.customer9.getItems().add(new Purchase(this.prod2, 3));
        this.customer9.getItems().add(new Purchase(this.prod3, 1));
    }



    @Test
    void t052_reStartResetsTimeAndQueue() {
        t052_reStartResetsTimeAndQueue(this.fifoCashier);
        t052_reStartResetsTimeAndQueue(this.priorityCashier);
    }

    private void t052_reStartResetsTimeAndQueue(Cashier cashier) {
        if (cashier == null) return;
        cashier.reStart(LocalTime.NOON);
        cashier.doTheWorkUntil(LocalTime.NOON.plusSeconds(25));
        assertThat(cashier.getCurrentTime(), is(LocalTime.NOON.plusSeconds(25)));
        assertThat(cashier.getTotalIdleTime(), is(25));
        cashier.add(this.customer1);
        assertThat(cashier.getWaitingQueue().size(), is(1));
        cashier.reStart(LocalTime.NOON);
        assertThat(cashier.getWaitingQueue().size(), is(0));
        assertThat(cashier.getTotalIdleTime(), is(0));
    }

    @Test
    void t053_expectedCheckOutTimeFollowsCustomerAndCashierType() {
        t053_expectedCheckOutTimeFollowsCustomerAndCashierType(this.fifoCashier, 20, 2);
        t053_expectedCheckOutTimeFollowsCustomerAndCashierType(this.priorityCashier, 20, 2);
    }

    private void t053_expectedCheckOutTimeFollowsCustomerAndCashierType(Cashier cashier, int c1, int c2) {
        if (cashier == null) return;
        cashier.reStart(LocalTime.NOON);
        assertThat(cashier.expectedCheckOutTime(this.customer0.getNumberOfItems()), is(0));
        assertThat(cashier.expectedCheckOutTime(this.customer1.getNumberOfItems()), is(c1 + 1 * c2));
        assertThat(cashier.expectedCheckOutTime(this.customer2.getNumberOfItems()), is(c1 + 2 * c2));
        assertThat(cashier.expectedCheckOutTime(this.customer9.getNumberOfItems()), is(c1 + 9 * c2));
    }

    @Test
    void t054_expectedWaitingTimeFollowsQueueAndCurrentCustomer() {
        t054_expectedWaitingTimeFollowsQueueAndCurrentCustomer(this.fifoCashier, 20+9*2, 20+9*2-5, 40+11*2-10, 1);
        t054_expectedWaitingTimeFollowsQueueAndCurrentCustomer(this.priorityCashier, 0, 20+9*2-5, 40+11*2-10, 1);
    }

    private void t054_expectedWaitingTimeFollowsQueueAndCurrentCustomer(Cashier cashier,
                         int firstWaitingTime, int secondWaitingTime, int thirdWaitingTime, int finalQueueSize) {
        cashier.reStart(LocalTime.NOON);
        assertThat(cashier.expectedWaitingTime(this.customer9), is(0));
        cashier.add(this.customer9);
        assertThat(cashier.getWaitingQueue().size(), is(1));
        assertThat(cashier.expectedWaitingTime(this.customer2), is(firstWaitingTime));
        cashier.doTheWorkUntil(LocalTime.NOON.plusSeconds(5));
        assertThat(cashier.getWaitingQueue().size(), is(0));
        assertThat(cashier.expectedWaitingTime(this.customer2), is(secondWaitingTime));
        cashier.add(this.customer2);
        cashier.doTheWorkUntil(LocalTime.NOON.plusSeconds(10));
        assertThat(cashier.expectedWaitingTime(this.customer1), is(thirdWaitingTime));
        assertThat(cashier.getWaitingQueue().size(), is(finalQueueSize));
        assertThat(cashier.getMaxQueueLength(), is(finalQueueSize+1));
    }

    @Test
    void t051_fifoCashierConcreteClassHasBeenDefined() {
        assertTrue(this.fifoCashier instanceof Cashier, "FIFOCashier is not a Cashier");
    }

    @Test
    void t101_priorityCashierConcreteClassHasBeenDefined() {
        assertTrue(this.priorityCashier instanceof Cashier, "PriotityCashier is not a Cashier");
    }
}