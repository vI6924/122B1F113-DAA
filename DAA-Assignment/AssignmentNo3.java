// emergency relif supply optimization using fractional knapsack algorithm to main utility value
//122B1F113

import java.util.*;

class Item {
    String name;
    double weight;
    double value;
    boolean divisible;

    Item(String name, double weight, double value, boolean divisible) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.divisible = divisible;
    }

    double ratio() {
        return value / weight;
    }
}

public class AssignmentNo3 {

    public static double maximizeUtility(List<Item> items, double capacity) {
        
        items.sort((a, b) -> Double.compare(b.ratio(), a.ratio()));

        double totalValue = 0.0;
        double remainingCapacity = capacity;

        System.out.println("Selected items for transport:");
        for (Item item : items) {
            if (remainingCapacity <= 0) break;

            if (item.weight <= remainingCapacity) {
                
                totalValue += item.value;
                remainingCapacity -= item.weight;
                System.out.println("- " + item.name + " (100%) | value = " + item.value);
            } else if (item.divisible) {
                
                double fraction = remainingCapacity / item.weight;
                double valueTaken = item.value * fraction;
                totalValue += valueTaken;
                System.out.println("- " + item.name + " (" + (fraction * 100) + "%) | value = " + valueTaken);
                remainingCapacity = 0;
            }
        }

        return totalValue;
    }

    public static void main(String[] args) {
        
        List<Item> items = new ArrayList<>();
        items.add(new Item("Medical Kits", 10, 500, false));
        items.add(new Item("Food Packets", 20, 300, true));
        items.add(new Item("Drinking Water", 15, 200, true));
        items.add(new Item("Blankets", 25, 150, false));
        items.add(new Item("Baby Formula", 5, 180, false));

        double boatCapacity = 40; 

        double maxValue = maximizeUtility(items, boatCapacity);
        System.out.println("\numElements🚤 Maximum total utility value carried: " + maxValue);
    }
}

/*
-------------------- SAMPLE OUTPUT --------------------

Selected items for transport:
- Medical Kits (100%) | value = 500.0
- Baby Formula (100%) | value = 180.0
- Food Packets (100%) | value = 300.0
- Drinking Water (33.333332%) | value = 66.666664

🚤 Maximum total utility value carried: 1046.666664

-------------------------------------------------------
*/
