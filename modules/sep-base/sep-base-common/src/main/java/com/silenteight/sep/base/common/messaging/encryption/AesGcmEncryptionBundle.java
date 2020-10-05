package com.silenteight.sep.base.common.messaging.encryption;

import com.silenteight.sep.base.common.support.crypto.NonceGenerator;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class AesGcmEncryptionBundle {

  private static final Encoder BASE64_ENCODER = Base64.getEncoder();
  private static final Decoder BASE64_DECODER = Base64.getDecoder();

  private final byte[] key;
  private final NonceGenerator nonceGenerator;
  private final String nonceHeaderKey;
  private final int macSizeInBits;

  public AesGcmEncryptionBundle(AesGcmEncryptionParameters parameters) {
    this.key = parameters.getKey();
    this.nonceGenerator = parameters.getNonceGenerator();
    this.nonceHeaderKey = parameters.getNonceHeader();
    this.macSizeInBits = parameters.getMacSizeInBits();
  }

  private static AEADBlockCipher getCipher() {
    return new GCMBlockCipher(new AESEngine());
  }

  public AmqpMessageDecrypter getDecrypter() {
    return new Decrypter();
  }

  public AmqpMessageEncypter getEncrypter() {
    return new Encrypter();
  }

  private AEADParameters cipherParamsWithNonce(byte[] nonce) {
    return new AEADParameters(new KeyParameter(key), macSizeInBits, nonce);
  }

  private static byte[] doProcess(Message message, AEADBlockCipher cipher) {
    return new AeadBlockCipherProcessor(cipher).process(message.getBody());
  }

  private class Decrypter implements AmqpMessageDecrypter {

    @Override
    public Message decrypt(Message message) {
      AEADBlockCipher cipher = getCipher();
      MessageProperties messageProperties = message.getMessageProperties();
      byte[] nonce = getNonce(messageProperties);

      cipher.init(false, cipherParamsWithNonce(nonce));
      byte[] decrypted = doProcess(message, cipher);

      return decryptedMessage(messageProperties, decrypted);
    }

    private Message decryptedMessage(MessageProperties messageProperties, byte[] decrypted) {
      return MessageBuilder.withBody(decrypted)
          .copyProperties(messageProperties)
          .removeHeader(nonceHeaderKey)
          .build();
    }

    private byte[] getNonce(MessageProperties messageProperties) {
      return BASE64_DECODER.decode(messageProperties.<byte[]>getHeader(nonceHeaderKey));
    }
  }

  private class Encrypter implements AmqpMessageEncypter {

    @Override
    public Message encrypt(Message message) {
      AEADBlockCipher cipher = getCipher();
      AEADParameters params = cipherParamsWithGeneratedNonce();

      cipher.init(true, params);
      byte[] encryptedData = doProcess(message, cipher);

      return encryptedMessage(message, encryptedData, params.getNonce());
    }

    private AEADParameters cipherParamsWithGeneratedNonce() {
      return cipherParamsWithNonce(nonceGenerator.generate());
    }

    private Message encryptedMessage(Message originalMessage, byte[] encrypted, byte[] nonce) {
      return MessageBuilder.withBody(encrypted)
          .copyProperties(originalMessage.getMessageProperties())
          .setHeader(nonceHeaderKey, BASE64_ENCODER.encode(nonce))
          .build();
    }
  }
}
