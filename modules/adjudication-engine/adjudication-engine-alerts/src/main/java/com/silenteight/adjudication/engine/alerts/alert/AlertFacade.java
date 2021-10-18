package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Alert;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Service
@Slf4j
public class AlertFacade {

  @NonNull
  private final CreateAlertsUseCase createAlertsUseCase;

  @Nonnull
  public List<Alert> createAlerts(@NonNull Iterable<Alert> alerts) {
    var newAlerts = createAlertsUseCase.createAlerts(alerts);

    log.info(
        "Created new alerts: count={}, names={}", newAlerts.size(),
        newAlerts.stream().map(Alert::getName).collect(Collectors.joining(", ")));

    return newAlerts;
  }
}
