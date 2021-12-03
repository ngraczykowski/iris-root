package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.universaldatasource.app.feature.port.incoming.DeleteFeaturesUseCase;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME;

@Slf4j
@RequiredArgsConstructor
@Configuration
class FeatureInputRetentionInboundAmqpConfiguration {

  private final DeleteFeaturesUseCase deleteFeaturesUseCase;

  @RabbitListener(queues = UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME)
  public void featureInputRetentionReceive(AlertsExpired alertsExpired) {
    log.info("Feature input retention message received");
    deleteFeaturesUseCase.delete(alertsExpired.getAlertsList());
  }
}
