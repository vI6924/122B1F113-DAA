import java.util.*;

class Order {
    long timestamp;
    String orderId;

    Order(long timestamp, String orderId) {
        this.timestamp = timestamp;
        this.orderId = orderId;
    }
}

public class AssignmentNo1 {

    
    public static void mergeSort(Order[] orders, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(orders, left, mid);        
            mergeSort(orders, mid + 1, right);   
            merge(orders, left, mid, right);     
        }
    }

    
    private static void merge(Order[] orders, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Order[] leftArray = new Order[n1];
        Order[] rightArray = new Order[n2];

        for (int idx = 0; idx < n1; idx++)
            leftArray[idx] = orders[left + idx];
        for (int counter = 0; counter < n2; counter++)
            rightArray[counter] = orders[mid + 1 + counter];

        int idx = 0, counter = 0, k = left;

        while (idx < n1 && counter < n2) {
            if (leftArray[idx].timestamp <= rightArray[counter].timestamp) {
                orders[k++] = leftArray[idx++];
            } else {
                orders[k++] = rightArray[counter++];
            }
        }

        while (idx < n1) {
            orders[k++] = leftArray[idx++];
        }

        while (counter < n2) {
            orders[k++] = rightArray[counter++];
        }
    }

    
    public static void main(String[] args) {
        int numElements = 10; 
        Order[] orders = new Order[numElements];

        
        Random rand = new Random();
        for (int idx = 0; idx < numElements; idx++) {
            orders[idx] = new Order(rand.nextInt(1000000), "Order_" + idx);
        }

        System.out.println("Before Sorting:");
        for (Order o : orders) {
            System.out.println(o.orderId + " - " + o.timestamp);
        }

        mergeSort(orders, 0, orders.length - 1);

        System.out.println("\nAfter Sorting by Timestamp:");
        for (Order o : orders) {
            System.out.println(o.orderId + " - " + o.timestamp);
        }
    }
}
