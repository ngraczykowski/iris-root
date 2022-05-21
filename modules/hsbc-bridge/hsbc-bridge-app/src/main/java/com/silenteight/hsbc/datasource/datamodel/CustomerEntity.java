package com.silenteight.hsbc.datasource.datamodel;

public interface CustomerEntity {

  String getCountriesOfIncorporation();

  String getEdqIncorporationCountries();

  String getEdqIncorporationCountriesCodes();

  String getRegistrationCountry();

  String getCountriesOfRegistrationOriginal();

  String getEdqRegistrationCountriesCodes();

  String getEntityNameOriginal();

  String getEntityName();

  String getOriginalScriptName();

  String getTradesWithCountries();

  String getSubsidiariesOperatesInCountries();

  String getCountriesOfBusiness();

  String getCountriesOfHeadOffice();

  String getAddressCountry();

  String getEdqAddressCountryCode();

  String getEdqBusinessCountries();

  String getCountriesAll();

  String getCountriesAllCodes();

  String getSourceAddressCountry();

  String getEdqTradesWithCountries();

  String getEdqHeadOfficeCountries();

  String getOperatingCountries();

  String getEdqOperatingCountriesCodes();

  String getAddress();

  String getProfileFullAddress();

  String getExternalProfileId();

  String getEdqLobCountryCode();
}
