package com.silenteight.hsbc.datasource.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Feature {

  NAME("name"),
  GENDER("gender"),
  REGISTRATION_COUNTRY("registrationCountry"),
  INCORPORATION_COUNTRY("incorporationCountry"),
  RESIDENCY_COUNTRY("residencyCountry"),
  NATIONALITY_COUNTRY("nationalityCountry"),
  OTHER_COUNTRY("otherCountry"),
  NATIONAL_ID_DOCUMENT("nationalIdDocument"),
  PASSPORT_NUMBER_DOCUMENT("passportNumberDocument"),
  OTHER_DOCUMENT("otherDocument"),
  DATE_OF_BIRTH("dateOfBirth");

  private final String name;

  public static Feature getByName(@NonNull String featureName) {

    String name = featureName.replace("features/", "");

    return Stream.of(values())
        .filter(n -> n.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new FeatureNotFoundException(name));
  }

  static class FeatureNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5623696698660542440L;

    public FeatureNotFoundException(String featureName) {
      super("Feature not found: " + featureName);
    }
  }
}
