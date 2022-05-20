package com.silenteight.sep.base.common.messaging;

public interface MessageSenderFactory {

  MessageSender get(String exchangeName);
}
