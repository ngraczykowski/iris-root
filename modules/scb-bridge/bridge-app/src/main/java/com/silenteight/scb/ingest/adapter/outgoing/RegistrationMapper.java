package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.registration.api.library.v1.*;
import com.silenteight.scb.ingest.domain.model.*;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;

import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
@RequiredArgsConstructor
class RegistrationMapper {

  private final PayloadConverter converter;

  RegisterBatchIn toRegisterBatchIn(Batch batch) {
    return RegisterBatchIn.builder()
        .batchId(batch.id())
        .alertCount(batch.alertCount())
        .batchMetadata(converter.serializeFromObjectToJson(batch.metadata()).getOrElse(EMPTY))
        .batchPriority(batch.priority().getValue())
        .build();
  }

  RegisterAlertsAndMatchesIn toRegisterAlertsAndMatchesIn(RegistrationRequest request) {
    return RegisterAlertsAndMatchesIn.builder()
        .batchId(request.getBatchId())
        .alertsWithMatches(request.getAlertsWithMatches().stream()
            .map(this::toAlertWithMatchesIn)
            .toList())
        .build();
  }

  private AlertWithMatchesIn toAlertWithMatchesIn(AlertWithMatches alertWithMatches) {
    return AlertWithMatchesIn.builder()
        .alertId(alertWithMatches.getAlertId())
        .status(AlertStatusIn.valueOf(alertWithMatches.getStatus().name()))
        .errorDescription(getErrorDescription(alertWithMatches).getDescription())
        .metadata(getMetadata(alertWithMatches))
        .matches(alertWithMatches.getMatches().stream().map(this::toMatchIn).toList())
        .build();
  }

  private AlertErrorDescription getErrorDescription(
      AlertWithMatches alertWithMatches) {
    return Optional
        .ofNullable(alertWithMatches.getAlertErrorDescription())
        .orElse(AlertErrorDescription.NONE);
  }

  private String getMetadata(AlertWithMatches alertWithMatches) {
    if (isMetadataNull(alertWithMatches)) {
      return EMPTY;
    }
    return converter.serializeFromObjectToJson(alertWithMatches.getMetadata())
        .getOrElse(EMPTY);
  }

  private boolean isMetadataNull(AlertWithMatches alertWithMatches) {
    return alertWithMatches.getMetadata() == null;
  }

  private MatchIn toMatchIn(Match match) {
    return MatchIn.builder()
        .matchId(match.getId())
        .build();
  }

  RegistrationResponse toRegistrationResponse(RegisterAlertsAndMatchesOut response) {
    return RegistrationResponse.builder()
        .registeredAlertWithMatches(
            response.getRegisteredAlertWithMatches().stream()
                .map(this::toRegisteredAlertWithMatches)
                .toList())
        .build();
  }

  private RegisteredAlertWithMatches toRegisteredAlertWithMatches(
      RegisteredAlertWithMatchesOut response) {
    return RegisteredAlertWithMatches.builder()
        .alertId(response.getAlertId())
        .alertName(response.getAlertName())
        .alertStatus(AlertStatus.valueOf(response.getAlertStatus().name()))
        .registeredMatches(
            response.getRegisteredMatches().stream().map(this::toRegisteredMatch).toList())
        .build();
  }

  private RegisteredMatch toRegisteredMatch(RegisteredMatchOut registeredMatchOut) {
    return RegisteredMatch.builder()
        .matchId(registeredMatchOut.getMatchId())
        .matchName(registeredMatchOut.getMatchName())
        .build();
  }
}
