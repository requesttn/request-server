package tn.request.bazooka.opt;

import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class NotEmptyOptionalBazooka<T> extends BazookaOpt<T> {

    protected NotEmptyOptionalBazooka(Optional<T> value) {
        super(value);
    }

    @Override
    public <E extends Exception> BazookaOpt<T> thenThrow(E exception) throws E {
        return this;
    }

    public T orElseGet() {
        return optional.get();
    }
}
