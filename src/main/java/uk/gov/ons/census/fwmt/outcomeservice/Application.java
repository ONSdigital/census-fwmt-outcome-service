package uk.gov.ons.census.fwmt.outcomeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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

}
