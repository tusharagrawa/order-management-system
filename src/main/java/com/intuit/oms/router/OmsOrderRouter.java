package com.intuit.oms.router;

import com.intuit.oms.handler.OmsOrderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Slf4j
@Configuration
public class OmsOrderRouter {

    @Autowired
    private OmsOrderHandler omsOrderHandler;

    @Value("${add-order-uri}")
    private String addOrderUrl;

    @Value("${get-all-order-uri}")
    private String getAllOrdersUrl;

    @Value("${get-customer-order-uri}")
    private String getAllOrdersForCustomerUrl;


    @Bean
    public RouterFunction<ServerResponse> orderFunction() {
        return RouterFunctions.route()
                .POST(addOrderUrl, omsOrderHandler::addOrder)
                .GET(getAllOrdersUrl, omsOrderHandler::getAllOrders)
                .GET(getAllOrdersForCustomerUrl, omsOrderHandler::getAllOrdersForCustomer)
                .build();
    }
}
