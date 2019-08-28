package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.config.OutcomePreprocessingQueueConfig;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.io.IOException;
import java.time.LocalTime;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Autowired
  private MessageConverter messageConverter;

  @Autowired
  private OutcomeService outcomeService;

  public void receiveMessage(String message) throws GatewayException {
    log.info("Received a message in Outcome queue");
    processStoredMessage(message);
  }

  private void processStoredMessage(String actualMessage) throws GatewayException {
    String outcomeSurveyType = null;

    if (actualMessage.contains("HouseholdObject")) {
      outcomeSurveyType = "HH";
    } else if (actualMessage.contains("CCSPL")) {
      outcomeSurveyType = "CCPL";
    } else if (actualMessage.contains("CCSINT")) {
      outcomeSurveyType = "CCSINT";
    }

    switch (outcomeSurveyType) {
      case "HH":
        HouseholdOutcome householdOutcome = messageConverter.convertMessageToDTO(HouseholdOutcome.class,
                actualMessage);
        outcomeService.createHouseHoldOutcomeEvent(householdOutcome);
        break;
      case "CCSPL":
//        CancelFieldWorkerJobRequest fwmtCancelJobRequest = messageConverter.convertMessageToDTO(CancelFieldWorkerJobRequest.class,
//                actualMessage);
//        gatewayEventManager
//                .triggerEvent(caseId.asText(), CANONICAL_CANCEL_RECEIVED, LocalTime.now());
//        jobService.cancelJob(fwmtCancelJobRequest);
        break;
      case "CCSINT":
//        UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = messageConverter.convertMessageToDTO(UpdateFieldWorkerJobRequest.class,
//                actualMessage);
//        gatewayEventManager
//                .triggerEvent(caseId.asText(), CANONICAL_UPDATE_RECEIVED, LocalTime.now());
//        jobService.updateJob(fwmtUpdateJobRequest);
        break;
      default:
        //throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot process message for case ID " + caseId.asText());
    }
    //log.info("Sending " + caseId.asText() + " job to TM");
  }
}
