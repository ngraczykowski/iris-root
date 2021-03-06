package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

@Data
public class WorldCheckEntity implements
    com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity {

  private String recordId;
  private String inputStream;
  private String listKey;
  private String listSubKey;
  private String listRecordType;
  private String listRecordOrigin;
  private String listRecordId;
  private String listRecordSubId;
  private String registrationNumber;
  private String entityNameOriginal;
  private String entityNameDerived;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String aliasIsAcronym;
  private String vesselIndicator;
  private String vesselInfo;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String addressCountry;
  private String registrationCountry;
  private String operatingCountries;
  private String countryCodesAll;
  private String countriesAll;
  private String profileHyperlink;
  private String searchHyperlink;
  private String linkedProfiles;
  private String linkedRelationships;
  private String riskScore;
  private String pepRiskScore;
  private String dataConfidenceScore;
  private String dataConfidenceComment;
  private String inactiveFlag;
  private String inactiveSinceDate;
  private String pepClassification;
  private String category;
  private String externalSources;
  private String linkedTo;
  private String companies;
  private String furtherInformation;
  private String subCategoryDescription;
  private String updateCategory;
  private String nativeAliasLanguageName;
  private String nativeAliasLanguageCountry;
  private String cachedExternalSources;
  private String addedDate;
  private String lastUpdatedDate;
  private String edqWcListType;
  private String edqOwsWatchlistName;
}
