package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Service
@Slf4j
public class AlertFacade {

  @NonNull
  private final CreateAlertsUseCase createAlertsUseCase;
  @Nonnull
  private final AddLabelsUseCase addLabelsUseCase;
  @Nonnull
  private final RemoveLabelUseCase removeLabelUseCase;

  @Nonnull
  public List<Alert> createAlerts(@NonNull Iterable<Alert> alerts) {
    var newAlerts = createAlertsUseCase.createAlerts(alerts);

    log.info(
        "Created new alerts: count={}, names={}", newAlerts.size(),
        newAlerts.stream().map(Alert::getName).collect(Collectors.joining(", ")));

    return newAlerts;
  }

  public Map<String, String> addLabels(List<String> alertNames, Map<String, String> labels) {
    var addedLabels = addLabelsUseCase.addLabels(alertNames, labels);

    log.info("Added labels: labels={}, for alerts={}", labels, alertNames);

    return addedLabels;
  }

  public List<String> removeLabels(List<String> alertNames, List<String> labelsNames) {
    var affectedAlerts = removeLabelUseCase.removeLabels(alertNames, labelsNames);

    log.info("Removed labels: labels={}, for alerts={}", labelsNames, alertNames);

    return affectedAlerts;
  }
}
