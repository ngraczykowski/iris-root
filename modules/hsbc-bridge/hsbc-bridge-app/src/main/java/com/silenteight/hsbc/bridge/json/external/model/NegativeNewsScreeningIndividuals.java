package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@Data
public class NegativeNewsScreeningIndividuals {

  @JsonProperty("NNSIndividuals.Record Id")
  private String recordId;

  @JsonProperty("NNSIndividuals.Input Stream")
  private String inputStream;

  @JsonProperty("NNSIndividuals.List Key")
  private String listKey;

  @JsonProperty("NNSIndividuals.List Sub Key")
  private String listSubKey;

  @JsonProperty("NNSIndividuals.List Record Type")
  private String listRecordType;

  @JsonProperty("NNSIndividuals.List Record Origin")
  private String listRecordOrigin;

  @JsonProperty("NNSIndividuals.List Record Id")
  private String listRecordId;

  @JsonProperty("NNSIndividuals.List Record Sub Id")
  private String listRecordSubId;

  @JsonProperty("NNSIndividuals.Passport Number")
  private String passportNumber;

  @JsonProperty("NNSIndividuals.National Id")
  private String nationalId;

  @JsonProperty("NNSIndividuals.Title")
  private String title;

  @JsonProperty("NNSIndividuals.Full Name (Original)")
  private String originalFullName;

  @JsonProperty("NNSIndividuals.Given Names (Original)")
  private String originalGivenNames;

  @JsonProperty("NNSIndividuals.Family Name (Original)")
  private String originalFamilyName;

  @JsonProperty("NNSIndividuals.Full Name (Derived)")
  private String derivedFullName;

  @JsonProperty("NNSIndividuals.Given Name (Derived)")
  private String derivedGivenName;

  @JsonProperty("NNSIndividuals.Family Name (Derived)")
  private String derivedFamilyName;

  @JsonProperty("NNSIndividuals.Name Type")
  private String nameType;

  @JsonProperty("NNSIndividuals.Name Quality")
  private String nameQuality;

  @JsonProperty("NNSIndividuals.Primary Name")
  private String primaryName;

  @JsonProperty("NNSIndividuals.Original Script Name")
  private String originalScriptName;

  @JsonProperty("NNSIndividuals.Gender")
  private String gender;

  @JsonProperty("NNSIndividuals.Gender Derived Flag")
  private String genderDerivedFlag;

  @JsonProperty("NNSIndividuals.Date of Birth")
  private String dateOfBirth;

  @JsonProperty("NNSIndividuals.Year of Birth")
  private String yearOfBirth;

  @JsonProperty("NNSIndividuals.Deceased Flag")
  private String deceasedFlag;

  @JsonProperty("NNSIndividuals.Deceased Date")
  private String deceasedDate;

  @JsonProperty("NNSIndividuals.Occupation")
  private String occupation;

  @JsonProperty("NNSIndividuals.Address")
  private String address;

  @JsonProperty("NNSIndividuals.City")
  private String city;

  @JsonProperty("NNSIndividuals.State")
  private String state;

  @JsonProperty("NNSIndividuals.Postal Code")
  private String postalCode;

  @JsonProperty("NNSIndividuals.Address Country")
  private String addressCountry;

  @JsonProperty("NNSIndividuals.Residency Country")
  private String residenceCountry;

  @JsonProperty("NNSIndividuals.Country of Birth")
  private String countryOfBirth;

  @JsonProperty("NNSIndividuals.Nationalities")
  private String nationalities;

  @JsonProperty("NNSIndividuals.Country Codes (All)")
  private String allCountryCodes;

  @JsonProperty("NNSIndividuals.Countries (All)")
  private String allCountries;

  @JsonProperty("NNSIndividuals.Profile Hyperlink")
  private String profileHyperlink;

  @JsonProperty("NNSIndividuals.Search Hyperlink")
  private String searchHyperlink;

  @JsonProperty("NNSIndividuals.Linked Profiles")
  private String linkedProfiles;

  @JsonProperty("NNSIndividuals.Linked Relationships")
  private String linkedRelationships;

  @JsonProperty("NNSIndividuals.Risk Score")
  private String riskScore;

  @JsonProperty("NNSIndividuals.PEP Risk Score")
  private String pepRiskScore;

  @JsonProperty("NNSIndividuals.Data Confidence Score")
  private String dataConfidenceScore;

  @JsonProperty("NNSIndividuals.Data Confidence Comment")
  private String dataConfidenceComment;

  @JsonProperty("NNSIndividuals.Inactive Flag")
  private String inactiveFlag;

  @JsonProperty("NNSIndividuals.Inactive Since Date")
  private String inactiveSinceDate;

  @JsonProperty("NNSIndividuals.PEP Classification")
  private String pepClassification;

  @JsonProperty("NNSIndividuals.Category")
  private String category;

  @JsonProperty("NNSIndividuals.External Sources")
  private String externalSources;

  @JsonProperty("NNSIndividuals.Linked to")
  private String linkedTo;

  @JsonProperty("NNSIndividuals.Companies")
  private String companies;

  @JsonProperty("NNSIndividuals.Date of Birth (Original)")
  private String originalDateOfBirth;

  @JsonProperty("NNSIndividuals.Year of Birth (Approximated)")
  private String approximatedYearOfBirth;

  @JsonProperty("NNSIndividuals.Place of Birth (Original)")
  private String originalPlaceOfBirth;

  @JsonProperty("NNSIndividuals.Further Information")
  private String furtherInformation;

  @JsonProperty("NNSIndividuals.Countries (Original)")
  private String originalCountries;

  @JsonProperty("NNSIndividuals.Social Security Number")
  private String socialSecurityNumber;

  @JsonProperty("NNSIndividuals.Passport Country")
  private String passportCountry;

  @JsonProperty("NNSIndividuals.Sub-Category Description")
  private String subCategoryDescription;

  @JsonProperty("NNSIndividuals.Update Category")
  private String updateCategory;

  @JsonProperty("NNSIndividuals.Native Alias Language Name")
  private String nativeAliasLanguageName;

  @JsonProperty("NNSIndividuals.Native Alias Language Country")
  private String nativeAliasLanguageCountry;

  @JsonProperty("NNSIndividuals.Cached External Sources")
  private String cachedExternalSources;

  @JsonProperty("NNSIndividuals.Added Date")
  private String addedDate;

  @JsonProperty("NNSIndividuals.Last Updated Date")
  private String lastUpdatedDate;

  @JsonProperty("NNSIndividuals.edqOWSWatchlistName")
  private String edqOWSWatchlistName;

  @JsonProperty("NNSIndividuals.DOBs")
  private String dobs;

  @JsonProperty("NNSIndividuals.ID Numbers")
  private String idNumbers;

  @JsonProperty("NNSIndividuals.RecordType")
  private String recordType;

  @JsonProperty("NNSIndividuals.SIC code-global keywords")
  @JsonDeserialize(using = MultiValueFieldDeserializer.class)
  private List<String> sicCodeGlobalKeyword;

  @JsonProperty("NNSIndividuals.SIC code-local keywords")
  @JsonDeserialize(using = MultiValueFieldDeserializer.class)
  private List<String> sicCodeLocalKeyword;
}
