package com.silenteight.sep.base.common.messaging.encryption;

import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.support.crypto.ScryptKeyDerivationFunction;
import com.silenteight.sep.base.common.support.crypto.ScryptKeyDerivationFunction.ScryptParameters;
import com.silenteight.sep.base.common.support.crypto.SecureRandomNonceGenerator;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(MessagingEncryptionProperties.class)
@AutoConfigureAfter(MessagingConfiguration.class)
class MessagingEncryptionConfiguration {

  MessagingEncryptionConfiguration(MessagingEncryptionProperties encryptionProps) {
    this.aesGcmEncryptionBundle = configureEncryptionBundle(encryptionProps);
  }

  private final AesGcmEncryptionBundle aesGcmEncryptionBundle;

  @Bean
  AmqpMessageEncypter amqpMessageEncypter() {
    return aesGcmEncryptionBundle.getEncrypter();
  }

  @Bean
  AmqpMessageDecrypter amqpMessageDecrypter() {
    return aesGcmEncryptionBundle.getDecrypter();
  }

  private static AesGcmEncryptionBundle configureEncryptionBundle(
      MessagingEncryptionProperties encryptionProperties) {
    byte[] key = generateEncryptionKey(encryptionProperties);
    var nonceGenerator = new SecureRandomNonceGenerator(encryptionProperties.getNonceSizeInBits());

    var encryptorParams = new AesGcmEncryptionParameters(
        key, nonceGenerator, encryptionProperties.getNonceHeader(),
        encryptionProperties.getMacSizeInBits());

    return new AesGcmEncryptionBundle(encryptorParams);
  }

  private static byte[] generateEncryptionKey(MessagingEncryptionProperties encryptionProperties) {
    var scrypt = new ScryptKeyDerivationFunction(ScryptParameters.defaults());
    return scrypt.generate(
        encryptionProperties.getKeySeed().getBytes(),
        encryptionProperties.getSalt().getBytes(),
        encryptionProperties.getKeySizeInBits()
    );
  }
}
