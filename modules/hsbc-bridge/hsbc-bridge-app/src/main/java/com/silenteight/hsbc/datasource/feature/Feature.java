package com.silenteight.hsbc.datasource.feature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Feature {

  ALLOW_LIST_COMMON_AP("commonAp"),
  ALLOW_LIST_COMMON_NAME("commonNames"),
  ALLOW_LIST_COMMON_WP("commonMp"),
  ALLOW_LIST_INVALID_ALERT("invalidAlert"),
  NAME("name"),
  GENDER("gender"),
  GEO_PLACE_OF_BIRTH("geoPlaceOfBirth"),
  GEO_RESIDENCIES("geoResidencies"),
  IS_PEP("isPep"),
  REGISTRATION_COUNTRY("registrationCountry"),
  INCORPORATION_COUNTRY("incorporationCountry"),
  RESIDENCY_COUNTRY("residencyCountry"),
  NATIONALITY_COUNTRY("nationalityCountry"),
  OTHER_COUNTRY("otherCountry"),
  NATIONAL_ID_DOCUMENT("nationalIdDocument"),
  PASSPORT_NUMBER_DOCUMENT("passportNumberDocument"),
  OTHER_DOCUMENT("otherDocument"),
  DATE_OF_BIRTH("dateOfBirth"),
  HISTORICAL_IS_CASE_TP_MARKED("isCaseTpMarked"),
  HISTORICAL_IS_AP_TP_MARKED("isApTpMarked"),
  HISTORICAL_IS_TP_MARKED("isTpMarked");

  private static final String PREFIX = "features/";

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
