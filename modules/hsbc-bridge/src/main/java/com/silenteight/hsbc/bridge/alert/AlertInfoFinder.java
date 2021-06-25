package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.Alert.Match;
import com.silenteight.hsbc.bridge.report.AlertFinder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AlertInfoFinder implements AlertFinder {

  private final AlertRepository repository;

  @Override
  public Collection<Alert> find(@NonNull Collection<Long> alertIds) {
    return repository.findByIdIn(alertIds).stream()
        .map(this::mapToAlert)
        .collect(toList());
  }

  private Alert mapToAlert(AlertEntity alertEntity) {
    return new Alert() {
      @Override
      public String getName() {
        return alertEntity.getName();
      }

      @Override
      public Map<String, String> getMetadata() {
        return createAlertMetadata(alertEntity);
      }

      @Override
      public Collection<Match> getMatches() {
        return alertEntity.getMatches().stream()
            .map(AlertInfoFinder::mapToMatch)
            .collect(toList());
      }
    };
  }

  private static HashMap<String, String> createAlertMetadata(AlertEntity alertEntity) {
    var map = new HashMap<String, String>();
    map.put("id", nullToEmpty(alertEntity.getExternalId()));
    map.put("name", nullToEmpty(alertEntity.getName()));
    map.put("discriminator", nullToEmpty(alertEntity.getDiscriminator()));
    map.put("errorMessage", nullToEmpty(alertEntity.getErrorMessage()));
    map.put("bulkId", alertEntity.getBulkId());
    map.put("status", alertEntity.getStatus().toString());
    map.putAll(alertEntity.getMetadata().stream()
        .collect(Collectors.toMap(AlertMetadata::getKey, AlertMetadata::getValue)));

    return map;
  }

  private static Match mapToMatch(AlertMatchEntity matchEntity) {
    return new Match() {
      @Override
      public String getName() {
        return matchEntity.getName();
      }

      @Override
      public Map<String, String> getMetadata() {
        return createMatchMetadata(matchEntity);
      }
    };
  }

  private static Map<String, String> createMatchMetadata(AlertMatchEntity matchEntity) {
    var map = new HashMap<String, String>();
    map.put("id", nullToEmpty(matchEntity.getExternalId()));
    map.put("name", nullToEmpty(matchEntity.getName()));

    return map;
  }
}
