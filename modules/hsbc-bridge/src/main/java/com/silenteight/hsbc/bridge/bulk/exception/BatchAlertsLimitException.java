package com.silenteight.hsbc.bridge.bulk.exception;

public class BatchAlertsLimitException extends RuntimeException {

  private static final long serialVersionUID = 3595489095447790785L;

  public BatchAlertsLimitException(int alertLimit) {
    super("Too many alerts. Maximal number of alerts in a single batch is: " + alertLimit + ".");
  }
}
