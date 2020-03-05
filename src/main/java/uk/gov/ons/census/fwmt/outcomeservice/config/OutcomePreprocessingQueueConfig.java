package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessagingMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.census.fwmt.common.retry.GatewayMessageRecover;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingReceiver;

import static uk.gov.ons.census.fwmt.outcomeservice.config.ConnectionFactoryBuilder.createConnectionFactory;

@Configuration
public class OutcomePreprocessingQueueConfig {

  private String username;
  private String password;
  private String hostname;
  private int port;
  private String virtualHost;

  public OutcomePreprocessingQueueConfig(@Value("${rabbitmq.fwmt.username}") String username,
      @Value("${rabbitmq.fwmt.password}") String password,
      @Value("${rabbitmq.fwmt.hostname}") String hostname,
      @Value("${rabbitmq.fwmt.port}") Integer port,
      @Value("${rabbitmq.fwmt.virtualHost}") String virtualHost) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.port = port;
    this.virtualHost = virtualHost;
  }

  public static final String OUTCOME_PREPROCESSING_QUEUE = "Outcome.Preprocessing";
  public static final String OUTCOME_PREPROCESSING_EXCHANGE = "Outcome.Preprocessing.Exchange";
  public static final String OUTCOME_PREPROCESSING_ROUTING_KEY = "Outcome.Preprocessing.Request";
  public static final String OUTCOME_PREPROCESSING_DLQ = "Outcome.PreprocessingDLQ";

  // Connection Factory
  @Bean
  @Primary
  public ConnectionFactory connectionFactory() {
    return createConnectionFactory(port, hostname, virtualHost, password, username);
  }

  @Bean
  @Primary
  @Qualifier("fwmtRabbitTemplate")
  public RabbitTemplate fwmtRabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate();
    rabbitTemplate.setConnectionFactory(connectionFactory());
    return rabbitTemplate;
  }

  // Interceptor
  @Bean
  public RetryOperationsInterceptor interceptor(
      @Qualifier("retryTemplate") RetryOperations retryOperations) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new GatewayMessageRecover());
    interceptor.setRetryOperations(retryOperations);
    return interceptor;
  }

  @Bean
  public AmqpAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  //Listener Adapter
  @Bean
  public MessageListenerAdapter outcomePreprocessingListenerAdapter(OutcomePreprocessingReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  //Message Listener
  @Bean
  public SimpleMessageListenerContainer outcomePreprocessingMessageListener(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("outcomePreprocessingListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    Advice[] adviceChain = {retryOperationsInterceptor};
    messageListenerAdapter.setMessageConverter(new MessagingMessageConverter());
    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(OUTCOME_PREPROCESSING_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    container.setAmqpAdmin(amqpAdmin());
    return container;
  }
}
