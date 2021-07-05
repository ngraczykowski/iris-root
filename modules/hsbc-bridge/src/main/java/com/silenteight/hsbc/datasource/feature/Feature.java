package com.silenteight.hsbc.datasource.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Feature {

  ALLOW_LIST_COMMON_AP("f_common_ap"),
  ALLOW_LIST_COMMON_NAME("f_common_names"),
  ALLOW_LIST_COMMON_WP("f_common_mp"),
  NAME("name"),
  GENDER("gender"),
  IS_PEP("isPep"),
  REGISTRATION_COUNTRY("registrationCountry"),
  INCORPORATION_COUNTRY("incorporationCountry"),
  RESIDENCY_COUNTRY("residencyCountry"),
  NATIONALITY_COUNTRY("nationalityCountry"),
  OTHER_COUNTRY("otherCountry"),
  NATIONAL_ID_DOCUMENT("nationalIdDocument"),
  PASSPORT_NUMBER_DOCUMENT("passportNumberDocument"),
  OTHER_DOCUMENT("otherDocument"),
  DATE_OF_BIRTH("dateOfBirth");

  private final static String PREFIX = "features/";

  private final String name;

  public String getFullName() {
    return PREFIX + getName();
  }

  public static Feature getByFullName(@NonNull String name) {
    return Stream.of(values())
        .filter(n -> n.getFullName().equalsIgnoreCase(name))
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
