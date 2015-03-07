package scotlandyard;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ScotlandYardGraphReader extends
        GraphReader<Integer, Route> {

  @Override
  public Graph<Integer, Route> readGraph(String filename) throws IOException {
    File file = new File(filename);
    Scanner in = new Scanner(file);

    Graph<Integer, Route> graph = new Graph<Integer, Route>();

    String[] topLine = in.nextLine().split(" ");
    int numberOfNodes = Integer.parseInt(topLine[0]);
    int numberOfEdges = Integer.parseInt(topLine[1]);

    for (int i = 0; i < numberOfNodes; i++) {
      String line = in.nextLine();
      if (line.isEmpty()) {
        continue;
      }
      graph.add(getNode(line));
    }

    for (int i = 0; i < numberOfEdges; i++) {
      String line = in.nextLine();
      if (line.isEmpty()) {
        continue;
      }
      graph.add(getEdge(line));
    }
    return graph;
  }

  private Edge<Integer, Route> getEdge(String line) {
    String[] parts = line.split(" ");

    Route data = null;

    try {
      data = Route.valueOf(parts[2]);

    }
    catch (IllegalArgumentException e) {
      System.err.println("Error in graph. Cannot convert " + parts[2] + " to RouteType");
      System.exit(1);
    }

    Edge<Integer, Route> edge = new Edge<Integer, Route>
            (getIdFromName(parts[0])
                    , getIdFromName(parts[1])
                    , data);
    return edge;

  }

  private Node<Integer> getNode(String line) {
    int nodeNumber = getIdFromName(line);
    Node<Integer> node = new Node<Integer>(nodeNumber);
    return node;
  }

  private int getIdFromName(String name) {
    return Integer.parseInt(name);
  }


}
