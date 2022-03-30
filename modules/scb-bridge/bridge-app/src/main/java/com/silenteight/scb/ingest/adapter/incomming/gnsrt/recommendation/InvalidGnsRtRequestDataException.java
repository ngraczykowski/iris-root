package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation;

public class InvalidGnsRtRequestDataException extends RuntimeException {

  private static final long serialVersionUID = 5116276160761223498L;

  public InvalidGnsRtRequestDataException(String message) {
    super(message);
  }
}