package com.example.orderservice.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {
   private List<CreateOrderDetailRequest> items;
   private BigInteger paymentId;
   private BigDecimal totalAmount;
}
