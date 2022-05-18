package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.warehouse.index.model.IndexBridgeRecommendationRequest;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseRecommendation;
import com.silenteight.payments.bridge.warehouse.index.port.IndexBridgeRecommendationUseCase;

import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.silenteight.payments.bridge.warehouse.index.service.ActionMapper.mapAction;
import static com.silenteight.payments.bridge.warehouse.index.service.ActionMapper.mapComment;

@Service
@RequiredArgsConstructor
@Slf4j
class IndexBridgeRecommendationService implements IndexBridgeRecommendationUseCase {

  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final IndexAlertUseCase indexAlertUseCase;

  @Override
  public void index(IndexBridgeRecommendationRequest request) {
    var alertBuilder = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(request.getDiscriminator());

    var bridgeRecommendation = WarehouseRecommendation.builder()
        .fircoSystemId(request.getSystemId())
        .recommendationComment(mapComment(null))
        .recommendedAction(mapAction(null))
        .createTime(Instant.now().toString())
        .status(request.getStatus())
        .reason(request.getReason())
        .deliveryStatus("PENDING")
        .build();

    alertBuilder.addPayload(bridgeRecommendation);
    indexAlertUseCase.index(alertBuilder.build(), RequestOrigin.CMAPI);
  }
}
