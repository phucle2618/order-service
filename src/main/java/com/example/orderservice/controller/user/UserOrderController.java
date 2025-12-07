package com.example.orderservice.controller.user;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.orderservice.model.Order;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.dto.UpdateOrderResponse;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.example.orderservice.logic.OrderLogic;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.CreateOrderResponse;

@RestController
@RequestMapping("/api")
public class UserOrderController {

   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private OrderLogic orderLogic;

   @Operation(summary = "Get order", description = "Returns order by order id with details.")
   @ApiResponse(responseCode = "200", description = "Get order by order id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
   @GetMapping(value = "/order/{id}", produces = "application/json")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<OrderResponse> getOrderById(
      @Parameter(description = "Order ID") @RequestParam(required = true) @PathVariable Long id
   ) {
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
   @PreAuthorize("hasRole('USER')")
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

   @PostMapping(value = "/orders", produces = "application/json")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<CreateOrderResponse> getOrderByUserId(Authentication authentication, @RequestBody CreateOrderRequest createOrderRequest) {
      Claims claims = (Claims) authentication.getDetails();
      Long userId = claims.get("userId", Long.class);
      CreateOrderResponse createOrderResponse = orderLogic.createOrder(userId, createOrderRequest);

      return ResponseEntity.ok(createOrderResponse);
   }

   @PutMapping(value = "/orders", produces = "application/json")
   @PreAuthorize("hasRole('USER')")
   public ResponseEntity<UpdateOrderResponse> getOrderByUserId(Authentication authentication, @RequestBody UpdateOrderRequest updateOrderRequest) {
      Claims claims = (Claims) authentication.getDetails();
      Long userId = claims.get("userId", Long.class);
      if (!updateOrderRequest.getUserId().equals(Long.valueOf(userId))) {
         return ResponseEntity.notFound().build();
      }
      UpdateOrderResponse createOrderResponse = orderLogic.updateOrder(updateOrderRequest);

      return ResponseEntity.ok(createOrderResponse);
   }
}
