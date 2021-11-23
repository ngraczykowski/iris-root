package com.silenteight.hsbc.datasource.datamodel;

public interface WorldCheckEntity extends ListRecordId {

  String getCountryCodesAll();

  String getCountriesAll();

  String getRegistrationCountry();

  String getAddressCountry();

  String getOperatingCountries();

  String getNativeAliasLanguageCountry();

  String getEntityNameDerived();

  String getOriginalScriptName();

  String getEntityNameOriginal();

  String getListRecordId();

  String getFurtherInformation();

  String getLastUpdatedDate();

  String getLinkedTo();

  String getPrimaryName();
}
