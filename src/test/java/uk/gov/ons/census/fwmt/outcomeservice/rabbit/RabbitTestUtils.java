package uk.gov.ons.census.fwmt.outcomeservice.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public class RabbitTestUtils {
  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
  static Message createMessage(String data, Integer retryCount) {
    final MessageProperties messageProperties = createQueueProperties();
    final Message message = new Message(data.getBytes(), messageProperties);
    messageProperties.setHeader("retryCount", retryCount);
    return message;
  }
  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DM_DEFAULT_ENCODING")
  static Message createMessage(Integer retryCount) {
    final MessageProperties messageProperties = createQueueProperties();
    final Message message = new Message("dummydata".getBytes(), messageProperties);
    messageProperties.setHeader("retryCount", retryCount);
    return message;
  }

  
  static MessageProperties createQueueProperties() {
    MessageProperties queueProperties = new MessageProperties();
    return queueProperties;
  }
}
