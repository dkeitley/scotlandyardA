package scotlandyard;

public class Edge<X, Y> {
  X source;
  X target;

  Y data;

  public Edge(X source, X target, Y data) {
    this.source = source;
    this.target = target;
    this.data = data;
  }

  public Edge() {

  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Edge<?, ?>) {
      Edge<?, ?> that = (Edge<?, ?>) o;
      return source.equals(that.source)
              && target.equals(that.target)
              && data.equals(that.data);
    }
    return false;
  }

  public void setSource(X source) {
    this.source = source;
  }

  public void setTarget(X target) {
    this.target = target;
  }

  public void setData(Y data) {
    this.data = data;
  }

  public Y data() {
    return data;
  }

  public X source() {
    return source;
  }

  public X target() {
    return target;
  }

  public X other(X val) {
    if (source.equals(val)) {
      return target;
    } else {
      return source;
    }
  }

  public String toString() {
    return source.toString() + " " + target.toString() + " "
            + data.toString();
  }

}
