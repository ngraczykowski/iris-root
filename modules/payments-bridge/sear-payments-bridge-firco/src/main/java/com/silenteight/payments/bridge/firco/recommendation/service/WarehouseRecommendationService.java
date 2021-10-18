package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.common.model.WarehouseRecommendation;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.AdjudicationRecommendationCompletedEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Parser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Nullable;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;

@MessageEndpoint
@RequiredArgsConstructor
class WarehouseRecommendationService {

  protected static final Parser JSON_TO_STRUCT_PARSER = JsonFormat.parser();

  private final ObjectMapper objectMapper;
  private final CommonChannels commonChannels;

  @Order(1)
  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  void accept(RecommendationCompletedEvent event) {
    var alertData = event.getData(AlertData.class);
    var payloadBuilder = Struct.newBuilder();
    try {
      var warehouseRecommendation = buildWarehouseRecommendation(event);
      var alertDataJson = objectMapper.writeValueAsString(warehouseRecommendation);

      // Unknown alert
      JSON_TO_STRUCT_PARSER.merge(alertDataJson, payloadBuilder);
    } catch (InvalidProtocolBufferException | JsonProcessingException e) {
      e.printStackTrace();
    }

    var alertBuilder = Alert.newBuilder()
        .setDiscriminator(alertData.getDiscriminator())
        .setPayload(payloadBuilder)
        .build();

    var indexRequest = ProductionDataIndexRequest.newBuilder()
        .addAlerts(alertBuilder)
        .build();

    commonChannels.warehouseRequested().send(
        MessageBuilder.withPayload(indexRequest).build());
  }

  private WarehouseRecommendation buildWarehouseRecommendation(
      RecommendationCompletedEvent original) {

    if (AdjudicationRecommendationCompletedEvent.class.isAssignableFrom(original.getClass())) {
      AdjudicationRecommendationCompletedEvent event =
          (AdjudicationRecommendationCompletedEvent) original;
      var recommendation = event.getRecommendation().getRecommendation();
      return WarehouseRecommendation.builder()
          .recommendationComment(mapComment(recommendation.getRecommendationComment()))
          .recommendedAction(mapAction(recommendation.getRecommendedAction()))
          .policy("")
          .policyTitle("")
          // TODO
          .build();
    } else {
      return WarehouseRecommendation.builder()
          .recommendationComment(mapComment(null))
          .recommendedAction(mapAction(null))
          .policy("")
          .policyTitle("")
          // TODO
          .build();
    }
  }

  private static String mapAction(@Nullable String action) {
    return StringUtils.isNotBlank(action) ? action : "ACTION_INVESTIGATE";
  }

  private static String mapComment(@Nullable String comment) {
    return StringUtils.isNotBlank(comment)
           ? comment
           : "S8 recommended action: Manual Investigation";
  }

}
