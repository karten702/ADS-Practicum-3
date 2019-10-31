import java.time.LocalTime;

public class PriorityCashier extends Cashier {

    int maxNumPriorityItems;

    public PriorityCashier(String name, int maxNumPriorityItems) {
        super(name);
        this.maxNumPriorityItems = maxNumPriorityItems;
    }

    @Override
    public int expectedCheckOutTime(int numberOfItems) {
        return 0;
    }

    @Override
    public int expectedWaitingTime(Customer customer) {
        return 0;
    }

    @Override
    public void doTheWorkUntil(LocalTime targetTime) {

    }
}
