import java.util.List;

public class Frontend implements FrontendInterface{

    private BackendInterface backend;

    public Frontend(BackendInterface backend){
        this.backend = backend;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger HTML page. This HTML output should include:
     *     - a text input field with the id="start", for the start location
     *     - a text input field with the id="end", for the end location
     *     - a button labelled "Find Shortest Path" to request this computation
     * Ensure these text fields are clearly labelled, so the user can understand
     * how to use them.
     * @return an HTML string containing input controls the user can use to
     *         request a shortest path computation
     */
    public String generateShortestPathPromptHTML() {

        return
        """
        <label> start <input id = "start" type = "text"/></label>
        <label> end <input id = "end" type = "text"/></label>
        <button> Find Shortest Path </button>

        """;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger HTML page.  This HTML output should include:
     *     - a paragraph tag for the path's start and end locations
     *     - an ordered list tag for locations along that shortest path
     *     - a paragraph tag that includes the total time along this path
     * Or, if there is no such path, the HTML returned should instead indicate
     * the kind of problem encountered.
     * @param start is the starting location to find a shortest path from
     * @param end is the end location that this shortest path should end at
     * @return an HTML string for the shortest path between these two locations
     */
    public String generateShortestPathResponseHTML(String start, String end){
        // Check if input is null or empty before calling backend
        if (start == null || end == null || start.isBlank() || end.isBlank()) {
            return "<p style='color:red;'>Error: Please provide both start and end locations.</p>";
        }

        try {
            List<String> locationsList = backend.findLocationsOnShortestPath(start, end);
            List<Double> times = backend.findTimesOnShortestPath(start, end);

            // Check if lists returned are valid
            if (locationsList == null || locationsList.isEmpty()) {
                return "<p>No rail connection was found between " + start + " and " + end + ".</p>";
            }

            // Using StringBuilder for efficiency in loops
            StringBuilder result = new StringBuilder();
            result.append("<p>From: ").append(start).append(" To: ").append(end).append("</p>");
            result.append("<ol>");

            for (String location : locationsList) {
                result.append("<li>").append(location).append("</li>");
            }
            result.append("</ol>");

            double totalTime = 0.0;
            if (times != null) {
                for (Double time : times) {
                    totalTime += time;
                }
            }
            result.append("<p>Total Time: ").append(totalTime).append("</p>");

            return result.toString();
        } catch (Exception e) {
            return "<p style='color:red;'>An error occurred: " + e.getMessage() + "</p>";
        }
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a larger
     * HTML page. This HTML output should include:
     *     - a text input field with the id="from", for the start locations
     *     - a text input field with the id="time", for the max time
     *     - a button labelled "Reachable From Within" to submit this request
     * Ensure these text fields are clearly labelled, so the user can understand
     * how to use them.
     * @return an HTML string containing input controls the user can use to
     *         request a calculation of the ten closest locations
     */
    public String generateReachableFromWithinPromptHTML(){
        return
        """
        <label> start <input type = "text" id ="from"> </label>
        <label> max time<input type = "text" id ="time"> </label>
        <button> Reachable From Within </button>
        """;
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a larger
     * HTML page. This HTML output should include:
     *     - a paragraph tag describing the start location and maximum time
     *     - an unordered list tag for locations that can be reached within
     *         that maximum time
     * Or, if no such locations can be found, the HTML returned should instead
     * indicate the kind of problem encountered.
     * @param start is the starting location to search from
     * @param maxTime is the maximum time away the start that a
     *        location can be to be reported
     * @return an HTML string for the closest locations from the specified start
     *         location.
     */
    public String generateReachableFromWithinResponseHTML(String start, double maxTime){
        if (start == null || start.isBlank()) {
            return "<p style='color:red;'>Error: Start location cannot be empty.</p>";
        }

        try {
            List<String> locations = backend.getReachableFromWithin(start, maxTime);

            // Fix for Rec 3 (Output Clarity): Added <br> for better readability
            StringBuilder result = new StringBuilder();
            result.append("<p>Start location: ").append(start)
                  .append("<br>Max time: ").append(maxTime).append("</p>");

            if (locations == null || locations.isEmpty()) {
                result.append("<p>No locations are reachable within this time.</p>");
            } else {
                result.append("<ul>");
                for (String loc : locations) {
                    result.append("<li>").append(loc).append("</li>");
                }
                result.append("</ul>");
            }

            return result.toString();
        } catch (java.util.NoSuchElementException e) {
            // Fix for Rec 2: Specific exception catching
            return "<p style='color:red;'>Error: The location '" + start + "' was not found.</p>";
        } catch (Exception e) {
            return "<p style='color:red;'>Problem occurred: " + e.getMessage() + "</p>";
        }
    }



}