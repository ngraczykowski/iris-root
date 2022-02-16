package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.recommendation.infrastructure.amqp.Lz4Utils

import net.jpountz.lz4.LZ4Compressor
import net.jpountz.lz4.LZ4Factory
import net.jpountz.lz4.LZ4FrameOutputStream
import net.jpountz.lz4.LZ4FrameOutputStream.BLOCKSIZE
import net.jpountz.lz4.LZ4FrameOutputStream.FLG.Bits
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.postprocessor.AbstractCompressingPostProcessor
import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class RecommendationReceivedFlowRabbitMqTestConfig implements BeanPostProcessor {

  static def TEST_BATCH_COMPLETED_QUEUE_NAME = "test-batch-completed-queue"

  @Override
  Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof RabbitTemplate) {
      configureRabbitTemplate((RabbitTemplate) bean)
    }
    return bean
  }

  private static void configureRabbitTemplate(RabbitTemplate template) {
    template.addBeforePublishPostProcessors(new Lz4CompressingPostProcessor())
  }

  @Bean
  Queue testBatchCompletedQueue() {
    return QueueBuilder.durable(TEST_BATCH_COMPLETED_QUEUE_NAME).build()
  }

  @Bean
  Binding testBatchCompletedBinding(
      DirectExchange batchCompletedExchange, Queue testBatchCompletedQueue) {
    return BindingBuilder
        .bind(testBatchCompletedQueue)
        .to(batchCompletedExchange)
        .with('')
  }
}

class Lz4CompressingPostProcessor extends AbstractCompressingPostProcessor {

  private static final def LZ4_ENCODING = "lz4"
  private static final def FACTORY = LZ4Factory.fastestInstance()
  private static final int compressionLevel = 1

  @Override
  protected OutputStream getCompressorStream(OutputStream stream) throws IOException {
    return new LZ4FrameOutputStream(
        stream,
        BLOCKSIZE.SIZE_64KB,
        -1,
        createCompressor(),
        Lz4Utils.createHash(),
        Bits.BLOCK_INDEPENDENCE)
  }

  private static LZ4Compressor createCompressor() {
    if (compressionLevel <= 1)
      return FACTORY.fastCompressor()
    else
      return FACTORY.highCompressor(Math.min(compressionLevel, 9))
  }

  @Override
  protected String getEncoding() {
    return LZ4_ENCODING
  }
}
