package scotlandyard;

import java.util.ArrayList;
import java.util.List;

public class Graph<X, Y> {
  protected List<Node<X>> nodes;
  protected List<Edge<X, Y>> edges;

  public Graph() {
    nodes = new ArrayList<Node<X>>();
    edges = new ArrayList<Edge<X, Y>>();
  }

  public void add(Node<X> node) {
    nodes.add(node);
  }

  public void add(Edge<X, Y> edge) {
    edges.add(edge);
  }

  public List<Edge<X, Y>> getEdges() {
    return edges;
  }

  public List<Node<X>> getNodes() {
    return nodes;
  }

  public Node<X> getNode(X id) {
    for (Node<X> node : nodes) {
      if (node.data() == id) {
        return node;
      }
    }

    return null;
  }

  public List<Edge<X, Y>> getEdges(X nodeVal) {
    List<Edge<X, Y>> output = new ArrayList<Edge<X, Y>>();
    for (Edge<X, Y> edge : edges) {
      if (edge.target.equals(nodeVal) || edge.source.equals(nodeVal)) {
        output.add(edge);
      }
    }

    return output;
  }

  public String toString() {
    String output = "";
    for (Node<X> node : nodes) {
      output += node.toString() + "\n";
    }

    for (Edge<X, Y> edge : edges) {
      output += edge.toString() + "\n";
    }

    return output;
  }

}
