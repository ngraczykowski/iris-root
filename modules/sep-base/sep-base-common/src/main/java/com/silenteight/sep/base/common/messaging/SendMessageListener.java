package com.silenteight.sep.base.common.messaging;

import org.springframework.amqp.core.Message;

public interface SendMessageListener {

  void onSent(Message message);
}
