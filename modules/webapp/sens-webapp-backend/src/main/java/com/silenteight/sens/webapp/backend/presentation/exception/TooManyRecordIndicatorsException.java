package com.silenteight.sens.webapp.backend.presentation.exception;

public class TooManyRecordIndicatorsException extends IllegalStateException {

  private static final long serialVersionUID = 9123769819971146193L;

  private static final String MESSAGE =
      "Found %d alert indicators for unit %s, account %s and signature %s";

  public TooManyRecordIndicatorsException(
      int size, String unit, String account, String recordSignature) {
    super(String.format(MESSAGE, size, unit, account, recordSignature));
  }
}
