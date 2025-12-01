package com.example.orderservice.dto;

import java.math.BigDecimal;
import com.example.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.example.orderservice.dto.UpdateOrderDetailRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderRequest {
   private Long orderId;
   private Long userId;
   private OrderStatus status;
   private Long paymentId;
   private List<UpdateOrderDetailRequest> items;
}
