package com.dss.spring_demo_order.service;

import com.dss.spring_demo_order.entity.Order;
import com.dss.spring_demo_order.feign.OrderInterface;
import com.dss.spring_demo_order.repository.OrderRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderInterface orderInterface;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderInterface orderInterface) {
        this.orderRepository = orderRepository;
        this.orderInterface = orderInterface;
    }

    public List<Order> fetchAllOrders(){
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> fetchOrdersOfUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) return Collections.emptyList();
        return orders;
    }

    public Optional<Order> fetchOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }
}
