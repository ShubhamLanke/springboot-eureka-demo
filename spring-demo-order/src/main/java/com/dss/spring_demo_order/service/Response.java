package com.dss.spring_demo_order.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private ResponseStatus status;
    private String message;
    private T data;
}
