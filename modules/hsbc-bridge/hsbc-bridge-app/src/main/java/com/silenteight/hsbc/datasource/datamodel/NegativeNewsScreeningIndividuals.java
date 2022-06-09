package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;

public interface NegativeNewsScreeningIndividuals extends ListRecordId {

  List<String> getSicCodeGlobalKeyword();

  List<String> getSicCodeLocalKeyword();

  String getFurtherInformation();

  String getPassportNumber();

  String getIdNumbers();

  String getCountryOfBirth();

  String getNationalities();

  String getPassportCountry();

  String getNativeAliasLanguageCountry();

  String getPrimaryName();

  String getOriginalFullName();

  String getDerivedFullName();

  String getOriginalGivenNames();

  String getOriginalFamilyName();

  String getOriginalScriptName();

  String getAddressCountry();

  String getResidenceCountry();

  String getAllCountryCodes();

  String getAllCountries();

  String getOriginalCountries();

  String getLastUpdatedDate();

  String getLinkedTo();

  String getOriginalPlaceOfBirth();

  String getAddress();

  String getGender();

  String getGenderDerivedFlag();

  String getDobs();

  String getYearOfBirth();

  default boolean isContainingValue(String value) {
    return getSicCodeGlobalKeyword().contains(value) || getSicCodeLocalKeyword().contains(value);
  }
}
