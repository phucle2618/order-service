package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import com.example.orderservice.model.OrderDetail;
import com.example.orderservice.model.OrderStatus;
import java.util.Optional;

@Entity
@Data
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_amount", precision = 13, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "payment_id")
    private Long paymentId;
    
    public Order() {}
}
