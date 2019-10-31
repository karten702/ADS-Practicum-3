import java.time.LocalTime;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityCashier extends FIFOCashier {

    private int maxNumPriorityItems;
    //private Customer servicingCustomer;

    public PriorityCashier(String name, int maxNumPriorityItems) {
        super(name);
        this.maxNumPriorityItems = maxNumPriorityItems;
        waitingQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1.getNumberOfItems() <= maxNumPriorityItems && o2.getNumberOfItems() > maxNumPriorityItems) {
                return -1;
            }
            return 0;
        });
    }

//    @Override
//    public void reStart(LocalTime currentTime) {
//        this.waitingQueue.clear();
//        this.currentTime = currentTime;
//        this.totalIdleTime = 0;
//        this.maxQueueLength = 0;
//        this.timeServicingCustomer = 0;
//    }

//    /**
//     * calculate the expected nett checkout time of a customer with a given number of items
//     * this may be different for different types of Cashiers
//     * @param numberOfItems
//     * @return
//     */
//    @Override
//    public int expectedCheckOutTime(int numberOfItems) {
//        if (numberOfItems > 0)
//            return (checkoutTimePerCustomer + (numberOfItems * checkoutTimePerItem));
//        else
//            return 0;
//    }

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
        boolean priorityCustomer = customer.getNumberOfItems() <= maxNumPriorityItems;

        if (servicingCustomer != null){
            totalWaitTime += expectedCheckOutTime(servicingCustomer.getNumberOfItems()) - timeServicingCustomer;//;servicingCustomer.getActualCheckOutTime();
        }

        for(Customer waitingCustomer : waitingQueue){
            if (!waitingCustomer.equals(customer)) {
                if (priorityCustomer && waitingCustomer.getNumberOfItems() > maxNumPriorityItems)
                    break;

                totalWaitTime += expectedCheckOutTime(waitingCustomer.getNumberOfItems());
            }
            else
                break;
        }

        return totalWaitTime;
    }

//    @Override
//    public void doTheWorkUntil(LocalTime targetTime) {
//        while (currentTime.isBefore(targetTime)) {
//            if (waitingQueue.isEmpty() && servicingCustomer == null) {
//                setTotalIdleTime(getTotalIdleTime()+1);
//                this.setCurrentTime(currentTime.plusSeconds(1));
//                continue;
//            }
//            else if (!waitingQueue.isEmpty() && servicingCustomer == null && waitingQueue.peek().getQueuedAt().isAfter(currentTime)) {
//                setTotalIdleTime(getTotalIdleTime()+1);
//                this.setCurrentTime(currentTime.plusSeconds(1));
//                continue;
//            }
//            if (servicingCustomer == null) {
//                servicingCustomer = waitingQueue.poll();
//                servicingCustomer.setActualWaitingTime(servicingCustomer.getQueuedAt().compareTo(currentTime));
//            }
//
//            if (timeServicingCustomer < expectedCheckOutTime(servicingCustomer.getNumberOfItems())) {
//                timeServicingCustomer++;
//                this.setCurrentTime(currentTime.plusSeconds(1));
//            }
//            else {
//                servicingCustomer.setActualCheckOutTime(timeServicingCustomer);
//                timeServicingCustomer = 0;
//                servicingCustomer = null;
//                maxQueueLength--;
//                this.setCurrentTime(currentTime.plusSeconds(1));
//            }
//        }
//    }
}
