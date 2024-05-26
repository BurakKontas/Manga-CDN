package com.aburakkontas.manga_cdn.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan({
    "com.aburakkontas.manga_cdn.domain.entities",
    "org.axonframework"
})
public class DomainInjection {
}
