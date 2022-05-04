package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.Match;
import com.silenteight.adjudication.engine.alerts.match.MatchEntity;
import com.silenteight.adjudication.engine.alerts.match.MatchFacade;
import com.silenteight.adjudication.engine.alerts.match.NewAlertMatches;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.toOffsetDateTime;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
class CreateAlertsUseCase {

  private static final int MAX_PRIORITY = 10;
  private static final int MIN_PRIORITY = 1;
  private static final int DEFAULT_PRIORITY = 5;

  @NonNull
  private final AlertRepository repository;
  private final MatchFacade matchFacade;

  @Timed(value = "ae.alerts.use_cases", extraTags = { "package", "alert" })
  @Transactional
  List<Alert> createAlerts(Iterable<Alert> alerts) {
    var savedAlerts = new ArrayList<AlertEntity>();
    var newAlertMatches = new ArrayList<NewAlertMatches>();

    for (var alert : alerts) {
      var savedAlert = repository.save(createEntity(alert));
      savedAlerts.add(savedAlert);
      newAlertMatches.add(createAlertMatches(savedAlert.getId(), alert.getMatchesList()));
    }

    var savedMatches = matchFacade.createMatches(newAlertMatches);
    var alertMatches = savedMatches.stream().collect(groupingBy(MatchEntity::getAlertId));

    return savedAlerts
        .stream()
        .map(alert -> alert.toAlert(
            alertMatches
                .getOrDefault(alert.getId(), List.of())
                .stream()
                .map(MatchEntity::toMatch)
                .collect(toList())))
        .collect(toList());
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

  private static NewAlertMatches createAlertMatches(Long alertId, List<Match> matches) {
    return NewAlertMatches.builder().matches(matches).parentAlert("alerts/" + alertId).build();
  }
}
