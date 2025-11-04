import java.util.*;

class Route {
    int dest;
    double baseCost;
    double liveCost;

    Route(int dest, double baseCost, double liveCost) {
        this.dest = dest;
        this.baseCost = baseCost;
        this.liveCost = liveCost;
    }
}

public class AssignmentNo5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Enter total number of stages: ");
        int stages = sc.nextInt();

        int[] nodesInStage = new int[stages];
        System.out.print("Enter node count in each stage (" + stages + " values): ");
        for (int idx = 0; idx < stages; idx++) nodesInStage[idx] = sc.nextInt();

        int totalNodes = 0;
        int[] startIndex = new int[stages];
        for (int idx = 0; idx < stages; idx++) {
            startIndex[idx] = totalNodes;
            totalNodes += nodesInStage[idx];
        }

        
        System.out.print("Enter number of edges: ");
        int edgeCount = sc.nextInt();

        List<List<Route>> graph = new ArrayList<>();
        List<List<Integer>> revGraph = new ArrayList<>();
        for (int idx = 0; idx < totalNodes; idx++) {
            graph.add(new ArrayList<>());
            revGraph.add(new ArrayList<>());
        }

        System.out.println("Enter each edge as: source destination cost");
        for (int idx = 0; idx < edgeCount; idx++) {
            int from = sc.nextInt();
            int to = sc.nextInt();
            double cost = sc.nextDouble();

            if (from < 0 || from >= totalNodes || to < 0 || to >= totalNodes) {
                System.err.println("Invalid edge input.");
                return;
            }

            graph.get(from).add(new Route(to, cost, cost));
            revGraph.get(to).add(from);
        }

        
        final double INF = 1e18;
        double[] minCost = new double[totalNodes];
        Arrays.fill(minCost, INF);
        int[] nextHop = new int[totalNodes];
        Arrays.fill(nextHop, -1);

        
        for (int k = 0; k < nodesInStage[stages - 1]; k++) {
            int node = startIndex[stages - 1] + k;
            minCost[node] = 0.0;
        }

        
        for (int st = stages - 2; st >= 0; st--) {
            for (int counter = 0; counter < nodesInStage[st]; counter++) {
                int u = startIndex[st] + counter;
                double best = INF;
                int bestNext = -1;

                for (Route r : graph.get(u)) {
                    double newCost = r.liveCost + minCost[r.dest];
                    if (newCost < best) {
                        best = newCost;
                        bestNext = r.dest;
                    }
                }
                minCost[u] = best;
                nextHop[u] = bestNext;
            }
        }

        
        System.out.println("\nOptimal costs from Stage 0 nodes:");
        for (int counter = 0; counter < nodesInStage[0]; counter++) {
            int node = startIndex[0] + counter;
            if (minCost[node] >= INF / 2)
                System.out.println("Node " + node + ": Unreachable");
            else
                System.out.printf("Node %d: Cost = %.6f%numElements", node, minCost[node]);
        }

        
        System.out.print("\nEnter a source node (stage 0) to display path, or -1 to skip: ");
        int src = sc.nextInt();

        if (src >= 0 && src < totalNodes) {
            if (minCost[src] >= INF / 2) {
                System.out.println("No route from " + src);
            } else {
                System.out.print("Best path from " + src + " : ");
                double total = 0;
                int cur = src;
                while (cur != -1) {
                    System.out.print(cur);
                    int nxt = nextHop[cur];
                    if (nxt == -1) break;
                    System.out.print(" -> ");
                    double cst = 0;
                    for (Route r : graph.get(cur)) {
                        if (r.dest == nxt) {
                            cst = r.liveCost;
                            break;
                        }
                    }
                    total += cst;
                    cur = nxt;
                }
                System.out.printf("%nTotal route cost: %.6f%numElements", total);
            }
        }

        
        System.out.print("\nEnter number of live edge cost updates (0 to finish): ");
        int updates = sc.nextInt();

        while (updates-- > 0) {
            System.out.print("Enter update (u v multiplier): ");
            int u = sc.nextInt();
            int v = sc.nextInt();
            double factor = sc.nextDouble();

            for (Route r : graph.get(u))
                if (r.dest == v)
                    r.liveCost = r.baseCost * factor;

            final double[] INF_REF = {INF};

            java.util.function.Function<Integer, Double> updateNode = (Integer node) -> {
                double best = INF_REF[0];
                int bestNext = -1;
                for (Route r : graph.get(node)) {
                    if (minCost[r.dest] >= INF_REF[0] / 2) continue;
                    double candidate = r.liveCost + minCost[r.dest];
                    if (candidate < best) {
                        best = candidate;
                        bestNext = r.dest;
                    }
                }
                nextHop[node] = bestNext;
                return best;
            };

            Queue<Integer> q = new LinkedList<>();
            double newCost = updateNode.apply(u);
            if (Math.abs(newCost - minCost[u]) > 1e-9) {
                minCost[u] = newCost;
                q.add(u);
            }

            while (!q.isEmpty()) {
                int node = q.poll();
                for (int pred : revGraph.get(node)) {
                    double nc = updateNode.apply(pred);
                    if (Math.abs(nc - minCost[pred]) > 1e-9) {
                        minCost[pred] = nc;
                        q.add(pred);
                    }
                }
            }
        }

        
        System.out.println("\nAfter updates, best costs from Stage 0 nodes:");
        for (int counter = 0; counter < nodesInStage[0]; counter++) {
            int node = startIndex[0] + counter;
            if (minCost[node] >= INF / 2)
                System.out.println("Node " + node + ": Unreachable");
            else
                System.out.printf("Node %d: Cost = %.6f%numElements", node, minCost[node]);
        }

        System.out.print("\nEnter a Stage 0 source node to view updated path (-1 to exit): ");
        src = sc.nextInt();

        if (src >= 0 && src < totalNodes) {
            if (minCost[src] >= INF / 2) {
                System.out.println("No route from " + src);
            } else {
                System.out.print("Updated path from " + src + " : ");
                double total = 0;
                int cur = src;
                while (cur != -1) {
                    System.out.print(cur);
                    int nxt = nextHop[cur];
                    if (nxt == -1) break;
                    System.out.print(" -> ");
                    double cst = 0;
                    for (Route r : graph.get(cur)) {
                        if (r.dest == nxt) {
                            cst = r.liveCost;
                            break;
                        }
                    }
                    total += cst;
                    cur = nxt;
                }
                System.out.printf("%nUpdated route cost: %.6f%numElements", total);
            }
        }

        sc.close();
    }
}
