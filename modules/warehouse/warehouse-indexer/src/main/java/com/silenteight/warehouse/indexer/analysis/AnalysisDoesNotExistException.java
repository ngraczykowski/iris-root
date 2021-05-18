package com.silenteight.warehouse.indexer.analysis;

public class AnalysisDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = 4893798708637715414L;

  AnalysisDoesNotExistException(String message) {
    super(message);
  }
}
