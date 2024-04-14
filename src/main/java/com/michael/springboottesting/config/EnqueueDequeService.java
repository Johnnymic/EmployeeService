package com.michael.springboottesting.config;

import com.michael.springboottesting.EmployeeDto.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnqueueDequeService {

    private  final AmqpTemplate amqpTemplate;

    private static  final Logger LOGGER = LoggerFactory.getLogger(EnqueueDequeService.class);

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.routingkey}")
    String routingKey;
    public EnqueueDequeService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publishMessage(EmployeeDto customDto){
      LOGGER.info("Json message --> %s " + customDto.toString());
        amqpTemplate.convertAndSend(exchange,routingKey,customDto);
    }


}
