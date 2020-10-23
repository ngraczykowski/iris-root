package com.silenteight.sep.base.common.messaging;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.support.postprocessor.MessagePostProcessorUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MessageProcessorsOrderingIT {

  @Test
  void preProcessorsHaveCorrectOrdering() {
    var encryptMessagePostProcessor = new EncryptMessagePostProcessor(null);
    var lz4CompressMessagePostProcessor = new Lz4CompressMessagePostProcessor();

    var orderedPreProcessors = MessagePostProcessorUtils.sort(
        List.of(encryptMessagePostProcessor, lz4CompressMessagePostProcessor)
    );

    assertThat(orderedPreProcessors)
        .containsExactly(encryptMessagePostProcessor, lz4CompressMessagePostProcessor);
  }

  @Test
  void postProcessorsHaveCorrectOrdering() {
    var decryptMessagePostProcessor = new DecryptMessagePostProcessor(null);
    var lz4DecompressMessagePostProcessor = new Lz4DecompressMessagePostProcessor();

    var orderedPostProcessors = MessagePostProcessorUtils.sort(
        List.of(decryptMessagePostProcessor, lz4DecompressMessagePostProcessor)
    );

    assertThat(orderedPostProcessors)
        .containsExactly(lz4DecompressMessagePostProcessor, decryptMessagePostProcessor);
  }
}
