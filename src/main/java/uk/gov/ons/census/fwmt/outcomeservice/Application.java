package uk.gov.ons.census.fwmt.outcomeservice;

import com.godaddy.logging.LoggingConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

@SpringBootApplication
@EnableSwagger2
@EnableRetry
@ComponentScan({"uk.gov.ons.census.fwmt.outcomeservice", "uk.gov.ons.census.fwmt.events",
    "uk.gov.ons.ctp.integration.common.product"})
public class Application {

  public static final String APPLICATION_NAME = "FWMT Gateway - Outcome Service";

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Value("#{'${logging.profile}' == 'CLOUD'}")
  private boolean useJsonLogging;

  @PostConstruct
  public void initJsonLogging() {
    HashMap<Class<?>, Function<Object, String>> customMappers = new HashMap<>();
    customMappers.put(LocalTime.class, Object::toString);
    customMappers.put(LocalDateTime.class, Object::toString);

    LoggingConfigs configs;

    if (useJsonLogging) {
      configs = LoggingConfigs.builder().customMapper(customMappers).build().useJson();
    }
    else {
      configs = LoggingConfigs.builder().customMapper(customMappers).build();
    }
    LoggingConfigs.setCurrent(configs);
  }

}
