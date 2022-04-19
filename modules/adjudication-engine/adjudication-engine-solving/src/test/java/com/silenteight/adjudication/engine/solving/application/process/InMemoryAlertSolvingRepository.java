package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

import java.util.HashMap;
import java.util.Map;

class InMemoryAlertSolvingRepository implements AlertSolvingRepository {

  private final Map<Long, AlertSolving> alertSolvings = new HashMap<>();

  @Override
  public AlertSolving get(Long key) {
    return alertSolvings.get(key);
  }

  @Override
  public AlertSolving save(AlertSolving model) {
    alertSolvings.put(model.id(), model);

    return model;
  }
}
