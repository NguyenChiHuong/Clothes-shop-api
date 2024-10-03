package com.example.shopweb.service.impl;

import com.example.shopweb.dto.OrderDetailDto;
import com.example.shopweb.entity.OrderDetailEntity;
import com.example.shopweb.entity.OrderEntity;
import com.example.shopweb.entity.ProductEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.repository.OrderDetailRepository;
import com.example.shopweb.repository.OrderRepository;
import com.example.shopweb.repository.ProductRepository;
import com.example.shopweb.response.OrderDetailResponse;
import com.example.shopweb.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetaiService implements IOrderDetailService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetailEntity createOrderDetail(OrderDetailDto orderDetailDto) throws Exception {
        OrderEntity order = orderRepository.findById(orderDetailDto.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Order id "+orderDetailDto.getOrderId()+"not found"));

        ProductEntity product = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Product id "+orderDetailDto.getProductId()+"not found"));

        //Cập nhật các trường của đơn hàng từ dto
        OrderDetailEntity orderDetailEntity = OrderDetailEntity.builder()
                .order(order)
                .product(product)
                .price(orderDetailDto.getPrice())
                .color(orderDetailDto.getColor())
                .numberOfProducts(orderDetailDto.getNumberOfProducts())
                .totalMoney(orderDetailDto.getTotalMoney())
                .build();

        return orderDetailRepository.save(orderDetailEntity);
    }

    @Override
    public OrderDetailEntity updateOrderDetail(OrderDetailDto orderDetailDto, UUID id) throws Exception {
        OrderDetailEntity existOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Order Detail "+id+" not found"));

        OrderEntity existOrder  = orderRepository.findById(orderDetailDto.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Order id "+orderDetailDto.getOrderId()+" not found"));

        ProductEntity existProduct = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(()-> new DataNotFoundException("Product id "+orderDetailDto.getProductId()+" not found"));


        existOrderDetail.setOrder(existOrder);
        existOrderDetail.setProduct(existProduct);
        existOrderDetail.setPrice(orderDetailDto.getPrice());
        existOrderDetail.setColor(orderDetailDto.getColor());
        existOrderDetail.setNumberOfProducts(orderDetailDto.getNumberOfProducts());
        existOrderDetail.setTotalMoney(orderDetailDto.getTotalMoney());
        return orderDetailRepository.save(existOrderDetail);
    }

    @Override
    public Optional<OrderDetailResponse> getOrderDetail(UUID id){
        return orderDetailRepository.findById(id).map(OrderDetailResponse::fromOrderDetail);
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailsByOrder(UUID id) {
        return orderDetailRepository.findByOrderId(id).stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderDetail(UUID id) {
        orderDetailRepository.deleteById(id);
    }
}
