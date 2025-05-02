package com.dss.spring_demo_order.controller;

import com.dss.spring_demo_order.entity.User;
import com.dss.spring_demo_order.entity.Order;
import com.dss.spring_demo_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final WebClient webClient;
    private final OrderService orderService;

    @Autowired
    public OrderController(@Value("${springdemo}") String userServiceBaseUrl, OrderService orderService) {
        this.webClient = WebClient.builder().baseUrl(userServiceBaseUrl).build();
        this.orderService = orderService;
    }

    private static final List<Order> ORDERS = Arrays.asList(
            new Order(1, 1, "Laptop"),
            new Order(2, 2, "Mobile"),
            new Order(3, 3, "Keyboard")
    );

    @GetMapping("/{orderId}")
    public Mono<String> getOrderWithUser(@PathVariable("orderId") Integer orderId) {
        Order order = ORDERS.stream().filter(orders -> orders.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(order)) {
            return Mono.just("Order not found!");
        }

        return webClient.get().uri("/users/" + order.getUserId())
                .retrieve()
                .bodyToMono(User.class)
                .map(user -> "OrderId: " + order.getId() + ", Product: " + order.getProduct() + ", Ordered by: " + user.getName());
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return webClient.get()
                .uri("/users/all")
                .retrieve()
                .bodyToFlux(User.class);
    }

    @GetMapping("/fetch_data")
    public Flux<String> fetchData() {
        return webClient.get()
                .uri("https://api.restful-api.dev/objects")
                .retrieve()
                .bodyToFlux(String.class);
    }

    @PostMapping("/save_order")
    public Order saveOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping("/get_orders/{userId}")
    public List<Order> fetchOrdersOfUser(@PathVariable Integer userId) {
        return orderService.fetchOrdersOfUser(userId);
    }
}
