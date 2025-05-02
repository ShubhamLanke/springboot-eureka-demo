package com.dss.springdemo.service;

import com.dss.springdemo.ResponseStatus;
import com.dss.springdemo.config.Response;
import com.dss.springdemo.controller.dto.OrderDTO;
import com.dss.springdemo.entity.User;
import com.dss.springdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Autowired
    public UserService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
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


    public Response<?> getUserWithOrders(String orderServiceBaseUrl, Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            return new Response<>(ResponseStatus.ERROR, "User not found with ID: " + userId, null);

        User user = optionalUser.get();

        String url = orderServiceBaseUrl + "/get_orders/" + userId;
        ResponseEntity<OrderDTO[]> response = restTemplate.getForEntity(url, OrderDTO[].class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("User", user);
            result.put("Order", List.of(response.getBody()));
            return new Response<>(ResponseStatus.SUCCESS, "Fetched orders of user successfully!", result);
        } else {
            return new Response<>(ResponseStatus.ERROR, "Error while fetching the orders.", null);
        }
    }
}