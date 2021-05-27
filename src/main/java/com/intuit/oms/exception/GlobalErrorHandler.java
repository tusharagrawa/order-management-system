package com.intuit.oms.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();

        if (throwable instanceof IOException)
            return handleBackendProcessingExceptions(serverWebExchange, bufferFactory);
        else if (throwable instanceof ResponseStatusException)
            return handleResponseStatusException(serverWebExchange, bufferFactory, (ResponseStatusException) throwable);
        else {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
            DataBuffer dataBuffer = bufferFactory.wrap("Internal Server Error".getBytes());
            return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
        }
    }

    private Mono<Void> handleBackendProcessingExceptions(ServerWebExchange serverWebExchange, DataBufferFactory bufferFactory) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        DataBuffer dataBuffer = null;
        try {
            dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(new HttpError("Custom message")));
        } catch (JsonProcessingException e) {
            dataBuffer = bufferFactory.wrap("".getBytes());
        }
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private Mono<Void> handleResponseStatusException(ServerWebExchange serverWebExchange, DataBufferFactory bufferFactory, ResponseStatusException responseStatusException) {
        serverWebExchange.getResponse().setStatusCode(responseStatusException.getStatus());
        DataBuffer dataBuffer = null;
        try {
            dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(new HttpError("Invalid Requests, Please Validate if Request URLs and Parameters are correct")));
        } catch (JsonProcessingException e) {
            dataBuffer = bufferFactory.wrap("".getBytes());
        }
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    public class HttpError {

        private final String message;

        HttpError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}