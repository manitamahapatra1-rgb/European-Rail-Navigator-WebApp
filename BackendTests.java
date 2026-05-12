import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BackendTests {

    /**
     * Checks that loadGraphData reads location names from a dot file and that
     * getListOfAll returns those locations.
     */
    @Test
    public void roleTest1() throws IOException {
        Backend backend = new Backend(new Graph_Placeholder());

        Path file = Files.createTempFile("rail", ".dot");
        Files.writeString(file,
                "digraph test {\n" +
                "\"Union South\" -> \"Computer Sciences and Statistics\" [minutes=1];\n" +
                "\"Computer Sciences and Statistics\" -> \"Weeks Hall for Geological Sciences\" [minutes=2];\n" +
                "}\n");

        backend.loadGraphData(file.toString());

        assertEquals(
                Arrays.asList(
                        "Computer Sciences and Statistics",
                        "Union South",
                        "Weeks Hall for Geological Sciences"),
                backend.getListOfAll());
    }

    /**
     * Checks that shortest path locations and times are returned correctly when
     * using the provided Graph_Placeholder.
     */
    @Test
    public void roleTest2() {
        Backend backend = new Backend(new Graph_Placeholder());

        assertEquals(
                Arrays.asList(
                        "Union South",
                        "Computer Sciences and Statistics",
                        "Weeks Hall for Geological Sciences"),
                backend.findLocationsOnShortestPath(
                        "Union South",
                        "Weeks Hall for Geological Sciences"));

        assertEquals(
                Arrays.asList(1.0, 2.0),
                backend.findTimesOnShortestPath(
                        "Union South",
                        "Weeks Hall for Geological Sciences"));
    }

    /**
     * Checks that reachable locations are returned for a valid start location,
     * and that NoSuchElementException is thrown for a missing start location.
     */
    @Test
    public void roleTest3() throws IOException {
        Backend backend = new Backend(new Graph_Placeholder());

        Path file = Files.createTempFile("rail", ".dot");
        Files.writeString(file,
                "digraph test {\n" +
                "\"Union South\" -> \"Computer Sciences and Statistics\" [minutes=1];\n" +
                "\"Computer Sciences and Statistics\" -> \"Weeks Hall for Geological Sciences\" [minutes=2];\n" +
                "}\n");

        backend.loadGraphData(file.toString());

        assertEquals(
                Arrays.asList(
                        "Computer Sciences and Statistics",
                        "Union South",
                        "Weeks Hall for Geological Sciences"),
                backend.getReachableFromWithin("Union South", 3.0));

        assertThrows(
                NoSuchElementException.class,
                () -> backend.getReachableFromWithin("Not Real Place", 3.0));
    }
}