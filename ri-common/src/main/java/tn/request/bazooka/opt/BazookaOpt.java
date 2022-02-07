package tn.request.bazooka.opt;

import java.util.Optional;

public abstract class BazookaOpt<T> {
  protected Optional<T> optional;

  protected BazookaOpt(Optional<T> optional) {
    this.optional = optional;
  }

  public static <T> BazookaOpt<T> checkIfEmpty(Optional<T> optional) {
    if (optional.isEmpty()) {
      return new EmptyOptionalBazooka<>(optional);
    } else {
      return new NotEmptyOptionalBazooka<>(optional);
    }
  }

  public abstract <E extends Exception> BazookaOpt<T> thenThrow(E exception) throws E;

  public abstract T orElseGet();
}
