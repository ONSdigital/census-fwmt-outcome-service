package uk.gov.ons.census.fwmt.outcomeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.OutcomeService;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
@Component
public class OutcomePreprocessingReceiver {

  @Autowired
  private OutcomeService outcomeService;

  @Autowired
  private ObjectMapper jsonObjectMapper;

  public void receiveMessage(GenericMessage spgOutcomeString) throws GatewayException {
    log.info("Received a message in Outcome queue");
    processStoredMessage(spgOutcomeString);
  }

  private void processStoredMessage(GenericMessage actualMessage) throws GatewayException {
    JsonNode actualMessageRootNode;
    JsonNode outcomeCode;
    String processedMessage;

    byte[] genericMessage = (byte[]) actualMessage.getPayload();
    processedMessage = new String(genericMessage, Charset.defaultCharset());

    try {
      actualMessageRootNode = jsonObjectMapper.readTree(processedMessage);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }

    outcomeCode = actualMessageRootNode.path("outcomeCode");

    outcomeService.createSpgOutcomeEvent(actualMessage, outcomeCode.asText());
  }
}
