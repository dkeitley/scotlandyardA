package scotlandyard;

import java.io.IOException;

public abstract class GraphReader<X, Y> {
  abstract public Graph<X, Y> readGraph(String filename) throws IOException;
}
