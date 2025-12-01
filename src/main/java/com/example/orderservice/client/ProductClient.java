package com.example.orderservice.client;

import com.example.orderservice.dto.ProductResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.orderservice.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProductClient {

   @Autowired
   private final WebClientConfig webClient;

   @Value("${product.service.url}")
   private String productServiceUrl;

   public ProductClient(WebClientConfig webClient) {
      this.webClient = webClient;
   }

   public ProductResponse getProductById(Long id) {
      System.out.println("Fetching products for ID: " + id);
      ProductResponse prd = new ProductResponse();
      prd.setId(id);
      prd.setName("Sample Product");
      prd.setPrice(new java.math.BigDecimal("99.99"));
      return prd;
      // return webClient.webClient()
      //    .get()
      //    .uri(productServiceUrl + "/api/product/" + id)
      //    .retrieve()
      //    .bodyToMono(ProductResponse.class)
      //    .block();
   }
}
