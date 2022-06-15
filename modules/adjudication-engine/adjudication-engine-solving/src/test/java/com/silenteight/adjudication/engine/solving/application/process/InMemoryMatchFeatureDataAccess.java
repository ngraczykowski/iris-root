/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class InMemoryMatchFeatureDataAccess implements MatchFeatureDataAccess {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static Map<Long, String> alerts =
      Map.of(
          1L,
          "withFeatureAndCategoryValues.json",
          2L,
          "withOnlyOneCategoryValue.json",
          3L,
          "withoutFeatures.json",
          4L,
          "withoutValues.json");

  @Override
  public Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    var alertsAggregate = new HashMap<Long, AlertAggregate>();
    for (int i = 0; i < alerts.size(); i++) {
      try {
        alertsAggregate.put(
            alerts.toArray(new Long[0])[i], getAlertWithAllData((alerts.toArray(new Long[0])[i])));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return alertsAggregate;
  }

  @Override
  public AlertAggregate findAnalysisAlertAndAggregate(Long analysis, Long alert) {
    try {
      return getAlertWithAllData(alert);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static AlertAggregate getAlertWithAllData(Long alertId) throws IOException {

    var classLoader = InMemoryMatchFeatureDataAccess.class.getClassLoader();
    var file =
        new File(
            Objects.requireNonNull(classLoader.getResource("alerts/" + alerts.get(alertId)))
                .getFile());

    return OBJECT_MAPPER.readValue(file, AlertAggregate.class);
  }
}
