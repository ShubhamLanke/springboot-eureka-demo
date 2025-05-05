package com.dss.spring_demo_order.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("SPRING-DEMO-ORDER")
public interface OrderInterface {
}
