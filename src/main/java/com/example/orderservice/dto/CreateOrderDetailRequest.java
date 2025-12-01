package com.example.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderDetailRequest {
   private Long productId;
   private Integer quantity;
}
