package com.intuit.oms.repository;

import com.intuit.oms.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OmsProductRepository extends ReactiveCrudRepository<Product, Long> {

    @Query("select * from product")
    Flux<Product> getAllProducts();
}
