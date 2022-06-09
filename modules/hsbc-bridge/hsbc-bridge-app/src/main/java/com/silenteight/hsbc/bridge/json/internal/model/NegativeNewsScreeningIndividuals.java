package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.util.List;

@Data
public class NegativeNewsScreeningIndividuals implements
    com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals {

  private String recordId;
  private String inputStream;
  private String listKey;
  private String listSubKey;
  private String listRecordType;
  private String listRecordOrigin;
  private String listRecordId;
  private String listRecordSubId;
  private String passportNumber;
  private String nationalId;
  private String title;
  private String originalFullName;
  private String originalGivenNames;
  private String originalFamilyName;
  private String derivedFullName;
  private String derivedGivenName;
  private String derivedFamilyName;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String gender;
  private String genderDerivedFlag;
  private String dateOfBirth;
  private String yearOfBirth;
  private String deceasedFlag;
  private String deceasedDate;
  private String occupation;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String addressCountry;
  private String residenceCountry;
  private String countryOfBirth;
  private String nationalities;
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
  private String inactiveSinceDate;
  private String pepClassification;
  private String category;
  private String externalSources;
  private String linkedTo;
  private String companies;
  private String originalDateOfBirth;
  private String approximatedYearOfBirth;
  private String originalPlaceOfBirth;
  private String furtherInformation;
  private String originalCountries;
  private String socialSecurityNumber;
  private String passportCountry;
  private String subCategoryDescription;
  private String updateCategory;
  private String nativeAliasLanguageName;
  private String nativeAliasLanguageCountry;
  private String cachedExternalSources;
  private String addedDate;
  private String lastUpdatedDate;
  private String edqOWSWatchlistName;
  private String dobs;
  private String idNumbers;
  private String recordType;
  private List<String> sicCodeGlobalKeyword;
  private List<String> sicCodeLocalKeyword;
}
