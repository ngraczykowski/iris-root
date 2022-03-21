package com.silenteight.connector.ftcc.callback.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.response.ResponseProcessor;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchCompletedHandler {

  private static final String QUEUE_NAME = "ftcc_batch completed_queue";
  public static final String DEFAULT_EXCHANGE = "notify-batch-completed-exchange";
  private final ResponseProcessor responseProcessor;

  @RabbitListener(bindings = {
      @QueueBinding(
          value = @Queue(QUEUE_NAME),
          exchange = @Exchange("${ftcc.core-bridge.inbound.batch-completed.exchange:"
              + DEFAULT_EXCHANGE + "}"))
  })
  public void handle(MessageBatchCompleted messageBatchCompleted) {
    log.info("BatchCompleted BatchID={} AnalysisId={} ", messageBatchCompleted.getBatchId(),
        messageBatchCompleted.getAnalysisId());
    responseProcessor.process(messageBatchCompleted);
  }
}