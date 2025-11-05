// Delivery route optimization apply the branch and sound algorithm to determine the least cost delivery route accross cities
122B1F113    
import java.util.*;

class Node implements Comparable<Node> {
    int level;
    int pathCost;
    int reducedCost;
    int vertex;
    List<Integer> path;
    int[][] reducedMatrix;

    Node(int numElements) {
        reducedMatrix = new int[numElements][numElements];
        path = new ArrayList<>();
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.reducedCost, other.reducedCost);
    }
}

public class AssignmentNo8 {

    static final int INF = 9999999;

    static void copyMatrix(int[][] src, int[][] dest, int numElements) {
        for (int idx = 0; idx < numElements; idx++)
            System.arraycopy(src[idx], 0, dest[idx], 0, numElements);
    }

    static int reduceMatrix(int[][] matrix, int numElements) {
        int reductionCost = 0;

        
        for (int idx = 0; idx < numElements; idx++) {
            int rowMin = INF;
            for (int counter = 0; counter < numElements; counter++)
                if (matrix[idx][counter] < rowMin)
                    rowMin = matrix[idx][counter];

            if (rowMin != INF && rowMin != 0) {
                reductionCost += rowMin;
                for (int counter = 0; counter < numElements; counter++)
                    if (matrix[idx][counter] != INF)
                        matrix[idx][counter] -= rowMin;
            }
        }

        
        for (int counter = 0; counter < numElements; counter++) {
            int colMin = INF;
            for (int idx = 0; idx < numElements; idx++)
                if (matrix[idx][counter] < colMin)
                    colMin = matrix[idx][counter];

            if (colMin != INF && colMin != 0) {
                reductionCost += colMin;
                for (int idx = 0; idx < numElements; idx++)
                    if (matrix[idx][counter] != INF)
                        matrix[idx][counter] -= colMin;
            }
        }

        return reductionCost;
    }

    static Node createNode(int[][] parentMatrix, List<Integer> path, int level, int idx, int counter, int numElements) {
        Node node = new Node(numElements);
        copyMatrix(parentMatrix, node.reducedMatrix, numElements);

        
        if (level != 0) {
            for (int k = 0; k < numElements; k++)
                node.reducedMatrix[idx][k] = INF;
        }

        
        for (int k = 0; k < numElements; k++)
            node.reducedMatrix[k][counter] = INF;

        
        if (level + 1 < numElements)
            node.reducedMatrix[counter][0] = INF;

        node.path = new ArrayList<>(path);
        node.path.add(counter);
        node.level = level;
        node.vertex = counter;
        return node;
    }

    static void solveTSP(int[][] costMatrix, int numElements) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        List<Integer> path = new ArrayList<>();
        path.add(0);

        Node root = new Node(numElements);
        copyMatrix(costMatrix, root.reducedMatrix, numElements);

        root.path = path;
        root.level = 0;
        root.vertex = 0;
        root.pathCost = 0;
        root.reducedCost = reduceMatrix(root.reducedMatrix, numElements);

        pq.add(root);
        int minCost = INF;
        List<Integer> finalPath = new ArrayList<>();

        while (!pq.isEmpty()) {
            Node minNode = pq.poll();
            int idx = minNode.vertex;

            
            if (minNode.level == numElements - 1) {
                List<Integer> completePath = new ArrayList<>(minNode.path);
                completePath.add(0); 
                int totalCost = minNode.pathCost + costMatrix[idx][0];
                if (totalCost < minCost) {
                    minCost = totalCost;
                    finalPath = completePath;
                }
                continue;
            }

            for (int counter = 0; counter < numElements; counter++) {
                if (minNode.reducedMatrix[idx][counter] != INF) {
                    Node child = createNode(minNode.reducedMatrix, minNode.path, minNode.level + 1, idx, counter, numElements);
                    child.pathCost = minNode.pathCost + costMatrix[idx][counter];
                    child.reducedCost = child.pathCost + reduceMatrix(child.reducedMatrix, numElements);
                    pq.add(child);
                }
            }
        }

        
        System.out.print("\nOptimal Delivery Route (SwiftShip): ");
        for (int idx = 0; idx < finalPath.size(); idx++) {
            System.out.print(finalPath.get(idx));
            if (idx < finalPath.size() - 1) System.out.print(" ");
        }
        System.out.println("\nMinimum Total Delivery Cost: " + minCost);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of cities: ");
        int numElements = sc.nextInt();

        int[][] costMatrix = new int[numElements][numElements];
        System.out.println("Enter cost matrix (use large number for no direct route):");
        for (int idx = 0; idx < numElements; idx++)
            for (int counter = 0; counter < numElements; counter++)
                costMatrix[idx][counter] = sc.nextInt();

        solveTSP(costMatrix, numElements);
        sc.close();
    }
}

/*
-------------------- SAMPLE OUTPUT --------------------

Enter number of cities: 4
Enter cost matrix (use large number for no direct route):
9999999 10 15 20
10 9999999 35 25
15 35 9999999 30
20 25 30 9999999

Optimal Delivery Route (SwiftShip): 0 1 3 2 0
Minimum Total Delivery Cost: 80

-------------------------------------------------------
*/
