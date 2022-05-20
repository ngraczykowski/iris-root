package com.silenteight.hsbc.datasource.provider;

import java.util.List;

public class FeatureNotAllowedException extends RuntimeException {

  private static final long serialVersionUID = -5953573234534485573L;

  public FeatureNotAllowedException(String feature, List<String> allowedValues) {
    super("Feature: " + feature + " is not allowed. Allowed values are: " + allowedValues);
  }
}
