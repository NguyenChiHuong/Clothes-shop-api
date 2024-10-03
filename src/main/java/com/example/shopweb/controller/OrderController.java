package com.example.shopweb.controller;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.OrderDto;
import com.example.shopweb.response.OrderResponse;
import com.example.shopweb.service.IOrderService;
import com.example.shopweb.util.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDto orderDto,
            BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse newOrder = orderService.createOrder(orderDto);
            return ResponseEntity.ok(newOrder);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") UUID userId){
        try{
            List<OrderResponse> ordersByUser = orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok(ordersByUser);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") UUID id){
        try{
            Optional<OrderResponse> getOrder = orderService.getOrder(id);
            return ResponseEntity.ok(getOrder);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody OrderDto orderDto){
        //Công việc của admin
        try{
            OrderResponse updateOrder = orderService.updateOrder(orderDto,id);
            return ResponseEntity.ok(updateOrder);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> deleteOrder(@Valid @PathVariable UUID id){
        //Xóa mềm, cập nhật trường active = false
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok(OrderResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.DELETE_ORDER_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(OrderResponse.builder()
                            .message(ex.getMessage())
                    .build());
        }
    }
}
