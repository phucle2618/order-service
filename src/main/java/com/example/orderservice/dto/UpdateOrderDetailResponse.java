package com.example.orderservice.dto;

import java.math.BigDecimal;

public class UpdateOrderDetailResponse {
   private Long id;
   private Long productId;
   private String productName;
   private BigDecimal unitPrice;
   private Integer quantity;
   private BigDecimal totalPrice;
}
