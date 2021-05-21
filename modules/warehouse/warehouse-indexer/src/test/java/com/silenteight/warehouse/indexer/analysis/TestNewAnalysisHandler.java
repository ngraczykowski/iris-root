package com.silenteight.warehouse.indexer.analysis;

import java.util.concurrent.CopyOnWriteArrayList;

class TestNewAnalysisHandler implements NewAnalysisHandler {

  private final CopyOnWriteArrayList<NewAnalysisEvent> events = new CopyOnWriteArrayList<>();

  @Override
  public void handle(NewAnalysisEvent event) {
    events.add(event);
  }

  public void reset() {
    events.clear();
  }

  public NewAnalysisEvent getLastEvent() {
    return events.get(events.size() - 1);
  }

  public int getEventReceivedCount() {
    return events.size();
  }
}
