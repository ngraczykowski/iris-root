package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@Data
public class CustomerIndividual {

  @JsonProperty("CustomerIndividuals.Record Id")
  private String recordId;
  @JsonProperty("CustomerIndividuals.Input Stream")
  private String inputStream;
  @JsonProperty("CustomerIndividuals.Source System History ID")
  private BigDecimal sourceSystemHistoryId;
  @JsonProperty("CustomerIndividuals.Close Of Business Date")
  private String closeOfBusinessDate;
  @JsonProperty("CustomerIndividuals.Partition Number")
  private BigDecimal partitionNumber;
  @JsonProperty("CustomerIndividuals.Source System Identifier")
  private BigDecimal sourceSystemIdentifier;
  @JsonProperty("CustomerIndividuals.External Profile ID")
  private String externalProfileId;
  @JsonProperty("CustomerIndividuals.Concatenated Profile ID")
  private String concatenatedProfileId;
  @JsonProperty("CustomerIndividuals.Profile Type")
  private String profileType;
  @JsonProperty("CustomerIndividuals.Party Role Type Code")
  private String partyRoleTypeCode;
  @JsonProperty("CustomerIndividuals.Profile Status")
  private String profileStatus;
  @JsonProperty("CustomerIndividuals.Profile Segment")
  private String profileSegment;
  @JsonProperty("CustomerIndividuals.LoB Name")
  private String lobName;
  @JsonProperty("CustomerIndividuals.LoB Region")
  private String lobRegion;
  @JsonProperty("CustomerIndividuals.LoB Country")
  private String lobCountry;
  @JsonProperty("CustomerIndividuals.HSBC Legal Entity Code")
  private String hsbcLegalEntityCode;
  @JsonProperty("CustomerIndividuals.Name Category Code")
  private String nameCategoryCode;
  @JsonProperty("CustomerIndividuals.NameTypeCode")
  private String nameTypeCode;
  @JsonProperty("CustomerIndividuals.Name Language Type Code")
  private String nameLanguageTypeCode;
  @JsonProperty("CustomerIndividuals.Given Name")
  private String givenName;
  @JsonProperty("CustomerIndividuals.Middle Name")
  private String middleName;
  @JsonProperty("CustomerIndividuals.Family Name (Original)")
  private String familyNameOriginal;
  @JsonProperty("CustomerIndividuals.Initials (Original)")
  private String initialsOriginal;
  @JsonProperty("CustomerIndividuals.Profile Full Name")
  private String profileFullName;
  @JsonProperty("CustomerIndividuals.PEP Indicator")
  private String pepIndicator;
  @JsonProperty("CustomerIndividuals.Special Category Customer Indicator")
  private String specialCategoryCustomerIndicator;
  @JsonProperty("CustomerIndividuals.Gender Code")
  private String genderCode;
  @JsonProperty("CustomerIndividuals.Birth Date")
  private String birthDate;
  @JsonProperty("CustomerIndividuals.Place of Birth")
  private String placeOfBirth;
  @JsonProperty("CustomerIndividuals.Town of Birth")
  private String townOfBirth;
  @JsonProperty("CustomerIndividuals.State, Province or County of Birth")
  private String stateProvinceOrCountyOfBirth;
  @JsonProperty("CustomerIndividuals.Country of Birth (Original)")
  private String countryOfBirthOriginal;
  @JsonProperty("CustomerIndividuals.Source Address Type")
  private String sourceAddressType;
  @JsonProperty("CustomerIndividuals.Address Format Code")
  private String addressFormatCode;
  @JsonProperty("CustomerIndividuals.Address Language Type Code")
  private String addressLanguageTypeCode;
  @JsonProperty("CustomerIndividuals.Source Address Line 1")
  private String sourceAddressLine1;
  @JsonProperty("CustomerIndividuals.Source Address Line 2")
  private String sourceAddressLine2;
  @JsonProperty("CustomerIndividuals.Source Address Line 3")
  private String sourceAddressLine3;
  @JsonProperty("CustomerIndividuals.Source Address Line 4")
  private String sourceAddressLine4;
  @JsonProperty("CustomerIndividuals.Source Address Line 5")
  private String sourceAddressLine5;
  @JsonProperty("CustomerIndividuals.Source Address Line 6")
  private String sourceAddressLine6;
  @JsonProperty("CustomerIndividuals.Source Address Line 7")
  private String sourceAddressLine7;
  @JsonProperty("CustomerIndividuals.Source Address Line 8")
  private String sourceAddressLine8;
  @JsonProperty("CustomerIndividuals.Source Address Line 9")
  private String sourceAddressLine9;
  @JsonProperty("CustomerIndividuals.Source Address Line 10")
  private String sourceAddressLine10;
  @JsonProperty("CustomerIndividuals.Source Postal Code")
  private String sourcePostalCode;
  @JsonProperty("CustomerIndividuals.Source Country")
  private String sourceCountry;
  @JsonProperty("CustomerIndividuals.Identification Document 1")
  private String identificationDocument1;
  @JsonProperty("CustomerIndividuals.Identification Document 2")
  private String identificationDocument2;
  @JsonProperty("CustomerIndividuals.Identification Document 3")
  private String identificationDocument3;
  @JsonProperty("CustomerIndividuals.Identification Document 4")
  private String identificationDocument4;
  @JsonProperty("CustomerIndividuals.Identification Document 5")
  private String identificationDocument5;
  @JsonProperty("CustomerIndividuals.Identification Document 6")
  private String identificationDocument6;
  @JsonProperty("CustomerIndividuals.Identification Document 7")
  private String identificationDocument7;
  @JsonProperty("CustomerIndividuals.Identification Document 8")
  private String identificationDocument8;
  @JsonProperty("CustomerIndividuals.Identification Document 9")
  private String identificationDocument9;
  @JsonProperty("CustomerIndividuals.Identification Document 10")
  private String identificationDocument10;
  @JsonProperty("CustomerIndividuals.Residence Countries")
  private String residenceCountries;
  @JsonProperty("CustomerIndividuals.NationalityCitizenshipCountries")
  private String nationalityCitizenshipCountries;
  @JsonProperty("CustomerIndividuals.Employer 1 Details")
  private String employer1Details;
  @JsonProperty("CustomerIndividuals.Employer 1 Address 1")
  private String employer1Address1;
  @JsonProperty("CustomerIndividuals.Employer 1 Address 2")
  private String employer1Address2;
  @JsonProperty("CustomerIndividuals.Employer 1 Address 3")
  private String employer1Address3;
  @JsonProperty("CustomerIndividuals.Employer 2 Details")
  private String employer2Details;
  @JsonProperty("CustomerIndividuals.Employer 2 Address 1")
  private String employer2Address1;
  @JsonProperty("CustomerIndividuals.Employer 2 Address 2")
  private String employer2Address2;
  @JsonProperty("CustomerIndividuals.Employer 2 Address 3")
  private String employer2Address3;
  @JsonProperty("CustomerIndividuals.Employer 3 Details")
  private String employer3Details;
  @JsonProperty("CustomerIndividuals.Employer 3 Address 1")
  private String employer3Address1;
  @JsonProperty("CustomerIndividuals.Employer 3 Address 2")
  private String employer3Address2;
  @JsonProperty("CustomerIndividuals.Employer 3 Address 3")
  private String employer3Address3;
  @JsonProperty("CustomerIndividuals.edqCustID")
  private String edqCustId;
  @JsonProperty("CustomerIndividuals.edqCustSubID")
  private String edqCustSubId;
  @JsonProperty("CustomerIndividuals.Title (Derived)")
  private String titleDerived;
  @JsonProperty("CustomerIndividuals.Given Names (Derived)")
  private String givenNamesDerived;
  @JsonProperty("CustomerIndividuals.Full Name")
  private String fullNameDerived;
  @JsonProperty("CustomerIndividuals.Family Name (Derived)")
  private String familyNameDerived;
  @JsonProperty("CustomerIndividuals.Initials")
  private String initials;
  @JsonProperty("CustomerIndividuals.edqLoBCountryCode")
  private String edqLobCountryCode;
  @JsonProperty("CustomerIndividuals.Country of Birth")
  private String countryOfBirth;
  @JsonProperty("CustomerIndividuals.Address Country")
  private String addressCountry;
  @JsonProperty("CustomerIndividuals.edqAddressCountryCode")
  private String edqAddressCountryCode;
  @JsonProperty("CustomerIndividuals.Country of Residence")
  private String countryOfResidence;
  @JsonProperty("CustomerIndividuals.edqBirthCountryCode")
  private String edqBirthCountryCode;
  @JsonProperty("CustomerIndividuals.edqResidenceCountriesCode")
  private String edqResidenceCountriesCode;
  @JsonProperty("CustomerIndividuals.Nationality or Citizenship")
  private String nationalityOrCitizenship;
  @JsonProperty("CustomerIndividuals.Nationality Countries")
  private String nationalityCountries;
  @JsonProperty("CustomerIndividuals.Countries (All)")
  private String countriesAll;
  @JsonProperty("CustomerIndividuals.edqCountriesAllCodes")
  private String edqCountriesAllCodes;
  @JsonProperty("CustomerIndividuals.edqEmployerAllCountries")
  private String edqEmployerAllCountries;
  @JsonProperty("CustomerIndividuals.edqEmployerAllCountriesCodes")
  private String edqEmployerAllCountriesCodes;
  @JsonProperty("CustomerIndividuals.edqLoB")
  private String edqLob;
  @JsonProperty("CustomerIndividuals.edqPermission")
  private String edqPermission;
  @JsonProperty("CustomerIndividuals.Tax ID")
  private String taxId;
  @JsonProperty("CustomerIndividuals.Date of Birth")
  private String dateOfBirth;
  @JsonProperty("CustomerIndividuals.DOB (Original)")
  private String dobOriginal;
  @JsonProperty("CustomerIndividuals.edqDOBString")
  private String edqDobString;
  @JsonProperty("CustomerIndividuals.Year of Birth")
  private String yearOfBirth;
  @JsonProperty("CustomerIndividuals.City")
  private String city;
  @JsonProperty("CustomerIndividuals.Postal Code")
  private String postalCode;
  @JsonProperty("CustomerIndividuals.Profile Full Address")
  private String profileFullAddress;
  @JsonProperty("CustomerIndividuals.Gender")
  private String gender;
  @JsonProperty("CustomerIndividuals.Gender Derived Flag")
  private String genderDerivedFlag;
  @JsonProperty("CustomerIndividuals.Profile Hyperlink")
  private String profileHyperlink;
  @JsonProperty("CustomerIndividuals.Search Hyperlink")
  private String searchHyperlink;
  @JsonProperty("CustomerIndividuals.edqListKey")
  private String edqListKey;
  @JsonProperty("CustomerIndividuals.Passport Number")
  private String passportNumber;
  @JsonProperty("CustomerIndividuals.Passport Issue Country")
  private String passportIssueCountry;
  @JsonProperty("CustomerIndividuals.Social Security Number")
  private String socialSecurityNumber;
  @JsonProperty("CustomerIndividuals.edqCloseOfBusinessDate")
  private String edqCloseOfBusinessDate;
  @JsonProperty("CustomerIndividuals.Profile Occupation")
  private String profileOccupation;
  @JsonProperty("CustomerIndividuals.Person or Business Indicator")
  private String personOrBusinessIndicator;
  @JsonProperty("CustomerIndividuals.Profile Name Type")
  private String profileNameType;
  @JsonProperty("CustomerIndividuals.edqPartyRoleTypeDescription")
  private String edqPartyRoleTypeDescription;
  @JsonProperty("CustomerIndividuals.edqPartyStatusCodeDescription")
  private String edqPartyStatusCodeDescription;
  @JsonProperty("CustomerIndividuals.edqDay1SpikeFlag")
  private String edqDay1SpikeFlag;
  @JsonProperty("CustomerIndividuals.Original Script Name")
  private String originalScriptName;
  @JsonProperty("CustomerIndividuals.Address")
  private String address;
  @JsonProperty("CustomerIndividuals.Given Names (Original)")
  private String givenNamesOriginal;
  @JsonProperty("CustomerIndividuals.Surname")
  private String surname;
  @JsonProperty("CustomerIndividuals.edqScreeningMode")
  private String edqScreeningMode;
  @JsonProperty("CustomerIndividuals.edqCaseKey")
  private String edqCaseKey;
  @JsonProperty("CustomerIndividuals.Address Type")
  private String addressType;
  @JsonProperty("CustomerIndividuals.SSC Codes")
  private String sscCodes;
  @JsonProperty("CustomerIndividuals.CTRP Fragment")
  private String ctrpFragment;
  @JsonProperty("CustomerIndividuals.Request User Name")
  private String requestUserName;
  @JsonProperty("CustomerIndividuals.ReqDT")
  private String reqDt;
  @JsonProperty("CustomerIndividuals.Auto Discount Decision")
  private String autoDiscountDecision;
  @JsonProperty("CustomerIndividuals.RecordType")
  private String recordType;
  @JsonProperty("CustomerIndividuals.Dummy")
  private String dummy;
}
