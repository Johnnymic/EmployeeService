package com.michael.springboottesting.config;

import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    @Value("${rabbitmq.queue}")
    String queueName;

    @Value("${rabbitmq.exchange}")
    String exchange;

    @RabbitListener(queues = "${rabbitmq.queue}" )
    @RabbitHandler
    public void handleMessage(EmployeeDto user){
      log.info("Received Message from queue " + queueName + ": " + user.toString());

    }
}
