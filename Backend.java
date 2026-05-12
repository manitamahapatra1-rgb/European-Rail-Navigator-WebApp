import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {

    private GraphADT<String, Double> graph;
    private List<String> allLocations;

    /*
    * Implementing classes should support the constructor below.
    * @param graph object to store the backend's graph data
    */
    public Backend(GraphADT<String, Double> graph) {
        this.graph = graph;
        this.allLocations = new ArrayList<String>();
    }

    /**
     * Loads graph data from a dot file. If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*->\\s*\"([^\"]+)\"\\s*\\[minutes=(\\d+)\\];");

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            for (String location : allLocations) {
                graph.removeNode(location);
            }
            allLocations.clear();

            String line = reader.readLine();
            while (line != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String start = matcher.group(1);
                String end = matcher.group(2);
                Double time = Double.parseDouble(matcher.group(3));

                if (!graph.containsNode(start)) {
                    graph.insertNode(start);
                }
                if (!graph.containsNode(end)) {
                    graph.insertNode(end);
                }
                graph.insertEdge(start, end, time);

                if (!allLocations.contains(start)) {
                    allLocations.add(start);
                }
                if (!allLocations.contains(end)) {
                    allLocations.add(end);
                }
            }
            line = reader.readLine();
        }

            Collections.sort(allLocations);
        }
    }

    /**
     * Returns a list of all locations in the graph.
     * @return list of all location names
     */
    @Override
    public List<String> getListOfAll() {
        return new ArrayList<String>(allLocations);
    }

    /**
     * Return the sequence of locations along the shortest path from start to
     * end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the nodes along the shortest path from start to end,
     *         or an empty list if no such path exists
     */
    @Override
    public List<String> findLocationsOnShortestPath(String start, String end) {
        try {
            return graph.shortestPathData(start, end);
        } catch (NoSuchElementException e) {
            return new ArrayList<String>();
        }
    }

    /**
     * Return the times in minutes between each two nodes on the shortest path
     * from start to end, or an empty list if no such path exists.
     * @param start the start of the path
     * @param end the end of the path
     * @return a list with the times in minutes between two nodes along the
     * shortest path from start to end, or an empty list if no such path exists
     */
    @Override
    public List<Double> findTimesOnShortestPath(String start, String end) {
        List<Double> times = new ArrayList<Double>();
        List<String> path = findLocationsOnShortestPath(start, end);

        if (path.size() == 0) {
            return times;
        }

        for (int i = 1; i < path.size(); i++) {
            try {
                times.add(graph.getEdge(path.get(i - 1), path.get(i)));
            } catch (NoSuchElementException e) {
                // return an empty list if an edge on the path cannot be retrieved
                return new ArrayList<Double>();
            }
        }

        return times;
    }

    /**
     * Returns the list of locations that can be reached when starting from the
     * provided start, and travelling maxTime in minutes.
     * @param start the location to find the reachable locations from
     * @param maxTime is the maximum time it can take to get from from the
     * start to report a location
     * @return the list of locations that can be reached from start in maxTime
     * or fewer minutes
     * @throws NoSuchElementException if start does not exist
     */
    @Override
    public List<String> getReachableFromWithin(String start, double maxTime)
            throws NoSuchElementException {
        if (!graph.containsNode(start)) {
            throw new NoSuchElementException("Start location not found: " + start);
        }

        List<String> reachable = new ArrayList<String>();

        for (String location : allLocations) {
            if (location.equals(start)) {
                reachable.add(location);
            } else {
                try {
                    if (graph.shortestPathCost(start, location) <= maxTime) {
                        reachable.add(location);
                    }
                } catch (NoSuchElementException e) {
                    // do nothing for unreachable locations
                }
            }
        }

        return reachable;
    }
}