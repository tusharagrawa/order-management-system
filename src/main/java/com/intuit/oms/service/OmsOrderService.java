package com.intuit.oms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.oms.model.Order;
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
import java.util.Objects;

@Slf4j
@Service
public class OmsOrderService {

    @Value("${orders-file-path}")
    private String ordersFilePath;

    @Autowired
    private ObjectMapper objectMapper;

    public Mono<Void> addOrder(Mono<Order> orderDetail) {
        return orderDetail
                .map(this::parseJsonOrder)
                .flatMap(this::addItemInJson)
                .onErrorMap(error -> {
                    log.error("Exception while Adding Order to Json File: " + error.toString());
                    return new Exception("Exception while Adding Order to Json File");
                })
                .doOnSuccess(out -> log.info("Successfully Saved New Order in File"))
                .subscribeOn(Schedulers.immediate());
    }

    private JSONObject parseJsonOrder(Order orderObject) {
        JSONObject orderJson = new JSONObject();
        orderJson.put("orderId", orderObject.getOrderId());
        orderJson.put("customerId", orderObject.getCustomerId());
        orderJson.put("productId", orderObject.getProductId());
        orderJson.put("productQuantity", orderObject.getProductQuantity());
        orderJson.put("productPrice", orderObject.getProductPrice());
        return orderJson;
    }

    private Mono<Void> addItemInJson(JSONObject product) {

        JSONParser jsonParser = new JSONParser();

        try {
            Object obj = jsonParser.parse(new FileReader(ordersFilePath));
            JSONArray jsonArray = (JSONArray) obj;

            jsonArray.add(product);

            FileWriter file = new FileWriter(ordersFilePath);
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();

        } catch (IOException | ParseException e) {
            log.error("Exception While adding Order to Json File: " + e);
        }

        return Mono.empty();
    }

    @SneakyThrows
    public Flux<Order> getAllOrders() {

        return Flux.fromArray(objectMapper.readValue(new File(ordersFilePath), Order[].class))
                .doOnError(error -> log.error("Exception while fetching All Orders List: " + error.toString()))
                .subscribeOn(Schedulers.immediate());
    }

    public Flux<Order> getAllOrdersForCustomer(Long customerId) {
        return getAllOrders()
                .filter(order -> Objects.equals(order.getCustomerId(), customerId))
                .doOnError(error -> log.error("Exception while fetching orders List for customer: " + error.toString()))
                .subscribeOn(Schedulers.immediate());
    }
}
