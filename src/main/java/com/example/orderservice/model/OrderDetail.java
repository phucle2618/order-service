package com.example.orderservice.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_details")
public class OrderDetail implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;

   @Column(name = "order_id", nullable = false)
   private Long orderId;

   @Column(name = "product_id", nullable = false)
   private Long productId;

   @Column(name = "product_name", length = 255)
   private String productName;

   @Column(name = "unit_price", precision = 13, scale = 2)
   private BigDecimal unitPrice;

   @Column(name = "quantity")
   private Integer quantity;

   @Column(name = "total_price", precision = 13, scale = 2)
   private BigDecimal totalPrice;

   public OrderDetail() {}

   public OrderDetail(Long id, Long orderId, Long productId, String productName,
                  BigDecimal unitPrice, Integer quantity, BigDecimal totalPrice) {
      this.id = id;
      this.orderId = orderId;
      this.productId = productId;
      this.productName = productName;
      this.unitPrice = unitPrice;
      this.quantity = quantity;
      this.totalPrice = totalPrice;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getOrderId() {
      return orderId;
   }

   public void setOrderId(Long orderId) {
      this.orderId = orderId;
   }

   public Long getProductId() {
      return productId;
   }

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   public String getProductName() {
      return productName;
   }

   public void setProductName(String productName) {
      this.productName = productName;
   }

   public BigDecimal getUnitPrice() {
      return unitPrice;
   }

   public void setUnitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
   }

   public Integer getQuantity() {
      return quantity;
   }

   public void setQuantity(Integer quantity) {
      this.quantity = quantity;
   }

   public BigDecimal getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(BigDecimal totalPrice) {
      this.totalPrice = totalPrice;
   }
}