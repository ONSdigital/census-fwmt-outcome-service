package uk.gov.ons.census.fwmt.outcomeservice.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import uk.gov.census.ffa.storage.utils.StorageUtils;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class EncryptNames {
  @Autowired
  private StorageUtils storageUtils;

  public static String receivedNames(String contents, Collection<InputStream> publicKeyResources)
      throws GatewayException {
    Collection<PGPPublicKey> publicPgpKeys = getPublicPgpKeys(publicKeyResources);
    return encryptNames(contents.getBytes(Charset.defaultCharset()), publicPgpKeys);
  }

  //  A strange error that shouldn't occur due to a library we have no control over doing a @NotNull check.
  //  Annotation added to ignore the linked error
  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE")
  public static String encryptNames(byte[] contents, Collection<PGPPublicKey> publicPgpKeys)
      throws GatewayException {
    String name;
    final byte[] compressedContents = compressFile(contents);
    final PGPEncryptedDataGenerator generator = new PGPEncryptedDataGenerator(
        new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256).setWithIntegrityPacket(true)
            .setSecureRandom(new SecureRandom()).setProvider(new BouncyCastleProvider()));
    for (PGPPublicKey publicKey : publicPgpKeys) {
      generator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider(new BouncyCastleProvider()));
    }
    final ByteArrayOutputStream encryptedBytes = new ByteArrayOutputStream();
    try (OutputStream armoredOutputStream = new ArmoredOutputStream(encryptedBytes);
        OutputStream encryptedOut = generator.open(armoredOutputStream, compressedContents.length)) {

      encryptedOut.write(compressedContents);
    } catch (PGPException | IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "failed to encrypt file");
    }
    name = new String(encryptedBytes.toByteArray(), Charset.defaultCharset());
    return name;
  }

  private static Collection<PGPPublicKey> getPublicPgpKeys(Collection<InputStream> publicKeyResources)
      throws GatewayException {
    var publicKeys = new ArrayList<PGPPublicKey>();
    for (InputStream resource : publicKeyResources) {
      PGPPublicKey publicPgpKey = getPublicPgpKey(resource);
      publicKeys.add(publicPgpKey);
    }
    return publicKeys;
  }

  private static PGPPublicKey getPublicPgpKey(InputStream pgpKey) throws GatewayException {
    try {
      InputStream input = PGPUtil.getDecoderStream(pgpKey);

      JcaPGPPublicKeyRingCollection pgpPublicKeyRingCollection = new JcaPGPPublicKeyRingCollection(input);
      input.close();

      PGPPublicKey key = null;
      PGPPublicKey masterKey = null;
      Iterator<PGPPublicKeyRing> keyRings = pgpPublicKeyRingCollection.getKeyRings();
      while (key == null && keyRings.hasNext()) {
        PGPPublicKeyRing kRing = keyRings.next();
        Iterator<PGPPublicKey> publicKeys = kRing.getPublicKeys();
        while (key == null && publicKeys.hasNext()) {
          PGPPublicKey k = publicKeys.next();

          if (k.isEncryptionKey() && !k.isMasterKey()) {
            key = k;
          }
          else if (k.isEncryptionKey() && k.isMasterKey()) {
            masterKey = k; //should only ever be set if there is no subkey for encryption
          }
        }
        if (key == null && masterKey != null) {
          key = masterKey;
        }

      }
      return key;
    } catch (IOException | PGPException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "test", e);
    }
  }
  private static byte[] compressFile(byte[] inputFile) throws GatewayException {
    try {
      final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFile);
      final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
      final PGPLiteralDataGenerator literal = new PGPLiteralDataGenerator();
      final PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(
          CompressionAlgorithmTags.ZIP);
      final OutputStream outputStream =
          literal.open(compressedDataGenerator.open(byteOutputStream), PGPLiteralData.BINARY, "filename",
              inputStream.available(), new Date());
      Streams.pipeAll(inputStream, outputStream);
      compressedDataGenerator.close();
      return byteOutputStream.toByteArray();
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Test");

    }
  }
}
