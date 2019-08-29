package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

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
        break;
      case "CCSINT":
        break;
      default:
//        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot process message for case ID " + caseId.asText());
    }
//    log.info("Sending " + caseId.asText() + " job to TM");
  }
}
