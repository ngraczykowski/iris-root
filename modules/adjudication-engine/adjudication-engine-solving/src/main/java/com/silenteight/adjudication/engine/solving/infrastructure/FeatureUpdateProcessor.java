package com.silenteight.adjudication.engine.solving.infrastructure;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

import com.hazelcast.map.EntryProcessor;

import java.util.Map.Entry;

abstract class FeatureUpdateProcessor implements EntryProcessor<Long, AlertSolving, Boolean> {

  @Override
  public Boolean process(Entry<Long, AlertSolving> entry) {
    return true;
  }
}
