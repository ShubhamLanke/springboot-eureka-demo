package com.dss.springdemo.config;

import com.dss.springdemo.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private ResponseStatus status;
    private String message;
    private T data;
}
