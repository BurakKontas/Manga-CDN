package com.aburakkontas.manga_cdn.application;

import com.aburakkontas.manga_cdn.application.interceptors.*;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.queryhandling.DefaultQueryGateway;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonInterceptorRegistrations {

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus) {
        commandBus.registerHandlerInterceptor(new LoggingCommandHandlerInterceptor());

        return DefaultCommandGateway.builder()
                .commandBus(commandBus)
                .build();
    }

    @Bean
    public QueryGateway queryGateway(QueryBus queryBus) {
        queryBus.registerHandlerInterceptor(new LoggingQueryHandlerInterceptor());
        queryBus.registerHandlerInterceptor(new ExceptionQueryHandlerInterceptor());

        return DefaultQueryGateway.builder()
                .queryBus(queryBus)
                .build();
    }

}
