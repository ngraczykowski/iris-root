package com.silenteight.warehouse.simulation.processing.mapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertV1MappingService {

  private static final int NO_MATCH_ALERT = 0;

  @NonNull
  private final PayloadConverter payloadConverter;

  public List<SimulationAlertDefinition> mapFields(List<Alert> alerts, String analysisName) {
    return alerts.stream()
        .map(alert -> mapFields(alert, analysisName))
        .filter(Objects::nonNull)
        .collect(toList());
  }

  public SimulationAlertDefinition mapFields(Alert alert, String analysisName) {
    String alertName = alert.getName();

    try {
      assertAlertNameExists(alert);
      assertNoMatches(alert);

      String payload = payloadConverter.convertPayload(alert.getPayload());

      return new SimulationAlertDefinition(analysisName, alertName, payload, emptyList(), null);
    } catch (RuntimeException e) {
      log.warn("Mapping simulation alert failed, alertName=" + alertName, e);
      return null;
    }
  }

  private static void assertAlertNameExists(Alert alert) {
    String name = alert.getName();
    if (isNull(name) || name.isBlank())
      throw new IllegalArgumentException("alertName not set in simulation request.");
  }

  private static void assertNoMatches(Alert alert) {
    if (alert.getMatchesCount() > NO_MATCH_ALERT) {
      log.warn(
          "Received alert with {} matches. Simulation does not handle matches.",
          alert.getMatchesCount());
    }
  }
}
