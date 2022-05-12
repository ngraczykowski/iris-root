package com.silenteight.warehouse.simulation.processing.mapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchDefinition;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.singletonList;
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
        .map(tryMappingOrNull(analysisName))
        .filter(Objects::nonNull)
        .collect(toList());
  }

  private Function<Alert, SimulationAlertDefinition> tryMappingOrNull(String analysisName) {
    return alert -> {
      try {
        return mapFields(alert, analysisName);
      } catch (RuntimeException e) {
        log.warn("Mapping simulation alert failed, alertName=" + Optional.ofNullable(alert)
            .map(Alert::getName)
            .orElse("<empty>"), e);
        return null;
      }
    };
  }

  public SimulationAlertDefinition mapFields(Alert alert, String analysisName) {
    String alertName = alert.getName();

    assertAlertNameExists(alert);
    assertNoMatches(alert);

    String payload = payloadConverter.convertPayload(alert.getPayload());

    List<SimulationMatchDefinition> matches =
        singletonList(mapMatch(alert, analysisName, alertName));

    return new SimulationAlertDefinition(analysisName, alertName, payload, matches, true);
  }

  SimulationMatchDefinition mapMatch(Alert alert, String analysisName, String alertName) {
    String matchName = alertName;
    String matchPayload = payloadConverter.convertPayload(alert.getPayload());

    return new SimulationMatchDefinition(analysisName, alertName, matchName, matchPayload);
  }

  private static void assertAlertNameExists(Alert alert) {
    String name = alert.getName();
    if (isNull(name) || name.isBlank())
      throw new IllegalArgumentException("alertName not set in simulation request.");
  }

  private static void assertNoMatches(Alert alert) {
    if (alert.getMatchesCount() > NO_MATCH_ALERT) {
      throw new IllegalArgumentException("This simulation request contains matches "
          + "which is not supported by Data API v1. Migration to Data API v2 is required.");
    }
  }
}
