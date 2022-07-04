package com.silenteight.hsbc.datasource.datamodel;

import org.apache.commons.lang3.StringUtils;

public interface NegativeNewsScreeningEntities extends ListRecordId {

  String getSicCodeGlobalKeyword();

  String getSicCodeLocalKeyword();

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
    return StringUtils.contains(getSicCodeGlobalKeyword(), value)
        || StringUtils.contains(getSicCodeLocalKeyword(), value);
  }
}
