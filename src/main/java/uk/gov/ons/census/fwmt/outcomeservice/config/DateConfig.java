package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateConfig {
  @Bean DateFormat dateFormat() {
    return new SimpleDateFormat("yyyy-MM-dd 'T'HH:mm:ss.SSSX");
  }
}
