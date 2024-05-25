package com.aburakkontas.manga_cdn.infrastructure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.aburakkontas.manga_cdn.infrastructure")
@EnableJpaRepositories("com.aburakkontas.manga_cdn.infrastructure.repositories")
public class InfrastructureInjection {

}
