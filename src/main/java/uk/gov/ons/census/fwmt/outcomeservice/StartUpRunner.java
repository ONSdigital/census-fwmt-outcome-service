package uk.gov.ons.census.fwmt.outcomeservice;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SPGOutcomeLookup;
import uk.gov.ons.census.fwmt.outcomeservice.converter.spg.SPGReasonCodeLookup;

@Configuration
public class StartUpRunner implements CommandLineRunner{

  @Autowired
  private SPGOutcomeLookup spgOutcomeLookup;

  @Autowired
  private SPGReasonCodeLookup spgReasonCodeLookup;

  @Autowired
  private ResourceLoader resourceLoader;

  @Value(value = "${outcomeservice.outcomeCodeLookup.path}")
  private String outcomeCodeLookupPath;

  @Value(value = "${outcomeservice.reasonCodeLookup.path}")
  private String reasonCodeLookupPath;

  @Override
  public void run(String... args) throws Exception {
    buildSPGOutcomeLookup();
    buildSPGReasonCodeLookup();
  }

  private void buildSPGOutcomeLookup() throws GatewayException {
    String line;
    Resource resource = resourceLoader.getResource(outcomeCodeLookupPath);
    try (BufferedReader in = new BufferedReader(new FileReader(resource.getFile(), UTF_8))) {
      if ((line = in.readLine()) != null) {
        String[] lookup = line.split("\t");
        spgOutcomeLookup.add(lookup[0], lookup[1].split(","));
      }
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, e, "Cannot process outcome lookup");
    }
  }

  private void buildSPGReasonCodeLookup() throws GatewayException {
    String line;
    Resource resource = resourceLoader.getResource(reasonCodeLookupPath);
    try (BufferedReader in = new BufferedReader(new FileReader(resource.getFile(), UTF_8))) {
      if ((line = in.readLine()) != null) {
        String[] lookup = line.split(",");
        spgReasonCodeLookup.add(lookup[0], lookup[1]);
      }
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, e, "Cannot process reason code lookup");
    }
  }
}
