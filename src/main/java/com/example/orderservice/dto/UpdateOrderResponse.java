package com.example.orderservice.dto;

import java.math.BigDecimal;
import com.example.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderResponse {
   private Long orderId;
   private Long userId;
   private BigDecimal totalAmount;
   private OrderStatus status;
   private Long paymentId;
   private List<UpdateOrderDetailResponse> items;
}
