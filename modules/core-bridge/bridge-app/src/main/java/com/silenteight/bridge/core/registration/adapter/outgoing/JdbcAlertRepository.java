package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.adapter.outgoing.AlertEntity.Status;
import com.silenteight.bridge.core.registration.domain.model.Alert;
import com.silenteight.bridge.core.registration.domain.model.AlertId;
import com.silenteight.bridge.core.registration.domain.model.Match;
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class JdbcAlertRepository implements AlertRepository {

  private final CrudAlertRepository alertRepository;

  @Override
  public void saveAlerts(List<Alert> alerts) {
    var alertEntities = alerts.stream()
        .map(this::mapToAlertEntity)
        .toList();
    alertRepository.saveAll(alertEntities);
  }

  @Override
  public List<AlertId> findAllAlertIdsByBatchIdAndAlertIdIn(String batchId, List<String> alertIds) {
    return alertRepository.findByBatchIdAndAlertIdIn(batchId, alertIds).stream()
        .map(this::mapToAlertId)
        .toList();
  }

  @Override
  public List<Alert> findAllByBatchId(String batchId) {
    return alertRepository.findAllByBatchId(batchId)
        .stream()
        .map(this::mapToAlert)
        .toList();
  }

  private AlertId mapToAlertId(AlertIdProjection alertIdProjection) {
    return new AlertId(alertIdProjection.alertId());
  }

  private AlertEntity mapToAlertEntity(Alert alert) {
    return AlertEntity.builder()
        .name(alert.name())
        .status(Status.valueOf(alert.status().name()))
        .alertId(alert.alertId())
        .batchId(alert.batchId())
        .matches(alert.matches().stream()
            .map(this::mapToMatchEntity)
            .collect(Collectors.toSet()))
        .errorDescription(alert.errorDescription())
        .build();
  }

  private MatchEntity mapToMatchEntity(Match match) {
    return MatchEntity.builder()
        .name(match.name())
        .status(MatchEntity.Status.valueOf(match.status().name()))
        .matchId(match.matchId())
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();
  }

  private Alert mapToAlert(AlertEntity alert) {
    return Alert.builder()
        .name(alert.name())
        .status(Alert.Status.valueOf(alert.status().name()))
        .alertId(alert.alertId())
        .batchId(alert.batchId())
        .matches(alert.matches().stream()
            .map(this::mapToMatch)
            .toList())
        .errorDescription(alert.errorDescription())
        .build();
  }

  private Match mapToMatch(MatchEntity match) {
    return Match.builder()
        .name(match.name())
        .status(Match.Status.valueOf(match.status().name()))
        .matchId(match.matchId())
        .build();
  }
}
