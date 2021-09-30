package com.silenteight.payments.bridge.svb.etl.countrycode.other;

public class UnitCodeFormatException extends RuntimeException {

  private static final long serialVersionUID = -6018828149456825285L;

  public UnitCodeFormatException(String unit) {
    super("Incorrect unit code: " + unit);
  }
}
