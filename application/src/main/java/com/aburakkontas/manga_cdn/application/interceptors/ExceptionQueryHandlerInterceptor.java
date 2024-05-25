package com.aburakkontas.manga_cdn.application.interceptors;

import com.aburakkontas.manga_cdn.domain.exceptions.ExceptionWithErrorCode;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryExecutionException;
import org.axonframework.queryhandling.QueryMessage;

import javax.annotation.Nonnull;

public class ExceptionQueryHandlerInterceptor implements MessageHandlerInterceptor<QueryMessage<?,?>> {
    @Override
    public Object handle(@Nonnull UnitOfWork<? extends QueryMessage<?, ?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) throws Exception {
        try {
            return interceptorChain.proceed();
        } catch (ExceptionWithErrorCode e) {
            throw new QueryExecutionException(e.getMessage(), e, e.getCode());
        } catch (Exception e) {
            throw new QueryExecutionException(e.getCause().getMessage(), e.getCause(), e);
        }
    }
}
