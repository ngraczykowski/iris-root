package com.silenteight.sep.base.common.messaging.postprocessing;

import org.springframework.amqp.core.MessagePostProcessor;

public interface SepMessagePostProcessors {

  MessagePostProcessor getSendPostProcessor();

  MessagePostProcessor getReceivePostProcessor();
}
