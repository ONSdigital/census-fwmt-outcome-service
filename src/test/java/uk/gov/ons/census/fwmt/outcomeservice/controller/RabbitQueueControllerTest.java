package uk.gov.ons.census.fwmt.outcomeservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.rabbit.QueueMigrator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RabbitQueueControllerTest {

  private MockMvc mockMvc;

  @InjectMocks
  private RabbitQueueController controller;

  @Mock
  private QueueMigrator migrator;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void shouldMigrateDLQtoPreprocessingQueue() throws Exception {
    this.mockMvc.perform(get("/jobs/migratequeue")).andExpect(status().isOk());
    verify(migrator).migrate(eq(RabbitQueueController.originQ), eq(RabbitQueueController.destRoute), eq(RabbitQueueController.exchange));
  }

  @Test
  public void shouldMigrateTransientQueueToGWLinkQueue() throws Exception {
    this.mockMvc.perform(
        get("/jobs/migratequeue")
            .param("originQ", "originTest")
            .param("destRoute", "destTest")
            .param("exchange", "exchange")
    ).andExpect(status().isOk());
    verify(migrator).migrate(eq("originTest"), eq("destTest"), eq("exchange"));
  }

  @Test
  public void shouldReturnBadRequestIfQueueOrRouteIsInvalid() throws Exception {
    doThrow(GatewayException.class).when(migrator).migrate(any(), any(), eq("exchange"));
    this.mockMvc.perform(
        get("/jobs/migratequeue")
            .param("originQ", "originTest")
            .param("destRoute", "destTest")
            .param("exchange", "exchange")
    ).andExpect(status().isBadRequest());

  }
}