package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.AdjudicationRecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.BridgeRecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseRecommendation;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import com.google.protobuf.util.Values;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.Instant;
import javax.annotation.Nullable;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.toInstant;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class WarehouseRecommendationService {

  private final IndexAlertUseCase indexAlertUseCase;
  private final IndexedAlertBuilderFactory alertBuilderFactory;

  @Order(1)
  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  void accept(RecommendationCompletedEvent event) {
    var alertData = event.getData(AlertData.class);

    var alert = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(alertData.getDiscriminator())
        .setName(getAeAlertName(event))
        .addPayload(buildWarehouseRecommendation(event))
        .build();

    indexAlertUseCase.index(alert);
  }

  private WarehouseRecommendation buildWarehouseRecommendation(
      RecommendationCompletedEvent original) {
    if (original instanceof AdjudicationRecommendationCompletedEvent) {
      var event = (AdjudicationRecommendationCompletedEvent) original;

      var policy = getFieldFromFirstMatch(event, "policy");
      var policyTitle = getFieldFromFirstMatch(event, "policy_title");

      var recommendation = event.getRecommendation().getRecommendation();
      return WarehouseRecommendation.builder()
          .recommendationComment(mapComment(recommendation.getRecommendationComment()))
          .recommendedAction(mapAction(recommendation.getRecommendedAction()))
          .createTime(toInstant(recommendation.getCreateTime()).toString())
          .policy(policy)
          .policyTitle(policyTitle)
          .status(AlertMessageStatus.RECOMMENDED.name())
          .deliveryStatus(DeliveryStatus.PENDING.name())
          .build();
    } else {
      var event = (BridgeRecommendationCompletedEvent) original;
      return WarehouseRecommendation.builder()
          .recommendationComment(mapComment(null))
          .recommendedAction(mapAction(null))
          .createTime(Instant.now().toString())
          .status(event.getStatus())
          .reason(event.getReason())
          .deliveryStatus(DeliveryStatus.PENDING.name())
          .policy("")
          .policyTitle("")
          .build();
    }
  }

  private String getFieldFromFirstMatch(
      AdjudicationRecommendationCompletedEvent event, String name) {

    var metadata = event.getRecommendation().getMetadata();
    var matchesList = metadata.getMatchesList();

    if (!matchesList.isEmpty()) {
      return matchesList.get(0).getReason().getFieldsOrDefault(
          name, Values.of("")).getStringValue();
    }

    return "";
  }

  private static String mapAction(@Nullable String action) {
    return StringUtils.isNotBlank(action) ? action : "ACTION_INVESTIGATE";
  }

  private static String mapComment(@Nullable String comment) {
    return StringUtils.isNotBlank(comment)
           ? comment
           : "S8 recommended action: Manual Investigation";
  }

  private String getAeAlertName(RecommendationCompletedEvent original) {
    if (original instanceof AdjudicationRecommendationCompletedEvent) {
      var event = (AdjudicationRecommendationCompletedEvent) original;
      return event.getAlertName();
    } else {
      return "";
    }
  }
}
