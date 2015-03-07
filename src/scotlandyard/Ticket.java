package scotlandyard;

public enum Ticket {
  Bus, Taxi, Underground, DoubleMove, SecretMove;

  public static Ticket fromRoute(Route route) {
    switch (route) {
      case Taxi:
        return Taxi;
      case Bus:
        return Bus;
      case Underground:
        return Underground;
      case Boat:
        return SecretMove;
      default:
        return Taxi;
    }
  }
}
