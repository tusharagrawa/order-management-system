package com.intuit.oms.handler;

import com.intuit.oms.model.Product;
import com.intuit.oms.service.OmsProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OmsProductHandler {

    @Autowired
    private OmsProductService omsProductService;

    public Mono<ServerResponse> addProduct(ServerRequest serverRequest) {
        Mono<Product> product = serverRequest.bodyToMono(Product.class);
        return ServerResponse.ok().body(omsProductService.addProduct(product), Product.class);
    }

    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
        return ServerResponse.ok().body(omsProductService.getAllProducts(), Product.class);
    }

    public Mono<ServerResponse> retrieveProductConfiguration(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable("productId"));
        return ServerResponse.ok().body(omsProductService.retrieveProductConfiguration(productId), Product.class);
    }

    public Mono<ServerResponse> updateProductConfiguration(ServerRequest serverRequest) {
        Mono<Product> product = serverRequest.bodyToMono(Product.class);
        return ServerResponse.ok().body(omsProductService.updateProductConfiguration(product), Product.class);
    }
}
