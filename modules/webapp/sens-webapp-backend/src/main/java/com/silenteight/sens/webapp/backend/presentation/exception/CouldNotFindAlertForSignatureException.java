package com.silenteight.sens.webapp.backend.presentation.exception;

public class CouldNotFindAlertForSignatureException extends RuntimeException {

  private static final long serialVersionUID = 1809060740128654962L;

  private static final String MESSAGE =
      "Could not find alert indicator for unit %s, account %s and signature %s";

  public CouldNotFindAlertForSignatureException(
      String unit, String account, String recordSignature) {
    super(String.format(MESSAGE, unit, account, recordSignature));
  }
}
