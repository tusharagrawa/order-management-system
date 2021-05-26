package com.intuit.oms.router;

import com.intuit.oms.handler.OmsProductHandler;
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
public class OmsProductRouter {

    @Autowired
    private OmsProductHandler omsProductHandler;

    @Value("${get-all-products-uri}")
    private String getAllProductsUrl;

    @Value("${retrieve-product-configuration-uri}")
    private String retrieveProductConfiguration;

    @Value("${update-product-configuration-uri}")
    private String updateProductConfiguration;


    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET(getAllProductsUrl, omsProductHandler::getAllProducts)
                .GET(retrieveProductConfiguration, omsProductHandler::retrieveProductConfiguration)
                .POST(updateProductConfiguration, omsProductHandler::updateProductConfiguration)
                .build();
    }
}
