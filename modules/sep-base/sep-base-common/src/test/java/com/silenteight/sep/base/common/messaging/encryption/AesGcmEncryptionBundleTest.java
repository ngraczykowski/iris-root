package com.silenteight.sep.base.common.messaging.encryption;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class AesGcmEncryptionBundleTest {

  @Nested
  class GivenValidParameters {

    private static final String ENCRYPTION_HEADER = "encryption-nonce";

    private final SecureRandom secureRandom = new SecureRandom();

    private final AesGcmEncryptionBundle underTest = new AesGcmEncryptionBundle(
        new AesGcmEncryptionParameters(
            getRandomBytes(32),
            () -> getRandomBytes(16),
            ENCRYPTION_HEADER,
            128
        )
    );

    byte[] getRandomBytes(int size) {
      byte[] bytesToReturn = new byte[size];
      secureRandom.nextBytes(bytesToReturn);
      return bytesToReturn;
    }

    private final AmqpMessageEncypter encrypter = underTest.getEncrypter();
    private final AmqpMessageDecrypter decrypter = underTest.getDecrypter();

    @Test
    void encryptsAndDecryptsCorrectly() {
      Message toEncrypt = new Message("toEncrypt".getBytes(UTF_8), new MessageProperties());

      Message encrypted = encrypter.encrypt(toEncrypt);
      Message decrypted = decrypter.decrypt(encrypted);

      assertBodiesAreEqual(decrypted, toEncrypt);
    }

    @Test
    @DisplayName("Test wherever cipher is reinitialized on each call")
    void canEncryptAndDecryptMultipleTimes() {
      Message toEncrypt1 = new Message("toEncrypt1".getBytes(UTF_8), new MessageProperties());
      Message toEncrypt2 = new Message("toEncrypt2".getBytes(UTF_8), new MessageProperties());

      Message encrypted1 = encrypter.encrypt(toEncrypt1);
      Message encrypted2 = encrypter.encrypt(toEncrypt2);

      Message decrypted1 = decrypter.decrypt(encrypted1);
      Message decrypted2 = decrypter.decrypt(encrypted2);

      assertBodiesAreEqual(decrypted1, toEncrypt1);
      assertBodiesAreEqual(decrypted2, toEncrypt2);
    }

    @Test
    void whenEncrypted_containsBase64EncodedNonceHeader() {
      Message toEncrypt = new Message("toEncrypt".getBytes(UTF_8), new MessageProperties());
      Decoder decoder = Base64.getDecoder();

      Message encrypted = encrypter.encrypt(toEncrypt);

      byte[] header = encrypted.getMessageProperties().getHeader(ENCRYPTION_HEADER);
      assertThat(header).isNotEmpty();
      try {
        decoder.decode(header);
      } catch (IllegalArgumentException iae) {
        fail("Header is not Base64");
      }
    }

    @Test
    void afterDecryption_doesNotContainNonceHeader() {
      Message toEncrypt = new Message("toEncrypt".getBytes(UTF_8), new MessageProperties());

      Message encrypted = encrypter.encrypt(toEncrypt);
      Message decrypted = decrypter.decrypt(encrypted);

      MessageProperties actualProperties = decrypted.getMessageProperties();
      assertThat(actualProperties.<byte[]>getHeader(ENCRYPTION_HEADER)).isNull();
    }

    void assertBodiesAreEqual(Message actual, Message expected) {
      assertThat(actual.getBody()).isEqualTo(expected.getBody());
    }
  }
}
