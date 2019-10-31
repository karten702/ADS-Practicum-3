import java.time.LocalTime;
import java.util.LinkedList;

public class FIFOCashier extends Cashier {

    protected int checkoutTimePerCustomer = 20;
    protected int checkoutTimePerItem = 2;
    protected int timeServicingCustomer = 0;
    protected Customer servicingCustomer;

    public FIFOCashier(String name) {
        super(name);
        waitingQueue = new LinkedList<>();
    }

    @Override
    public void reStart(LocalTime currentTime) {
        this.waitingQueue.clear();
        this.currentTime = currentTime;
        this.totalIdleTime = 0;
        this.maxQueueLength = 0;
        this.timeServicingCustomer = 0;
    }

    /**
     * calculate the expected nett checkout time of a customer with a given number of items
     * this may be different for different types of Cashiers
     * @param numberOfItems
     * @return
     */
    @Override
    public int expectedCheckOutTime(int numberOfItems) {
        if (numberOfItems > 0)
            return (checkoutTimePerCustomer + (numberOfItems * checkoutTimePerItem));
        else
            return 0;
    }

    /**
     * calculate the currently expected waiting time of a given customer for this cashier.
     * this may depend on:
     * a) the type of cashier,
     * b) the remaining work of the cashier's current customer(s) being served
     * c) the position that the given customer may obtain in the queue
     * d) and the workload of the customers in the waiting queue in front of the given customer
     * @param customer
     * @return
     */
    @Override
    public int expectedWaitingTime(Customer customer) {
        int totalWaitTime = 0;

        if (servicingCustomer != null){
            totalWaitTime += expectedCheckOutTime(servicingCustomer.getNumberOfItems()) - timeServicingCustomer;
        }

        for(Customer waitingCustomer : waitingQueue){
            if (!waitingCustomer.equals(customer))
                totalWaitTime += expectedCheckOutTime(waitingCustomer.getNumberOfItems());
            else
                break;
        }

        return totalWaitTime;
    }

    /**
     * proceed the cashier's work until the given targetTime has been reached
     * this work may involve:
     * a) continuing or finishing the current customer(s) begin served
     * b) serving new customers that are waiting on the queue
     * c) sitting idle, taking a break until time has reached targetTime,
     *      after which new customers may arrive.
     * @param targetTime
     */
    @Override
    public void doTheWorkUntil(LocalTime targetTime) {
        while (currentTime.isBefore(targetTime)) {
            if (waitingQueue.isEmpty() && servicingCustomer == null) {
                setTotalIdleTime(getTotalIdleTime()+1);
                this.setCurrentTime(currentTime.plusSeconds(1));
                continue;
            }
            else if (!waitingQueue.isEmpty() && servicingCustomer == null && waitingQueue.peek().getQueuedAt().isAfter(currentTime)) {
                setTotalIdleTime(getTotalIdleTime()+1);
                this.setCurrentTime(currentTime.plusSeconds(1));
                continue;
            }
            if (servicingCustomer == null) {
                servicingCustomer = waitingQueue.poll();
                servicingCustomer.setActualWaitingTime(servicingCustomer.getQueuedAt().compareTo(currentTime));
            }

            if (timeServicingCustomer < expectedCheckOutTime(servicingCustomer.getNumberOfItems())) {
                timeServicingCustomer++; //servicingCustomer.setActualCheckOutTime(servicingCustomer.getActualCheckOutTime()+1);
                this.setCurrentTime(currentTime.plusSeconds(1));
            }
            else {
                servicingCustomer.setActualCheckOutTime(timeServicingCustomer);
                timeServicingCustomer = 0;
                servicingCustomer = null;
                maxQueueLength--;
                this.setCurrentTime(currentTime.plusSeconds(1));
            }
        }
    }
}
