package com.aburakkontas.manga_cdn.api;

import com.aburakkontas.manga_cdn.application.ApplicationInjection;
import com.aburakkontas.manga_cdn.domain.DomainInjection;
import com.aburakkontas.manga_cdn.infrastructure.InfrastructureInjection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ApplicationInjection.class, InfrastructureInjection.class, DomainInjection.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
