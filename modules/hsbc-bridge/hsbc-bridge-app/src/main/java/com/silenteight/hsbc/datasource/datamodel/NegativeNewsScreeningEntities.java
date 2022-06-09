package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;

public interface NegativeNewsScreeningEntities extends ListRecordId {

  List<String> getSicCodeGlobalKeyword();

  List<String> getSicCodeLocalKeyword();

  String getFurtherInformation();

  String getPrimaryName();

  String getOriginalEntityName();

  String getDerivedEntityName();

  String getOriginalScriptName();

  String getAllCountryCodes();

  String getAllCountries();

  String getLastUpdateDate();

  String getLinkedTo();

  String getAddressCountry();

  String getOperatingCountries();

  String getNativeAliasLanguageCountry();

  String getRegistrationCountry();

  default boolean isContainingValue(String value) {
    return getSicCodeGlobalKeyword().contains(value) || getSicCodeLocalKeyword().contains(value);
  }
}
