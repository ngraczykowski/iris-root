package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Service
public class AlertFacade {

  @NonNull
  private final CreateAlertsUseCase createAlertsUseCase;

  @Nonnull
  public List<Alert> createAlerts(@NonNull Iterable<Alert> alerts) {
    return createAlertsUseCase.createAlerts(alerts);
  }
}
