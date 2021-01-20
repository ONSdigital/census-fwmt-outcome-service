package uk.gov.ons.census.fwmt.outcomeservice.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;

@Repository
public interface GatewayCacheRepository extends JpaRepository<GatewayCache, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  GatewayCache findByCaseId(String caseId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  GatewayCache findByOriginalCaseId(String caseId);

}
