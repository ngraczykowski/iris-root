package com.silenteight.universaldatasource.app.feature.service;

public class MatchFeatureInputMappingException extends RuntimeException {

  private static final long serialVersionUID = -7457823269043373654L;

  public MatchFeatureInputMappingException(Throwable cause) {
    super("Unable to map feature input", cause);
  }
}
