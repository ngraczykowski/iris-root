package com.silenteight.scb.outputrecommendation.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.registration.api.v1.MessageBatchError;
import com.silenteight.scb.outputrecommendation.domain.OutputRecommendationFacade;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchErrorRabbitAmqpListener {

  private final BatchMapper batchMapper;
  private final OutputRecommendationFacade outputRecommendationFacade;

  @RabbitListener(
      queues = "${amqp.output-recommendation.incoming.batch-error.solving.queue-name}",
      errorHandler = "outputRecommendationAmqpErrorHandler"
  )
  public void subscribe(MessageBatchError message) {
    log.info("Received batch error message for batchId: {}", message.getBatchId());
    var command = batchMapper.fromBatchErrorMessage(message);
    outputRecommendationFacade.prepareErrorBatchRecommendations(command);
  }

}
