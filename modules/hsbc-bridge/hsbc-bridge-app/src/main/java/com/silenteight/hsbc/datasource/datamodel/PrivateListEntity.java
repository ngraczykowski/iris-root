package com.silenteight.hsbc.datasource.datamodel;

public interface PrivateListEntity extends ListRecordId {

  String getEdqTaxNumber();

  String getAddressCountry();

  String getOperatingCountries();

  String getCountryCodesAll();

  String getCountriesAll();

  String getEntityNameOriginal();

  String getEntityNameDerived();

  String getPrimaryName();
}
