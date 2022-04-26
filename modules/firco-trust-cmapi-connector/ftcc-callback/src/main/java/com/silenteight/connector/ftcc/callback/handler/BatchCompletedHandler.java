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
import org.springframework.stereotype.Component;

import static com.silenteight.connector.ftcc.common.MdcParams.BATCH_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchCompletedHandler {

  private static final String QUEUE_NAME = "ftcc_batch completed_queue";
  public static final String DEFAULT_EXCHANGE = "notify-batch-completed-exchange";
  private static final String EXCHANGE =
      "${ftcc.core-bridge.inbound.batch-completed.exchange:" + DEFAULT_EXCHANGE + "}";
  public static final String DEFAULT_ROUTING_KEY = "solving";
  private static final String ROUTING_KEY =
      "${ftcc.core-bridge.inbound.batch-completed.routing-key:" + DEFAULT_ROUTING_KEY + "}";
  private final ResponseProcessor responseProcessor;
  private final BatchCompletedService batchCompletedService;

  @RabbitListener(bindings = {
      @QueueBinding(value = @Queue(QUEUE_NAME), exchange = @Exchange(EXCHANGE), key = ROUTING_KEY)})
  public void handle(MessageBatchCompleted messageBatchCompleted) {
    String batchName = messageBatchCompleted.getBatchId();
    String analysisName = messageBatchCompleted.getAnalysisName();
    MDC.put(BATCH_NAME, batchName);
    try {
      log.info("BatchCompleted received batchName={} analysisName={}", batchName, analysisName);

      batchCompletedService.save(batchName, analysisName);
      responseProcessor.process(messageBatchCompleted);
    } finally {
      MDC.remove(BATCH_NAME);
    }
  }
}
