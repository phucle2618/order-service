package com.example.orderservice.logic;

import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.orderservice.repository.OrderDetailRepository;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.CreateOrderDetailRequest;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.CreateOrderResponse;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.ProductResponse;
import com.example.orderservice.dto.UpdateOrderDetailRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.dto.UpdateOrderResponse;
import com.example.orderservice.dto.OrderDetailResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderDetail;
import com.example.orderservice.model.OrderStatus;
import java.util.List;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.sql.Connection;

import org.springframework.stereotype.Service;

@Service
public class OrderLogic {
   @Autowired
   private OrderRepository orderRepository;

   @Autowired
   private OrderDetailRepository orderDetailRepository;

   @Autowired
   private ProductClient productClient;

   private final DataSource dataSource;

   public OrderLogic(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public CreateOrderResponse createOrder(Long userId, CreateOrderRequest createOrderRequest) {
      Connection connection = null;
      try {
         connection = dataSource.getConnection();
         connection.setAutoCommit(false);
         BigDecimal totalAmount = BigDecimal.ZERO;
         Order order = new Order();
         List<OrderDetail> orderDetails = new ArrayList<>();
         order.setUserId(userId);
         order.setStatus(OrderStatus.PENDING);
         for (CreateOrderDetailRequest orderDetailRequest : createOrderRequest.getItems()) {
            ProductResponse product = productClient.getProductById(orderDetailRequest.getProductId());
            if (product == null) {
               throw new RuntimeException("Product not found!");
            }
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderDetailRequest.getQuantity()));
            totalAmount = totalAmount.add(totalPrice);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setTotalPrice(totalPrice);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetails.add(orderDetail);
         }
         order.setTotalAmount(totalAmount);
         orderRepository.save(order, connection);
         for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrderId(order.getId());
         }
         orderDetailRepository.saveAll(orderDetails, connection);
         connection.commit();

         return new CreateOrderResponse(order.getId());
      } catch (Exception e) {
         if (connection != null) {
            try {
               connection.rollback();
            } catch (Exception rollbackEx) {
               rollbackEx.printStackTrace();
            }
         }
         throw new RuntimeException("Failed to create order: " + e.getMessage());
      } finally {
         if (connection != null) {
            try {
               connection.setAutoCommit(true);
               connection.close();
            } catch (Exception closeEx) {
               closeEx.printStackTrace();
            }
         }
      }
   }

   public OrderResponse findById(Long id, Order order) {
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

      return orderResponse;
   }

   public List<OrderResponse> findByUserId(Long userId) {
      List<Order> orders = orderRepository.findByUserId(userId);
      List<OrderResponse> ordersResponse = new ArrayList<>();
      ordersResponse = orders.stream().map(orderItem -> {
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

      return ordersResponse;
   }

   public UpdateOrderResponse updateOrder(UpdateOrderRequest updateOrderRequest) {
      Connection connection = null;
      try {
         connection = dataSource.getConnection();
         connection.setAutoCommit(false);
         BigDecimal totalAmount = BigDecimal.ZERO;
         Order order = orderRepository.findById(updateOrderRequest.getOrderId());
         if (order == null) {
            throw new RuntimeException("Order not found!");
         }
         order.setStatus(updateOrderRequest.getStatus());
         order.setPaymentId(updateOrderRequest.getPaymentId());
         for (UpdateOrderDetailRequest orderDetailRequest : updateOrderRequest.getItems()) {
            ProductResponse product = productClient.getProductById(orderDetailRequest.getProductId());
            if (product == null) {
               throw new RuntimeException("Product not found!");
            }
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderDetailRequest.getQuantity()));
            totalAmount = totalAmount.add(totalPrice);
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailRequest.getId());
            if (orderDetail == null) {
               throw new RuntimeException("Order detail not found!");
            }
            orderDetail.setProductId(product.getId());
            orderDetail.setProductName(product.getName());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setTotalPrice(totalPrice);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetailRepository.save(orderDetail, connection);
         }
         order.setTotalAmount(totalAmount);
         orderRepository.save(order, connection);
         connection.commit();
         
         return new UpdateOrderResponse(
            order.getId(),
            order.getUserId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getPaymentId(),
            null
         );
      } catch (Exception e) {
         if (connection != null) {
            try {
               connection.rollback();
            } catch (Exception rollbackEx) {
               rollbackEx.printStackTrace();
            }
         }
         throw new RuntimeException("Failed to update order: " + e.getMessage());
      } finally {
         if (connection != null) {
            try {
               connection.setAutoCommit(true);
               connection.close();
            } catch (Exception closeEx) {
               closeEx.printStackTrace();
            }
         }
      }
   }
}
