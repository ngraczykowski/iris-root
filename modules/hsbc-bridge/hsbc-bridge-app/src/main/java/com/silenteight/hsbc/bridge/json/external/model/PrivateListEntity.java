package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PrivateListEntity {

  @JsonProperty("PrivateListEntities.Record Id")
  private String recordId;
  @JsonProperty("PrivateListEntities.Input Stream")
  private String inputStream;
  @JsonProperty("PrivateListEntities.List Key")
  private String listKey;
  @JsonProperty("PrivateListEntities.List Sub Key")
  private String listSubKey;
  @JsonProperty("PrivateListEntities.List Record Type")
  private String listRecordType;
  @JsonProperty("PrivateListEntities.List Record Origin")
  private String listRecordOrigin;
  @JsonProperty("PrivateListEntities.List Record Id")
  private String listRecordId;
  @JsonProperty("PrivateListEntities.List Record Sub Id")
  private String listRecordSubId;
  @JsonProperty("PrivateListEntities.Registration Number")
  private String registrationNumber;
  @JsonProperty("PrivateListEntities.Entity Name (Original)")
  private String entityNameOriginal;
  @JsonProperty("PrivateListEntities.Entity Name (Derived)")
  private String entityNameDerived;
  @JsonProperty("PrivateListEntities.Name Type")
  private String nameType;
  @JsonProperty("PrivateListEntities.Name Quality")
  private String nameQuality;
  @JsonProperty("PrivateListEntities.Primary Name")
  private String primaryName;
  @JsonProperty("PrivateListEntities.Original Script Name")
  private String originalScriptName;
  @JsonProperty("PrivateListEntities.Alias is Acronym")
  private String aliasIsAcronym;
  @JsonProperty("PrivateListEntities.Vessel Indicator")
  private String vesselIndicator;
  @JsonProperty("PrivateListEntities.Vessel Info")
  private String vesselInfo;
  @JsonProperty("PrivateListEntities.Address")
  private String address;
  @JsonProperty("PrivateListEntities.City")
  private String city;
  @JsonProperty("PrivateListEntities.State")
  private String state;
  @JsonProperty("PrivateListEntities.Postal Code")
  private String postalCode;
  @JsonProperty("PrivateListEntities.Address Country")
  private String addressCountry;
  @JsonProperty("PrivateListEntities.Registration Country")
  private String registrationCountry;
  @JsonProperty("PrivateListEntities.Operating Countries")
  private String operatingCountries;
  @JsonProperty("PrivateListEntities.Country Codes (All)")
  private String countryCodesAll;
  @JsonProperty("PrivateListEntities.Countries (All)")
  private String countriesAll;
  @JsonProperty("PrivateListEntities.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("PrivateListEntities.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("PrivateListEntities.Linked Profiles")
  private String linkedProfiles;
  @JsonProperty("PrivateListEntities.Linked Relationships")
  private String linkedRelationships;
  @JsonProperty("PrivateListEntities.Risk Score")
  private String riskScore;
  @JsonProperty("PrivateListEntities.PEP Risk Score")
  private String pepRiskScore;
  @JsonProperty("PrivateListEntities.Data Confidence Score")
  private String dataConfidenceScore;
  @JsonProperty("PrivateListEntities.Data Confidence Comment")
  private String dataConfidenceComment;
  @JsonProperty("PrivateListEntities.Inactive Flag")
  private String inactiveFlag;
  @JsonProperty("PrivateListEntities.Inactive Since Date")
  private String inactiveSinceDate;
  @JsonProperty("PrivateListEntities.PEP Classification")
  private String pepClassification;
  @JsonProperty("PrivateListEntities.Added Date")
  private String addedDate;
  @JsonProperty("PrivateListEntities.Last Updated Date")
  private String lastUpdatedDate;
  @JsonProperty("PrivateListEntities.Address Type")
  private String addressType;
  @JsonProperty("PrivateListEntities.Current Address")
  private String currentAddress;
  @JsonProperty("PrivateListEntities.Telephone Number")
  private String telephoneNumber;
  @JsonProperty("PrivateListEntities.edqBusinessTelephoneNumber")
  private String edqBusinessTelephoneNumber;
  @JsonProperty("PrivateListEntities.Telephone Number (Business)")
  private String telephoneNumberBusiness;
  @JsonProperty("PrivateListEntities.edqCellNumberPersonal")
  private String edqCellNumberPersonal;
  @JsonProperty("PrivateListEntities.edqTaxNumber")
  private String edqTaxNumber;
  @JsonProperty("PrivateListEntities.edqEmailAddress")
  private String edqEmailAddress;
  @JsonProperty("PrivateListEntities.SCION Reference Number")
  private String scionReferenceNumber;
  @JsonProperty("PrivateListEntities.Role")
  private String role;
  @JsonProperty("PrivateListEntities.List Category")
  private String listCategory;
  @JsonProperty("PrivateListEntities.Further Info")
  private String furtherInfo;
  @JsonProperty("PrivateListEntities.edqBusinessType")
  private String edqBusinessType;
  @JsonProperty("PrivateListEntities.Date of Incorporation")
  private String dateOfIncorporation;
  @JsonProperty("PrivateListEntities.edqDataOfIncorporationSTRING")
  private String edqDataOfIncorporationString;
  @JsonProperty("PrivateListEntities.External Customer ID")
  private String externalCustomerID;
  @JsonProperty("PrivateListEntities.List Customer Type")
  private String listCustomerType;
  @JsonProperty("PrivateListEntities.edqListName")
  private String edqListName;
  @JsonProperty("PrivateListEntities.edqWatchlistLstType")
  private String edqWatchlistLstType;
  @JsonProperty("PrivateListEntities.Primary Name.1")
  private String primaryName1;
  @JsonProperty("PrivateListEntities.Entity Name")
  private String entityName;
  @JsonProperty("PrivateListEntities.Entity Name.1")
  private String entityName1;
  @JsonProperty("PrivateListEntities.Entity Name.2")
  private String entityName2;
  @JsonProperty("PrivateListEntities.Case Country of Origin")
  private String caseCountryOfOrigin;
  @JsonProperty("PrivateListEntities.LOB")
  private String lob;
  @JsonProperty("CustomerIndividuals.Dummy")
  private String dummy;
}
