import java.util.*;

class CargoItem {
    int id;
    int weight;
    int utility;
    boolean perishable;

    CargoItem(int id, int weight, int utility, boolean perishable) {
        this.id = id;
        this.weight = weight;
        this.utility = utility;
        this.perishable = perishable;
    }
}

public class AssignmentNo6 {

    
    static void boostPerishableUtility(List<CargoItem> items) {
        for (CargoItem it : items) {
            if (it.perishable) {
                it.utility = (int) (it.utility * 1.2); 
            }
        }
    }

    
    static void displaySelectedItems(int[][] dp, List<CargoItem> items, int capacity) {
        int idx = items.size();
        int w = capacity;
        int totalWeight = 0;

        System.out.println("\nItems loaded in the truck:");
        while (idx > 0 && w > 0) {
            if (dp[idx][w] != dp[idx - 1][w]) {
                CargoItem it = items.get(idx - 1);
                System.out.println("✅ Item " + it.id +
                        " | Weight: " + it.weight +
                        " | Utility: " + it.utility +
                        " | Perishable: " + (it.perishable ? "Yes" : "No"));
                totalWeight += it.weight;
                w -= it.weight;
            }
            idx--;
        }
        System.out.println("Total Weight Loaded: " + totalWeight + " kg");
    }

    
    static int knapsackDP(List<CargoItem> items, int capacity) {
        int numElements = items.size();
        int[][] dp = new int[numElements + 1][capacity + 1];

        for (int idx = 1; idx <= numElements; idx++) {
            for (int w = 1; w <= capacity; w++) {
                CargoItem it = items.get(idx - 1);
                if (it.weight <= w) {
                    dp[idx][w] = Math.max(it.utility + dp[idx - 1][w - it.weight], dp[idx - 1][w]);
                } else {
                    dp[idx][w] = dp[idx - 1][w];
                }
            }
        }

        displaySelectedItems(dp, items, capacity);
        return dp[numElements][capacity];
    }

    public static void main(String[] args) {
        System.out.println("🚚 SwiftCargo - Truck Loading Optimization (Knapsack with Perishables)");

        List<CargoItem> items = new ArrayList<>();
        items.add(new CargoItem(1, 10, 60, true));
        items.add(new CargoItem(2, 20, 100, false));
        items.add(new CargoItem(3, 30, 120, true));
        items.add(new CargoItem(4, 25, 90, false));
        items.add(new CargoItem(5, 15, 75, true));

        int truckCapacity = 50;

        boostPerishableUtility(items);

        int maxUtility = knapsackDP(items, truckCapacity);
        System.out.println("\numElements📦 Maximum Total Utility (using DP): " + maxUtility);
    }
}
