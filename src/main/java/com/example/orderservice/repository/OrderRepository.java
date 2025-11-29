package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

   private final DataSource dataSource;

   public OrderRepository(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public Order findById(Long id) {
      String sql = "SELECT * FROM orders WHERE id = ?";
      try (
         Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ) {   
         preparedStatement.setLong(1, id);

         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setUserId(resultSet.getLong("user_id"));
            order.setStatus(com.example.orderservice.model.OrderStatus.valueOf(resultSet.getString("status")));
            order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
            order.setPaymentId(resultSet.getLong("payment_id"));
            return order;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public List<Order> findByUserId(Long userId) {
      String sql = "SELECT * FROM orders WHERE user_id = ?";
      List<Order> orders = new ArrayList<>();
      try (
         Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ) {   
         preparedStatement.setLong(1, userId);

         ResultSet resultSet = preparedStatement.executeQuery();
         while (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setUserId(resultSet.getLong("user_id"));
            order.setStatus(com.example.orderservice.model.OrderStatus.valueOf(resultSet.getString("status")));
            order.setTotalAmount(resultSet.getBigDecimal("total_amount"));
            order.setPaymentId(resultSet.getLong("payment_id"));
            orders.add(order);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return orders;
   }
}
