package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@Data
public class NegativeNewsScreeningEntities {

  @JsonProperty("NNSEntities.Record Id")
  private String recordId;

  @JsonProperty("NNSEntities.Input Stream")
  private String inputStream;

  @JsonProperty("NNSEntities.List Key")
  private String listKey;

  @JsonProperty("NNSEntities.List Sub Key")
  private String listSubKey;

  @JsonProperty("NNSEntities.List Record Type")
  private String listRecordType;

  @JsonProperty("NNSEntities.List Record Origin")
  private String listRecordOrigin;

  @JsonProperty("NNSEntities.List Record Id")
  private String listRecordId;

  @JsonProperty("NNSEntities.List Record Sub Id")
  private String listRecordSubId;

  @JsonProperty("NNSEntities.Registration Number")
  private String registrationNumber;

  @JsonProperty("NNSEntities.Entity Name (Original)")
  private String originalEntityName;

  @JsonProperty("NNSEntities.Entity Name (Derived)")
  private String derivedEntityName;

  @JsonProperty("NNSEntities.Name Type")
  private String nameType;

  @JsonProperty("NNSEntities.Name Quality")
  private String nameQuality;

  @JsonProperty("NNSEntities.Primary Name")
  private String primaryName;

  @JsonProperty("NNSEntities.Original Script Name")
  private String originalScriptName;

  @JsonProperty("NNSEntities.Alias is Acronym")
  private String isAliasAcronym;

  @JsonProperty("NNSEntities.Vessel Indicator")
  private String vesselIndicator;

  @JsonProperty("NNSEntities.Vessel Info")
  private String vesselInfo;

  @JsonProperty("NNSEntities.Address")
  private String address;

  @JsonProperty("NNSEntities.City")
  private String city;

  @JsonProperty("NNSEntities.State")
  private String state;

  @JsonProperty("NNSEntities.Postal Code")
  private String postalCode;

  @JsonProperty("NNSEntities.Address Country")
  private String addressCountry;

  @JsonProperty("NNSEntities.Registration Country")
  private String registrationCountry;

  @JsonProperty("NNSEntities.Operating Countries")
  private String operatingCountries;

  @JsonProperty("NNSEntities.Country Codes (All)")
  private String allCountryCodes;

  @JsonProperty("NNSEntities.Countries (All)")
  private String allCountries;

  @JsonProperty("NNSEntities.Profile Hyperlink")
  private String profileHyperlink;

  @JsonProperty("NNSEntities.Search Hyperlink")
  private String searchHyperlink;

  @JsonProperty("NNSEntities.Linked Profiles")
  private String linkedProfiles;

  @JsonProperty("NNSEntities.Linked Relationships")
  private String linkedRelationships;

  @JsonProperty("NNSEntities.Risk Score")
  private String riskScore;

  @JsonProperty("NNSEntities.PEP Risk Score")
  private String pepRiskScore;

  @JsonProperty("NNSEntities.Data Confidence Score")
  private String dataConfidenceScore;

  @JsonProperty("NNSEntities.Data Confidence Comment")
  private String dataConfidenceComment;

  @JsonProperty("NNSEntities.Inactive Flag")
  private String inactiveFlag;

  @JsonProperty("NNSEntities.Inactive Since Date")
  private String inactiveSinceData;

  @JsonProperty("NNSEntities.PEP Classification")
  private String pepClassification;

  @JsonProperty("NNSEntities.Category")
  private String category;

  @JsonProperty("NNSEntities.External Sources")
  private String externalSources;

  @JsonProperty("NNSEntities.Linked to")
  private String linkedTo;

  @JsonProperty("NNSEntities.Companies")
  private String companies;

  @JsonProperty("NNSEntities.Further Information")
  private String furtherInformation;

  @JsonProperty("NNSEntities.Sub-Category Description")
  private String subCategoryDescription;

  @JsonProperty("NNSEntities.Update Category")
  private String updateCategory;

  @JsonProperty("NNSEntities.Native Alias Language Name")
  private String nativeAliasLanguageName;

  @JsonProperty("NNSEntities.Native Alias Language Country")
  private String nativeAliasLanguageCountry;

  @JsonProperty("NNSEntities.Cached External Sources")
  private String cachedExternalSources;

  @JsonProperty("NNSEntities.Added Date")
  private String addedDate;

  @JsonProperty("NNSEntities.Last Updated Date")
  private String lastUpdateDate;

  @JsonProperty("NNSEntities.edqWCListType")
  private String edqWCListType;

  @JsonProperty("NNSEntities.edqOWSWatchlistName")
  private String edqOWSWatchlistName;

  @JsonProperty("NNSEntities.SIC code-global keywords")
  @JsonAlias("NNSEntities.SIC code-global Keywords")
  @JsonDeserialize(using = MultiValueFieldDeserializer.class)
  private List<String> sicCodeGlobalKeyword;

  @JsonProperty("NNSEntities.SIC code-local keywords")
  @JsonAlias("NNSEntities.SIC code-local Keywords")
  @JsonDeserialize(using = MultiValueFieldDeserializer.class)
  private List<String> sicCodeLocalKeyword;
}
