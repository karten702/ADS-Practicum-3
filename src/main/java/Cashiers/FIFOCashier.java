package Cashiers;

import java.time.LocalTime;

public class FIFOCashier extends Cashier {


    protected FIFOCashier(String name) {
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
