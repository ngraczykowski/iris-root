package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.universaldatasource.app.category.port.incoming.DeleteCategoryValuesUseCase;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.universaldatasource.common.messaging.RabbitQueueCommons.UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME;

@Slf4j
@RequiredArgsConstructor
@Configuration
class CategoryValueRetentionInboundAmqpConfiguration {

  private final DeleteCategoryValuesUseCase deleteCategoryValuesUseCase;

  @RabbitListener(queues = UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME)
  public void categoryValueRetentionReceive(AlertsExpired alertsExpired) {
    log.info("Category value retention message received");
    deleteCategoryValuesUseCase.delete(alertsExpired.getAlertsList());
  }
}
