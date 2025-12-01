package com.example.orderservice.controller.admin;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.orderservice.model.Order;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.dto.UpdateOrderResponse;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;
import com.example.orderservice.logic.OrderLogic;

@RestController
@RequestMapping("/admin")
public class AdminOrderController {

   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private OrderLogic orderLogic;

   @GetMapping(value = "/order/{id}", produces = "application/json")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
      Order order = orderRepository.findById(id);
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      Claims claims = (Claims) authentication.getDetails();
      String userId = claims.get("userId", Integer.class).toString();
      if (!order.getUserId().equals(Long.valueOf(userId))) {
         return ResponseEntity.notFound().build();
      }
      OrderResponse orderResponse = orderLogic.findById(id, order);

      return ResponseEntity.ok(orderResponse);
   }

   @GetMapping(value = "/orders", produces = "application/json")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<OrderResponse>> getOrderByUserId(Authentication authentication) {
      Claims claims = (Claims) authentication.getDetails();
      Long userId = claims.get("userId", Long.class);
      List<Order> orders = orderRepository.findByUserId(userId);
      if (orders.isEmpty()) {
         return ResponseEntity.notFound().build();
      }
      List<OrderResponse> ordersResponse = orderLogic.findByUserId(userId);

      return ResponseEntity.ok(ordersResponse);
   }

   @PutMapping(value = "/orders", produces = "application/json")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<UpdateOrderResponse> getOrderByUserId(@RequestBody UpdateOrderRequest updateOrderRequest) {
      UpdateOrderResponse createOrderResponse = orderLogic.updateOrder(updateOrderRequest);

      return ResponseEntity.ok(createOrderResponse);
   }
}
