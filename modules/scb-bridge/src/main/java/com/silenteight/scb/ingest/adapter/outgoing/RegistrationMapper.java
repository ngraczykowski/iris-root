package com.silenteight.scb.ingest.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.registration.api.library.v1.*;
import com.silenteight.scb.ingest.domain.model.*;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredAlertWithMatches;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse.RegisteredMatch;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class RegistrationMapper {

  private final PayloadConverter converter;

  RegisterBatchIn toRegisterBatchIn(Batch batch) {
    return RegisterBatchIn.builder()
        .batchId(batch.id())
        .alertCount(batch.alertCount())
        .batchMetadata(converter.serializeFromObjectToJson(batch.metadata()))
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

  private AlertWithMatchesIn toAlertWithMatchesIn(AlertWithMatchesMetadata alertWithMetadata) {
    return AlertWithMatchesIn.builder()
        .alertId(alertWithMetadata.getAlertId())
        .status(AlertStatusIn.valueOf(alertWithMetadata.getStatus().name()))
        .errorDescription(getErrorDescription(alertWithMetadata).getDescription())
        .metadata(getMetadata(alertWithMetadata))
        .matches(alertWithMetadata.getMatches().stream().map(this::toMatchIn).toList())
        .build();
  }

  private AlertErrorDescription getErrorDescription(
      AlertWithMatchesMetadata alertWithMatchesMetadata) {
    return Optional
        .ofNullable(alertWithMatchesMetadata.getAlertErrorDescription())
        .orElse(AlertErrorDescription.NONE);
  }

  private String getMetadata(AlertWithMatchesMetadata alertWithMatchesMetadata) {
    return Optional.ofNullable(alertWithMatchesMetadata.getMetadata())
        .map(converter::serializeFromObjectToJson)
        .orElse("");
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
