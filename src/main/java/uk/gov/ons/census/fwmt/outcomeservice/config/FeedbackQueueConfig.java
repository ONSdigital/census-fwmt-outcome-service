package uk.gov.ons.census.fwmt.outcomeservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtActionInstruction;
import uk.gov.ons.census.fwmt.common.rm.dto.FwmtCancelActionInstruction;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FeedbackQueueConfig {

  @Bean(name = "feedbackRabbitTemplate")
  @Qualifier("FB_RT")
  public RabbitTemplate rabbitTemplate(@Qualifier("FB_MC") MessageConverter mc,
      ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(mc);
    return template;
  }

  @Bean(name = "feedbackJsonMessageConverter")
  @Qualifier("FB_MC")
  public MessageConverter jsonMessageConverter(@Qualifier("FB_CM") DefaultClassMapper cm) {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter(objectMapper);
    jsonMessageConverter.setClassMapper(cm);
    return jsonMessageConverter;
  }

  @Bean(name = "feedbackClassMapper")
  @Qualifier("FB_CM")
  public DefaultClassMapper classMapper() {
    DefaultClassMapper classMapper = new DefaultClassMapper();
    Map<String, Class<?>> idClassMapping = new HashMap<>();
    idClassMapping.put("uk.gov.ons.census.fwmtadapter.model.dto.fwmt.FwmtActionInstruction", FwmtActionInstruction.class);
    idClassMapping.put("uk.gov.ons.census.fwmtadapter.model.dto.fwmt.FwmtCancelActionInstruction", FwmtCancelActionInstruction.class);
    idClassMapping.put("uk.gov.ons.census.fwmt.common.rm.dto.FwmtActionInstruction", FwmtActionInstruction.class);
    idClassMapping.put("uk.gov.ons.census.fwmt.common.rm.dto.FwmtCancelActionInstruction", FwmtCancelActionInstruction.class);
    classMapper.setIdClassMapping(idClassMapping);
    classMapper.setTrustedPackages("*");
    return classMapper;
  }
}
