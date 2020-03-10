package uk.gov.ons.census.fwmt.outcomeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SPGOutcomeLookup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
  SPGOutcomeLookup createSPGOutcomeLookup(){
    SPGOutcomeLookup lookupMap = new SPGOutcomeLookup();
    String line;
      try(BufferedReader in = new BufferedReader(new FileReader("outcomeCodeLookup.txt"));) {
        if ((line = in.readLine()) != null) {
          String[] lookup = line.split("\t");
          lookupMap.add(lookup[0], lookup[1].split(","));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    return lookupMap;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

}
