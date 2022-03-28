package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.warehouse.index.model.IndexAlertRegisteredRequest;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertRegisteredUseCase;
import com.silenteight.payments.bridge.warehouse.index.service.IndexedAlertBuilderFactory.AlertBuilder;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class IndexAlertRegisteredService implements IndexAlertRegisteredUseCase {

  private final IndexedAlertBuilderFactory payloadBuilderFactory;
  private final IndexAlertService indexService;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public void index(IndexAlertRegisteredRequest request) {

    var alertBuilder = payloadBuilderFactory.newBuilder()
        .setName(request.getName())
        .setDiscriminator(request.getDiscriminator())
        .addPayload(request.toWarehouseAlert());
    addMatchesToBuilder(alertBuilder, request.getMatches());
    indexService.index(alertBuilder.build(), RequestOrigin.CMAPI);
  }

  private void addMatchesToBuilder(AlertBuilder alertBuilder, List<IndexMatch> matches) {
    for (var match : matches) {
      var matchId = match.getMatchId();
      var matchName = match.getMatchName();
      alertBuilder.newMatch()
          .setName(matchName)
          .setDiscriminator(matchName)
          .addPayload(createWhMatch(match, matchId))
          .finish();
    }
  }

  private static WarehouseMatch createWhMatch(IndexMatch match, String matchId) {
    return WarehouseMatch.builder()
        .matchId(matchId)
        .matchingText(match.getMatchingTexts())
        .build();
  }
}
