package com.intuit.oms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.oms.model.Product;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class OmsProductService {

    @Value("${products-data}")
    private Resource productsFilePath;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public Flux<Product> getAllProducts() {
        return Flux.fromArray(objectMapper.readValue(productsFilePath.getFile(), Product[].class))
                .doOnError(error -> log.error("Exception while reading Products List: " + error.getMessage()))
                .subscribeOn(Schedulers.immediate());
    }

    @SneakyThrows
    public Mono<Product> retrieveProductConfiguration(Long productId) {
        return getAllProducts()
                .filter(product -> Objects.equals(product.getProductId(), productId))
                .single()
                .doOnError(error -> log.error("Exception while reading Products List: " + error.getMessage()))
                .subscribeOn(Schedulers.immediate());
    }

    @SneakyThrows
    public Mono<?> updateProductConfiguration(Mono<Product> productDetails) {
        return null;
    }
}
