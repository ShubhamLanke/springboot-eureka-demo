package com.dss.springdemo.service;

import com.dss.springdemo.config.ResponseStatus;
import com.dss.springdemo.config.Response;
import com.dss.springdemo.controller.dto.OrderDTO;
import com.dss.springdemo.entity.User;
import com.dss.springdemo.feign.UserInterface;
import com.dss.springdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final UserInterface userInterface;

    @Autowired
    public UserService(RestTemplate restTemplate, UserRepository userRepository, UserInterface userInterface) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.userInterface = userInterface;
    }

    public Response<User> saveUser(User user) {
        if (user.getName() == null || user.getEmail() == null) {
            return new Response<>(ResponseStatus.ERROR, "Name or email must not be null", null);
        }
        User savedUser = userRepository.save(user);
        return new Response<>(ResponseStatus.SUCCESS, "User saved successfully", savedUser);
    }

    public Response<User> updateUser(Integer id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return new Response<>(ResponseStatus.ERROR, "User not found with id: " + id, null);
        }

        User user = existingUser.get();
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());

        User savedUser = userRepository.save(user);
        return new Response<>(ResponseStatus.SUCCESS, "User updated successfully", savedUser);
    }

    public Response<List<User>> fetchAllOrders() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) return new Response<>(ResponseStatus.ERROR, "User not found.", null);
        return new Response<>(ResponseStatus.SUCCESS, "User found.", users);
    }

    public Response<User> getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new Response<>(ResponseStatus.SUCCESS, "User found", value))
                .orElseGet(() -> new Response<>(ResponseStatus.ERROR, "User not found with ID: " + id, null));
    }


    public Response<?> getUserWithOrders(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return new Response<>(ResponseStatus.ERROR, "User not found with ID: " + userId, null);

        User user = optionalUser.get();
        List<OrderDTO> response = userInterface.getUserWithOrders(userId);
        if (! response.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("User", user);
            result.put("Order",response);
            return new Response<>(ResponseStatus.SUCCESS, "Fetched orders of user successfully!", result);
        } else {
            return new Response<>(ResponseStatus.ERROR, "Error while fetching the orders.", null);
        }
    }

    public Response<String> placeOrder(OrderDTO orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderDTO orderDTO = userInterface.placeOrder(orderRequest);

        if (response.getStatusCode() == HttpStatus.OK) {
            return new Response<>(ResponseStatus.SUCCESS, "Order placed successfully", null);
        } else {
            return new Response<>(ResponseStatus.ERROR, "Failed to place order", null);
        }
    }
    }
}