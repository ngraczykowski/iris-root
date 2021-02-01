package com.silenteight.sep.base.common.messaging.postprocessing;

import lombok.Value;

import org.springframework.amqp.core.MessagePostProcessor;

@Value
class SimpleSepMessagePostProcessors implements SepMessagePostProcessors {

  MessagePostProcessor sendPostProcessor;
  MessagePostProcessor receivePostProcessor;
}
