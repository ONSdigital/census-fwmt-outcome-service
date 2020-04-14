package uk.gov.ons.census.fwmt.outcomeservice.message;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtActionInstruction;

@Service
public class RmFieldRepublishProducer {

  @Autowired
  @Qualifier("RmField")
  RabbitTemplate rabbitTemplate;

  public void republish(FwmtActionInstruction fieldworkFollowup) throws GatewayException {
    rabbitTemplate.convertAndSend("RM.Field", fieldworkFollowup);
  }
}
