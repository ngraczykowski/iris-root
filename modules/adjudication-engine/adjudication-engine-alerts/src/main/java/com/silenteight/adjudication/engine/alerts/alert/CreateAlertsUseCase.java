package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.toOffsetDateTime;
import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Service
class CreateAlertsUseCase {

  private static final int MAX_PRIORITY = 10;
  private static final int MIN_PRIORITY = 1;
  private static final int DEFAULT_PRIORITY = 5;

  @NonNull
  private final AlertRepository repository;

  @Transactional
  List<Alert> createAlerts(Iterable<Alert> alerts) {
    return StreamSupport.stream(alerts.spliterator(), false)
        .map(this::createEntity)
        .map(repository::save)
        .map(AlertEntity::toAlert)
        .collect(toUnmodifiableList());
  }

  private AlertEntity createEntity(Alert alert) {
    var builder = AlertEntity.builder()
        .clientAlertIdentifier(alert.getAlertId())
        .priority(DEFAULT_PRIORITY);

    if (alert.getPriority() != 0) {
      builder.priority(Math.max(MIN_PRIORITY, Math.min(MAX_PRIORITY, alert.getPriority())));
    }

    if (alert.hasAlertTime()) {
      builder.alertedAt(toOffsetDateTime(alert.getAlertTime()));
    }

    if (alert.getLabelsCount() > 0) {
      builder.labels(alert.getLabelsMap());
    }

    return builder.build();
  }
}
