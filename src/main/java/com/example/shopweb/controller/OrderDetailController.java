package com.example.shopweb.controller;

import com.example.shopweb.constant.Constant;
import com.example.shopweb.dto.OrderDetailDto;
import com.example.shopweb.entity.OrderDetailEntity;
import com.example.shopweb.response.OrderDetailResponse;
import com.example.shopweb.response.OrderResponse;
import com.example.shopweb.service.IOrderDetailService;
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
@RequestMapping("${api.prefix}/order_detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDto orderDetailDto,
            BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderDetailEntity newOrderDetail = orderDetailService.createOrderDetail(orderDetailDto);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") UUID id){
        try{
            Optional<OrderDetailResponse> getOrderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(getOrderDetail);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //Lấy ra danh sách order details của 1 order
    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("order_id") UUID orderId){
        try{
            List<OrderDetailResponse> getOrderDetailsByOrder = orderDetailService.getOrderDetailsByOrder(orderId);
            return ResponseEntity.ok(getOrderDetailsByOrder);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody OrderDetailDto orderDetailDto){
        try{
            OrderDetailEntity updateOrderDetail = orderDetailService.updateOrderDetail(orderDetailDto, id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(updateOrderDetail));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> deleteOrderDetail(@Valid @PathVariable("id") UUID id){
        try{
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok(OrderDetailResponse.builder()
                            .message(localizationUtil.getLocalizedMessage(Constant.DELETE_ORDER_DETAIL_SUCCESSFULLY))
                    .build());
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(OrderDetailResponse.builder()
                            .message(ex.getMessage())
                    .build());
        }
    }

}
