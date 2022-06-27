package com.silenteight.simulator.dataset.create.exception;

public class EmptyDatasetException extends RuntimeException {

  private static final long serialVersionUID = 6128120667012766407L;

  public EmptyDatasetException() {
    super("Dataset contains no alerts");
  }
}
