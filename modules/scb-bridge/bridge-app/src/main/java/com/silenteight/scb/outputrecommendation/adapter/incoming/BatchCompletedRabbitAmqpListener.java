package com.silenteight.scb.outputrecommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BatchCompletedRabbitAmqpListener {

  private final BatchMapper batchMapper;
  private final OutputRecommendationFacade outputRecommendationFacade;

  @RabbitListener(
      queues = "${amqp.output-recommendation.incoming.notify-batch-completed.solving.queue-name}",
      errorHandler = "outputRecommendationAmqpErrorHandler"
  )
  public void subscribe(MessageBatchCompleted batchCompleted) {
    log.info(
        "Received batch completed message for analysisName: {} and batchId: {}",
        batchCompleted.getAnalysisName(), batchCompleted.getBatchId());

    var command = batchMapper.fromBatchCompletedMessage(batchCompleted);
    outputRecommendationFacade.prepareCompletedBatchRecommendations(command);
  }
}
