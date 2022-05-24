package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import com.silenteight.bridge.core.registration.domain.model.*;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class JdbcAlertMapper {

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
        .isArchived(alert.isArchived())
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

  AlertToRetention toAlertToRetention(AlertIdNameBatchIdProjection projection) {
    return AlertToRetention.builder()
        .alertPrimaryId(projection.id())
        .alertId(projection.alertId())
        .batchId(projection.batchId())
        .alertName(projection.name())
        .build();
  }

  MapSqlParameterSource toAlertParameters(Alert alert) {
    return new MapSqlParameterSource()
        .addValue("alertId", alert.alertId())
        .addValue("batchId", alert.batchId())
        .addValue("name", alert.name())
        .addValue("metadata", alert.metadata())
        .addValue("status", alert.status().name())
        .addValue("errorDescription", alert.errorDescription())
        .addValue("alertTime", Timestamp.from(Optional.ofNullable(alert.alertTime())
            .map(OffsetDateTime::toInstant)
            .orElseGet(Instant::now)))
        .addValue("isArchived", alert.isArchived());
  }

  MapSqlParameterSource toMatchParameters(Alert alert, Match match) {
    return new MapSqlParameterSource()
        .addValue("matchId", match.matchId())
        .addValue("alertId", alert.alertId())
        .addValue("batchId", alert.batchId())
        .addValue("name", match.name());
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
