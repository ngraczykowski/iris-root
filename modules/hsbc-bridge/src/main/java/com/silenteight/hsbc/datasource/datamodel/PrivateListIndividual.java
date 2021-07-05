package com.silenteight.hsbc.datasource.datamodel;

public interface PrivateListIndividual extends ListRecordId {

  String getGender();

  String getGenderDerivedFlag();

  String getEdqSuffix();

  String getPassportNumber();

  String getNationalId();

  String getEdqDrivingLicence();

  String getEdqTaxNumber();

  String getDateOfBirth();

  String getYearOfBirth();

  String getCountryCodesAll();

  String getCountriesAll();

  String getFullNameOriginal();

  String getFullNameDerived();

  String getGivenNamesOriginal();

  String getFamilyNameOriginal();

  String getAddressCountry();

  String getResidencyCountry();

  String getCountryOfBirth();

  String getNationalities();
}
