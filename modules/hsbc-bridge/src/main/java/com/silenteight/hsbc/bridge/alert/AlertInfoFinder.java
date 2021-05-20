package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.AlertFinder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AlertInfoFinder implements AlertFinder {

  private final AlertRepository repository;

  @Override
  public Collection<Alert> find(@NonNull Collection<Long> alertIds) {
    return repository.findByIdIn(alertIds).stream()
        .map(a -> new Alert() {
          @Override
          public String getName() {
            return a.getName();
          }

          @Override
          public Map<String, String> getMetadata() {
            return new HashMap<>();
          }

          @Override
          public Collection<Match> getMatches() {
            return a.getMatches().stream()
                .map(m -> new Match() {
                  @Override
                  public String getName() {
                    return m.getName();
                  }

                  @Override
                  public Map<String, String> getMetadata() {
                    return new HashMap<>();
                  }
                }).collect(toList());
          }
        }).collect(toList());
  }
}
