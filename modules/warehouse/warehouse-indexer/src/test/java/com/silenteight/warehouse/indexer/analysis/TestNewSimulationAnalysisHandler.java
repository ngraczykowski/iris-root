package com.silenteight.warehouse.indexer.analysis;

import java.util.concurrent.CopyOnWriteArrayList;

class TestNewSimulationAnalysisHandler implements NewSimulationAnalysisHandler {

  private final CopyOnWriteArrayList<NewSimulationAnalysisEvent> events =
      new CopyOnWriteArrayList<>();

  @Override
  public void handle(NewSimulationAnalysisEvent event) {
    events.add(event);
  }

  public void reset() {
    events.clear();
  }

  public NewSimulationAnalysisEvent getLastEvent() {
    return events.get(events.size() - 1);
  }

  public int getEventReceivedCount() {
    return events.size();
  }
}
