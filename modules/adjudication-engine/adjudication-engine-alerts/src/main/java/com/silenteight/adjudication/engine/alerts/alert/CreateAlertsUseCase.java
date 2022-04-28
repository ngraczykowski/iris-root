package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static com.google.common.collect.Lists.partition;
import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.toOffsetDateTime;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.StreamSupport.stream;

@RequiredArgsConstructor
@Service
class CreateAlertsUseCase {

  private static final int MAX_PRIORITY = 10;
  private static final int MIN_PRIORITY = 1;
  private static final int DEFAULT_PRIORITY = 5;
  private static final int SAVE_PARTITION_SIZE = 1_024;

  @NonNull
  private final AlertRepository repository;

  @Timed(value = "ae.alerts.use_cases", extraTags = { "package", "alert" })
  List<Alert> createAlerts(List<Alert> alerts) {
    return partition(alerts, SAVE_PARTITION_SIZE).stream()
        .map(CreateAlertsUseCase::createEntities)
        .flatMap(entities -> getStream(repository.saveAll(entities)))
        .map(AlertEntity::toAlert)
        .collect(toUnmodifiableList());
  }

  private static List<AlertEntity> createEntities(List<Alert> alerts) {
    return alerts.stream().map(CreateAlertsUseCase::createEntity).collect(Collectors.toList());
  }

  private static AlertEntity createEntity(Alert alert) {
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

  @Nonnull
  private static <T> Stream<T> getStream(Iterable<T> m) {
    return stream(m.spliterator(), false);
  }
}
