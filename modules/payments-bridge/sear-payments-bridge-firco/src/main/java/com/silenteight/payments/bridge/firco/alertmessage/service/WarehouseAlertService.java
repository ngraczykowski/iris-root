package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.RequestHitDto;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.AlertAddedToAnalysisEvent;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory;
import com.silenteight.payments.bridge.warehouse.index.model.IndexedAlertBuilderFactory.AlertBuilder;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseAlert;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseMatch;
import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertUseCase;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;
import java.util.UUID;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_ADDED_TO_ANALYSIS;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class WarehouseAlertService {

  private final IndexedAlertBuilderFactory payloadBuilderFactory;
  private final AlertMessageStatusService alertMessageStatusService;
  private final IndexAlertUseCase indexAlertUseCase;

  @ServiceActivator(inputChannel = ALERT_ADDED_TO_ANALYSIS)
  void accept(AlertAddedToAnalysisEvent event) {
    var alertData = event.getData(AlertData.class);
    var alertMessageDto = event.getData(AlertMessageDto.class);

    var alertBuilder = payloadBuilderFactory.newBuilder()
        .setName(event.getAlertRegisteredName())
        .setDiscriminator(alertData.getDiscriminator())
        .addPayload(mapToWarehouseAlert(event.getAlertId(), alertData));
    addMatchesToBuilder(alertBuilder, event, alertMessageDto.getHits());
    indexAlertUseCase.index(alertBuilder.build(), RequestOrigin.CMAPI);
  }

  private WarehouseAlert mapToWarehouseAlert(UUID alertId, AlertData alertData) {
    var status = alertMessageStatusService.findByAlertId(alertId);

    return WarehouseAlert.builder()
        .alertMessageId(alertId.toString())
        .fircoSystemId(alertData.getSystemId())
        .status(status.getStatus().name())
        .build();
  }

  private void addMatchesToBuilder(AlertBuilder alertBuilder,
      AlertAddedToAnalysisEvent event, List<RequestHitDto> hits) {
    for (int index = 0; index < hits.size(); index++) {
      var hit = hits.get(index).getHit();
      if (!hit.isBlocking()) {
        continue;
      }

      var matchId = hit.getMatchId(index);
      var matchName = event.getMatches().get(matchId);
      if (matchName == null) {
        log.error("No matchName found for matchId: {} for. The match will not be added to "
            + "warehouse for alertId: {} ", matchId, event.getAlertId());
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
