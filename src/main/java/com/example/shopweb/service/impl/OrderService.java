package com.example.shopweb.service.impl;

import com.example.shopweb.dto.OrderDto;
import com.example.shopweb.entity.OrderEntity;
import com.example.shopweb.entity.UserEntity;
import com.example.shopweb.exceptions.DataNotFoundException;
import com.example.shopweb.repository.OrderRepository;
import com.example.shopweb.repository.UserRepository;
import com.example.shopweb.response.OrderResponse;
import com.example.shopweb.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDto orderDto) throws DataNotFoundException {
        UserEntity user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(()-> new DataNotFoundException("User not found"));

        //Tạo một luồng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDto.class, OrderEntity.class)
                .addMappings(mapper -> mapper.skip(OrderEntity::setId));

        //Cập nhật các trường của đơn hàng từ dto
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setOrderDate(new Date());
        orderEntity.setStatus(OrderEntity.UserStatus.pending);

        // Ánh xạ các trường từ OrderDto sang OrderEntity
        modelMapper.map(orderDto, orderEntity);

        //Kiểm tra shipping date phải lớn hơn nga hôm nay
        LocalDate shippingDate = orderDto.getShippingDate() == null ? LocalDate.now() : orderDto.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today");
        }
        orderEntity.setShippingDate(shippingDate);
        orderEntity.setActive(true);
        OrderEntity newOrder= orderRepository.save(orderEntity);

        modelMapper.typeMap(OrderEntity.class, OrderResponse.class)
                .addMappings(mapper -> {
                    mapper.map(OrderEntity::getOrderDate, OrderResponse::setOrderDate);
                });

        // Ánh xạ từ OrderEntity sang OrderResponse và trả về
        OrderResponse orderResponse = modelMapper.map(newOrder, OrderResponse.class);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(OrderDto orderDto, UUID id) throws DataNotFoundException {

        OrderEntity existOrder = orderRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Order id "+id+" not found"));

        UserEntity existUser = userRepository.findById(orderDto.getUserId())
                .orElseThrow(()-> new DataNotFoundException("User id "+orderDto.getUserId()+" not found"));

        //Tạo một luồng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDto.class, OrderEntity.class)
                .addMappings(mapper -> mapper.skip(OrderEntity::setId));

        // Ánh xạ các trường từ OrderDto sang OrderEntity
        modelMapper.map(orderDto, existOrder);
        existOrder.setUser(existUser);

        //Kiểm tra shipping date phải lớn hơn hoặc bằng shipping date cũ
        LocalDate shippingDate = orderDto.getShippingDate() == null
                ? existOrder.getShippingDate() : orderDto.getShippingDate();
        if(shippingDate.isBefore(existOrder.getShippingDate())) {
            throw new DataNotFoundException("Date must be at least shipping product old");
        }
        existOrder.setShippingDate(shippingDate);
        existOrder.setActive(true);
        OrderEntity updateOrder= orderRepository.save(existOrder);

        modelMapper.typeMap(OrderEntity.class, OrderResponse.class)
                .addMappings(mapper -> {
                    mapper.map(OrderEntity::getOrderDate, OrderResponse::setOrderDate);
                });

        // Ánh xạ từ OrderEntity sang OrderResponse và trả về
        OrderResponse orderResponse = modelMapper.map(updateOrder, OrderResponse.class);
        return orderResponse;
    }

    @Override
    public Optional<OrderResponse> getOrder(UUID id) throws DataNotFoundException {
        return orderRepository.findById(id).map(OrderResponse::fromOrder);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(UUID id) {
        return orderRepository.findByUserId(id).stream()
                .map(OrderResponse::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(UUID id) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if(order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }
}
