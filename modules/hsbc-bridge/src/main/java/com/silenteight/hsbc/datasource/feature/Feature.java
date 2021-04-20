package com.silenteight.hsbc.datasource.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Feature {

  GENDER("gender"),
  NATIONALITY_ID("nationalityId"),
  NATIONALITY_COUNTRY("nationality"),
  COUNTRY("country");

  private String name;

  public static Feature getByName(@NonNull String featureName) {
    return Stream.of(values())
        .filter(n -> n.getName().equalsIgnoreCase(featureName))
        .findFirst()
        .orElseThrow(() -> new FeatureNotFoundException(featureName));
  }

  static class FeatureNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5623696698660542440L;

    public FeatureNotFoundException(String featureName) {
      super("Feature not found: " + featureName);
    }
  }
}
