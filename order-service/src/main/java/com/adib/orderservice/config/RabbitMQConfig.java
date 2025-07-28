package com.adib.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.order.queue.name}")
    private String orderQueue;

    @Bean
    public Queue queue()
    {
        return new Queue(orderQueue);
    }

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Bean
    public TopicExchange exchange()
    {
        return new TopicExchange(exchange);
    }

    @Value("${rabbitmq.binding.routing.key}")
    private String orderRoutingKey;

    @Bean
    public Binding binding()
    {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(orderRoutingKey);
    }

    @Bean
    public MessageConverter converter()
    {
        return new Jackson2JsonMessageConverter();
    }

    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
