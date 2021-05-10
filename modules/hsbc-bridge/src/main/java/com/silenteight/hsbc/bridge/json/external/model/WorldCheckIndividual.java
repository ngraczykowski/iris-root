package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
public class WorldCheckIndividual {

  @JsonProperty("WorldCheckIndividuals.Record Id")
  private String recordId;
  @JsonProperty("WorldCheckIndividuals.Input Stream")
  private String inputStream;
  @JsonProperty("WorldCheckIndividuals.List Key")
  private String listKey;
  @JsonProperty("WorldCheckIndividuals.List Sub Key")
  private String listSubKey;
  @JsonProperty("WorldCheckIndividuals.List Record Type")
  private String listRecordType;
  @JsonProperty("WorldCheckIndividuals.List Record Origin")
  private String listRecordOrigin;
  @JsonProperty("WorldCheckIndividuals.List Record Id")
  private String listRecordId;
  @JsonProperty("WorldCheckIndividuals.List Record Sub Id")
  private String listRecordSubId;
  @JsonProperty("WorldCheckIndividuals.Passport Number")
  private String passportNumber;
  @JsonProperty("WorldCheckIndividuals.National Id")
  private String nationalId;
  @JsonProperty("WorldCheckIndividuals.Title")
  private String title;
  @JsonProperty("WorldCheckIndividuals.Full Name (Original)")
  private String fullNameOriginal;
  @JsonProperty("WorldCheckIndividuals.Given Names (Original)")
  private String givenNamesOriginal;
  @JsonProperty("WorldCheckIndividuals.Family Name (Original)")
  private String familyNameOriginal;
  @JsonProperty("WorldCheckIndividuals.Full Name (Derived)")
  private String fullNameDerived;
  @JsonProperty("WorldCheckIndividuals.Given Name (Derived)")
  private String givenNamesDerived;
  @JsonProperty("WorldCheckIndividuals.Family Name (Derived)")
  private String familyNameDerived;
  @JsonProperty("WorldCheckIndividuals.Name Type")
  private String nameType;
  @JsonProperty("WorldCheckIndividuals.Name Quality")
  private String nameQuality;
  @JsonProperty("WorldCheckIndividuals.Primary Name")
  private String primaryName;
  @JsonProperty("WorldCheckIndividuals.Original Script Name")
  private String originalScriptName;
  @JsonProperty("WorldCheckIndividuals.Gender")
  private String gender;
  @JsonProperty("WorldCheckIndividuals.Gender Derived Flag")
  private String genderDerivedFlag;
  @JsonProperty("WorldCheckIndividuals.Date of Birth")
  private String dateOfBirth;
  @JsonProperty("WorldCheckIndividuals.Year of Birth")
  private Integer yearOfBirth;
  @JsonProperty("WorldCheckIndividuals.Deceased Flag")
  private String deceasedFlag;
  @JsonProperty("WorldCheckIndividuals.Deceased Date")
  private String deceasedDate;
  @JsonProperty("WorldCheckIndividuals.Occupation")
  private String occupation;
  @JsonProperty("WorldCheckIndividuals.Address")
  private String address;
  @JsonProperty("WorldCheckIndividuals.City")
  private String city;
  @JsonProperty("WorldCheckIndividuals.State")
  private String state;
  @JsonProperty("WorldCheckIndividuals.Postal Code")
  private String postalCode;
  @JsonProperty("WorldCheckIndividuals.Address Country")
  private String addressCountry;
  @JsonProperty("WorldCheckIndividuals.Residency Country")
  private String residencyCountry;
  @JsonProperty("WorldCheckIndividuals.Country of Birth")
  private String countryOfBirth;
  @JsonProperty("WorldCheckIndividuals.Nationalities")
  private String nationalities;
  @JsonProperty("WorldCheckIndividuals.Country Codes (All)")
  private String countryCodesAll;
  @JsonProperty("WorldCheckIndividuals.Countries (All)")
  private String countriesAll;
  @JsonProperty("WorldCheckIndividuals.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("WorldCheckIndividuals.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("WorldCheckIndividuals.Linked Profiles")
  private String linkedProfiles;
  @JsonProperty("WorldCheckIndividuals.Linked Relationships")
  private String linkedRelationships;
  @JsonProperty("WorldCheckIndividuals.Risk Score")
  private BigDecimal riskScore;
  @JsonProperty("WorldCheckIndividuals.PEP Risk Score")
  private BigDecimal pepRiskScore;
  @JsonProperty("WorldCheckIndividuals.Data Confidence Score")
  private BigDecimal dataConfidenceScore;
  @JsonProperty("WorldCheckIndividuals.Data Confidence Comment")
  private String dataConfidenceComment;
  @JsonProperty("WorldCheckIndividuals.Inactive Flag")
  private String inactiveFlag;
  @JsonProperty("WorldCheckIndividuals.Inactive Since Date")
  private String inactiveSinceDate;
  @JsonProperty("WorldCheckIndividuals.PEP Classification")
  private String pepClassification;
  @JsonProperty("WorldCheckIndividuals.Category")
  private String category;
  @JsonProperty("WorldCheckIndividuals.External Sources")
  private String externalSources;
  @JsonProperty("WorldCheckIndividuals.Linked to")
  private String linkedTo;
  @JsonProperty("WorldCheckIndividuals.Companies")
  private String companies;
  @JsonProperty("WorldCheckIndividuals.Date of Birth (Original)")
  private String dateOfBirthOriginal;
  @JsonProperty("WorldCheckIndividuals.Year of Birth (Approximated)")
  private String yearOfBirthApproximated;
  @JsonProperty("WorldCheckIndividuals.Place of Birth (Original)")
  private String placeOfBirthOriginal;
  @JsonProperty("WorldCheckIndividuals.Further Information")
  private String furtherInformation;
  @JsonProperty("WorldCheckIndividuals.Countries (Original)")
  private String countriesOriginal;
  @JsonProperty("WorldCheckIndividuals.Social Security Number")
  private String socialSecurityNumber;
  @JsonProperty("WorldCheckIndividuals.Passport Country")
  private String passportCountry;
  @JsonProperty("WorldCheckIndividuals.Sub-Category Description")
  private String subCategoryDescription;
  @JsonProperty("WorldCheckIndividuals.Update Category")
  private String updateCategory;
  @JsonProperty("WorldCheckIndividuals.Native Alias Language Name")
  private String nativeAliasLanguageName;
  @JsonProperty("WorldCheckIndividuals.Native Alias Language Country")
  private String nativeAliasLanguageCountry;
  @JsonProperty("WorldCheckIndividuals.Cached External Sources")
  private String cachedExternalSources;
  @JsonProperty("WorldCheckIndividuals.Added Date")
  private String addedDate;
  @JsonProperty("WorldCheckIndividuals.Last Updated Date")
  private String lastUpdatedDate;
  @JsonProperty("WorldCheckIndividuals.edqOWSWatchlistName")
  private String edqOwsWatchlistName;
  @JsonProperty("WorldCheckIndividuals.DOBs")
  private String dobs;
  @JsonProperty("WorldCheckIndividuals.ID Numbers")
  private String idNumbers;
  @JsonProperty("WorldCheckIndividuals.RecordType")
  private String recordType;
  @JsonProperty("WorldCheckIndividuals.Dummy")
  private String dummy;
}
