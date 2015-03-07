package scotlandyard;

public abstract class Move {
  public Move(Colour colour) {
    this.colour = colour;
  }

  public final Colour colour;


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Move) {
      Move that = (Move) obj;
      return (colour.equals(that.colour));
    }

    return false;
  }


    @Override
  public String toString() {
    return this.colour.toString();
  }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
