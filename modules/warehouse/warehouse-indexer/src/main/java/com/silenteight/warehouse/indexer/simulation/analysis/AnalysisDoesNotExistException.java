package com.silenteight.warehouse.indexer.simulation.analysis;

public class AnalysisDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = 4893798708637715414L;

  public AnalysisDoesNotExistException(String message) {
    super(message);
  }
}
