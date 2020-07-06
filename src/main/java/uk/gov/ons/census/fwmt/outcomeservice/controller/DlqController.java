package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomeProcessPreprocessingDlq;

@Controller
public class DlqController {

  @Autowired
  OutcomeProcessPreprocessingDlq outcomeProcessPreprocessingDLQ;

  @Autowired
  SimpleMessageListenerContainer simpleMessageListenerContainer;

  @GetMapping("/ProcessDLQ")
  public ResponseEntity<String> startDLQProcessor() throws GatewayException {
    outcomeProcessPreprocessingDLQ.processDLQ();
    return ResponseEntity.ok("DLQ listener started.");
  }

  @GetMapping("/StartPreprocessorListener")
  public ResponseEntity<String> startPreprocessorListener() {
    simpleMessageListenerContainer.start();
    return ResponseEntity.ok("Queue listener started.");
  }

  @GetMapping("/StopPreprocessorListener")
  public ResponseEntity<String> stopPreprocessorListener() {
    simpleMessageListenerContainer.stop();
    return ResponseEntity.ok("Queue listener stopped.");
  }
}
