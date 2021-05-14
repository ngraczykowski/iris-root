package com.silenteight.hsbc.datasource.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Feature {

  REGISTRATION_COUNTRY("registrationCountry"),
  GENDER("gender"),
  DOCUMENT("document"),
  INCORPORATION_COUNTRY("incorporationCountry"),
  RESIDENCY_COUNTRY("residencyCountry"),
  NATIONALITY_COUNTRY("nationality"),
  NATIONALITY_ID("nationalityId"),
  DATE_OF_BIRTH("dateOfBirth");

  private final String name;

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
