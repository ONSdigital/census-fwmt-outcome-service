package uk.gov.ons.census.fwmt.outcomeservice.util;

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
import org.springframework.core.io.Resource;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class EncryptNames {
  public static String receivedNames(String contents, Collection<Resource> publicKeyResources)
      throws GatewayException {
    Collection<PGPPublicKey> publicPgpKeys = getPublicPgpKeys(publicKeyResources);
    return encryptNames(contents.getBytes(), publicPgpKeys);
  }

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
      //      eventManager.triggerErrorEvent(this.getClass(), e, "Failed to encryptFile file", "<N/A>", FAILED_FILE_ENCRYPTION);
    }
    name = new String(encryptedBytes.toByteArray());
    return name;
  }

  private static Collection<PGPPublicKey> getPublicPgpKeys(Collection<Resource> publicKeyResources)
      throws GatewayException {
    var publicKeys = new ArrayList<PGPPublicKey>();
    for (Resource resource : publicKeyResources) {
      PGPPublicKey publicPgpKey = getPublicPgpKey(resource);
      publicKeys.add(publicPgpKey);

    }

    return publicKeys;

  }

  private static PGPPublicKey getPublicPgpKey(Resource pgpKey) throws GatewayException {
    try {
      InputStream input = PGPUtil.getDecoderStream(pgpKey.getInputStream());

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
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "test");
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
