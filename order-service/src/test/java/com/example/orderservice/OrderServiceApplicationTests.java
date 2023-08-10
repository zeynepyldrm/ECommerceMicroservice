package com.example.orderservice;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreatePlaceOrder() throws Exception {
        OrderRequest orderRequest = getMockOrderRequest();
        String orderRequestString = objectMapper.writeValueAsString(orderRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Assertions.assertEquals(4,orderRepository.findAll().size());

    }

    private OrderRequest getMockOrderRequest() {
        List orderLines = new ArrayList();
        Order order = new Order();
        order.setOrderNumber("12");
        orderLines.add(order);

        return OrderRequest.builder().orderLineItemsDtoList(orderLines).build();
    }

}
