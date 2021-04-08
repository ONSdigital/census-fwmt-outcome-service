package uk.gov.ons.census.fwmt.outcomeservice.spgutilitymethods;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.outcomeservice.data.GatewayCache;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.GatewayCacheService;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.SwitchCaseIdService;
import static uk.gov.ons.census.fwmt.outcomeservice.util.SpgUtilityMethods.regionLookup;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpgUtilityMethodsTest {

    @Test
    @DisplayName("Should set Northern Irish region to W")
    public void shouldSetNorthernIrishRegionToN() {
        String region;
        region = regionLookup("YA-NNM1-ZI-09");
        Assertions.assertEquals("N", region);
    }

    @Test
    @DisplayName("Should set Welsh region to W")
    public void shouldSetWelshRegionToW() {
        String region;
        region = regionLookup("HW-NNM1-ZI-09");
        Assertions.assertEquals("W", region);
    }

    @Test
    @DisplayName("Should set English region to E")
    public void shouldSetEnglishRegionToW() {
        String region;
        region = regionLookup("AG-NNM1-ZI-09");
        Assertions.assertEquals("E", region);
    }
}
