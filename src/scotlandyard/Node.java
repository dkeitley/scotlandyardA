package scotlandyard;

public class Node<X> {
  X data;

  public Node(X data) {
    this.data = data;
  }

  public Node() {
  }

  public void setValue(X data) {
    this.data = data;
  }

  public X data() {
    return data;
  }

  public String toString() {
    return data.toString();
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof Node<?> && data.equals((Node<?>) that);
  }
}
