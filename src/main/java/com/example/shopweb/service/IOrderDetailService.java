package com.example.shopweb.service;

import com.example.shopweb.dto.OrderDetailDto;
import com.example.shopweb.entity.OrderDetailEntity;
import com.example.shopweb.response.OrderDetailResponse;
import com.example.shopweb.response.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderDetailService {
    OrderDetailEntity createOrderDetail(OrderDetailDto orderDetailDto) throws Exception;

    OrderDetailEntity updateOrderDetail(OrderDetailDto orderDetailDto, UUID id) throws Exception;

    Optional<OrderDetailResponse> getOrderDetail(UUID id);

    List<OrderDetailResponse> getOrderDetailsByOrder(UUID id);

    void deleteOrderDetail(UUID id);
}
