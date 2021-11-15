package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.RequestHitDto;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAlert;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertRegisteredUseCase;
import com.silenteight.payments.bridge.warehouse.index.service.IndexedAlertBuilderFactory.AlertBuilder;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class IndexAlertRegisteredService implements IndexAlertRegisteredUseCase {

  private final IndexedAlertBuilderFactory payloadBuilderFactory;
  private final IndexAlertService indexService;

  @Override
  public void index(AlertData alertData, AlertMessageDto alertMessageDto,
      AeAlert aeAlert, String status) {

    var alertBuilder = payloadBuilderFactory.newBuilder()
        .setName(aeAlert.getAlertName())
        .setDiscriminator(alertData.getDiscriminator())
        .addPayload(mapToWarehouseAlert(alertData, status));
    addMatchesToBuilder(alertBuilder, aeAlert, alertMessageDto.getHits());
    indexService.index(alertBuilder.build(), RequestOrigin.CMAPI);
  }

  private WarehouseAlert mapToWarehouseAlert(AlertData alertData, String status) {
    return WarehouseAlert.builder()
        .alertMessageId(alertData.getAlertId().toString())
        .fircoSystemId(alertData.getSystemId())
        .status(status)
        .build();
  }

  private void addMatchesToBuilder(
      AlertBuilder alertBuilder,
      AeAlert aeAlert, List<RequestHitDto> hits) {
    for (int index = 0; index < hits.size(); index++) {
      var hit = hits.get(index).getHit();
      if (!hit.isBlocking()) {
        continue;
      }

      var matchId = hit.getMatchId(index);
      var matchName = aeAlert.getMatches().get(matchId);
      if (matchName == null) {
        log.error("No matchName found for matchId: {} for. The match will not be added to "
            + "warehouse for alertId: {} ", matchId, aeAlert.getAlertId());
      } else {
        alertBuilder.newMatch()
            .setName(matchName)
            .setDiscriminator(matchName)
            .addPayload(
                WarehouseMatch.builder()
                    .matchId(matchId)
                    .matchingText(hit.getMatchingText())
                    .build())
            .finish();
      }
    }
  }

}
