package com.silenteight.adjudication.engine.trace.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.trace.process.TraceProcess;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static org.springframework.amqp.core.ExchangeTypes.TOPIC;

@ConditionalOnProperty(prefix = "ae.solving.journal", name = "enabled", havingValue = "true")
@Slf4j
@Service
@RequiredArgsConstructor
class TraceMessageReceivedListener {

  public static final String TRACKING_EXCHANGE_NAME = "ae.journal";
  public static final String TRACKING_ROUTING_KEY = "";
  public static final String TRACKING_QUEUE_NAME = "ae.solving-journal";
  private final TraceProcess traceProcess;

  @RabbitListener(autoStartup = "true",
      messageConverter = "aeTraceJackson2JsonMessageConverter",
      bindings = @QueueBinding(
          value = @Queue(name = TRACKING_QUEUE_NAME),
          exchange = @Exchange(
              name = TRACKING_EXCHANGE_NAME,
              type = TOPIC
          ), key = TRACKING_ROUTING_KEY)
  )
  void onReceivedTrace(@Payload TraceMessage traceMessage) {
    traceProcess.handle(traceMessage);
  }
}
