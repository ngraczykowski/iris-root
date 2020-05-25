package com.silenteight.serp.common.messaging;

public interface MessageSenderFactory {

  MessageSender get(String exchangeName);
}
