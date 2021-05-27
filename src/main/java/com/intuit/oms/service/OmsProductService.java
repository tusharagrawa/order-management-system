package com.intuit.oms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.oms.model.Product;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OmsProductService {

    @Value("${products-data}")
    private String productsFilePath;

    @Autowired
    private ObjectMapper objectMapper;

    public Mono<Void> addProduct(Mono<Product> product) {

        return product
                .map(this::parseJsonProduct)
                .flatMap(this::addItemInJson)
                .onErrorMap(error -> {
                    log.error("Exception while Adding Product to Json FIle: " + error.toString());
                    return new Exception("Exception while Adding Product to Json FIle");
                })
                .doOnSuccess(out -> log.info("Successfully Saved New Product in File"))
                .subscribeOn(Schedulers.immediate());
    }

    @SneakyThrows
    public Flux<Product> getAllProducts() {
        return Flux.fromArray(objectMapper.readValue(new File(productsFilePath), Product[].class))
                .doOnError(error -> log.error("Exception while reading Products List: " + error.toString()))
                .subscribeOn(Schedulers.immediate());
    }

    @SneakyThrows
    public Mono<Product> retrieveProductConfiguration(Long productId) {
        return getAllProducts()
                .filter(product -> Objects.equals(product.getProductId(), productId))
                .single()
                .doOnError(error -> log.error("Exception while reading Products List: " + error.toString()))
                .subscribeOn(Schedulers.immediate());
    }

    public Mono<Product> updateProductConfiguration(Mono<Product> productDetails) {
        return getAllProducts()
                .collectList()
                .zipWith(productDetails)
                .flatMap(tuple -> {
                    List<JSONObject> productList = tuple.getT1().stream()
                            .map(val -> Objects.equals(val.getProductId(), tuple.getT2().getProductId()) ? parseJsonProduct(tuple.getT2()) : parseJsonProduct(val))
                            .collect(Collectors.toList());
                    return addAllInJson(productList);
                })
                .flatMap(val -> productDetails)
                .onErrorMap(error -> {
                    log.error("Exception while Updating Product to Json FIle: " + error.toString());
                    return new Exception("Exception while Updating Product to Json FIle");
                })
                .doOnSuccess(out -> log.info("Successfully Updating Product in File"))
                .subscribeOn(Schedulers.immediate());
    }

    private JSONObject parseJsonProduct(Product productObject) {
        JSONObject productJson = new JSONObject();
        productJson.put("productId", productObject.getProductId());
        productJson.put("productName", productObject.getProductName());
        productJson.put("quantity", productObject.getQuantity());
        productJson.put("price", productObject.getPrice());
        return productJson;
    }

    private Mono<Void> addAllInJson(List<JSONObject> productList) {

        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(productsFilePath));
            JSONArray jsonArray = (JSONArray) obj;
            jsonArray.removeAll(jsonArray);
            productList.forEach(product -> {
                jsonArray.add(product);
            });

            FileWriter file = new FileWriter(productsFilePath);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (IOException | ParseException e) {
            log.error("Exception While adding Product to Json File: " + e);
        }

        return Mono.empty();
    }

    private Mono<Void> addItemInJson(JSONObject product) {

        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(productsFilePath));
            JSONArray jsonArray = (JSONArray) obj;

            jsonArray.add(product);

            FileWriter file = new FileWriter(productsFilePath);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (IOException | ParseException e) {
            log.error("Exception While adding Product to Json File: " + e);
        }

        return Mono.empty();
    }
}
