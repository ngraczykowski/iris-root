package com.silenteight.searpayments.scb.etl.countrycode.scstar;

public class ScstarBicCodeFormatException extends RuntimeException {

  private static final long serialVersionUID = -4661658947792826376L;

  public ScstarBicCodeFormatException(String bic) {
    super("Incorrect receiver or sender code (BIC) for SCTAR: " + bic);
  }
}
