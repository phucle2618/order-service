package com.example.orderservice.repository;

import com.example.orderservice.model.OrderDetail;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class OrderDetailRepository {

   private final DataSource dataSource;

   private static final Logger logger = LoggerFactory.getLogger(OrderDetailRepository.class);

   public OrderDetailRepository(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public OrderDetail findById(Long id) {
      String sql = "SELECT * FROM order_details WHERE id = ?";
      try (
         Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ) {   
         preparedStatement.setLong(1, id);

         ResultSet resultSet = preparedStatement.executeQuery();
         logger.info("Executing query to find OrderDetail by id: " + id);
         logger.info(resultSet.toString());
         if (resultSet.next()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(resultSet.getLong("id"));
            orderDetail.setOrderId(resultSet.getLong("order_id"));
            orderDetail.setProductId(resultSet.getLong("product_id"));
            orderDetail.setProductName(resultSet.getString("product_name"));
            orderDetail.setUnitPrice(resultSet.getBigDecimal("unit_price"));
            orderDetail.setQuantity(resultSet.getInt("quantity"));
            orderDetail.setTotalPrice(resultSet.getBigDecimal("total_price"));
            return orderDetail;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public List<OrderDetail> getOrderDetails(Long orderId) {
      String sql = "SELECT * FROM order_details WHERE order_id = ?";
      try (
         Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ) {   
         preparedStatement.setLong(1, orderId);

         ResultSet resultSet = preparedStatement.executeQuery();
         List<OrderDetail> orderDetails = new ArrayList<>();
         while (resultSet.next()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(resultSet.getLong("id"));
            orderDetail.setOrderId(resultSet.getLong("order_id"));
            orderDetail.setProductId(resultSet.getLong("product_id"));
            orderDetail.setProductName(resultSet.getString("product_name"));
            orderDetail.setUnitPrice(resultSet.getBigDecimal("unit_price"));
            orderDetail.setQuantity(resultSet.getInt("quantity"));
            orderDetail.setTotalPrice(resultSet.getBigDecimal("total_price"));
            orderDetails.add(orderDetail);
         }
         return orderDetails;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return new ArrayList<>();
   }
}
