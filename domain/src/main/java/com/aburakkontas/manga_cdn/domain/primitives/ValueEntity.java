package com.aburakkontas.manga_cdn.domain.primitives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ValueEntity {
    private final List<DomainEvent> _domainEvents = new ArrayList<DomainEvent>();

    protected void arise(DomainEvent domainEvent) {
        _domainEvents.add(domainEvent);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(_domainEvents);
    }
}
