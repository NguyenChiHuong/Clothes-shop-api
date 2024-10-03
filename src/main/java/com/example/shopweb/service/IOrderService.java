package com.example.shopweb.service;

import com.example.shopweb.dto.OrderDto;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.response.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderService {
    OrderResponse createOrder(OrderDto orderDto) throws DataNotFoundException;

    OrderResponse updateOrder(OrderDto orderDto, UUID id) throws DataNotFoundException;

    Optional<OrderResponse> getOrder(UUID id) throws DataNotFoundException;

    List<OrderResponse> getAllOrdersByUserId(UUID id);

    void deleteOrder(UUID id);
}
