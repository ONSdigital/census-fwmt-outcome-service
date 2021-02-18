package uk.gov.ons.census.fwmt.outcomeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.rabbit.QueueMigrator;

@Slf4j
@RestController
@RequestMapping("/jobs")
public class RabbitQueueController {

  public static final String originQ = "Outcome.PreprocesingDLQ";
  public static final String destRoute = "Outcome.Preprocessing.Request";
  public static final String exchange = "Outcome.Preprocessing.Exchange";

  @Autowired
  private QueueMigrator queueMigrator;

  @GetMapping(value = "/migratequeue")
  public ResponseEntity<String> migrateQueue(@RequestParam(defaultValue = "Outcome.PreprocesingDLQ") String originQ,
      @RequestParam(defaultValue = "Outcome.Preprocessing.Request") String destRoute, @RequestParam(defaultValue = "Outcome.Preprocessing.Exchange") String exchange) {

    try {
      return ResponseEntity.ok(queueMigrator.migrate(originQ, destRoute, exchange));
    } catch (GatewayException e) {
      log.error("Failed to send message from Q {} to route {}", originQ, destRoute);
      return ResponseEntity.badRequest().body("Failed to move messages from " + originQ + "to Route " + destRoute);
    }

  }
}
