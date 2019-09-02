package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DLQController {

    @Autowired
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @GetMapping("/ProcessDLQ")
    public ResponseEntity startListener() {
        rabbitListenerEndpointRegistry.getListenerContainer("rabbitDLQ");
        rabbitListenerEndpointRegistry.start();
        return ResponseEntity.ok("DLQ listener started.");
    }

    @GetMapping("/StopProcessingDLQ")
    public ResponseEntity stopListener() {
        rabbitListenerEndpointRegistry.getListenerContainer("rabbitDLQ");
        rabbitListenerEndpointRegistry.stop();
        return ResponseEntity.ok("DLQ listener stopped.");
    }
}
