import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * This is a placeholder for the fully working Backend that will be developed
 * by one of your teammates this week and then integrated with your role code
 * in a future week.  It is designed to help develop and test the functionality
 * of your own Frontend role code this week.  Note the limitations described
 * below.
 */
public class Backend_Placeholder implements BackendInterface {

    // Presumably this placeholder is using a placeholder graph that is itself
    // not fully functional.
    GraphADT<String,Double> graph;
    public Backend_Placeholder(GraphADT<String,Double> graph) {
        this.graph = graph;
    }

    // this method adds a single extra location to the graph when called
    public void loadGraphData(String filename) throws IOException {
        graph.insertNode("Alpha_Centauri_A");
    }

    public List<String> getListOfAll() {
        return new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
    }

    public List<String> findLocationsOnShortestPath(String start, String end) {
        return graph.shortestPathData(start,end);
    }

    // returns list of increasing values
    public List<Double> findTimesOnShortestPath(String start, String end) {
        List<String> locations = graph.shortestPathData(start,end);
        List<Double> times = new ArrayList<>();
        for(int i=0;i<locations.size();i++) times.add(i+1.0);
        return times;
    }

    // design 1
    // always returns entire list of locations
    public List<String> getReachableFromWithin(String start, double travelTime) throws NoSuchElementException {
        return new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
    }

    // design 2
    // always returns the locations leading to the last node
    public List<String> getFurthestFromList(String start) throws NoSuchElementException {
        List<String> all = new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
        String last = all.get(all.size()-1);
        return graph.shortestPathData(start,last);
    }

    // design 3
    // always returns the last node in the list
    public String getFurthestLocationFrom(String start) throws NoSuchElementException {
    List<String> all = new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
        return all.get(all.size()-1);
    }

    // design 4
    // always returns last location
    public String getClosestLocationFromAll(List<String> starts) throws NoSuchElementException {
        List<String> all = new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
        return all.get(all.size()-1);
    }

    // design 5
    // returns list of all locations
    public List<String> getTenClosestLocations(String start) throws NoSuchElementException {
        return new ArrayList<>(java.util.Arrays.asList("Union South", "Computer Sciences and Statistics", "Weeks Hall for Geological Sciences"));
    }
}