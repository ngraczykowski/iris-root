package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.util.List;

@Data
public class NegativeNewsScreeningEntities implements
    com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningEntities {

  private String recordId;
  private String inputStream;
  private String listKey;
  private String listSubKey;
  private String listRecordType;
  private String listRecordOrigin;
  private String listRecordId;
  private String listRecordSubId;
  private String registrationNumber;
  private String originalEntityName;
  private String derivedEntityName;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String isAliasAcronym;
  private String vesselIndicator;
  private String vesselInfo;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String addressCountry;
  private String registrationCountry;
  private String operatingCountries;
  private String allCountryCodes;
  private String allCountries;
  private String profileHyperlink;
  private String searchHyperlink;
  private String linkedProfiles;
  private String linkedRelationships;
  private String riskScore;
  private String pepRiskScore;
  private String dataConfidenceScore;
  private String dataConfidenceComment;
  private String inactiveFlag;
  private String inactiveSinceData;
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
  private String lastUpdateDate;
  private String edqWCListType;
  private String edqOWSWatchlistName;
  private List<String> sicCodeGlobalKeyword;
  private List<String> sicCodeLocalKeyword;
}
