package com.aburakkontas.manga_cdn.domain.primitives;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class ValueObject<TValue> {

    public abstract Stream<TValue> getAtomicValues();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ValueObject other = (ValueObject) obj;
        return getAtomicValues().equals(other.getAtomicValues());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAtomicValues());
    }
}
