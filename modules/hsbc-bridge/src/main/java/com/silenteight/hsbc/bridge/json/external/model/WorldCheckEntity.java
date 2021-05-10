package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
public class WorldCheckEntity {

  @JsonProperty("WorldCheckEntities.Record Id")
  private String recordId;
  @JsonProperty("WorldCheckEntities.Input Stream")
  private String inputStream;
  @JsonProperty("WorldCheckEntities.List Key")
  private String listKey;
  @JsonProperty("WorldCheckEntities.List Sub Key")
  private String listSubKey;
  @JsonProperty("WorldCheckEntities.List Record Type")
  private String listRecordType;
  @JsonProperty("WorldCheckEntities.List Record Origin")
  private String listRecordOrigin;
  @JsonProperty("WorldCheckEntities.List Record Id")
  private String listRecordId;
  @JsonProperty("WorldCheckEntities.List Record Sub Id")
  private String listRecordSubId;
  @JsonProperty("WorldCheckEntities.Registration Number")
  private String registrationNumber;
  @JsonProperty("WorldCheckEntities.Entity Name (Original)")
  private String entityNameOriginal;
  @JsonProperty("WorldCheckEntities.Entity Name (Derived)")
  private String entityNameDerived;
  @JsonProperty("WorldCheckEntities.Name Type")
  private String nameType;
  @JsonProperty("WorldCheckEntities.Name Quality")
  private String nameQuality;
  @JsonProperty("WorldCheckEntities.Primary Name")
  private String primaryName;
  @JsonProperty("WorldCheckEntities.Original Script Name")
  private String originalScriptName;
  @JsonProperty("WorldCheckEntities.Alias is Acronym")
  private String aliasIsAcronym;
  @JsonProperty("WorldCheckEntities.Vessel Indicator")
  private String vesselIndicator;
  @JsonProperty("WorldCheckEntities.Vessel Info")
  private String vesselInfo;
  @JsonProperty("WorldCheckEntities.Address")
  private String address;
  @JsonProperty("WorldCheckEntities.City")
  private String city;
  @JsonProperty("WorldCheckEntities.State")
  private String state;
  @JsonProperty("WorldCheckEntities.Postal Code")
  private String postalCode;
  @JsonProperty("WorldCheckEntities.Address Country")
  private String addressCountry;
  @JsonProperty("WorldCheckEntities.Registration Country")
  private String registrationCountry;
  @JsonProperty("WorldCheckEntities.Operating Countries")
  private String operatingCountries;
  @JsonProperty("WorldCheckEntities.Country Codes (All)")
  private String countryCodesAll;
  @JsonProperty("WorldCheckEntities.Countries (All)")
  private String countriesAll;
  @JsonProperty("WorldCheckEntities.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("WorldCheckEntities.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("WorldCheckEntities.Linked Profiles")
  private String linkedProfiles;
  @JsonProperty("WorldCheckEntities.Linked Relationships")
  private String linkedRelationships;
  @JsonProperty("WorldCheckEntities.Risk Score")
  private BigDecimal riskScore;
  @JsonProperty("WorldCheckEntities.PEP Risk Score")
  private BigDecimal pepRiskScore;
  @JsonProperty("WorldCheckEntities.Data Confidence Score")
  private BigDecimal dataConfidenceScore;
  @JsonProperty("WorldCheckEntities.Data Confidence Comment")
  private String dataConfidenceComment;
  @JsonProperty("WorldCheckEntities.Inactive Flag")
  private String inactiveFlag;
  @JsonProperty("WorldCheckEntities.Inactive Since Date")
  private String inactiveSinceDate;
  @JsonProperty("WorldCheckEntities.PEP Classification")
  private String pepClassification;
  @JsonProperty("WorldCheckEntities.Category")
  private String category;
  @JsonProperty("WorldCheckEntities.External Sources")
  private String externalSources;
  @JsonProperty("WorldCheckEntities.Linked to")
  private String linkedTo;
  @JsonProperty("WorldCheckEntities.Companies")
  private String companies;
  @JsonProperty("WorldCheckEntities.Further Information")
  private String furtherInformation;
  @JsonProperty("WorldCheckEntities.Sub-Category Description")
  private String subCategoryDescription;
  @JsonProperty("WorldCheckEntities.Update Category")
  private String updateCategory;
  @JsonProperty("WorldCheckEntities.Native Alias Language Name")
  private String nativeAliasLanguageName;
  @JsonProperty("WorldCheckEntities.Native Alias Language Country")
  private String nativeAliasLanguageCountry;
  @JsonProperty("WorldCheckEntities.Cached External Sources")
  private String cachedExternalSources;
  @JsonProperty("WorldCheckEntities.Added Date")
  private String addedDate;
  @JsonProperty("WorldCheckEntities.Last Updated Date")
  private String lastUpdatedDate;
  @JsonProperty("WorldCheckEntities.edqWCListType")
  private String edqWcListType;
  @JsonProperty("WorldCheckEntities.edqOWSWatchlistName")
  private String edqOwsWatchlistName;
}
