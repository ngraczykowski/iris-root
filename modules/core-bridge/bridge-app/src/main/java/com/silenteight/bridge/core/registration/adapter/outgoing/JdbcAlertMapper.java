package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.*;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
class JdbcAlertMapper {

  AlertEntity toAlertEntity(Alert alert) {
    return AlertEntity.builder()
        .name(alert.name())
        .status(Status.valueOf(alert.status().name()))
        .alertId(alert.alertId())
        .batchId(alert.batchId())
        .metadata(alert.metadata())
        .matches(alert.matches().stream()
            .map(this::toMatchEntity)
            .collect(Collectors.toSet()))
        .errorDescription(alert.errorDescription())
        .build();
  }

  AlertId toAlertId(AlertIdNameProjection projection) {
    return new AlertId(projection.alertId());
  }

  Alert toAlert(AlertEntity alert) {
    return Alert.builder()
        .name(alert.name())
        .status(Alert.Status.valueOf(alert.status().name()))
        .alertId(alert.alertId())
        .batchId(alert.batchId())
        .metadata(alert.metadata())
        .matches(alert.matches().stream()
            .map(this::toMatch)
            .toList())
        .errorDescription(alert.errorDescription())
        .build();
  }

  AlertName toAlertName(AlertIdNameProjection projection) {
    return new AlertName(projection.alertName());
  }

  AlertStatusStatistics toAlertsStatistics(List<AlertStatusStatisticsProjection> projection) {
    return new AlertStatusStatistics(projection.stream()
        .collect(Collectors.toMap(
            alert -> Alert.Status.valueOf(alert.status()),
            AlertStatusStatisticsProjection::count)));
  }

  private MatchEntity toMatchEntity(Match match) {
    return MatchEntity.builder()
        .name(match.name())
        .status(MatchEntity.Status.valueOf(match.status().name()))
        .matchId(match.matchId())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  private Match toMatch(MatchEntity match) {
    return Match.builder()
        .name(match.name())
        .status(Match.Status.valueOf(match.status().name()))
        .matchId(match.matchId())
        .build();
  }
}
