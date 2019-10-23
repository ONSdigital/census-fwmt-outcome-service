package uk.gov.ons.census.fwmt.outcomeservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("CCSOutcomeEntity")
public class CCSPLOutcomeEntity implements Serializable {

  private String caseId;

  private String ccsPropertyListingOutcome;

}
