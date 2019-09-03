package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Autowired
  private OutcomeService outcomeService;

  @Autowired
  private OutcomeMessageConverter outcomeMessageConverter;

  public void receiveMessage(HashMap message) throws GatewayException {
    log.info("Received a message in Outcome queue");
    processStoredMessage(message);
  }

  private void processStoredMessage(HashMap actualMessage) throws GatewayException {
    JsonNode actualMessageRootNode;
    String outcomeSurveyType;
    String processedMessage;

    Map<String, String> messageRemapped = new HashMap<>(actualMessage);

    Map.Entry<String, String> entry = messageRemapped.entrySet().iterator().next();

    outcomeSurveyType = entry.getKey();
    processedMessage = entry.getValue();

    try {
      actualMessageRootNode = jsonObjectMapper.readTree(processedMessage);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }

    JsonNode caseId = actualMessageRootNode.path("caseId");


    switch (outcomeSurveyType) {
      case "Household":
        HouseholdOutcome householdOutcome = outcomeMessageConverter.convertMessageToDTO(HouseholdOutcome.class,
                processedMessage);
        outcomeService.createHouseHoldOutcomeEvent(householdOutcome);
        break;
      case "CCSPL":
        CCSPropertyListingOutcome ccsPropertyListingOutcome = outcomeMessageConverter.convertMessageToDTO(CCSPropertyListingOutcome.class,
                processedMessage);
        outcomeService.createPropertyListingOutcomeEvent(ccsPropertyListingOutcome);
        break;
      case "CCSINT":
        CCSInterviewOutcome ccsInterviewOutcome = outcomeMessageConverter.convertMessageToDTO(CCSInterviewOutcome.class,
                processedMessage);
        outcomeService.createInterviewOutcomeEvent(ccsInterviewOutcome);
        break;
      default:
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot process message for case ID " + caseId.asText());
    }
  }
}
