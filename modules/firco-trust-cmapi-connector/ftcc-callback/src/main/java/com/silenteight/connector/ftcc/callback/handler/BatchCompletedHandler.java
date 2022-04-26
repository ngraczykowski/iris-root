package com.silenteight.connector.ftcc.callback.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.response.ResponseProcessor;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.silenteight.connector.ftcc.common.MdcParams.BATCH_NAME;
import static org.springframework.amqp.support.AmqpHeaders.RECEIVED_ROUTING_KEY;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchCompletedHandler {

  private static final String QUEUE_NAME = "ftcc_batch completed_queue";
  public static final String DEFAULT_EXCHANGE = "notify-batch-completed-exchange";
  private final ResponseProcessor responseProcessor;
  private final BatchCompletedService batchCompletedService;

  @RabbitListener(bindings = {
      @QueueBinding(
          value = @Queue(QUEUE_NAME),
          key = { "solving", "simulation" },
          exchange = @Exchange("${ftcc.core-bridge.inbound.batch-completed.exchange:"
              + DEFAULT_EXCHANGE + "}"))
  })
  public void handle(
      MessageBatchCompleted messageBatchCompleted,
      @Header(RECEIVED_ROUTING_KEY) String alertType) {
    String batchName = messageBatchCompleted.getBatchId();
    String analysisName = messageBatchCompleted.getAnalysisName();
    MDC.put(BATCH_NAME, batchName);
    try {
      log.info(
          "BatchCompleted received batchName={} analysisName={} alertType={}", batchName,
          analysisName, alertType);

      batchCompletedService.save(batchName, analysisName);
      responseProcessor.process(messageBatchCompleted, alertType);
    } finally {
      MDC.remove(BATCH_NAME);
    }
  }
}
