package com.silenteight.warehouse.indexer.simulation.analysis;

import lombok.AllArgsConstructor;

/**
 * @deprecated Represents an exception for missing ES index.
 */
@AllArgsConstructor
@Deprecated(since = "2.0.0", forRemoval = true)
public class AnalysisDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = 4893798708637715414L;

  public AnalysisDoesNotExistException(String message) {
    super(message);
  }
}
