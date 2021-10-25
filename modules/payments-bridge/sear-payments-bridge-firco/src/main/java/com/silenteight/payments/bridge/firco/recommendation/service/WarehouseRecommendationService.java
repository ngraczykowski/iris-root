package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.data.api.v2.Alert;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.AdjudicationRecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.BridgeRecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory.AlertBuilder;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatchRecommendation;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseRecommendation;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.RECOMMENDATION_COMPLETED;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.toInstant;
import static java.util.Collections.emptyMap;

@MessageEndpoint
@Slf4j
class WarehouseRecommendationService {

  private static final Printer JSON_PRINTER = JsonFormat.printer();

  private final IndexAlertUseCase indexAlertUseCase;
  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final ObjectMapper objectMapper;
  private final MapType additionalDataMapType;

  WarehouseRecommendationService(
      IndexAlertUseCase indexAlertUseCase, IndexedAlertBuilderFactory alertBuilderFactory,
      ObjectMapper objectMapper) {

    this.indexAlertUseCase = indexAlertUseCase;
    this.alertBuilderFactory = alertBuilderFactory;
    this.objectMapper = objectMapper;

    additionalDataMapType =
        objectMapper.getTypeFactory().constructMapType(TreeMap.class, String.class, JsonNode.class);
  }

  @Order(1)
  @ServiceActivator(inputChannel = RECOMMENDATION_COMPLETED)
  void accept(RecommendationCompletedEvent event) {
    var alertData = event.getData(AlertData.class);

    indexAlertUseCase.index(buildAlert(alertData, event), RequestOrigin.CMAPI);
  }

  private Alert buildAlert(AlertData alertData, RecommendationCompletedEvent event) {
    var alertBuilder = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(alertData.getDiscriminator());

    if (event instanceof AdjudicationRecommendationCompletedEvent) {
      buildAdjudicatedRecommendation(
          alertBuilder, (AdjudicationRecommendationCompletedEvent) event);
    } else {
      buildBridgeRecommendation(alertBuilder, (BridgeRecommendationCompletedEvent) event);
    }


    return alertBuilder.build();
  }

  private AlertBuilder buildAdjudicatedRecommendation(
      AlertBuilder alertBuilder,
      AdjudicationRecommendationCompletedEvent event) {

    var alertData = event.getData(AlertData.class);

    var recommendation = event.getRecommendation().getRecommendation();
    var indexRecommendation = WarehouseRecommendation.builder()
        .fircoSystemId(alertData.getSystemId())
        .recommendationName(recommendation.getName())
        .recommendationComment(mapComment(recommendation.getRecommendationComment()))
        .recommendedAction(mapAction(recommendation.getRecommendedAction()))
        .createTime(toInstant(recommendation.getCreateTime()).toString())
        .status(AlertMessageStatus.RECOMMENDED.name())
        .deliveryStatus(DeliveryStatus.PENDING.name())
        .build();

    alertBuilder
        .setName(recommendation.getAlert())
        .addPayload(indexRecommendation);

    var metadata = event.getRecommendation().getMetadata();
    metadata.getMatchesList().forEach(matchMetadata -> alertBuilder.newMatch()
        .setName(matchMetadata.getMatch())
        .setDiscriminator(matchMetadata.getMatch())
        .addPayload(buildMatchPayload(matchMetadata))
        .finish());

    return alertBuilder;
  }

  private WarehouseMatchRecommendation buildMatchPayload(MatchMetadata matchMetadata) {
    return WarehouseMatchRecommendation.builder()
        .categoryValues(matchMetadata.getCategoriesMap())
        .solution(matchMetadata.getSolution())
        .additionalData(buildAdditionalData(matchMetadata.getReason()))
        .featureValues(buildFeatureValues(matchMetadata.getFeaturesMap()))
        .build();
  }

  private Map<String, JsonNode> buildAdditionalData(Struct reason) {
    String json;
    try {
      json = JSON_PRINTER.print(reason);
    } catch (InvalidProtocolBufferException e) {
      log.warn("Unable to convert reason to JSON", e);
      return emptyMap();
    }

    try {
      return objectMapper.readValue(json, additionalDataMapType);
    } catch (JsonProcessingException e) {
      log.warn("Unable to build additional data from JSON", e);
      return emptyMap();
    }
  }

  private static Map<String, String> buildFeatureValues(Map<String, FeatureMetadata> featuresMap) {
    return featuresMap.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().getSolution()));
  }

  private static AlertBuilder buildBridgeRecommendation(
      AlertBuilder alertBuilder,
      BridgeRecommendationCompletedEvent event) {

    var alertData = event.getData(AlertData.class);
    var bridgeRecommendation = WarehouseRecommendation.builder()
        .fircoSystemId(alertData.getSystemId())
        .recommendationComment(mapComment(null))
        .recommendedAction(mapAction(null))
        .createTime(Instant.now().toString())
        .status(event.getStatus())
        .reason(event.getReason())
        .deliveryStatus(DeliveryStatus.PENDING.name())
        .build();

    return alertBuilder.addPayload(bridgeRecommendation);
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
