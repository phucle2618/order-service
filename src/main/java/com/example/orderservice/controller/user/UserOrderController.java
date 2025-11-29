package com.example.orderservice.controller.user;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.model.OrderDetail;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.OrderDetailResponse;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.Collection;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api")
public class UserOrderController {

   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private OrderDetailRepository orderDetailRepository;

   @GetMapping(value = "/order/{id}", produces = "application/json")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
      Order order = orderRepository.findById(id);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Claims claims = (Claims) authentication.getDetails();
      String userId = claims.get("userId", Integer.class).toString();
      if (!order.getUserId().equals(Long.valueOf(userId))) {
         return ResponseEntity.notFound().build();
      }
      if (order == null) {
         return ResponseEntity.notFound().build();
      }
      List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(id).stream()
         .filter(od -> od.getOrderId().equals(id))
         .map(od -> new OrderDetailResponse(
            od.getId(),
            od.getProductId(),
            od.getProductName(),
            od.getUnitPrice(),
            od.getQuantity(),
            od.getTotalPrice()
         ))
         .toList();

      OrderResponse orderResponse = new OrderResponse(
         order.getId(),
         order.getUserId(),
         order.getStatus(),
         order.getTotalAmount(),
         order.getPaymentId(),
         orderDetails
      );

      return ResponseEntity.ok(orderResponse);
   }

   @GetMapping(value = "/orders", produces = "application/json")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<List<OrderResponse>> getOrderByUserId(Authentication authentication) {
      Claims claims = (Claims) authentication.getDetails();
      Long userId = claims.get("userId", Long.class);
      List<Order> orders = orderRepository.findByUserId(userId);
      if (orders.isEmpty()) {
         return ResponseEntity.notFound().build();
      }
      List<OrderResponse> ordersResponse = orders.stream().map(orderItem -> {
         List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(orderItem.getId()).stream()
            .map(od -> new OrderDetailResponse(
               od.getId(),
               od.getProductId(),
               od.getProductName(),
               od.getUnitPrice(),
               od.getQuantity(),
               od.getTotalPrice()
            ))
            .toList();

         return new OrderResponse(
            orderItem.getId(),
            orderItem.getUserId(),
            orderItem.getStatus(),
            orderItem.getTotalAmount(),
            orderItem.getPaymentId(),
            orderDetails
         );
      }).toList();

      return ResponseEntity.ok(ordersResponse);
   }
}
