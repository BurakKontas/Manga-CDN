package com.aburakkontas.manga_cdn.application.interceptors;

import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class LoggingQueryHandlerInterceptor implements MessageHandlerInterceptor<QueryMessage<?,?>> {
    private static final Logger logger = LoggerFactory.getLogger(LoggingCommandHandlerInterceptor.class);


    @Override
    public Object handle(@Nonnull UnitOfWork<? extends QueryMessage<?,?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) throws Exception {
        QueryMessage<?,?> queryMessage = unitOfWork.getMessage();
        logger.info("Query received: {}", queryMessage.getPayloadType().getName());
        try {
            Object result = interceptorChain.proceed();
            logger.info("Query successful: {}", queryMessage.getPayloadType().getName());
            return result;
        } catch (Exception e) {
            logger.error("Query failed: {}", queryMessage.getPayloadType().getName());
            throw e;
        }
    }
}
