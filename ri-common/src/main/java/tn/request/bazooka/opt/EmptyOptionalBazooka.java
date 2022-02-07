package tn.request.bazooka.opt;

import java.util.Optional;

public class EmptyOptionalBazooka<T> extends BazookaOpt<T> {

    protected EmptyOptionalBazooka(Optional<T> value) {
        super(value);
    }

    @Override
    public <E extends Exception> BazookaOpt<T> thenThrow(E exception) throws E {
        throw exception;
    }

    @Override
    public T orElseGet() {
        throw new IllegalStateException("Cannot get the value of an empty optional");
    }
}
