package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSInterviewOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.household.HouseholdOutcome;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;
import java.io.IOException;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private ObjectMapper jsonObjectMapper;

  @Autowired
  private OutcomeService outcomeService;

  @Autowired
  private OutcomeMessageConverter outcomeMessageConverter;

  public void receiveMessage(GenericMessage message) throws GatewayException, IOException {
    log.info("Received a message in Outcome queue");
    processStoredMessage(message);
  }

  private void processStoredMessage(GenericMessage actualMessage) throws GatewayException {
    JsonNode actualMessageRootNode;
    JsonNode caseId;
    String outcomeSurveyType;
    String processedMessage;

    byte[] genericMessage = (byte[]) actualMessage.getPayload();
    processedMessage = new String(genericMessage);

    try {
      actualMessageRootNode = jsonObjectMapper.readTree(processedMessage);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }

    caseId = actualMessageRootNode.path("caseId");

    outcomeSurveyType = actualMessage.getHeaders().get("__OutcomeType__").toString();

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
