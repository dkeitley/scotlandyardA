package scotlandyard;

public class MovePass extends Move  {
  public MovePass(Colour colour) {
    super(colour);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MovePass) {
      return super.equals(obj);
    }
    return false;
  }



    @Override
  public String toString() {
    return "Move Pass " + super.toString();
  }
}
