package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorldCheckIndividual implements
    com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual {

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
  private String fullNameOriginal;
  private String givenNamesOriginal;
  private String familyNameOriginal;
  private String fullNameDerived;
  private String givenNamesDerived;
  private String familyNameDerived;
  private String nameType;
  private String nameQuality;
  private String primaryName;
  private String originalScriptName;
  private String gender;
  private String genderDerivedFlag;
  private String dateOfBirth;
  private Integer yearOfBirth;
  private String deceasedFlag;
  private String deceasedDate;
  private String occupation;
  private String address;
  private String city;
  private String state;
  private String postalCode;
  private String addressCountry;
  private String residencyCountry;
  private String countryOfBirth;
  private String nationalities;
  private String countryCodesAll;
  private String countriesAll;
  private String profileHyperlink;
  private String searchHyperlink;
  private String linkedProfiles;
  private String linkedRelationships;
  private BigDecimal riskScore;
  private BigDecimal pepRiskScore;
  private BigDecimal dataConfidenceScore;
  private String dataConfidenceComment;
  private String inactiveFlag;
  private String inactiveSinceDate;
  private String pepClassification;
  private String category;
  private String externalSources;
  private String linkedTo;
  private String companies;
  private String dateOfBirthOriginal;
  private String yearOfBirthApproximated;
  private String placeOfBirthOriginal;
  private String furtherInformation;
  private String countriesOriginal;
  private String socialSecurityNumber;
  private String passportCountry;
  private String subCategoryDescription;
  private String updateCategory;
  private String nativeAliasLanguageName;
  private String nativeAliasLanguageCountry;
  private String cachedExternalSources;
  private String addedDate;
  private String lastUpdatedDate;
  private String edqOwsWatchlistName;
  private String dobs;
  private String idNumbers;
  private String recordType;
}
