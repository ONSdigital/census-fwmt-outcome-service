package uk.gov.ons.census.fwmt.outcomeservice.dto;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.data.ce.CEOutcome;
import uk.gov.ons.census.fwmt.common.data.spg.CareCode;
import uk.gov.ons.census.fwmt.common.data.spg.CeDetails;
import uk.gov.ons.census.fwmt.common.data.spg.FulfilmentRequest;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;

@Component
public class OutcomeDtoMapper extends ConfigurableMapper {

  @Override
  protected final void configure(final MapperFactory factory) {
    factory.classMap(CEOutcome.class, OutcomeSuperSetDto.class).byDefault().register();
    factory.classMap(SPGOutcome.class, OutcomeSuperSetDto.class).byDefault().register();
    factory.classMap(NewStandaloneAddress.class, OutcomeSuperSetDto.class).byDefault().register();
    factory.classMap(NewUnitAddress.class, OutcomeSuperSetDto.class).byDefault().register();
    factory.classMap(CeDetails.class, CeDetailsDto.class).byDefault().register();
    factory.classMap(CareCode.class, CareCodeDto.class).byDefault().register();
    factory.classMap(FulfilmentRequest.class, FulfilmentRequestDto.class).byDefault().register();
  }
}
