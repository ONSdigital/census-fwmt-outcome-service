package uk.gov.ons.census.fwmt.outcomeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.ons.census.fwmt.common.data.nc.NCOutcome;
import uk.gov.ons.census.fwmt.common.data.shared.CeDetails;
import uk.gov.ons.census.fwmt.common.data.shared.Refusal;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.outcomeservice.message.OutcomePreprocessingProducer;
import uk.gov.ons.census.fwmt.outcomeservice.service.impl.NCTMCaseIdOverride;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OutcomeControllerTest {

    @Autowired
    private MockMvc mockOutcomeController;

    @MockBean
    private GatewayEventManager gatewayEventManager;

    @MockBean
    private NCTMCaseIdOverride nctmCaseIdOverride;

    @MockBean
    private OutcomePreprocessingProducer outcomePreprocessingProducer;

    @WithAnonymousUser
    @Test
    public void givenOutcomeToProcessNCEvents_shouldFail401() throws Exception {

        doNothing().when(gatewayEventManager).triggerEvent(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        doNothing().when(nctmCaseIdOverride).overrideTMCaseIdWithRMOriginalCaseId(anyString(), any(NCOutcome.class));
        doNothing().when(outcomePreprocessingProducer).sendHHStandaloneAddressToPreprocessingQueue(any(NCOutcome.class));

        this.mockOutcomeController.perform(post("/ncOutcome/b48bf28e-e7f4-4467-a9fb-e000b6a33543")
                .contentType("application/json")
                .content(convertObjectToJsonBytes(createNCOutCome()))
                .accept("application/json"))
                .andExpect(status().isUnauthorized());
        verify(gatewayEventManager, times(0)).triggerEvent(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString());
        verify(nctmCaseIdOverride, times(0)).overrideTMCaseIdWithRMOriginalCaseId(anyString(), any(NCOutcome.class));
        verify(outcomePreprocessingProducer, times(0)).sendHHStandaloneAddressToPreprocessingQueue(any(NCOutcome.class));
    }

    @WithMockUser("test_user")
    @Test
    public void givenOutcomeShouldProcessNCEvents_shouldSucceedWith200() throws Exception {

        doNothing().when(gatewayEventManager).triggerEvent(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        doNothing().when(nctmCaseIdOverride).overrideTMCaseIdWithRMOriginalCaseId(anyString(), any(NCOutcome.class));
        doNothing().when(outcomePreprocessingProducer).sendHHStandaloneAddressToPreprocessingQueue(any(NCOutcome.class));

        this.mockOutcomeController.perform(post("/ncOutcome/b48bf28e-e7f4-4467-a9fb-e000b6a33543")
                .contentType("application/json")
                .content(convertObjectToJsonBytes(createNCOutCome()))
                .accept("application/json"))
                .andExpect(status().isOk());

        verify(gatewayEventManager, times(1)).triggerEvent(anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString());
        verify(nctmCaseIdOverride, times(1)).overrideTMCaseIdWithRMOriginalCaseId(anyString(), any(NCOutcome.class));
        verify(outcomePreprocessingProducer, times(1)).sendHHStandaloneAddressToPreprocessingQueue(any(NCOutcome.class));
    }

    public byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

    private NCOutcome createNCOutCome() {
        NCOutcome ncOutcome = new NCOutcome("OFFICER_1", new CeDetails(), new Refusal());
        ncOutcome.setTransactionId(UUID.randomUUID());
        ncOutcome.setPrimaryOutcomeDescription("PrimaryOutcomeDescription");
        ncOutcome.setSecondaryOutcomeDescription("SecondaryOutcomeDescription");
        ncOutcome.setOutcomeCode("01-01-01");
        return ncOutcome;
    }
}
