package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRecommendationRequest;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatchRecommendation;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseRecommendation;
import com.silenteight.payments.bridge.warehouse.index.port.IndexRecommendationUseCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.toInstant;
import static com.silenteight.payments.bridge.warehouse.index.service.ActionMapper.mapAction;
import static com.silenteight.payments.bridge.warehouse.index.service.ActionMapper.mapComment;
import static java.util.Collections.emptyMap;

@Service
@Slf4j
class IndexRecommendationService implements IndexRecommendationUseCase {

  private static final Printer JSON_PRINTER = JsonFormat.printer();

  private final IndexAlertUseCase indexAlertUseCase;
  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final ObjectMapper objectMapper;
  private final MapType additionalDataMapType;

  IndexRecommendationService(
      IndexAlertUseCase indexAlertUseCase, IndexedAlertBuilderFactory alertBuilderFactory,
      ObjectMapper objectMapper) {

    this.indexAlertUseCase = indexAlertUseCase;
    this.alertBuilderFactory = alertBuilderFactory;
    this.objectMapper = objectMapper;
    additionalDataMapType =
        objectMapper.getTypeFactory().constructMapType(TreeMap.class, String.class, JsonNode.class);
  }

  @Override
  public void index(IndexRecommendationRequest request) {
    var alertBuilder = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(request.getDiscriminator());

    var recommendation = request.getRecommendation();
    var indexRecommendation = WarehouseRecommendation.builder()
        .fircoSystemId(request.getSystemId())
        .recommendationName(recommendation.getName())
        .recommendationComment(mapComment(recommendation.getRecommendationComment()))
        .recommendedAction(mapAction(recommendation.getRecommendedAction()))
        .createTime(toInstant(recommendation.getCreateTime()).toString())
        .status("RECOMMENDED")
        .deliveryStatus("PENDING")
        .build();

    alertBuilder
        .setName(recommendation.getAlert())
        .addPayload(indexRecommendation);

    var metadata = request.getMetadata();
    metadata.getMatchesList().forEach(matchMetadata -> alertBuilder.newMatch()
        .setName(matchMetadata.getMatch())
        .setDiscriminator(matchMetadata.getMatch())
        .addPayload(buildMatchPayload(matchMetadata))
        .finish());

    indexAlertUseCase.index(alertBuilder.build(), RequestOrigin.CMAPI);
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



}
