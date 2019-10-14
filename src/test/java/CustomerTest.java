import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class CustomerTest {

    private Product prod1 = new Product("A001", "Any-1", 1.0);
    private Product prod2 = new Product("A002", "Any-2", 2.0);
    private Product prod3 = new Product("A003", "Any-3", 3.0);
    private Customer customer0, customer1, customer2, customer9;

    @BeforeEach
    void setup() {
        this.customer0 = new Customer(LocalTime.NOON, "1000AA");
        this.customer1 = new Customer(LocalTime.NOON.plusSeconds(10), "1000AB");
        this.customer1.getItems().add(new Purchase(this.prod1,1));
        this.customer2 = new Customer(LocalTime.NOON.plusSeconds(20), "1000AB");
        this.customer2.getItems().add(new Purchase(this.prod2,2));
        this.customer9 = new Customer(LocalTime.NOON.plusSeconds(30), "1000AC");
        this.customer9.getItems().add(new Purchase(this.prod1,5));
        this.customer9.getItems().add(new Purchase(this.prod2,3));
        this.customer9.getItems().add(new Purchase(this.prod3,1));
    }

    @Test
    void t011_customerHoldsSetOfPurchases() {
        assertTrue(this.customer0.getItems() instanceof Set);
    }

    @Test
    void t012_customerCalculatesNumberOfItems() {
        assertEquals(0, this.customer0.getNumberOfItems());
        assertEquals(1, this.customer1.getNumberOfItems());
        assertEquals(2, this.customer2.getNumberOfItems());
        assertEquals(9, this.customer9.getNumberOfItems());
    }

    @Test
    void t013_customerCalculatesTotalBill() {
        assertEquals(0.0, this.customer0.calculateTotalBill());
        assertEquals(1.0, this.customer1.calculateTotalBill());
        assertThat(4.0, is(this.customer2.calculateTotalBill()));
        assertThat(14.0, is(this.customer9.calculateTotalBill()));
    }
}