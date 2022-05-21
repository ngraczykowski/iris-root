package com.silenteight.hsbc.bridge.match;

public class MatchDataNoLongerAvailableException extends RuntimeException {

  private static final String ERROR_MESSAGE =
      "Match data is no longer available due to retention period or broken data, name=";

  private static final long serialVersionUID = 190150560429582744L;

  public MatchDataNoLongerAvailableException(String name, Throwable cause) {
    super(ERROR_MESSAGE + name, cause);
  }

  public MatchDataNoLongerAvailableException(String name) {
    super(ERROR_MESSAGE + name);
  }
}
