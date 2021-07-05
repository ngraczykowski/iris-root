package com.silenteight.hsbc.datasource.datamodel;

public interface WorldCheckIndividual extends ListRecordId {

  String getGender();

  String getGenderDerivedFlag();

  String getPassportNumber();

  String getIdNumbers();

  String getAddressCountry();

  String getResidencyCountry();

  String getCountryOfBirth();

  String getPlaceOfBirthOriginal();

  String getNationalities();

  String getPassportCountry();

  String getFullNameOriginal();

  String getFullNameDerived();

  String getOriginalScriptName();

  String getGivenNamesOriginal();

  String getFamilyNameOriginal();

  String getListRecordId();

  String getCountryCodesAll();

  String getCountriesAll();

  String getCountriesOriginal();

  String getNativeAliasLanguageCountry();

  String getDobs();

  String getDateOfBirth();

  String getYearOfBirth();

  String getAddress();
}
