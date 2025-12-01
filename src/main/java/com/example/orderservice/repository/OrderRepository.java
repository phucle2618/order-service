package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

   /**
    * Data source for database connections
    */
   private final DataSource dataSource;

   /**
    * Constructor
    * @param dataSource
    */
   public OrderRepository(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   /**
    * Find order by ID
    * @param id
    * @return
    */
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
   /**
    * Find orders by user ID
    * @param userId
    * @return
    */
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

   /**
    * Save order to database
    * @param order
    * @return
    */
   public Order save(Order order, Connection externalConnection) throws SQLException {
      String sql = "";
      Connection connection = externalConnection;
      if (order.getId() == null) {
         sql = "INSERT INTO orders (user_id, status, total_amount, payment_id) VALUES (?, ?, ?, ?)";
      } else {
         sql = "UPDATE orders SET user_id = ?, status = ?, total_amount = ?, payment_id = ? WHERE id = ?";
      }
      try {
         if (connection == null) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(true);
         }
         PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
         preparedStatement.setLong(1, order.getUserId());
         preparedStatement.setString(2, order.getStatus().name());
         preparedStatement.setBigDecimal(3, order.getTotalAmount());
         System.out.println("Total: " + order.getTotalAmount());
         if (order.getPaymentId() != null) {
            preparedStatement.setLong(4, order.getPaymentId());
         } else {
            preparedStatement.setNull(4, java.sql.Types.BIGINT);
         }
         if (order.getId() != null) {
            preparedStatement.setLong(5, order.getId());
         }
         int affectedRows = preparedStatement.executeUpdate();
         if (affectedRows == 0) {
            throw new RuntimeException("Creating order failed, no rows affected.");
         }

         try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next() && order.getId() == null) {
               order.setId(generatedKeys.getLong(1));
            } else {
               throw new RuntimeException("Creating order failed, no ID obtained.");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return order;
   }
}
