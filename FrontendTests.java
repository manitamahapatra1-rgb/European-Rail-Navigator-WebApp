import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class FrontendTests {

    @Test
    public void roleTest1(){
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String result = frontend.generateShortestPathPromptHTML();
        assertTrue(result.contains("start"));
    }

    @Test
    public void roleTest2(){
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String result = frontend.generateShortestPathResponseHTML("Union South", "Weeks Hall for Geological Sciences");
        assertTrue(result.contains("Total Time:"));
    }

    @Test
    public void roleTest3(){
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(graph);
        Frontend frontend = new Frontend(backend);
        String result = frontend.generateReachableFromWithinPromptHTML();
        assertTrue(result.contains("start"));
        String result1 = frontend.generateReachableFromWithinResponseHTML("Union South", 10.0);
        assertTrue(result1.contains("Max time:"));
    }

    /**
     * Integration test that tests generateShortestPathPromptHTML
     * using real Frontend and Backend classes with no placeholders.
     */
    @Test
    public void testShortestPathPromptIntegration(){
        // create real backend with DijkstraGraph and HashTableMap
        Backend backend = new Backend(new DijkstraGraph<>());
        // create real frontend with real backend
        Frontend frontend = new Frontend(backend);
        // call shortest path prompt and store result
        String result = frontend.generateShortestPathPromptHTML();
        // check that result contains expected output
        assertTrue(result.contains("start"));
    }

    /**
     * Integration test that tests generateShortestPathResponseHTML
     * using real Frontend and Backend classes with actual graph data.
     */
    @Test
    public void testShortestPathResponseIntegration() throws IOException {
        // create real backend with DijkstraGraph and HashTableMap
        Backend backend = new Backend(new DijkstraGraph<>());
        // load the graph data from the dot file
        backend.loadGraphData("./europeanRail.dot");
        // create real frontend with real backend
        Frontend frontend = new Frontend(backend);
        // call shortest path response with two real nodes from europeanRail.dot
        String result = frontend.generateShortestPathResponseHTML("Amsterdam", "London");
        // check that result contains "From:" which is in the response HTML
        assertTrue(result.contains("From:"));
    }

    /**
     * Integration test that tests generateReachableFromWithinPromptHTML
     * using real Frontend and Backend classes with no placeholders.
     */
    @Test
    public void testReachablePromptIntegration(){
        // create real backend with DijkstraGraph and HashTableMap
        Backend backend = new Backend(new DijkstraGraph<>());
        // create real frontend with real backend
        Frontend frontend = new Frontend(backend);
        // call reachable prompt and store result
        String result = frontend.generateReachableFromWithinPromptHTML();
        // check that result contains expected output
        assertTrue(result.contains("start"));
    }

    /**
     * Integration test that tests generateReachableFromWithinResponseHTML
     * using real Frontend and Backend classes with actual graph data.
     */
    @Test
    public void testReachableResponseIntegration() throws IOException {
        // create real backend with DijkstraGraph and HashTableMap
        Backend backend = new Backend(new DijkstraGraph<>());
        // load the graph data from the dot file
        backend.loadGraphData("./europeanRail.dot");
        // create real frontend with real backend
        Frontend frontend = new Frontend(backend);
        // call reachable response with a real node and max time
        String result = frontend.generateReachableFromWithinResponseHTML("Amsterdam", 200.0);
        // check that result contains "Max time:" which is in the response HTML
        assertTrue(result.contains("Max time:"));
    }

}