package com.example.orderservice.dto;

import java.math.BigDecimal;

public class OrderDetailResponse {

   private Long id;
   private Long productId;
   private String productName;
   private BigDecimal unitPrice;
   private Integer quantity;
   private BigDecimal totalPrice;

   public OrderDetailResponse(Long id, Long productId, String productName,
                          BigDecimal unitPrice, Integer quantity, BigDecimal totalPrice) {
      this.id = id;
      this.productId = productId;
      this.productName = productName;
      this.unitPrice = unitPrice;
      this.quantity = quantity;
      this.totalPrice = totalPrice;
   }

   public Long getId() {
      return id;
   }

   public Long getProductId() {
      return productId;
   }

   public String getProductName() {
      return productName;
   }

   public BigDecimal getUnitPrice() {
      return unitPrice;
   }

   public Integer getQuantity() {
      return quantity;
   }

   public BigDecimal getTotalPrice() {
      return totalPrice;
   }
}