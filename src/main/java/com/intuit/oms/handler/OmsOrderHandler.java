package com.intuit.oms.handler;

import com.intuit.oms.model.Order;
import com.intuit.oms.service.OmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OmsOrderHandler {

    @Autowired
    private OmsOrderService omsOrderService;

    public Mono<ServerResponse> addOrder(ServerRequest serverRequest) {
        Mono<Order> orderDetail = serverRequest.bodyToMono(Order.class);
        return ServerResponse.ok().body(omsOrderService.addOrder(orderDetail), Order.class);
    }

    public Mono<ServerResponse> getAllOrders(ServerRequest serverRequest) {
        return ServerResponse.ok().body(omsOrderService.getAllOrders(), Order.class);
    }

    public Mono<ServerResponse> getAllOrdersForCustomer(ServerRequest serverRequest) {
        Long customerId = Long.valueOf(serverRequest.pathVariable("customerId"));
        return ServerResponse.ok().body(omsOrderService.getAllOrdersForCustomer(customerId), Order.class);
    }
}
