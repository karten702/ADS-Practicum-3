package Cashiers;

import java.time.LocalTime;

public class PriorityCashier extends Cashier {

    protected PriorityCashier(String name) {
        super(name);
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
