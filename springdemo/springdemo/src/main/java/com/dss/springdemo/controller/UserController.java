package com.dss.springdemo.controller;


import com.dss.springdemo.config.Response;
import com.dss.springdemo.controller.dto.OrderDTO;
import com.dss.springdemo.entity.User;
import com.dss.springdemo.feign.UserInterface;
import com.dss.springdemo.service.UserService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final UserInterface userInterface;

    @Autowired
    public UserController(RestTemplate restTemplate, UserService userService, UserInterface userInterface) {
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.userInterface = userInterface;
    }

    @PostMapping("/save_user")
    public ResponseEntity<Response<User>> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/update_user/{id}")
    public ResponseEntity<Response<User>> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllOrders());
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<Response<?>> getUserWithOrders(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getUserWithOrders(userId));
    }

    @PostMapping("/place_order")
    public ResponseEntity<Response<OrderDTO>> placeOrder(@RequestBody OrderDTO orderRequest) {
        return ResponseEntity.ok(userService.placeOrder(orderRequest));
    }

}