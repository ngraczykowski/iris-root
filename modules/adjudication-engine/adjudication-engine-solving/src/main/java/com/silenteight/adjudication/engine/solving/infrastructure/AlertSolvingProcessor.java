package com.silenteight.adjudication.engine.solving.infrastructure;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

import com.hazelcast.map.EntryProcessor;

import java.util.Map.Entry;

abstract class AlertSolvingProcessor implements EntryProcessor<Long, AlertSolving, AlertSolving> {

  private static final long serialVersionUID = 8369674121420399827L;

  @Override
  public AlertSolving process(Entry<Long, AlertSolving> entry) {
    return AlertSolving.empty();
  }
}
