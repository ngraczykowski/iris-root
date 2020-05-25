package com.silenteight.serp.common.messaging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Message;

@Slf4j
class ProcessLoggingMessageListener implements ReceiveMessageListener, SendMessageListener {

  private final ProcessProgressLogger receivedProgressLogger =
      new ProcessProgressLogger(log, "receive", 1000);

  private final ProcessProgressLogger sentProgressLogger =
      new ProcessProgressLogger(log, "send", 1000);

  @Override
  public void onReceived(Message message) {
    receivedProgressLogger.tick();
  }

  @Override
  public void onSent(Message message) {
    sentProgressLogger.tick();
  }
}
