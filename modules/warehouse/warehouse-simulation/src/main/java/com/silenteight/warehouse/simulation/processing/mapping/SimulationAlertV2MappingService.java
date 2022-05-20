package com.silenteight.warehouse.simulation.processing.mapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.SimulationAlert;
import com.silenteight.data.api.v2.SimulationMatch;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchDefinition;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertV2MappingService {

  @NonNull
  private final PayloadConverter payloadConverter;

  public List<SimulationAlertDefinition> mapAlerts(
      List<SimulationAlert> alerts, String analysisName) {

    return alerts.stream()
        .map(tryMappingOrNull(analysisName))
        .filter(Objects::nonNull)
        .collect(toList());
  }

  private Function<SimulationAlert, SimulationAlertDefinition> tryMappingOrNull(
      String analysisName) {

    return alert -> {
      try {
        return mapAlert(alert, analysisName);
      } catch (RuntimeException e) {
        log.warn("Mapping simulation alert failed, alertName=" + Optional.ofNullable(alert)
            .map(SimulationAlert::getName)
            .orElse("<empty>"), e);
        return null;
      }
    };
  }

  SimulationAlertDefinition mapAlert(SimulationAlert alert, String analysisName) {
    String alertName = Optional.ofNullable(alert)
        .map(SimulationAlert::getName)
        .filter(StringUtils::isNotBlank)
        .orElseThrow(() -> new IllegalArgumentException("alert name is not set"));
    String alertPayload = payloadConverter.convertPayload(alert.getPayload());

    List<SimulationMatchDefinition> matches = alert.getMatchesList().stream()
        .map(match -> mapMatch(match, analysisName, alertName))
        .collect(toList());

    return new SimulationAlertDefinition(analysisName, alertName, alertPayload, matches, true);
  }

  SimulationMatchDefinition mapMatch(SimulationMatch match, String analysisName, String alertName) {
    String matchName = Optional.ofNullable(match)
        .map(SimulationMatch::getName)
        .filter(StringUtils::isNotBlank)
        .orElseThrow(() -> new IllegalArgumentException("match name is not set"));
    String matchPayload = payloadConverter.convertPayload(match.getPayload());

    return new SimulationMatchDefinition(analysisName, alertName, matchName, matchPayload);
  }
}
