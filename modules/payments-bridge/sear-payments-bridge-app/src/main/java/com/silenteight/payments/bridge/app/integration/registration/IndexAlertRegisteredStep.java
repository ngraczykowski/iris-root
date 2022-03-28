package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.dto.input.RequestHitDto;
import com.silenteight.payments.bridge.warehouse.index.model.IndexAlertRegisteredRequest;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertRegisteredUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class IndexAlertRegisteredStep {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final IndexAlertRegisteredUseCase indexAlertRegisteredUseCase;

  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  void invoke(Context ctx) {
    var status = alertMessageStatusUseCase.getStatus(ctx.getAlertId());
    indexAlertRegisteredUseCase.index(
        createRequest(
            ctx.getAlertData(), ctx.getAeAlert(), ctx.getAlertMessageDto(), status.name()));
  }

  private static IndexAlertRegisteredRequest createRequest(
      AlertData alertData, AeAlert aeAlert, AlertMessageDto alertMessageDto, String status) {
    return IndexAlertRegisteredRequest
        .builder()
        .name(aeAlert.getAlertName())
        .discriminator(alertData.getDiscriminator())
        .alertId(alertData.getAlertId())
        .systemId(alertMessageDto.getSystemID())
        .status(status)
        .matches(createIndexMatches(aeAlert, alertMessageDto.getHits()))
        .build();
  }

  private static List<IndexMatch> createIndexMatches(
      AeAlert aeAlert, List<RequestHitDto> hits) {

    var matches = new ArrayList<IndexMatch>();

    for (int i = 0; i < hits.size(); i++) {
      var hit = hits.get(i).getHit();
      if (!hit.isBlocking()) {
        continue;
      }

      var matchId = hit.getMatchId(i);
      var matchName = aeAlert.getMatches().get(matchId);

      var indexMatch = IndexMatch
          .builder()
          .matchId(matchId)
          .matchName(matchName)
          .matchingTexts(hit.getMatchingText())
          .build();

      matches.add(indexMatch);
    }

    return matches;
  }
}
