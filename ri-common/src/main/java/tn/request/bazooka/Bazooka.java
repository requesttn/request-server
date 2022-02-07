package tn.request.bazooka;

public class Bazooka {
  private final boolean condition;

  public Bazooka(boolean condition) {
    this.condition = condition;
  }

  public static Bazooka checkIf(boolean condition) {
    return new Bazooka(condition);
  }

  public static NullBazooka checkIfNull(Object obj) {
    return new NullBazooka(obj == null);
  }

  public static Bazooka checkIfNot(boolean condition) {
    return new Bazooka(!condition);
  }

  public <E extends Exception> void thenThrow(E exception) throws E {
    throw exception;
  }
}
