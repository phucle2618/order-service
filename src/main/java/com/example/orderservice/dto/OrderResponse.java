package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import com.example.orderservice.dto.OrderDetailResponse;
import java.util.Optional;

public class OrderResponse {

   private Long id;
   private Long userId;
   private OrderStatus status;  
   private BigDecimal totalAmount;
   private Long paymentId;

   private List<OrderDetailResponse> orderDetails;

   public OrderResponse(Long id, Long userId, OrderStatus status, BigDecimal totalAmount, Long paymentId, List<OrderDetailResponse> orderDetails) {
      this.id = id;
      this.userId = userId;
      this.status = status;
      this.totalAmount = totalAmount;
      this.paymentId = paymentId;
      this.orderDetails = orderDetails;
   }

   public Long getId() {
      return id;
   }

   public Long getUserId() {
      return userId;
   }
   public OrderStatus getStatus() {
      return status;
   }

   public BigDecimal getTotalAmount() {
      return totalAmount;
   }

   public Long getPaymentId() {
      return paymentId;
   }

   public List<OrderDetailResponse> getOrderDetails() {
      return orderDetails;
   }
}
