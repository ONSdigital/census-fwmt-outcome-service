package uk.gov.ons.census.fwmt.outcomeservice.dto;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import uk.gov.ons.census.fwmt.common.data.spg.NewStandaloneAddress;
import uk.gov.ons.census.fwmt.common.data.spg.NewUnitAddress;
import uk.gov.ons.census.fwmt.common.data.spg.SPGOutcome;

@Component
public class OutcomeDTOMapper extends ConfigurableMapper {

  @Override
  protected final void configure(final MapperFactory factory) {
    factory.classMap(SPGOutcome.class, SPGOutcomeSuperSetDTO.class).byDefault().register();
    factory.classMap(NewStandaloneAddress.class, SPGOutcomeSuperSetDTO.class).byDefault().register();
    factory.classMap(NewUnitAddress.class, SPGOutcomeSuperSetDTO.class).byDefault().register();
  }
}
