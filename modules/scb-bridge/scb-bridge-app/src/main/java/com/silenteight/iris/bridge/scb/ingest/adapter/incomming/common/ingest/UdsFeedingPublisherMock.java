/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.feeding.domain.FeedingFacade;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.domain.exceptons.IngestJsonMessageException;
import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.iris.bridge.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.grpc.RecommendationServiceClientMock;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.AlertOut.AlertStatus;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Profile("dev")
public class UdsFeedingPublisherMock implements UdsFeedingPublisher {

  private final UdsFeedingPublisher udsFeedingPublisher;
  private final PayloadConverter payloadConverter;
  private final RabbitTemplate rabbitTemplate;
  private final RecommendationServiceClient recommendationServiceClient;
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);

  UdsFeedingPublisherMock(
      FeedingFacade feedingFacade,
      PayloadConverter payloadConverter,
      RabbitTemplate rabbitTemplate,
      RecommendationServiceClient recommendationServiceClient) {
    this.udsFeedingPublisher = new UdsFeedingPublisherImpl(1000, 8, 8, feedingFacade);
    this.payloadConverter = payloadConverter;
    this.rabbitTemplate = rabbitTemplate;
    this.recommendationServiceClient = recommendationServiceClient;
  }

  public IngestedAlertsStatus publishToUds(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext batchContext) {

    udsFeedingPublisher.publishToUds(internalBatchId, alerts, batchContext);

    executorService.schedule(() -> {

      var message = createMessageBatchCompleted(internalBatchId);
      stubRecommendationService(message.getAnalysisName(), alerts, internalBatchId);
      sendMessageBatchCompleted(message);

    }, RandomUtils.nextInt(3000, 6000), TimeUnit.MILLISECONDS);
    return new IngestedAlertsStatus(alerts, List.of());
  }

  private void stubRecommendationService(
      String analysisName, List<Alert> alerts, String internalBatchId) {
    recommendationServiceClientMock().add(
        analysisName,
        alerts.stream()
            .map(a -> RecommendationOut.builder()
                .batchId(internalBatchId)
                .recommendedAction(RecommendedAction.ACTION_INVESTIGATE.toString())
                .recommendationComment("someComment")
                .recommendedAt(OffsetDateTime.parse("2007-12-03T10:15:30+01:00"))
                .matches(List.of())
                .alert(AlertOut.builder()
                    .id(a.id().sourceId())
                    .name("alert/" + a.id().sourceId())
                    .status(AlertStatus.SUCCESS)
                    .metadata(payloadConverter
                        .serializeFromObjectToJson(AlertMetadata.builder()
                            .systemId(a.details().getSystemId())
                            .discriminator(a.id().discriminator())
                            .build())
                        .orElseThrow(IngestJsonMessageException::new))
                    .build())
                .build())
            .toList());
  }

  private RecommendationServiceClientMock recommendationServiceClientMock() {
    return (RecommendationServiceClientMock) recommendationServiceClient;
  }

  private void sendMessageBatchCompleted(MessageBatchCompleted message) {
    rabbitTemplate.convertAndSend(
        "core-bridge.notify-batch-completed-exchange", "solving", message);
  }

  private MessageBatchCompleted createMessageBatchCompleted(String internalBatchId) {
    return MessageBatchCompleted.newBuilder()
        .setBatchId(internalBatchId)
        .setAnalysisName("analysisName-" + internalBatchId)
        .setBatchMetadata("{\"batchSource\":\"GNS_RT\"}")
        .build();
  }
}
