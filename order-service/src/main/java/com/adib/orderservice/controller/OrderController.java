package com.adib.orderservice.controller;

import com.adib.orderservice.dto.Order;
import com.adib.orderservice.dto.OrderEvent;
import com.adib.orderservice.publisher.OrderProducer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private OrderProducer orderProducer;

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {
        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent event = new OrderEvent();
        event.setStatus("PENDING");
        event.setMessage("Order is in pending status");
        event.setOrder(order);

        orderProducer.sendMessage(event);

        return "Order sent to the RabbitMQ";
    }

}
