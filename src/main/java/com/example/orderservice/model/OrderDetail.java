package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import com.example.orderservice.model.Order;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetail implements Serializable {

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
}