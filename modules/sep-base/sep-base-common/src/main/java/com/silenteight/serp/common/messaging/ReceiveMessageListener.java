package com.silenteight.serp.common.messaging;

import org.springframework.amqp.core.Message;

public interface ReceiveMessageListener {

  void onReceived(Message message);
}
