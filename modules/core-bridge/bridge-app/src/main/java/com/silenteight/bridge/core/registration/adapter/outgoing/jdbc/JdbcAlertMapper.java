package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.*;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

  Alert toAlert(AlertEntity alert) {
    return Alert.builder()
        .name(alert.name())
        .status(AlertStatus.valueOf(alert.status().name()))
        .alertId(alert.alertId())
        .batchId(alert.batchId())
        .metadata(alert.metadata())
        .matches(alert.matches().stream()
            .map(this::toMatch)
            .toList())
        .errorDescription(alert.errorDescription())
        .build();
  }

  AlertName toAlertName(AlertNameProjection projection) {
    return new AlertName(projection.name());
  }

  List<AlertWithMatches> toAlertWithMatches(List<AlertWithMatchNamesProjection> projections) {
    return projections
        .stream()
        .collect(Collectors.groupingBy(this::toAlertKey, Collectors.toList()))
        .entrySet().stream()
        .map(entry -> toAlertWithMatches(entry.getKey(), entry.getValue()))
        .toList();
  }

  private MatchEntity toMatchEntity(Match match) {
    return MatchEntity.builder()
        .name(match.name())
        .matchId(match.matchId())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  private Match toMatch(MatchEntity match) {
    return new Match(match.name(), match.matchId());
  }

  private AlertWithMatches toAlertWithMatches(
      AlertKey alert, List<AlertWithMatchNamesProjection> projections) {
    return AlertWithMatches.builder()
        .id(alert.alertId)
        .name(alert.name)
        .metadata(alert.metadata)
        .errorDescription(alert.errorDescription)
        .status(AlertStatus.valueOf(alert.status))
        .matches(projections.stream()
            .map(this::toMatch)
            .filter(Objects::nonNull)
            .toList()
        ).build();
  }

  private AlertKey toAlertKey(AlertWithMatchNamesProjection projection) {
    return new AlertKey(
        projection.alertName(),
        projection.alertId(),
        projection.alertStatus(),
        projection.alertMetadata(),
        projection.alertErrorDescription()
    );
  }

  private AlertWithMatches.Match toMatch(AlertWithMatchNamesProjection projection) {
    return Optional.ofNullable(projection.matchId())
        .map(matchId -> new AlertWithMatches.Match(matchId, projection.matchName()))
        .orElse(null);
  }

  private record AlertKey(
      String name,
      String alertId,
      String status,
      String metadata,
      String errorDescription
  ) {}
}
