package com.dss.springdemo.feign;

import com.dss.springdemo.config.Response;
import com.dss.springdemo.controller.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "SPRING-DEMO-ORDER", path = "orders")
public interface UserInterface {

    @GetMapping(value = "/get_orders/{userId}",
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public List<OrderDTO> getUserWithOrders(@PathVariable Integer userId);

    @PostMapping("/place_order")
    public OrderDTO placeOrder(@RequestBody OrderDTO orderRequest);

}