package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.messaging.encryption.AesGcmEncryptionBundle;
import com.silenteight.sep.base.common.messaging.encryption.AesGcmEncryptionParameters;
import com.silenteight.sep.base.common.support.crypto.ScryptKeyDerivationFunction;
import com.silenteight.sep.base.common.support.crypto.ScryptKeyDerivationFunction.ScryptParameters;
import com.silenteight.sep.base.common.support.crypto.SecureRandomNonceGenerator;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.postprocessor.DelegatingDecompressingPostProcessor;
import org.springframework.amqp.support.postprocessor.GUnzipPostProcessor;
import org.springframework.amqp.support.postprocessor.UnzipPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ErrorHandler;

import java.util.Collection;

@RequiredArgsConstructor
class RabbitConfiguringPostProcessor implements BeanPostProcessor {

  private static final String NONCE_HEADER = "encryption-nonce";

  private static final DelegatingDecompressingPostProcessor DECOMPRESSING_POST_PROCESSOR;

  static {
    DelegatingDecompressingPostProcessor postProcessor =
        new DelegatingDecompressingPostProcessor();

    postProcessor.addDecompressor("lz4", new Lz4DecompressMessagePostProcessor());
    postProcessor.addDecompressor("zip", new UnzipPostProcessor());
    postProcessor.addDecompressor("gzip", new GUnzipPostProcessor());

    DECOMPRESSING_POST_PROCESSOR = postProcessor;
  }

  private final MessagingProperties properties;
  private final Collection<ReceiveMessageListener> listeners;
  @Setter
  private ErrorHandler errorHandler;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) {

    if (bean instanceof RabbitTemplate)
      configureRabbitTemplate((RabbitTemplate) bean);

    if (AbstractRabbitListenerContainerFactory.class.isAssignableFrom(bean.getClass()))
      configureContainerFactory((AbstractRabbitListenerContainerFactory<?>) bean);

    return bean;
  }

  private void configureRabbitTemplate(RabbitTemplate template) {

    if (errorHandler != null)
      template.setReplyErrorHandler(errorHandler);

    if (properties.getCompression().isEnabled()) {
      Lz4CompressMessagePostProcessor compressor = new Lz4CompressMessagePostProcessor();
      compressor.setCompressionLevel(properties.getCompression().getLevel());

      template.addBeforePublishPostProcessors(compressor);
    }

    MessagingProperties.Encryption encryptionParams = properties.getEncryption();

    if (encryptionParams.isEnabled()) {
      configureEncryption(template, encryptionParams);
    }

    template.addAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR);
  }

  private static void configureEncryption(
      RabbitTemplate template, MessagingProperties.Encryption encryptionParams) {
    AesGcmEncryptionBundle encryptionFactory = createEncryptionBundle(encryptionParams);

    template.addBeforePublishPostProcessors(
        new EncryptMessagePostProcessor(encryptionFactory.getEncrypter()));
    template.addAfterReceivePostProcessors(
        new DecryptMessagePostProcessor(encryptionFactory.getDecrypter()));
  }

  private static AesGcmEncryptionBundle createEncryptionBundle(
      MessagingProperties.Encryption encryptionParams) {
    byte[] key = generateEncryptionKey(encryptionParams);
    var nonceGenerator = new SecureRandomNonceGenerator(encryptionParams.getNonceSizeInBits());

    var encryptorParams = new AesGcmEncryptionParameters(
        key, nonceGenerator, NONCE_HEADER, encryptionParams.getMacSizeInBits());

    return new AesGcmEncryptionBundle(encryptorParams);
  }

  private static byte[] generateEncryptionKey(MessagingProperties.Encryption encryption) {
    var scrypt = new ScryptKeyDerivationFunction(ScryptParameters.defaults());
    return scrypt.generate(
        encryption.getKeySeed().getBytes(),
        encryption.getSalt().getBytes(),
        encryption.getKeySizeInBits()
    );
  }

  private void configureContainerFactory(AbstractRabbitListenerContainerFactory<?> factory) {
    if (errorHandler != null)
      factory.setErrorHandler(errorHandler);

    if (CollectionUtils.isEmpty(listeners)) {
      factory.setAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR);
      return;
    }

    ListeningMessagePostProcessor meteringProcessor =
        new ListeningMessagePostProcessor(listeners);

    factory.setAfterReceivePostProcessors(DECOMPRESSING_POST_PROCESSOR, meteringProcessor);
  }
}
