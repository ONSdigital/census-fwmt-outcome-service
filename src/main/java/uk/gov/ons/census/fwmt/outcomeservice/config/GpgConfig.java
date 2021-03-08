package uk.gov.ons.census.fwmt.outcomeservice.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.gov.census.ffa.storage.utils.StorageUtils;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.util.EncryptNames;

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
