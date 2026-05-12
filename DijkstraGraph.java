import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referenced by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode pred;

        public SearchNode(Node startNode) {
            this.node = startNode;
            this.cost = 0;
            this.pred = null;
        }

        public SearchNode(SearchNode pred, Edge newEdge) {
            this.node = newEdge.succ;
            this.cost = pred.cost + newEdge.data.doubleValue();
            this.pred = pred;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashTableMap<>());
    }

    /**
     * Insert a new directed edge with a non-negative weight into the graph. If
     * an edge between pred and succ already exists, update the data stored in
     * that edge to the new weight.
     *
     * @param pred is the data contained in the new edge's predecesor node
     * @param succ is the data contained in the new edge's succ node
     * @param weight is the non-negative data to be stored in the new edge
     * @return true if the edge could be inserted or updated, or false if the
     * pred or succ data are not found in any graph nodes or the weight
     * specified is negative.
     */
    @Override
    public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight) {
        if (weight.doubleValue() < 0)
            return false;
        return super.insertEdge(pred, succ, weight);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the starting node for the path
     * @param end   the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    protected SearchNode computeShortestPath(Node start, Node end) {
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();
        HashTableMap<Node, Node> visited = new HashTableMap<>();

        queue.add(new SearchNode(start));
        while(!queue.isEmpty()){
            SearchNode current = queue.remove(); //remove lowest cost path from queue

            if (visited.containsKey(current.node)) {
                continue; ///if visited, skip
            }

            visited.put(current.node, current.node); //mark as visited

            if (current.node == end){
                return current; //return the path if end node is reached
            }

            //add unvisited neighbors to the queue
            for (Edge edge : current.node.edgesLeaving) {
                if (!visited.containsKey(edge.succ)) {
                    queue.add(new SearchNode(current, edge));
                }
            }
        }
        //throw exception if no path is found
        throw new NoSuchElementException("There is no path between start node and end node");
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from nodes along this shortest path
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        Node startNode = nodes.get(start);
        Node endNode = nodes.get(end);

       SearchNode path = computeShortestPath(startNode, endNode);

       LinkedList<NodeType> shortestPath = new LinkedList<>();

       for (SearchNode current = path; current != null; current = current.pred){
        shortestPath.addFirst(current.node.data);
       }

       return shortestPath;

    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     * @throws NoSuchElementException if either the start or the end node
     * cannot be found, or there is no path from start node to end node
     * @throws NullPointerException if the start or end node are null
     */
    public double shortestPathCost(NodeType start, NodeType end) {

        Node startNode = nodes.get(start);
        Node endNode = nodes.get(end);

        return computeShortestPath(startNode, endNode).cost;
    }


    /**
     * Uses the example graph used in lecture
     * Tests for correct costs and sequence of graph different from the one in class
     */
    @Test
    public void dijkstraTest1() {
        DijkstraGraph<String, Integer> map = new DijkstraGraph<>();
        map.insertNode("A");
        map.insertNode("B");
        map.insertNode("C");
        map.insertNode("D");
        map.insertNode("E");
        map.insertNode("F");
        map.insertNode("H");
        map.insertNode("G");

        //edges going to B
        map.insertEdge("A", "B", 4);

        //edges going to C
        map.insertEdge("A", "C", 2);

        //edges going to D
        map.insertEdge("B", "D", 1);
        map.insertEdge("C", "D", 5);
        map.insertEdge("F", "D", 2);

        //edges going to E
        map.insertEdge("D", "E", 3);
        map.insertEdge("B", "E", 10);
        map.insertEdge("A", "E", 15);

        //edges going to F
        map.insertEdge("D", "F", 0);

        //edges going to H
        map.insertEdge("G", "H", 4);
        map.insertEdge("F", "H", 4);

        ArrayList<String> shortestPath = new ArrayList<>();
        shortestPath.add("A");
        shortestPath.add("B");
        shortestPath.add("D");
        shortestPath.add("F");

        assertEquals(5.0, map.shortestPathCost("A", "F"));
        assertEquals(shortestPath, map.shortestPathData("A", "F"));

    }

    /**
     * Uses the example graph used in lecture
     * Tests for exception thrown when no path exists
     * Tests for exception thrown when no such node exists
     */
    @Test
    public void dijkstraTest2() {
        DijkstraGraph<String, Integer> map = new DijkstraGraph<>();
        map.insertNode("A");
        map.insertNode("B");
        map.insertNode("C");
        map.insertNode("D");
        map.insertNode("E");
        map.insertNode("F");
        map.insertNode("H");
        map.insertNode("G");

        //edges going to B
        map.insertEdge("A", "B", 4);

        //edges going to C
        map.insertEdge("A", "C", 2);

        //edges going to D
        map.insertEdge("B", "D", 1);
        map.insertEdge("C", "D", 5);
        map.insertEdge("F", "D", 2);

        //edges going to E
        map.insertEdge("D", "E", 3);
        map.insertEdge("B", "E", 10);
        map.insertEdge("A", "E", 15);

        //edges going to F
        map.insertEdge("D", "F", 0);

        //edges going to H
        map.insertEdge("G", "H", 4);
        map.insertEdge("F", "H", 4);

        try {
            map.shortestPathCost("A", "G");
            fail(); //fail if the exception is not thrown
        } catch (NoSuchElementException e){
            assertEquals("There is no path between start node and end node", e.getMessage()); //pass if excpetion is thrown
        }

        try {
            map.shortestPathCost("A", "Z");
            fail(); //fail if the exception is not thrown
        } catch (NoSuchElementException e){
            assertEquals("key Z not in map", e.getMessage()); //pass if excpetion is thrown
        }
    }

    /**
     * Uses the example graph used in lecture and checks for the correct cost and sequence (matching with those in lecture)
     * Checks for correct structure using nodecount and edgecount
     */
    @Test
    public void dijkstraTest3() {
        DijkstraGraph<String, Integer> map = new DijkstraGraph<>();
        map.insertNode("A");
        map.insertNode("B");
        map.insertNode("C");
        map.insertNode("D");
        map.insertNode("E");
        map.insertNode("F");
        map.insertNode("H");
        map.insertNode("G");

        //edges going to B
        map.insertEdge("A", "B", 4);

        //edges going to C
        map.insertEdge("A", "C", 2);

        //edges going to D
        map.insertEdge("B", "D", 1);
        map.insertEdge("C", "D", 5);
        map.insertEdge("F", "D", 2);

        //edges going to E
        map.insertEdge("D", "E", 3);
        map.insertEdge("B", "E", 10);
        map.insertEdge("A", "E", 15);

        //edges going to F
        map.insertEdge("D", "F", 0);

        //edges going to H
        map.insertEdge("G", "H", 4);
        map.insertEdge("F", "H", 4);

        ArrayList<String> shortestPath = new ArrayList<>();
        shortestPath.add("A");
        shortestPath.add("B");
        shortestPath.add("D");
        shortestPath.add("F");
        shortestPath.add("H");

        assertEquals(9.0, map.shortestPathCost("A", "H"));
        assertEquals(shortestPath, map.shortestPathData("A", "H"));
    }
}