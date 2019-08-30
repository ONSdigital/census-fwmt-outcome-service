package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingReceiver;

@Configuration
public class OutcomePreprocessingQueueConfig {


  public static final String OUTCOME_PREPROCESSING_QUEUE = "Outcome.Preprocessing";
  public static final String OUTCOME_PREPROCESSING_EXCHANGE = "Outcome.Preprocessing.Exchange";
  public static final String OUTCOME_PREPROCESSING_ROUTING_KEY = "Outcome.Preprocessing.Request";
  public static final String OUTCOME_PREPROCESSING_DLQ = "Outcome.PreprocessingDLQ";

  @Autowired
  private AmqpAdmin amqpAdmin;

  //Queues
  @Bean
  public Queue outcomePreprocessingQueue() {
    Queue queue = QueueBuilder.durable(OUTCOME_PREPROCESSING_QUEUE)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", OUTCOME_PREPROCESSING_DLQ)
            .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }

  //Dead Letter Queue
  @Bean
  public Queue outcomePreprocessingDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(OUTCOME_PREPROCESSING_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }

  //Exchange
  @Bean
  public DirectExchange outcomePreprocessingExchange() {
    DirectExchange directExchange = new DirectExchange(OUTCOME_PREPROCESSING_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(amqpAdmin);
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding outcomePreprocessorBinding(@Qualifier("outcomePreprocessingQueue") Queue outcomePreprocessingQueue,
                                       @Qualifier("outcomePreprocessingExchange") DirectExchange outcomePreprocessingExchange) {
    Binding binding = BindingBuilder.bind(outcomePreprocessingQueue).to(outcomePreprocessingExchange)
            .with(OUTCOME_PREPROCESSING_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin);
    return binding;
  }
}
