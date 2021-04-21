package com.silenteight.hsbc.bridge.analysis;

public class AnalysisNotFoundException extends RuntimeException {

  AnalysisNotFoundException(long id) {
    super("Analysis has not been found, id: " + id);
  }
}
