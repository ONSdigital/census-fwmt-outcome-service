package uk.gov.ons.census.fwmt.outcomeservice.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import uk.gov.census.ffa.storage.utils.StorageUtils;

@SuppressFBWarnings(value="DM_EXIT", justification="App shouldnt start up")
@Configuration
public class GpgConfig {

  @Autowired
  private StorageUtils storageUtils;

  @Value("${outcomeservice.pgp.fwmtPublicKey}")
  private String fwmtPgpPublicKey;

  @Value("${outcomeservice.pgp.midlPublicKey}")
  private String midlPgpPublicKey;

  @Bean
  public byte[] fwmtPgpPublicKeyByteArray() throws IOException {
    try {
      URI fwmtPgpPublicKeyUri = URI.create(fwmtPgpPublicKey);
      InputStream fileInputStream = storageUtils.getFileInputStream(fwmtPgpPublicKeyUri);
      byte[] readAllBytes = fileInputStream.readAllBytes();
      return readAllBytes;
    } catch (IOException e) {
      System.exit(128);
      throw e;
    }

  }

  @Bean
  public byte[] midlPgpPublicKeyByteArray() throws IOException {
    try {
      URI midlPgpPublicKeyyUri = URI.create(midlPgpPublicKey);
      InputStream fileInputStream = storageUtils.getFileInputStream(midlPgpPublicKeyyUri);
      byte[] readAllBytes = fileInputStream.readAllBytes();
      return readAllBytes;
    } catch (IOException e) {
      System.exit(128);
      throw e;
    }

  }

  
}
