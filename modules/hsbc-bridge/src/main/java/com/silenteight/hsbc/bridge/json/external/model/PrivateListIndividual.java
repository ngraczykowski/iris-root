package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PrivateListIndividual {

  @JsonProperty("PrivateListIndividuals.Record Id")
  private String recordId;
  @JsonProperty("PrivateListIndividuals.Input Stream")
  private String inputStream;
  @JsonProperty("PrivateListIndividuals.List Key")
  private String listKey;
  @JsonProperty("PrivateListIndividuals.List Sub Key")
  private String listSubKey;
  @JsonProperty("PrivateListIndividuals.List Record Type")
  private String listRecordType;
  @JsonProperty("PrivateListIndividuals.List Record Origin")
  private String listRecordOrigin;
  @JsonProperty("PrivateListIndividuals.List Record Id")
  private String listRecordId;
  @JsonProperty("PrivateListIndividuals.List Record Sub Id")
  private String listRecordSubId;
  @JsonProperty("PrivateListIndividuals.Passport Number")
  private String passportNumber;
  @JsonProperty("PrivateListIndividuals.National ID")
  private String nationalId;
  @JsonProperty("PrivateListIndividuals.Title")
  private String title;
  @JsonProperty("PrivateListIndividuals.Full Name (Original)")
  private String fullNameOriginal;
  @JsonProperty("PrivateListIndividuals.Given Names (Original)")
  private String givenNamesOriginal;
  @JsonProperty("PrivateListIndividuals.Family Name (Original)")
  private String familyNameOriginal;
  @JsonProperty("PrivateListIndividuals.Full Name")
  private String fullNameDerived;
  @JsonProperty("PrivateListIndividuals.Given Names (Derived)")
  private String givenNamesDerived;
  @JsonProperty("PrivateListIndividuals.Family Name (Derived)")
  private String familyNameDerived;
  @JsonProperty("PrivateListIndividuals.Name Type")
  private String nameType;
  @JsonProperty("PrivateListIndividuals.Name Quality")
  private String nameQuality;
  @JsonProperty("PrivateListIndividuals.Primary Name")
  private String primaryName;
  @JsonProperty("PrivateListIndividuals.Original Script Name")
  private String originalScriptName;
  @JsonProperty("PrivateListIndividuals.Gender")
  private String gender;
  @JsonProperty("PrivateListIndividuals.Gender Derived Flag")
  private String genderDerivedFlag;
  @JsonProperty("PrivateListIndividuals.Date of Birth")
  private String dateOfBirth;
  @JsonProperty("PrivateListIndividuals.YOB")
  private String yearOfBirth;
  @JsonProperty("PrivateListIndividuals.Deceased Flag")
  private String deceasedFlag;
  @JsonProperty("PrivateListIndividuals.Deceased Date")
  private String deceasedDate;
  @JsonProperty("PrivateListIndividuals.Customer Occupation")
  private String occupation;
  @JsonProperty("PrivateListIndividuals.Address")
  private String address;
  @JsonProperty("PrivateListIndividuals.City")
  private String city;
  @JsonProperty("PrivateListIndividuals.Place of Birth")
  private String placeOfBirth;
  @JsonProperty("PrivateListIndividuals.Postal Code")
  private String postalCode;
  @JsonProperty("PrivateListIndividuals.Address Country")
  private String addressCountry;
  @JsonProperty("PrivateListIndividuals.Country of Residence")
  private String residencyCountry;
  @JsonProperty("PrivateListIndividuals.<Country> of Birth")
  private String countryOfBirth;
  @JsonProperty("PrivateListIndividuals.Nationalities")
  private String nationalities;
  @JsonProperty("PrivateListIndividuals.Country Codes (All)")
  private String countryCodesAll;
  @JsonProperty("PrivateListIndividuals.Countries (All)")
  private String countriesAll;
  @JsonProperty("PrivateListIndividuals.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("PrivateListIndividuals.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("PrivateListIndividuals.Linked Profiles")
  private String linkedProfiles;
  @JsonProperty("PrivateListIndividuals.Linked Relationships")
  private String linkedRelationships;
  @JsonProperty("PrivateListIndividuals.Risk Score")
  private String riskScore;
  @JsonProperty("PrivateListIndividuals.PEP Risk Score")
  private String pepRiskScore;
  @JsonProperty("PrivateListIndividuals.Data Confidence Score")
  private String dataConfidenceScore;
  @JsonProperty("PrivateListIndividuals.Data Confidence Comment")
  private String dataConfidenceComment;
  @JsonProperty("PrivateListIndividuals.Inactive Flag")
  private String inactiveFlag;
  @JsonProperty("PrivateListIndividuals.Inactive Since Date")
  private String inactiveSinceDate;
  @JsonProperty("PrivateListIndividuals.PEP Classification")
  private String pEPClassification;
  @JsonProperty("PrivateListIndividuals.Added Date")
  private String addedDate;
  @JsonProperty("PrivateListIndividuals.Last Updated Date")
  private String lastUpdatedDate;
  @JsonProperty("PrivateListIndividuals.Address Type")
  private String edqAddressType;
  @JsonProperty("PrivateListIndividuals.edqCurrentAddress")
  private String edqCurrentAddress;
  @JsonProperty("PrivateListIndividuals.Telephone Number")
  private String edqTelephoneNumber;
  @JsonProperty("PrivateListIndividuals.Cell Number (Business)")
  private String edqCellNumberBusiness;
  @JsonProperty("PrivateListIndividuals.Cell Number (Personal)")
  private String edqCellNumberPersonal;
  @JsonProperty("PrivateListIndividuals.Tax Number")
  private String edqTaxNumber;
  @JsonProperty("PrivateListIndividuals.Driving Licence")
  private String edqDrivingLicence;
  @JsonProperty("PrivateListIndividuals.Email Address")
  private String edqEmailAddress;
  @JsonProperty("PrivateListIndividuals.National Insurance Number")
  private String edqNiNumber;
  @JsonProperty("PrivateListIndividuals.SCION Reference Number")
  private String edqScionRef;
  @JsonProperty("PrivateListIndividuals.List Customer Type")
  private String edqListCustomerType;
  @JsonProperty("PrivateListIndividuals.Suffix")
  private String edqSuffix;
  @JsonProperty("PrivateListIndividuals.Role")
  private String edqRole;
  @JsonProperty("PrivateListIndividuals.List Category")
  private String edqListCategory;
  @JsonProperty("PrivateListIndividuals.Further Info")
  private String edqFurtherInfo;
  @JsonProperty("PrivateListIndividuals.Social Security Number")
  private String edqSsn;
  @JsonProperty("PrivateListIndividuals.External Customer ID")
  private String edqCustomerId;
  @JsonProperty("PrivateListIndividuals.edqListName")
  private String edqListName;
  @JsonProperty("PrivateListIndividuals.edqWatchlistListType")
  private String edqWatchListType;
  @JsonProperty("PrivateListIndividuals.Middle Name 1 (Derived)")
  private String middleName1Derived;
  @JsonProperty("PrivateListIndividuals.Middle Name 2 (Derived)")
  private String middleName2Derived;
  @JsonProperty("PrivateListIndividuals.Middle Name 1 (Original)")
  private String middleName1Original;
  @JsonProperty("PrivateListIndividuals.Middle Name 2 (Original)")
  private String middleName2Original;
  @JsonProperty("PrivateListIndividuals.Current Address")
  private String currentAddress;
  @JsonProperty("PrivateListIndividuals.RecordType")
  private String recordType;
  @JsonProperty("PrivateListIndividuals.Dummy")
  private String dummy;
}
