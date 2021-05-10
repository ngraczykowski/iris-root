package com.silenteight.hsbc.bridge.json.external.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen", date = "2021-04-30T09:49:39.903Z[GMT]")
public class CustomerIndividuals {

  private String customerIndividualsRecordId = null;
  private String customerIndividualsInputStream = null;
  private String customerIndividualsSourceSystemHistoryID = null;
  private String customerIndividualsCloseOfBusinessDate = null;
  private String customerIndividualsPartitionNumber = null;
  private String customerIndividualsSourceSystemIdentifier = null;
  private String customerIndividualsExternalProfileID = null;
  private String customerIndividualsConcatenatedProfileID = null;
  private String customerIndividualsProfileType = null;
  private String customerIndividualsPartyRoleTypeCode = null;
  private String customerIndividualsProfileStatus = null;
  private String customerIndividualsProfileSegment = null;
  private String customerIndividualsLoBName = null;
  private String customerIndividualsLoBRegion = null;
  private String customerIndividualsLoBCountry = null;
  private String customerIndividualsHSBCLegalEntityCode = null;
  private String customerIndividualsNameCategoryCode = null;
  private String customerIndividualsNameTypeCode = null;
  private String customerIndividualsNameLanguageTypeCode = null;
  private String customerIndividualsGivenName = null;
  private String customerIndividualsMiddleName = null;
  private String customerIndividualsFamilyNameOriginal = null;
  private String customerIndividualsInitialsOriginal = null;
  private String customerIndividualsProfileFullName = null;
  private String customerIndividualsPEPIndicator = null;
  private String customerIndividualsSpecialCategoryCustomerIndicator = null;
  private String customerIndividualsGenderCode = null;
  private String customerIndividualsBirthDate = null;
  private String customerIndividualsPlaceOfBirth = null;
  private String customerIndividualsTownOfBirth = null;
  private String customerIndividualsStateProvinceOrCountyOfBirth = null;
  private String customerIndividualsCountryOfBirthOriginal = null;
  private String customerIndividualsSourceAddressType = null;
  private String customerIndividualsAddressFormatCode = null;
  private String customerIndividualsAddressLanguageTypeCode = null;
  private String customerIndividualsSourceAddressLine1 = null;
  private String customerIndividualsSourceAddressLine2 = null;
  private String customerIndividualsSourceAddressLine3 = null;
  private String customerIndividualsSourceAddressLine4 = null;
  private String customerIndividualsSourceAddressLine5 = null;
  private String customerIndividualsSourceAddressLine6 = null;
  private String customerIndividualsSourceAddressLine7 = null;
  private String customerIndividualsSourceAddressLine8 = null;
  private String customerIndividualsSourceAddressLine9 = null;
  private String customerIndividualsSourceAddressLine10 = null;
  private String customerIndividualsSourcePostalCode = null;
  private String customerIndividualsSourceCountry = null;
  private String customerIndividualsIdentificationDocument1 = null;
  private String customerIndividualsIdentificationDocument2 = null;
  private String customerIndividualsIdentificationDocument3 = null;
  private String customerIndividualsIdentificationDocument4 = null;
  private String customerIndividualsIdentificationDocument5 = null;
  private String customerIndividualsIdentificationDocument6 = null;
  private String customerIndividualsIdentificationDocument7 = null;
  private String customerIndividualsIdentificationDocument8 = null;
  private String customerIndividualsIdentificationDocument9 = null;
  private String customerIndividualsIdentificationDocument10 = null;
  private String customerIndividualsResidenceCountries = null;
  private String customerIndividualsNationalityCitizenshipCountries = null;
  private String customerIndividualsEmployer1Details = null;
  private String customerIndividualsEmployer1Address1 = null;
  private String customerIndividualsEmployer1Address2 = null;
  private String customerIndividualsEmployer1Address3 = null;
  private String customerIndividualsEmployer2Details = null;
  private String customerIndividualsEmployer2Address1 = null;
  private String customerIndividualsEmployer2Address2 = null;
  private String customerIndividualsEmployer2Address3 = null;
  private String customerIndividualsEmployer3Details = null;
  private String customerIndividualsEmployer3Address1 = null;
  private String customerIndividualsEmployer3Address2 = null;
  private String customerIndividualsEmployer3Address3 = null;
  private String customerIndividualsEdqCustID = null;
  private String customerIndividualsEdqCustSubID = null;
  private String customerIndividualsTitleDerived = null;
  private String customerIndividualsGivenNamesDerived = null;
  private String customerIndividualsFullName = null;
  private String customerIndividualsFamilyNameDerived = null;
  private String customerIndividualsInitials = null;
  private String customerIndividualsEdqLoBCountryCode = null;
  private String customerIndividualsCountryOfBirth = null;
  private String customerIndividualsAddressCountry = null;
  private String customerIndividualsEdqAddressCountryCode = null;
  private String customerIndividualsCountryOfResidence = null;
  private String customerIndividualsEdqBirthCountryCode = null;
  private String customerIndividualsEdqResidenceCountriesCode = null;
  private String customerIndividualsNationalityOrCitizenship = null;
  private String customerIndividualsNationalityCountries = null;
  private String customerIndividualsCountriesAll = null;
  private String customerIndividualsEdqCountriesAllCodes = null;
  private String customerIndividualsEdqEmployerAllCountries = null;
  private String customerIndividualsEdqEmployerAllCountriesCodes = null;
  private String customerIndividualsEdqLoB = null;
  private String customerIndividualsEdqPermission = null;
  private String customerIndividualsTaxID = null;
  private String customerIndividualsDateOfBirth = null;
  private String customerIndividualsDOBOriginal = null;
  private String customerIndividualsEdqDOBString = null;
  private String customerIndividualsYearOfBirth = null;
  private String customerIndividualsCity = null;
  private String customerIndividualsPostalCode = null;
  private String customerIndividualsProfileFullAddress = null;
  private String customerIndividualsGender = null;
  private String customerIndividualsGenderDerivedFlag = null;
  private String customerIndividualsProfileHyperlink = null;
  private String customerIndividualsSearchHyperlink = null;
  private String customerIndividualsEdqListKey = null;
  private String customerIndividualsPassportNumber = null;
  private String customerIndividualsPassportIssueCountry = null;
  private String customerIndividualsSocialSecurityNumber = null;
  private String customerIndividualsEdqCloseOfBusinessDate = null;
  private String customerIndividualsProfileOccupation = null;
  private String customerIndividualsPersonOrBusinessIndicator = null;
  private String customerIndividualsProfileNameType = null;
  private String customerIndividualsEdqPartyRoleTypeDescription = null;
  private String customerIndividualsEdqPartyStatusCodeDescription = null;
  private String customerIndividualsEdqDay1SpikeFlag = null;
  private String customerIndividualsOriginalScriptName = null;
  private String customerIndividualsAddress = null;
  private String customerIndividualsGivenNamesOriginal = null;
  private String customerIndividualsSurname = null;
  private String customerIndividualsEdqScreeningMode = null;
  private String customerIndividualsEdqCaseKey = null;
  private String customerIndividualsAddressType = null;
  private String customerIndividualsSSCCodes = null;
  private String customerIndividualsCTRPFragment = null;
  private String customerIndividualsRequestUserName = null;
  private String customerIndividualsReqDT = null;
  private String customerIndividualsAutoDiscountDecision = null;
  private String customerIndividualsRecordType = null;
  private String customerIndividualsDummy = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Record Id")
  public String getCustomerIndividualsRecordId() {
    return customerIndividualsRecordId;
  }

  public void setCustomerIndividualsRecordId(String customerIndividualsRecordId) {
    this.customerIndividualsRecordId = customerIndividualsRecordId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Input Stream")
  public String getCustomerIndividualsInputStream() {
    return customerIndividualsInputStream;
  }

  public void setCustomerIndividualsInputStream(String customerIndividualsInputStream) {
    this.customerIndividualsInputStream = customerIndividualsInputStream;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source System History ID")
  public String getCustomerIndividualsSourceSystemHistoryID() {
    return customerIndividualsSourceSystemHistoryID;
  }

  public void setCustomerIndividualsSourceSystemHistoryID(
      String customerIndividualsSourceSystemHistoryID) {
    this.customerIndividualsSourceSystemHistoryID = customerIndividualsSourceSystemHistoryID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Close Of Business Date")
  public String getCustomerIndividualsCloseOfBusinessDate() {
    return customerIndividualsCloseOfBusinessDate;
  }

  public void setCustomerIndividualsCloseOfBusinessDate(
      String customerIndividualsCloseOfBusinessDate) {
    this.customerIndividualsCloseOfBusinessDate = customerIndividualsCloseOfBusinessDate;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Partition Number")
  public String getCustomerIndividualsPartitionNumber() {
    return customerIndividualsPartitionNumber;
  }

  public void setCustomerIndividualsPartitionNumber(String customerIndividualsPartitionNumber) {
    this.customerIndividualsPartitionNumber = customerIndividualsPartitionNumber;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source System Identifier")
  public String getCustomerIndividualsSourceSystemIdentifier() {
    return customerIndividualsSourceSystemIdentifier;
  }

  public void setCustomerIndividualsSourceSystemIdentifier(
      String customerIndividualsSourceSystemIdentifier) {
    this.customerIndividualsSourceSystemIdentifier = customerIndividualsSourceSystemIdentifier;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.External Profile ID")
  public String getCustomerIndividualsExternalProfileID() {
    return customerIndividualsExternalProfileID;
  }

  public void setCustomerIndividualsExternalProfileID(String customerIndividualsExternalProfileID) {
    this.customerIndividualsExternalProfileID = customerIndividualsExternalProfileID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Concatenated Profile ID")
  public String getCustomerIndividualsConcatenatedProfileID() {
    return customerIndividualsConcatenatedProfileID;
  }

  public void setCustomerIndividualsConcatenatedProfileID(
      String customerIndividualsConcatenatedProfileID) {
    this.customerIndividualsConcatenatedProfileID = customerIndividualsConcatenatedProfileID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Type")
  public String getCustomerIndividualsProfileType() {
    return customerIndividualsProfileType;
  }

  public void setCustomerIndividualsProfileType(String customerIndividualsProfileType) {
    this.customerIndividualsProfileType = customerIndividualsProfileType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Party Role Type Code")
  public String getCustomerIndividualsPartyRoleTypeCode() {
    return customerIndividualsPartyRoleTypeCode;
  }

  public void setCustomerIndividualsPartyRoleTypeCode(String customerIndividualsPartyRoleTypeCode) {
    this.customerIndividualsPartyRoleTypeCode = customerIndividualsPartyRoleTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Status")
  public String getCustomerIndividualsProfileStatus() {
    return customerIndividualsProfileStatus;
  }

  public void setCustomerIndividualsProfileStatus(String customerIndividualsProfileStatus) {
    this.customerIndividualsProfileStatus = customerIndividualsProfileStatus;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Segment")
  public String getCustomerIndividualsProfileSegment() {
    return customerIndividualsProfileSegment;
  }

  public void setCustomerIndividualsProfileSegment(String customerIndividualsProfileSegment) {
    this.customerIndividualsProfileSegment = customerIndividualsProfileSegment;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.LoB Name")
  public String getCustomerIndividualsLoBName() {
    return customerIndividualsLoBName;
  }

  public void setCustomerIndividualsLoBName(String customerIndividualsLoBName) {
    this.customerIndividualsLoBName = customerIndividualsLoBName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.LoB Region")
  public String getCustomerIndividualsLoBRegion() {
    return customerIndividualsLoBRegion;
  }

  public void setCustomerIndividualsLoBRegion(String customerIndividualsLoBRegion) {
    this.customerIndividualsLoBRegion = customerIndividualsLoBRegion;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.LoB Country")
  public String getCustomerIndividualsLoBCountry() {
    return customerIndividualsLoBCountry;
  }

  public void setCustomerIndividualsLoBCountry(String customerIndividualsLoBCountry) {
    this.customerIndividualsLoBCountry = customerIndividualsLoBCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.HSBC Legal Entity Code")
  public String getCustomerIndividualsHSBCLegalEntityCode() {
    return customerIndividualsHSBCLegalEntityCode;
  }

  public void setCustomerIndividualsHSBCLegalEntityCode(
      String customerIndividualsHSBCLegalEntityCode) {
    this.customerIndividualsHSBCLegalEntityCode = customerIndividualsHSBCLegalEntityCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Name Category Code")
  public String getCustomerIndividualsNameCategoryCode() {
    return customerIndividualsNameCategoryCode;
  }

  public void setCustomerIndividualsNameCategoryCode(String customerIndividualsNameCategoryCode) {
    this.customerIndividualsNameCategoryCode = customerIndividualsNameCategoryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.NameTypeCode")
  public String getCustomerIndividualsNameTypeCode() {
    return customerIndividualsNameTypeCode;
  }

  public void setCustomerIndividualsNameTypeCode(String customerIndividualsNameTypeCode) {
    this.customerIndividualsNameTypeCode = customerIndividualsNameTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Name Language Type Code")
  public String getCustomerIndividualsNameLanguageTypeCode() {
    return customerIndividualsNameLanguageTypeCode;
  }

  public void setCustomerIndividualsNameLanguageTypeCode(
      String customerIndividualsNameLanguageTypeCode) {
    this.customerIndividualsNameLanguageTypeCode = customerIndividualsNameLanguageTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Given Name")
  public String getCustomerIndividualsGivenName() {
    return customerIndividualsGivenName;
  }

  public void setCustomerIndividualsGivenName(String customerIndividualsGivenName) {
    this.customerIndividualsGivenName = customerIndividualsGivenName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Middle Name")
  public String getCustomerIndividualsMiddleName() {
    return customerIndividualsMiddleName;
  }

  public void setCustomerIndividualsMiddleName(String customerIndividualsMiddleName) {
    this.customerIndividualsMiddleName = customerIndividualsMiddleName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Family Name (Original)")
  public String getCustomerIndividualsFamilyNameOriginal() {
    return customerIndividualsFamilyNameOriginal;
  }

  public void setCustomerIndividualsFamilyNameOriginal(
      String customerIndividualsFamilyNameOriginal) {
    this.customerIndividualsFamilyNameOriginal = customerIndividualsFamilyNameOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Initials (Original)")
  public String getCustomerIndividualsInitialsOriginal() {
    return customerIndividualsInitialsOriginal;
  }

  public void setCustomerIndividualsInitialsOriginal(String customerIndividualsInitialsOriginal) {
    this.customerIndividualsInitialsOriginal = customerIndividualsInitialsOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Full Name")
  public String getCustomerIndividualsProfileFullName() {
    return customerIndividualsProfileFullName;
  }

  public void setCustomerIndividualsProfileFullName(String customerIndividualsProfileFullName) {
    this.customerIndividualsProfileFullName = customerIndividualsProfileFullName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.PEP Indicator")
  public String getCustomerIndividualsPEPIndicator() {
    return customerIndividualsPEPIndicator;
  }

  public void setCustomerIndividualsPEPIndicator(String customerIndividualsPEPIndicator) {
    this.customerIndividualsPEPIndicator = customerIndividualsPEPIndicator;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Special Category Customer Indicator")
  public String getCustomerIndividualsSpecialCategoryCustomerIndicator() {
    return customerIndividualsSpecialCategoryCustomerIndicator;
  }

  public void setCustomerIndividualsSpecialCategoryCustomerIndicator(
      String customerIndividualsSpecialCategoryCustomerIndicator) {
    this.customerIndividualsSpecialCategoryCustomerIndicator =
        customerIndividualsSpecialCategoryCustomerIndicator;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Gender Code")
  public String getCustomerIndividualsGenderCode() {
    return customerIndividualsGenderCode;
  }

  public void setCustomerIndividualsGenderCode(String customerIndividualsGenderCode) {
    this.customerIndividualsGenderCode = customerIndividualsGenderCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Birth Date")
  public String getCustomerIndividualsBirthDate() {
    return customerIndividualsBirthDate;
  }

  public void setCustomerIndividualsBirthDate(String customerIndividualsBirthDate) {
    this.customerIndividualsBirthDate = customerIndividualsBirthDate;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Place of Birth")
  public String getCustomerIndividualsPlaceOfBirth() {
    return customerIndividualsPlaceOfBirth;
  }

  public void setCustomerIndividualsPlaceOfBirth(String customerIndividualsPlaceOfBirth) {
    this.customerIndividualsPlaceOfBirth = customerIndividualsPlaceOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Town of Birth")
  public String getCustomerIndividualsTownOfBirth() {
    return customerIndividualsTownOfBirth;
  }

  public void setCustomerIndividualsTownOfBirth(String customerIndividualsTownOfBirth) {
    this.customerIndividualsTownOfBirth = customerIndividualsTownOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.State Province or County of Birth")
  public String getCustomerIndividualsStateProvinceOrCountyOfBirth() {
    return customerIndividualsStateProvinceOrCountyOfBirth;
  }

  public void setCustomerIndividualsStateProvinceOrCountyOfBirth(
      String customerIndividualsStateProvinceOrCountyOfBirth) {
    this.customerIndividualsStateProvinceOrCountyOfBirth =
        customerIndividualsStateProvinceOrCountyOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Country of Birth (Original)")
  public String getCustomerIndividualsCountryOfBirthOriginal() {
    return customerIndividualsCountryOfBirthOriginal;
  }

  public void setCustomerIndividualsCountryOfBirthOriginal(
      String customerIndividualsCountryOfBirthOriginal) {
    this.customerIndividualsCountryOfBirthOriginal = customerIndividualsCountryOfBirthOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Type")
  public String getCustomerIndividualsSourceAddressType() {
    return customerIndividualsSourceAddressType;
  }

  public void setCustomerIndividualsSourceAddressType(String customerIndividualsSourceAddressType) {
    this.customerIndividualsSourceAddressType = customerIndividualsSourceAddressType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Address Format Code")
  public String getCustomerIndividualsAddressFormatCode() {
    return customerIndividualsAddressFormatCode;
  }

  public void setCustomerIndividualsAddressFormatCode(String customerIndividualsAddressFormatCode) {
    this.customerIndividualsAddressFormatCode = customerIndividualsAddressFormatCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Address Language Type Code")
  public String getCustomerIndividualsAddressLanguageTypeCode() {
    return customerIndividualsAddressLanguageTypeCode;
  }

  public void setCustomerIndividualsAddressLanguageTypeCode(
      String customerIndividualsAddressLanguageTypeCode) {
    this.customerIndividualsAddressLanguageTypeCode = customerIndividualsAddressLanguageTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 1")
  public String getCustomerIndividualsSourceAddressLine1() {
    return customerIndividualsSourceAddressLine1;
  }

  public void setCustomerIndividualsSourceAddressLine1(
      String customerIndividualsSourceAddressLine1) {
    this.customerIndividualsSourceAddressLine1 = customerIndividualsSourceAddressLine1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 2")
  public String getCustomerIndividualsSourceAddressLine2() {
    return customerIndividualsSourceAddressLine2;
  }

  public void setCustomerIndividualsSourceAddressLine2(
      String customerIndividualsSourceAddressLine2) {
    this.customerIndividualsSourceAddressLine2 = customerIndividualsSourceAddressLine2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 3")
  public String getCustomerIndividualsSourceAddressLine3() {
    return customerIndividualsSourceAddressLine3;
  }

  public void setCustomerIndividualsSourceAddressLine3(
      String customerIndividualsSourceAddressLine3) {
    this.customerIndividualsSourceAddressLine3 = customerIndividualsSourceAddressLine3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 4")
  public String getCustomerIndividualsSourceAddressLine4() {
    return customerIndividualsSourceAddressLine4;
  }

  public void setCustomerIndividualsSourceAddressLine4(
      String customerIndividualsSourceAddressLine4) {
    this.customerIndividualsSourceAddressLine4 = customerIndividualsSourceAddressLine4;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 5")
  public String getCustomerIndividualsSourceAddressLine5() {
    return customerIndividualsSourceAddressLine5;
  }

  public void setCustomerIndividualsSourceAddressLine5(
      String customerIndividualsSourceAddressLine5) {
    this.customerIndividualsSourceAddressLine5 = customerIndividualsSourceAddressLine5;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 6")
  public String getCustomerIndividualsSourceAddressLine6() {
    return customerIndividualsSourceAddressLine6;
  }

  public void setCustomerIndividualsSourceAddressLine6(
      String customerIndividualsSourceAddressLine6) {
    this.customerIndividualsSourceAddressLine6 = customerIndividualsSourceAddressLine6;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 7")
  public String getCustomerIndividualsSourceAddressLine7() {
    return customerIndividualsSourceAddressLine7;
  }

  public void setCustomerIndividualsSourceAddressLine7(
      String customerIndividualsSourceAddressLine7) {
    this.customerIndividualsSourceAddressLine7 = customerIndividualsSourceAddressLine7;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 8")
  public String getCustomerIndividualsSourceAddressLine8() {
    return customerIndividualsSourceAddressLine8;
  }

  public void setCustomerIndividualsSourceAddressLine8(
      String customerIndividualsSourceAddressLine8) {
    this.customerIndividualsSourceAddressLine8 = customerIndividualsSourceAddressLine8;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 9")
  public String getCustomerIndividualsSourceAddressLine9() {
    return customerIndividualsSourceAddressLine9;
  }

  public void setCustomerIndividualsSourceAddressLine9(
      String customerIndividualsSourceAddressLine9) {
    this.customerIndividualsSourceAddressLine9 = customerIndividualsSourceAddressLine9;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Address Line 10")
  public String getCustomerIndividualsSourceAddressLine10() {
    return customerIndividualsSourceAddressLine10;
  }

  public void setCustomerIndividualsSourceAddressLine10(
      String customerIndividualsSourceAddressLine10) {
    this.customerIndividualsSourceAddressLine10 = customerIndividualsSourceAddressLine10;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Postal Code")
  public String getCustomerIndividualsSourcePostalCode() {
    return customerIndividualsSourcePostalCode;
  }

  public void setCustomerIndividualsSourcePostalCode(String customerIndividualsSourcePostalCode) {
    this.customerIndividualsSourcePostalCode = customerIndividualsSourcePostalCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Source Country")
  public String getCustomerIndividualsSourceCountry() {
    return customerIndividualsSourceCountry;
  }

  public void setCustomerIndividualsSourceCountry(String customerIndividualsSourceCountry) {
    this.customerIndividualsSourceCountry = customerIndividualsSourceCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 1")
  public String getCustomerIndividualsIdentificationDocument1() {
    return customerIndividualsIdentificationDocument1;
  }

  public void setCustomerIndividualsIdentificationDocument1(
      String customerIndividualsIdentificationDocument1) {
    this.customerIndividualsIdentificationDocument1 = customerIndividualsIdentificationDocument1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 2")
  public String getCustomerIndividualsIdentificationDocument2() {
    return customerIndividualsIdentificationDocument2;
  }

  public void setCustomerIndividualsIdentificationDocument2(
      String customerIndividualsIdentificationDocument2) {
    this.customerIndividualsIdentificationDocument2 = customerIndividualsIdentificationDocument2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 3")
  public String getCustomerIndividualsIdentificationDocument3() {
    return customerIndividualsIdentificationDocument3;
  }

  public void setCustomerIndividualsIdentificationDocument3(
      String customerIndividualsIdentificationDocument3) {
    this.customerIndividualsIdentificationDocument3 = customerIndividualsIdentificationDocument3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 4")
  public String getCustomerIndividualsIdentificationDocument4() {
    return customerIndividualsIdentificationDocument4;
  }

  public void setCustomerIndividualsIdentificationDocument4(
      String customerIndividualsIdentificationDocument4) {
    this.customerIndividualsIdentificationDocument4 = customerIndividualsIdentificationDocument4;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 5")
  public String getCustomerIndividualsIdentificationDocument5() {
    return customerIndividualsIdentificationDocument5;
  }

  public void setCustomerIndividualsIdentificationDocument5(
      String customerIndividualsIdentificationDocument5) {
    this.customerIndividualsIdentificationDocument5 = customerIndividualsIdentificationDocument5;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 6")
  public String getCustomerIndividualsIdentificationDocument6() {
    return customerIndividualsIdentificationDocument6;
  }

  public void setCustomerIndividualsIdentificationDocument6(
      String customerIndividualsIdentificationDocument6) {
    this.customerIndividualsIdentificationDocument6 = customerIndividualsIdentificationDocument6;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 7")
  public String getCustomerIndividualsIdentificationDocument7() {
    return customerIndividualsIdentificationDocument7;
  }

  public void setCustomerIndividualsIdentificationDocument7(
      String customerIndividualsIdentificationDocument7) {
    this.customerIndividualsIdentificationDocument7 = customerIndividualsIdentificationDocument7;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 8")
  public String getCustomerIndividualsIdentificationDocument8() {
    return customerIndividualsIdentificationDocument8;
  }

  public void setCustomerIndividualsIdentificationDocument8(
      String customerIndividualsIdentificationDocument8) {
    this.customerIndividualsIdentificationDocument8 = customerIndividualsIdentificationDocument8;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 9")
  public String getCustomerIndividualsIdentificationDocument9() {
    return customerIndividualsIdentificationDocument9;
  }

  public void setCustomerIndividualsIdentificationDocument9(
      String customerIndividualsIdentificationDocument9) {
    this.customerIndividualsIdentificationDocument9 = customerIndividualsIdentificationDocument9;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Identification Document 10")
  public String getCustomerIndividualsIdentificationDocument10() {
    return customerIndividualsIdentificationDocument10;
  }

  public void setCustomerIndividualsIdentificationDocument10(
      String customerIndividualsIdentificationDocument10) {
    this.customerIndividualsIdentificationDocument10 = customerIndividualsIdentificationDocument10;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Residence Countries")
  public String getCustomerIndividualsResidenceCountries() {
    return customerIndividualsResidenceCountries;
  }

  public void setCustomerIndividualsResidenceCountries(
      String customerIndividualsResidenceCountries) {
    this.customerIndividualsResidenceCountries = customerIndividualsResidenceCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.NationalityCitizenshipCountries")
  public String getCustomerIndividualsNationalityCitizenshipCountries() {
    return customerIndividualsNationalityCitizenshipCountries;
  }

  public void setCustomerIndividualsNationalityCitizenshipCountries(
      String customerIndividualsNationalityCitizenshipCountries) {
    this.customerIndividualsNationalityCitizenshipCountries =
        customerIndividualsNationalityCitizenshipCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 1 Details")
  public String getCustomerIndividualsEmployer1Details() {
    return customerIndividualsEmployer1Details;
  }

  public void setCustomerIndividualsEmployer1Details(String customerIndividualsEmployer1Details) {
    this.customerIndividualsEmployer1Details = customerIndividualsEmployer1Details;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 1 Address 1")
  public String getCustomerIndividualsEmployer1Address1() {
    return customerIndividualsEmployer1Address1;
  }

  public void setCustomerIndividualsEmployer1Address1(String customerIndividualsEmployer1Address1) {
    this.customerIndividualsEmployer1Address1 = customerIndividualsEmployer1Address1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 1 Address 2")
  public String getCustomerIndividualsEmployer1Address2() {
    return customerIndividualsEmployer1Address2;
  }

  public void setCustomerIndividualsEmployer1Address2(String customerIndividualsEmployer1Address2) {
    this.customerIndividualsEmployer1Address2 = customerIndividualsEmployer1Address2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 1 Address 3")
  public String getCustomerIndividualsEmployer1Address3() {
    return customerIndividualsEmployer1Address3;
  }

  public void setCustomerIndividualsEmployer1Address3(String customerIndividualsEmployer1Address3) {
    this.customerIndividualsEmployer1Address3 = customerIndividualsEmployer1Address3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 2 Details")
  public String getCustomerIndividualsEmployer2Details() {
    return customerIndividualsEmployer2Details;
  }

  public void setCustomerIndividualsEmployer2Details(String customerIndividualsEmployer2Details) {
    this.customerIndividualsEmployer2Details = customerIndividualsEmployer2Details;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 2 Address 1")
  public String getCustomerIndividualsEmployer2Address1() {
    return customerIndividualsEmployer2Address1;
  }

  public void setCustomerIndividualsEmployer2Address1(String customerIndividualsEmployer2Address1) {
    this.customerIndividualsEmployer2Address1 = customerIndividualsEmployer2Address1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 2 Address 2")
  public String getCustomerIndividualsEmployer2Address2() {
    return customerIndividualsEmployer2Address2;
  }

  public void setCustomerIndividualsEmployer2Address2(String customerIndividualsEmployer2Address2) {
    this.customerIndividualsEmployer2Address2 = customerIndividualsEmployer2Address2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 2 Address 3")
  public String getCustomerIndividualsEmployer2Address3() {
    return customerIndividualsEmployer2Address3;
  }

  public void setCustomerIndividualsEmployer2Address3(String customerIndividualsEmployer2Address3) {
    this.customerIndividualsEmployer2Address3 = customerIndividualsEmployer2Address3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 3 Details")
  public String getCustomerIndividualsEmployer3Details() {
    return customerIndividualsEmployer3Details;
  }

  public void setCustomerIndividualsEmployer3Details(String customerIndividualsEmployer3Details) {
    this.customerIndividualsEmployer3Details = customerIndividualsEmployer3Details;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 3 Address 1")
  public String getCustomerIndividualsEmployer3Address1() {
    return customerIndividualsEmployer3Address1;
  }

  public void setCustomerIndividualsEmployer3Address1(String customerIndividualsEmployer3Address1) {
    this.customerIndividualsEmployer3Address1 = customerIndividualsEmployer3Address1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 3 Address 2")
  public String getCustomerIndividualsEmployer3Address2() {
    return customerIndividualsEmployer3Address2;
  }

  public void setCustomerIndividualsEmployer3Address2(String customerIndividualsEmployer3Address2) {
    this.customerIndividualsEmployer3Address2 = customerIndividualsEmployer3Address2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Employer 3 Address 3")
  public String getCustomerIndividualsEmployer3Address3() {
    return customerIndividualsEmployer3Address3;
  }

  public void setCustomerIndividualsEmployer3Address3(String customerIndividualsEmployer3Address3) {
    this.customerIndividualsEmployer3Address3 = customerIndividualsEmployer3Address3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqCustID")
  public String getCustomerIndividualsEdqCustID() {
    return customerIndividualsEdqCustID;
  }

  public void setCustomerIndividualsEdqCustID(String customerIndividualsEdqCustID) {
    this.customerIndividualsEdqCustID = customerIndividualsEdqCustID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqCustSubID")
  public String getCustomerIndividualsEdqCustSubID() {
    return customerIndividualsEdqCustSubID;
  }

  public void setCustomerIndividualsEdqCustSubID(String customerIndividualsEdqCustSubID) {
    this.customerIndividualsEdqCustSubID = customerIndividualsEdqCustSubID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Title (Derived)")
  public String getCustomerIndividualsTitleDerived() {
    return customerIndividualsTitleDerived;
  }

  public void setCustomerIndividualsTitleDerived(String customerIndividualsTitleDerived) {
    this.customerIndividualsTitleDerived = customerIndividualsTitleDerived;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Given Names (Derived)")
  public String getCustomerIndividualsGivenNamesDerived() {
    return customerIndividualsGivenNamesDerived;
  }

  public void setCustomerIndividualsGivenNamesDerived(String customerIndividualsGivenNamesDerived) {
    this.customerIndividualsGivenNamesDerived = customerIndividualsGivenNamesDerived;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Full Name")
  public String getCustomerIndividualsFullName() {
    return customerIndividualsFullName;
  }

  public void setCustomerIndividualsFullName(String customerIndividualsFullName) {
    this.customerIndividualsFullName = customerIndividualsFullName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Family Name (Derived)")
  public String getCustomerIndividualsFamilyNameDerived() {
    return customerIndividualsFamilyNameDerived;
  }

  public void setCustomerIndividualsFamilyNameDerived(String customerIndividualsFamilyNameDerived) {
    this.customerIndividualsFamilyNameDerived = customerIndividualsFamilyNameDerived;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Initials")
  public String getCustomerIndividualsInitials() {
    return customerIndividualsInitials;
  }

  public void setCustomerIndividualsInitials(String customerIndividualsInitials) {
    this.customerIndividualsInitials = customerIndividualsInitials;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqLoBCountryCode")
  public String getCustomerIndividualsEdqLoBCountryCode() {
    return customerIndividualsEdqLoBCountryCode;
  }

  public void setCustomerIndividualsEdqLoBCountryCode(String customerIndividualsEdqLoBCountryCode) {
    this.customerIndividualsEdqLoBCountryCode = customerIndividualsEdqLoBCountryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Country of Birth")
  public String getCustomerIndividualsCountryOfBirth() {
    return customerIndividualsCountryOfBirth;
  }

  public void setCustomerIndividualsCountryOfBirth(String customerIndividualsCountryOfBirth) {
    this.customerIndividualsCountryOfBirth = customerIndividualsCountryOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Address Country")
  public String getCustomerIndividualsAddressCountry() {
    return customerIndividualsAddressCountry;
  }

  public void setCustomerIndividualsAddressCountry(String customerIndividualsAddressCountry) {
    this.customerIndividualsAddressCountry = customerIndividualsAddressCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqAddressCountryCode")
  public String getCustomerIndividualsEdqAddressCountryCode() {
    return customerIndividualsEdqAddressCountryCode;
  }

  public void setCustomerIndividualsEdqAddressCountryCode(
      String customerIndividualsEdqAddressCountryCode) {
    this.customerIndividualsEdqAddressCountryCode = customerIndividualsEdqAddressCountryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Country of Residence")
  public String getCustomerIndividualsCountryOfResidence() {
    return customerIndividualsCountryOfResidence;
  }

  public void setCustomerIndividualsCountryOfResidence(
      String customerIndividualsCountryOfResidence) {
    this.customerIndividualsCountryOfResidence = customerIndividualsCountryOfResidence;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqBirthCountryCode")
  public String getCustomerIndividualsEdqBirthCountryCode() {
    return customerIndividualsEdqBirthCountryCode;
  }

  public void setCustomerIndividualsEdqBirthCountryCode(
      String customerIndividualsEdqBirthCountryCode) {
    this.customerIndividualsEdqBirthCountryCode = customerIndividualsEdqBirthCountryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqResidenceCountriesCode")
  public String getCustomerIndividualsEdqResidenceCountriesCode() {
    return customerIndividualsEdqResidenceCountriesCode;
  }

  public void setCustomerIndividualsEdqResidenceCountriesCode(
      String customerIndividualsEdqResidenceCountriesCode) {
    this.customerIndividualsEdqResidenceCountriesCode =
        customerIndividualsEdqResidenceCountriesCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Nationality or Citizenship")
  public String getCustomerIndividualsNationalityOrCitizenship() {
    return customerIndividualsNationalityOrCitizenship;
  }

  public void setCustomerIndividualsNationalityOrCitizenship(
      String customerIndividualsNationalityOrCitizenship) {
    this.customerIndividualsNationalityOrCitizenship = customerIndividualsNationalityOrCitizenship;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Nationality Countries")
  public String getCustomerIndividualsNationalityCountries() {
    return customerIndividualsNationalityCountries;
  }

  public void setCustomerIndividualsNationalityCountries(
      String customerIndividualsNationalityCountries) {
    this.customerIndividualsNationalityCountries = customerIndividualsNationalityCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Countries (All)")
  public String getCustomerIndividualsCountriesAll() {
    return customerIndividualsCountriesAll;
  }

  public void setCustomerIndividualsCountriesAll(String customerIndividualsCountriesAll) {
    this.customerIndividualsCountriesAll = customerIndividualsCountriesAll;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqCountriesAllCodes")
  public String getCustomerIndividualsEdqCountriesAllCodes() {
    return customerIndividualsEdqCountriesAllCodes;
  }

  public void setCustomerIndividualsEdqCountriesAllCodes(
      String customerIndividualsEdqCountriesAllCodes) {
    this.customerIndividualsEdqCountriesAllCodes = customerIndividualsEdqCountriesAllCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqEmployerAllCountries")
  public String getCustomerIndividualsEdqEmployerAllCountries() {
    return customerIndividualsEdqEmployerAllCountries;
  }

  public void setCustomerIndividualsEdqEmployerAllCountries(
      String customerIndividualsEdqEmployerAllCountries) {
    this.customerIndividualsEdqEmployerAllCountries = customerIndividualsEdqEmployerAllCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqEmployerAllCountriesCodes")
  public String getCustomerIndividualsEdqEmployerAllCountriesCodes() {
    return customerIndividualsEdqEmployerAllCountriesCodes;
  }

  public void setCustomerIndividualsEdqEmployerAllCountriesCodes(
      String customerIndividualsEdqEmployerAllCountriesCodes) {
    this.customerIndividualsEdqEmployerAllCountriesCodes =
        customerIndividualsEdqEmployerAllCountriesCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqLoB")
  public String getCustomerIndividualsEdqLoB() {
    return customerIndividualsEdqLoB;
  }

  public void setCustomerIndividualsEdqLoB(String customerIndividualsEdqLoB) {
    this.customerIndividualsEdqLoB = customerIndividualsEdqLoB;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqPermission")
  public String getCustomerIndividualsEdqPermission() {
    return customerIndividualsEdqPermission;
  }

  public void setCustomerIndividualsEdqPermission(String customerIndividualsEdqPermission) {
    this.customerIndividualsEdqPermission = customerIndividualsEdqPermission;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Tax ID")
  public String getCustomerIndividualsTaxID() {
    return customerIndividualsTaxID;
  }

  public void setCustomerIndividualsTaxID(String customerIndividualsTaxID) {
    this.customerIndividualsTaxID = customerIndividualsTaxID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Date of Birth")
  public String getCustomerIndividualsDateOfBirth() {
    return customerIndividualsDateOfBirth;
  }

  public void setCustomerIndividualsDateOfBirth(String customerIndividualsDateOfBirth) {
    this.customerIndividualsDateOfBirth = customerIndividualsDateOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.DOB (Original)")
  public String getCustomerIndividualsDOBOriginal() {
    return customerIndividualsDOBOriginal;
  }

  public void setCustomerIndividualsDOBOriginal(String customerIndividualsDOBOriginal) {
    this.customerIndividualsDOBOriginal = customerIndividualsDOBOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqDOBString")
  public String getCustomerIndividualsEdqDOBString() {
    return customerIndividualsEdqDOBString;
  }

  public void setCustomerIndividualsEdqDOBString(String customerIndividualsEdqDOBString) {
    this.customerIndividualsEdqDOBString = customerIndividualsEdqDOBString;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Year of Birth")
  public String getCustomerIndividualsYearOfBirth() {
    return customerIndividualsYearOfBirth;
  }

  public void setCustomerIndividualsYearOfBirth(String customerIndividualsYearOfBirth) {
    this.customerIndividualsYearOfBirth = customerIndividualsYearOfBirth;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.City")
  public String getCustomerIndividualsCity() {
    return customerIndividualsCity;
  }

  public void setCustomerIndividualsCity(String customerIndividualsCity) {
    this.customerIndividualsCity = customerIndividualsCity;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Postal Code")
  public String getCustomerIndividualsPostalCode() {
    return customerIndividualsPostalCode;
  }

  public void setCustomerIndividualsPostalCode(String customerIndividualsPostalCode) {
    this.customerIndividualsPostalCode = customerIndividualsPostalCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Full Address")
  public String getCustomerIndividualsProfileFullAddress() {
    return customerIndividualsProfileFullAddress;
  }

  public void setCustomerIndividualsProfileFullAddress(
      String customerIndividualsProfileFullAddress) {
    this.customerIndividualsProfileFullAddress = customerIndividualsProfileFullAddress;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Gender")
  public String getCustomerIndividualsGender() {
    return customerIndividualsGender;
  }

  public void setCustomerIndividualsGender(String customerIndividualsGender) {
    this.customerIndividualsGender = customerIndividualsGender;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Gender Derived Flag")
  public String getCustomerIndividualsGenderDerivedFlag() {
    return customerIndividualsGenderDerivedFlag;
  }

  public void setCustomerIndividualsGenderDerivedFlag(String customerIndividualsGenderDerivedFlag) {
    this.customerIndividualsGenderDerivedFlag = customerIndividualsGenderDerivedFlag;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Hyperlink")
  public String getCustomerIndividualsProfileHyperlink() {
    return customerIndividualsProfileHyperlink;
  }

  public void setCustomerIndividualsProfileHyperlink(String customerIndividualsProfileHyperlink) {
    this.customerIndividualsProfileHyperlink = customerIndividualsProfileHyperlink;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Search Hyperlink")
  public String getCustomerIndividualsSearchHyperlink() {
    return customerIndividualsSearchHyperlink;
  }

  public void setCustomerIndividualsSearchHyperlink(String customerIndividualsSearchHyperlink) {
    this.customerIndividualsSearchHyperlink = customerIndividualsSearchHyperlink;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqListKey")
  public String getCustomerIndividualsEdqListKey() {
    return customerIndividualsEdqListKey;
  }

  public void setCustomerIndividualsEdqListKey(String customerIndividualsEdqListKey) {
    this.customerIndividualsEdqListKey = customerIndividualsEdqListKey;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Passport Number")
  public String getCustomerIndividualsPassportNumber() {
    return customerIndividualsPassportNumber;
  }

  public void setCustomerIndividualsPassportNumber(String customerIndividualsPassportNumber) {
    this.customerIndividualsPassportNumber = customerIndividualsPassportNumber;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Passport Issue Country")
  public String getCustomerIndividualsPassportIssueCountry() {
    return customerIndividualsPassportIssueCountry;
  }

  public void setCustomerIndividualsPassportIssueCountry(
      String customerIndividualsPassportIssueCountry) {
    this.customerIndividualsPassportIssueCountry = customerIndividualsPassportIssueCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Social Security Number")
  public String getCustomerIndividualsSocialSecurityNumber() {
    return customerIndividualsSocialSecurityNumber;
  }

  public void setCustomerIndividualsSocialSecurityNumber(
      String customerIndividualsSocialSecurityNumber) {
    this.customerIndividualsSocialSecurityNumber = customerIndividualsSocialSecurityNumber;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqCloseOfBusinessDate")
  public String getCustomerIndividualsEdqCloseOfBusinessDate() {
    return customerIndividualsEdqCloseOfBusinessDate;
  }

  public void setCustomerIndividualsEdqCloseOfBusinessDate(
      String customerIndividualsEdqCloseOfBusinessDate) {
    this.customerIndividualsEdqCloseOfBusinessDate = customerIndividualsEdqCloseOfBusinessDate;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Occupation")
  public String getCustomerIndividualsProfileOccupation() {
    return customerIndividualsProfileOccupation;
  }

  public void setCustomerIndividualsProfileOccupation(String customerIndividualsProfileOccupation) {
    this.customerIndividualsProfileOccupation = customerIndividualsProfileOccupation;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Person or Business Indicator")
  public String getCustomerIndividualsPersonOrBusinessIndicator() {
    return customerIndividualsPersonOrBusinessIndicator;
  }

  public void setCustomerIndividualsPersonOrBusinessIndicator(
      String customerIndividualsPersonOrBusinessIndicator) {
    this.customerIndividualsPersonOrBusinessIndicator =
        customerIndividualsPersonOrBusinessIndicator;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Profile Name Type")
  public String getCustomerIndividualsProfileNameType() {
    return customerIndividualsProfileNameType;
  }

  public void setCustomerIndividualsProfileNameType(String customerIndividualsProfileNameType) {
    this.customerIndividualsProfileNameType = customerIndividualsProfileNameType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqPartyRoleTypeDescription")
  public String getCustomerIndividualsEdqPartyRoleTypeDescription() {
    return customerIndividualsEdqPartyRoleTypeDescription;
  }

  public void setCustomerIndividualsEdqPartyRoleTypeDescription(
      String customerIndividualsEdqPartyRoleTypeDescription) {
    this.customerIndividualsEdqPartyRoleTypeDescription =
        customerIndividualsEdqPartyRoleTypeDescription;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqPartyStatusCodeDescription")
  public String getCustomerIndividualsEdqPartyStatusCodeDescription() {
    return customerIndividualsEdqPartyStatusCodeDescription;
  }

  public void setCustomerIndividualsEdqPartyStatusCodeDescription(
      String customerIndividualsEdqPartyStatusCodeDescription) {
    this.customerIndividualsEdqPartyStatusCodeDescription =
        customerIndividualsEdqPartyStatusCodeDescription;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqDay1SpikeFlag")
  public String getCustomerIndividualsEdqDay1SpikeFlag() {
    return customerIndividualsEdqDay1SpikeFlag;
  }

  public void setCustomerIndividualsEdqDay1SpikeFlag(String customerIndividualsEdqDay1SpikeFlag) {
    this.customerIndividualsEdqDay1SpikeFlag = customerIndividualsEdqDay1SpikeFlag;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Original Script Name")
  public String getCustomerIndividualsOriginalScriptName() {
    return customerIndividualsOriginalScriptName;
  }

  public void setCustomerIndividualsOriginalScriptName(
      String customerIndividualsOriginalScriptName) {
    this.customerIndividualsOriginalScriptName = customerIndividualsOriginalScriptName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Address")
  public String getCustomerIndividualsAddress() {
    return customerIndividualsAddress;
  }

  public void setCustomerIndividualsAddress(String customerIndividualsAddress) {
    this.customerIndividualsAddress = customerIndividualsAddress;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Given Names (Original)")
  public String getCustomerIndividualsGivenNamesOriginal() {
    return customerIndividualsGivenNamesOriginal;
  }

  public void setCustomerIndividualsGivenNamesOriginal(
      String customerIndividualsGivenNamesOriginal) {
    this.customerIndividualsGivenNamesOriginal = customerIndividualsGivenNamesOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Surname")
  public String getCustomerIndividualsSurname() {
    return customerIndividualsSurname;
  }

  public void setCustomerIndividualsSurname(String customerIndividualsSurname) {
    this.customerIndividualsSurname = customerIndividualsSurname;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqScreeningMode")
  public String getCustomerIndividualsEdqScreeningMode() {
    return customerIndividualsEdqScreeningMode;
  }

  public void setCustomerIndividualsEdqScreeningMode(String customerIndividualsEdqScreeningMode) {
    this.customerIndividualsEdqScreeningMode = customerIndividualsEdqScreeningMode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.edqCaseKey")
  public String getCustomerIndividualsEdqCaseKey() {
    return customerIndividualsEdqCaseKey;
  }

  public void setCustomerIndividualsEdqCaseKey(String customerIndividualsEdqCaseKey) {
    this.customerIndividualsEdqCaseKey = customerIndividualsEdqCaseKey;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Address Type")
  public String getCustomerIndividualsAddressType() {
    return customerIndividualsAddressType;
  }

  public void setCustomerIndividualsAddressType(String customerIndividualsAddressType) {
    this.customerIndividualsAddressType = customerIndividualsAddressType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.SSC Codes")
  public String getCustomerIndividualsSSCCodes() {
    return customerIndividualsSSCCodes;
  }

  public void setCustomerIndividualsSSCCodes(String customerIndividualsSSCCodes) {
    this.customerIndividualsSSCCodes = customerIndividualsSSCCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.CTRP Fragment")
  public String getCustomerIndividualsCTRPFragment() {
    return customerIndividualsCTRPFragment;
  }

  public void setCustomerIndividualsCTRPFragment(String customerIndividualsCTRPFragment) {
    this.customerIndividualsCTRPFragment = customerIndividualsCTRPFragment;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Request User Name")
  public String getCustomerIndividualsRequestUserName() {
    return customerIndividualsRequestUserName;
  }

  public void setCustomerIndividualsRequestUserName(String customerIndividualsRequestUserName) {
    this.customerIndividualsRequestUserName = customerIndividualsRequestUserName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.ReqDT")
  public String getCustomerIndividualsReqDT() {
    return customerIndividualsReqDT;
  }

  public void setCustomerIndividualsReqDT(String customerIndividualsReqDT) {
    this.customerIndividualsReqDT = customerIndividualsReqDT;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Auto Discount Decision")
  public String getCustomerIndividualsAutoDiscountDecision() {
    return customerIndividualsAutoDiscountDecision;
  }

  public void setCustomerIndividualsAutoDiscountDecision(
      String customerIndividualsAutoDiscountDecision) {
    this.customerIndividualsAutoDiscountDecision = customerIndividualsAutoDiscountDecision;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.RecordType")
  public String getCustomerIndividualsRecordType() {
    return customerIndividualsRecordType;
  }

  public void setCustomerIndividualsRecordType(String customerIndividualsRecordType) {
    this.customerIndividualsRecordType = customerIndividualsRecordType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerIndividuals.Dummy")
  public String getCustomerIndividualsDummy() {
    return customerIndividualsDummy;
  }

  public void setCustomerIndividualsDummy(String customerIndividualsDummy) {
    this.customerIndividualsDummy = customerIndividualsDummy;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerIndividuals customerIndividuals = (CustomerIndividuals) o;
    return
        Objects.equals(customerIndividualsRecordId, customerIndividuals.customerIndividualsRecordId)
            &&
            Objects.equals(
                customerIndividualsInputStream, customerIndividuals.customerIndividualsInputStream)
            &&
            Objects.equals(
                customerIndividualsSourceSystemHistoryID,
                customerIndividuals.customerIndividualsSourceSystemHistoryID) &&
            Objects.equals(
                customerIndividualsCloseOfBusinessDate,
                customerIndividuals.customerIndividualsCloseOfBusinessDate) &&
            Objects.equals(
                customerIndividualsPartitionNumber,
                customerIndividuals.customerIndividualsPartitionNumber) &&
            Objects.equals(
                customerIndividualsSourceSystemIdentifier,
                customerIndividuals.customerIndividualsSourceSystemIdentifier) &&
            Objects.equals(
                customerIndividualsExternalProfileID,
                customerIndividuals.customerIndividualsExternalProfileID) &&
            Objects.equals(
                customerIndividualsConcatenatedProfileID,
                customerIndividuals.customerIndividualsConcatenatedProfileID) &&
            Objects.equals(
                customerIndividualsProfileType, customerIndividuals.customerIndividualsProfileType)
            &&
            Objects.equals(
                customerIndividualsPartyRoleTypeCode,
                customerIndividuals.customerIndividualsPartyRoleTypeCode) &&
            Objects.equals(
                customerIndividualsProfileStatus,
                customerIndividuals.customerIndividualsProfileStatus) &&
            Objects.equals(
                customerIndividualsProfileSegment,
                customerIndividuals.customerIndividualsProfileSegment) &&
            Objects.equals(
                customerIndividualsLoBName, customerIndividuals.customerIndividualsLoBName) &&
            Objects.equals(
                customerIndividualsLoBRegion, customerIndividuals.customerIndividualsLoBRegion) &&
            Objects.equals(
                customerIndividualsLoBCountry, customerIndividuals.customerIndividualsLoBCountry) &&
            Objects.equals(
                customerIndividualsHSBCLegalEntityCode,
                customerIndividuals.customerIndividualsHSBCLegalEntityCode) &&
            Objects.equals(
                customerIndividualsNameCategoryCode,
                customerIndividuals.customerIndividualsNameCategoryCode) &&
            Objects.equals(
                customerIndividualsNameTypeCode,
                customerIndividuals.customerIndividualsNameTypeCode) &&
            Objects.equals(
                customerIndividualsNameLanguageTypeCode,
                customerIndividuals.customerIndividualsNameLanguageTypeCode) &&
            Objects.equals(
                customerIndividualsGivenName, customerIndividuals.customerIndividualsGivenName) &&
            Objects.equals(
                customerIndividualsMiddleName, customerIndividuals.customerIndividualsMiddleName) &&
            Objects.equals(
                customerIndividualsFamilyNameOriginal,
                customerIndividuals.customerIndividualsFamilyNameOriginal) &&
            Objects.equals(
                customerIndividualsInitialsOriginal,
                customerIndividuals.customerIndividualsInitialsOriginal) &&
            Objects.equals(
                customerIndividualsProfileFullName,
                customerIndividuals.customerIndividualsProfileFullName) &&
            Objects.equals(
                customerIndividualsPEPIndicator,
                customerIndividuals.customerIndividualsPEPIndicator) &&
            Objects.equals(
                customerIndividualsSpecialCategoryCustomerIndicator,
                customerIndividuals.customerIndividualsSpecialCategoryCustomerIndicator) &&
            Objects.equals(
                customerIndividualsGenderCode, customerIndividuals.customerIndividualsGenderCode) &&
            Objects.equals(
                customerIndividualsBirthDate, customerIndividuals.customerIndividualsBirthDate) &&
            Objects.equals(
                customerIndividualsPlaceOfBirth,
                customerIndividuals.customerIndividualsPlaceOfBirth) &&
            Objects.equals(
                customerIndividualsTownOfBirth, customerIndividuals.customerIndividualsTownOfBirth)
            &&
            Objects.equals(
                customerIndividualsStateProvinceOrCountyOfBirth,
                customerIndividuals.customerIndividualsStateProvinceOrCountyOfBirth) &&
            Objects.equals(
                customerIndividualsCountryOfBirthOriginal,
                customerIndividuals.customerIndividualsCountryOfBirthOriginal) &&
            Objects.equals(
                customerIndividualsSourceAddressType,
                customerIndividuals.customerIndividualsSourceAddressType) &&
            Objects.equals(
                customerIndividualsAddressFormatCode,
                customerIndividuals.customerIndividualsAddressFormatCode) &&
            Objects.equals(
                customerIndividualsAddressLanguageTypeCode,
                customerIndividuals.customerIndividualsAddressLanguageTypeCode) &&
            Objects.equals(
                customerIndividualsSourceAddressLine1,
                customerIndividuals.customerIndividualsSourceAddressLine1) &&
            Objects.equals(
                customerIndividualsSourceAddressLine2,
                customerIndividuals.customerIndividualsSourceAddressLine2) &&
            Objects.equals(
                customerIndividualsSourceAddressLine3,
                customerIndividuals.customerIndividualsSourceAddressLine3) &&
            Objects.equals(
                customerIndividualsSourceAddressLine4,
                customerIndividuals.customerIndividualsSourceAddressLine4) &&
            Objects.equals(
                customerIndividualsSourceAddressLine5,
                customerIndividuals.customerIndividualsSourceAddressLine5) &&
            Objects.equals(
                customerIndividualsSourceAddressLine6,
                customerIndividuals.customerIndividualsSourceAddressLine6) &&
            Objects.equals(
                customerIndividualsSourceAddressLine7,
                customerIndividuals.customerIndividualsSourceAddressLine7) &&
            Objects.equals(
                customerIndividualsSourceAddressLine8,
                customerIndividuals.customerIndividualsSourceAddressLine8) &&
            Objects.equals(
                customerIndividualsSourceAddressLine9,
                customerIndividuals.customerIndividualsSourceAddressLine9) &&
            Objects.equals(
                customerIndividualsSourceAddressLine10,
                customerIndividuals.customerIndividualsSourceAddressLine10) &&
            Objects.equals(
                customerIndividualsSourcePostalCode,
                customerIndividuals.customerIndividualsSourcePostalCode) &&
            Objects.equals(
                customerIndividualsSourceCountry,
                customerIndividuals.customerIndividualsSourceCountry) &&
            Objects.equals(
                customerIndividualsIdentificationDocument1,
                customerIndividuals.customerIndividualsIdentificationDocument1) &&
            Objects.equals(
                customerIndividualsIdentificationDocument2,
                customerIndividuals.customerIndividualsIdentificationDocument2) &&
            Objects.equals(
                customerIndividualsIdentificationDocument3,
                customerIndividuals.customerIndividualsIdentificationDocument3) &&
            Objects.equals(
                customerIndividualsIdentificationDocument4,
                customerIndividuals.customerIndividualsIdentificationDocument4) &&
            Objects.equals(
                customerIndividualsIdentificationDocument5,
                customerIndividuals.customerIndividualsIdentificationDocument5) &&
            Objects.equals(
                customerIndividualsIdentificationDocument6,
                customerIndividuals.customerIndividualsIdentificationDocument6) &&
            Objects.equals(
                customerIndividualsIdentificationDocument7,
                customerIndividuals.customerIndividualsIdentificationDocument7) &&
            Objects.equals(
                customerIndividualsIdentificationDocument8,
                customerIndividuals.customerIndividualsIdentificationDocument8) &&
            Objects.equals(
                customerIndividualsIdentificationDocument9,
                customerIndividuals.customerIndividualsIdentificationDocument9) &&
            Objects.equals(
                customerIndividualsIdentificationDocument10,
                customerIndividuals.customerIndividualsIdentificationDocument10) &&
            Objects.equals(
                customerIndividualsResidenceCountries,
                customerIndividuals.customerIndividualsResidenceCountries) &&
            Objects.equals(
                customerIndividualsNationalityCitizenshipCountries,
                customerIndividuals.customerIndividualsNationalityCitizenshipCountries) &&
            Objects.equals(
                customerIndividualsEmployer1Details,
                customerIndividuals.customerIndividualsEmployer1Details) &&
            Objects.equals(
                customerIndividualsEmployer1Address1,
                customerIndividuals.customerIndividualsEmployer1Address1) &&
            Objects.equals(
                customerIndividualsEmployer1Address2,
                customerIndividuals.customerIndividualsEmployer1Address2) &&
            Objects.equals(
                customerIndividualsEmployer1Address3,
                customerIndividuals.customerIndividualsEmployer1Address3) &&
            Objects.equals(
                customerIndividualsEmployer2Details,
                customerIndividuals.customerIndividualsEmployer2Details) &&
            Objects.equals(
                customerIndividualsEmployer2Address1,
                customerIndividuals.customerIndividualsEmployer2Address1) &&
            Objects.equals(
                customerIndividualsEmployer2Address2,
                customerIndividuals.customerIndividualsEmployer2Address2) &&
            Objects.equals(
                customerIndividualsEmployer2Address3,
                customerIndividuals.customerIndividualsEmployer2Address3) &&
            Objects.equals(
                customerIndividualsEmployer3Details,
                customerIndividuals.customerIndividualsEmployer3Details) &&
            Objects.equals(
                customerIndividualsEmployer3Address1,
                customerIndividuals.customerIndividualsEmployer3Address1) &&
            Objects.equals(
                customerIndividualsEmployer3Address2,
                customerIndividuals.customerIndividualsEmployer3Address2) &&
            Objects.equals(
                customerIndividualsEmployer3Address3,
                customerIndividuals.customerIndividualsEmployer3Address3) &&
            Objects.equals(
                customerIndividualsEdqCustID, customerIndividuals.customerIndividualsEdqCustID) &&
            Objects.equals(
                customerIndividualsEdqCustSubID,
                customerIndividuals.customerIndividualsEdqCustSubID) &&
            Objects.equals(
                customerIndividualsTitleDerived,
                customerIndividuals.customerIndividualsTitleDerived) &&
            Objects.equals(
                customerIndividualsGivenNamesDerived,
                customerIndividuals.customerIndividualsGivenNamesDerived) &&
            Objects.equals(
                customerIndividualsFullName, customerIndividuals.customerIndividualsFullName) &&
            Objects.equals(
                customerIndividualsFamilyNameDerived,
                customerIndividuals.customerIndividualsFamilyNameDerived) &&
            Objects.equals(
                customerIndividualsInitials, customerIndividuals.customerIndividualsInitials) &&
            Objects.equals(
                customerIndividualsEdqLoBCountryCode,
                customerIndividuals.customerIndividualsEdqLoBCountryCode) &&
            Objects.equals(
                customerIndividualsCountryOfBirth,
                customerIndividuals.customerIndividualsCountryOfBirth) &&
            Objects.equals(
                customerIndividualsAddressCountry,
                customerIndividuals.customerIndividualsAddressCountry) &&
            Objects.equals(
                customerIndividualsEdqAddressCountryCode,
                customerIndividuals.customerIndividualsEdqAddressCountryCode) &&
            Objects.equals(
                customerIndividualsCountryOfResidence,
                customerIndividuals.customerIndividualsCountryOfResidence) &&
            Objects.equals(
                customerIndividualsEdqBirthCountryCode,
                customerIndividuals.customerIndividualsEdqBirthCountryCode) &&
            Objects.equals(
                customerIndividualsEdqResidenceCountriesCode,
                customerIndividuals.customerIndividualsEdqResidenceCountriesCode) &&
            Objects.equals(
                customerIndividualsNationalityOrCitizenship,
                customerIndividuals.customerIndividualsNationalityOrCitizenship) &&
            Objects.equals(
                customerIndividualsNationalityCountries,
                customerIndividuals.customerIndividualsNationalityCountries) &&
            Objects.equals(
                customerIndividualsCountriesAll,
                customerIndividuals.customerIndividualsCountriesAll) &&
            Objects.equals(
                customerIndividualsEdqCountriesAllCodes,
                customerIndividuals.customerIndividualsEdqCountriesAllCodes) &&
            Objects.equals(
                customerIndividualsEdqEmployerAllCountries,
                customerIndividuals.customerIndividualsEdqEmployerAllCountries) &&
            Objects.equals(
                customerIndividualsEdqEmployerAllCountriesCodes,
                customerIndividuals.customerIndividualsEdqEmployerAllCountriesCodes) &&
            Objects.equals(customerIndividualsEdqLoB, customerIndividuals.customerIndividualsEdqLoB)
            &&
            Objects.equals(
                customerIndividualsEdqPermission,
                customerIndividuals.customerIndividualsEdqPermission) &&
            Objects.equals(customerIndividualsTaxID, customerIndividuals.customerIndividualsTaxID)
            &&
            Objects.equals(
                customerIndividualsDateOfBirth, customerIndividuals.customerIndividualsDateOfBirth)
            &&
            Objects.equals(
                customerIndividualsDOBOriginal, customerIndividuals.customerIndividualsDOBOriginal)
            &&
            Objects.equals(
                customerIndividualsEdqDOBString,
                customerIndividuals.customerIndividualsEdqDOBString) &&
            Objects.equals(
                customerIndividualsYearOfBirth, customerIndividuals.customerIndividualsYearOfBirth)
            &&
            Objects.equals(customerIndividualsCity, customerIndividuals.customerIndividualsCity) &&
            Objects.equals(
                customerIndividualsPostalCode, customerIndividuals.customerIndividualsPostalCode) &&
            Objects.equals(
                customerIndividualsProfileFullAddress,
                customerIndividuals.customerIndividualsProfileFullAddress) &&
            Objects.equals(customerIndividualsGender, customerIndividuals.customerIndividualsGender)
            &&
            Objects.equals(
                customerIndividualsGenderDerivedFlag,
                customerIndividuals.customerIndividualsGenderDerivedFlag) &&
            Objects.equals(
                customerIndividualsProfileHyperlink,
                customerIndividuals.customerIndividualsProfileHyperlink) &&
            Objects.equals(
                customerIndividualsSearchHyperlink,
                customerIndividuals.customerIndividualsSearchHyperlink) &&
            Objects.equals(
                customerIndividualsEdqListKey, customerIndividuals.customerIndividualsEdqListKey) &&
            Objects.equals(
                customerIndividualsPassportNumber,
                customerIndividuals.customerIndividualsPassportNumber) &&
            Objects.equals(
                customerIndividualsPassportIssueCountry,
                customerIndividuals.customerIndividualsPassportIssueCountry) &&
            Objects.equals(
                customerIndividualsSocialSecurityNumber,
                customerIndividuals.customerIndividualsSocialSecurityNumber) &&
            Objects.equals(
                customerIndividualsEdqCloseOfBusinessDate,
                customerIndividuals.customerIndividualsEdqCloseOfBusinessDate) &&
            Objects.equals(
                customerIndividualsProfileOccupation,
                customerIndividuals.customerIndividualsProfileOccupation) &&
            Objects.equals(
                customerIndividualsPersonOrBusinessIndicator,
                customerIndividuals.customerIndividualsPersonOrBusinessIndicator) &&
            Objects.equals(
                customerIndividualsProfileNameType,
                customerIndividuals.customerIndividualsProfileNameType) &&
            Objects.equals(
                customerIndividualsEdqPartyRoleTypeDescription,
                customerIndividuals.customerIndividualsEdqPartyRoleTypeDescription) &&
            Objects.equals(
                customerIndividualsEdqPartyStatusCodeDescription,
                customerIndividuals.customerIndividualsEdqPartyStatusCodeDescription) &&
            Objects.equals(
                customerIndividualsEdqDay1SpikeFlag,
                customerIndividuals.customerIndividualsEdqDay1SpikeFlag) &&
            Objects.equals(
                customerIndividualsOriginalScriptName,
                customerIndividuals.customerIndividualsOriginalScriptName) &&
            Objects.equals(
                customerIndividualsAddress, customerIndividuals.customerIndividualsAddress) &&
            Objects.equals(
                customerIndividualsGivenNamesOriginal,
                customerIndividuals.customerIndividualsGivenNamesOriginal) &&
            Objects.equals(
                customerIndividualsSurname, customerIndividuals.customerIndividualsSurname) &&
            Objects.equals(
                customerIndividualsEdqScreeningMode,
                customerIndividuals.customerIndividualsEdqScreeningMode) &&
            Objects.equals(
                customerIndividualsEdqCaseKey, customerIndividuals.customerIndividualsEdqCaseKey) &&
            Objects.equals(
                customerIndividualsAddressType, customerIndividuals.customerIndividualsAddressType)
            &&
            Objects.equals(
                customerIndividualsSSCCodes, customerIndividuals.customerIndividualsSSCCodes) &&
            Objects.equals(
                customerIndividualsCTRPFragment,
                customerIndividuals.customerIndividualsCTRPFragment) &&
            Objects.equals(
                customerIndividualsRequestUserName,
                customerIndividuals.customerIndividualsRequestUserName) &&
            Objects.equals(customerIndividualsReqDT, customerIndividuals.customerIndividualsReqDT)
            &&
            Objects.equals(
                customerIndividualsAutoDiscountDecision,
                customerIndividuals.customerIndividualsAutoDiscountDecision) &&
            Objects.equals(
                customerIndividualsRecordType, customerIndividuals.customerIndividualsRecordType) &&
            Objects.equals(customerIndividualsDummy, customerIndividuals.customerIndividualsDummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        customerIndividualsRecordId, customerIndividualsInputStream,
        customerIndividualsSourceSystemHistoryID, customerIndividualsCloseOfBusinessDate,
        customerIndividualsPartitionNumber, customerIndividualsSourceSystemIdentifier,
        customerIndividualsExternalProfileID, customerIndividualsConcatenatedProfileID,
        customerIndividualsProfileType, customerIndividualsPartyRoleTypeCode,
        customerIndividualsProfileStatus, customerIndividualsProfileSegment,
        customerIndividualsLoBName, customerIndividualsLoBRegion, customerIndividualsLoBCountry,
        customerIndividualsHSBCLegalEntityCode, customerIndividualsNameCategoryCode,
        customerIndividualsNameTypeCode, customerIndividualsNameLanguageTypeCode,
        customerIndividualsGivenName, customerIndividualsMiddleName,
        customerIndividualsFamilyNameOriginal, customerIndividualsInitialsOriginal,
        customerIndividualsProfileFullName, customerIndividualsPEPIndicator,
        customerIndividualsSpecialCategoryCustomerIndicator, customerIndividualsGenderCode,
        customerIndividualsBirthDate, customerIndividualsPlaceOfBirth,
        customerIndividualsTownOfBirth, customerIndividualsStateProvinceOrCountyOfBirth,
        customerIndividualsCountryOfBirthOriginal, customerIndividualsSourceAddressType,
        customerIndividualsAddressFormatCode, customerIndividualsAddressLanguageTypeCode,
        customerIndividualsSourceAddressLine1, customerIndividualsSourceAddressLine2,
        customerIndividualsSourceAddressLine3, customerIndividualsSourceAddressLine4,
        customerIndividualsSourceAddressLine5, customerIndividualsSourceAddressLine6,
        customerIndividualsSourceAddressLine7, customerIndividualsSourceAddressLine8,
        customerIndividualsSourceAddressLine9, customerIndividualsSourceAddressLine10,
        customerIndividualsSourcePostalCode, customerIndividualsSourceCountry,
        customerIndividualsIdentificationDocument1, customerIndividualsIdentificationDocument2,
        customerIndividualsIdentificationDocument3, customerIndividualsIdentificationDocument4,
        customerIndividualsIdentificationDocument5, customerIndividualsIdentificationDocument6,
        customerIndividualsIdentificationDocument7, customerIndividualsIdentificationDocument8,
        customerIndividualsIdentificationDocument9, customerIndividualsIdentificationDocument10,
        customerIndividualsResidenceCountries, customerIndividualsNationalityCitizenshipCountries,
        customerIndividualsEmployer1Details, customerIndividualsEmployer1Address1,
        customerIndividualsEmployer1Address2, customerIndividualsEmployer1Address3,
        customerIndividualsEmployer2Details, customerIndividualsEmployer2Address1,
        customerIndividualsEmployer2Address2, customerIndividualsEmployer2Address3,
        customerIndividualsEmployer3Details, customerIndividualsEmployer3Address1,
        customerIndividualsEmployer3Address2, customerIndividualsEmployer3Address3,
        customerIndividualsEdqCustID, customerIndividualsEdqCustSubID,
        customerIndividualsTitleDerived, customerIndividualsGivenNamesDerived,
        customerIndividualsFullName, customerIndividualsFamilyNameDerived,
        customerIndividualsInitials, customerIndividualsEdqLoBCountryCode,
        customerIndividualsCountryOfBirth, customerIndividualsAddressCountry,
        customerIndividualsEdqAddressCountryCode, customerIndividualsCountryOfResidence,
        customerIndividualsEdqBirthCountryCode, customerIndividualsEdqResidenceCountriesCode,
        customerIndividualsNationalityOrCitizenship, customerIndividualsNationalityCountries,
        customerIndividualsCountriesAll, customerIndividualsEdqCountriesAllCodes,
        customerIndividualsEdqEmployerAllCountries, customerIndividualsEdqEmployerAllCountriesCodes,
        customerIndividualsEdqLoB, customerIndividualsEdqPermission, customerIndividualsTaxID,
        customerIndividualsDateOfBirth, customerIndividualsDOBOriginal,
        customerIndividualsEdqDOBString, customerIndividualsYearOfBirth, customerIndividualsCity,
        customerIndividualsPostalCode, customerIndividualsProfileFullAddress,
        customerIndividualsGender, customerIndividualsGenderDerivedFlag,
        customerIndividualsProfileHyperlink, customerIndividualsSearchHyperlink,
        customerIndividualsEdqListKey, customerIndividualsPassportNumber,
        customerIndividualsPassportIssueCountry, customerIndividualsSocialSecurityNumber,
        customerIndividualsEdqCloseOfBusinessDate, customerIndividualsProfileOccupation,
        customerIndividualsPersonOrBusinessIndicator, customerIndividualsProfileNameType,
        customerIndividualsEdqPartyRoleTypeDescription,
        customerIndividualsEdqPartyStatusCodeDescription, customerIndividualsEdqDay1SpikeFlag,
        customerIndividualsOriginalScriptName, customerIndividualsAddress,
        customerIndividualsGivenNamesOriginal, customerIndividualsSurname,
        customerIndividualsEdqScreeningMode, customerIndividualsEdqCaseKey,
        customerIndividualsAddressType, customerIndividualsSSCCodes,
        customerIndividualsCTRPFragment, customerIndividualsRequestUserName,
        customerIndividualsReqDT, customerIndividualsAutoDiscountDecision,
        customerIndividualsRecordType, customerIndividualsDummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerIndividuals {\n");

    sb
        .append("    customerIndividualsRecordId: ")
        .append(toIndentedString(customerIndividualsRecordId))
        .append("\n");
    sb
        .append("    customerIndividualsInputStream: ")
        .append(toIndentedString(customerIndividualsInputStream))
        .append("\n");
    sb
        .append("    customerIndividualsSourceSystemHistoryID: ")
        .append(toIndentedString(customerIndividualsSourceSystemHistoryID))
        .append("\n");
    sb
        .append("    customerIndividualsCloseOfBusinessDate: ")
        .append(toIndentedString(customerIndividualsCloseOfBusinessDate))
        .append("\n");
    sb
        .append("    customerIndividualsPartitionNumber: ")
        .append(toIndentedString(customerIndividualsPartitionNumber))
        .append("\n");
    sb
        .append("    customerIndividualsSourceSystemIdentifier: ")
        .append(toIndentedString(customerIndividualsSourceSystemIdentifier))
        .append("\n");
    sb
        .append("    customerIndividualsExternalProfileID: ")
        .append(toIndentedString(customerIndividualsExternalProfileID))
        .append("\n");
    sb
        .append("    customerIndividualsConcatenatedProfileID: ")
        .append(toIndentedString(customerIndividualsConcatenatedProfileID))
        .append("\n");
    sb
        .append("    customerIndividualsProfileType: ")
        .append(toIndentedString(customerIndividualsProfileType))
        .append("\n");
    sb
        .append("    customerIndividualsPartyRoleTypeCode: ")
        .append(toIndentedString(customerIndividualsPartyRoleTypeCode))
        .append("\n");
    sb
        .append("    customerIndividualsProfileStatus: ")
        .append(toIndentedString(customerIndividualsProfileStatus))
        .append("\n");
    sb
        .append("    customerIndividualsProfileSegment: ")
        .append(toIndentedString(customerIndividualsProfileSegment))
        .append("\n");
    sb
        .append("    customerIndividualsLoBName: ")
        .append(toIndentedString(customerIndividualsLoBName))
        .append("\n");
    sb
        .append("    customerIndividualsLoBRegion: ")
        .append(toIndentedString(customerIndividualsLoBRegion))
        .append("\n");
    sb
        .append("    customerIndividualsLoBCountry: ")
        .append(toIndentedString(customerIndividualsLoBCountry))
        .append("\n");
    sb
        .append("    customerIndividualsHSBCLegalEntityCode: ")
        .append(toIndentedString(customerIndividualsHSBCLegalEntityCode))
        .append("\n");
    sb
        .append("    customerIndividualsNameCategoryCode: ")
        .append(toIndentedString(customerIndividualsNameCategoryCode))
        .append("\n");
    sb
        .append("    customerIndividualsNameTypeCode: ")
        .append(toIndentedString(customerIndividualsNameTypeCode))
        .append("\n");
    sb
        .append("    customerIndividualsNameLanguageTypeCode: ")
        .append(toIndentedString(customerIndividualsNameLanguageTypeCode))
        .append("\n");
    sb
        .append("    customerIndividualsGivenName: ")
        .append(toIndentedString(customerIndividualsGivenName))
        .append("\n");
    sb
        .append("    customerIndividualsMiddleName: ")
        .append(toIndentedString(customerIndividualsMiddleName))
        .append("\n");
    sb
        .append("    customerIndividualsFamilyNameOriginal: ")
        .append(toIndentedString(customerIndividualsFamilyNameOriginal))
        .append("\n");
    sb
        .append("    customerIndividualsInitialsOriginal: ")
        .append(toIndentedString(customerIndividualsInitialsOriginal))
        .append("\n");
    sb
        .append("    customerIndividualsProfileFullName: ")
        .append(toIndentedString(customerIndividualsProfileFullName))
        .append("\n");
    sb
        .append("    customerIndividualsPEPIndicator: ")
        .append(toIndentedString(customerIndividualsPEPIndicator))
        .append("\n");
    sb
        .append("    customerIndividualsSpecialCategoryCustomerIndicator: ")
        .append(toIndentedString(customerIndividualsSpecialCategoryCustomerIndicator))
        .append("\n");
    sb
        .append("    customerIndividualsGenderCode: ")
        .append(toIndentedString(customerIndividualsGenderCode))
        .append("\n");
    sb
        .append("    customerIndividualsBirthDate: ")
        .append(toIndentedString(customerIndividualsBirthDate))
        .append("\n");
    sb
        .append("    customerIndividualsPlaceOfBirth: ")
        .append(toIndentedString(customerIndividualsPlaceOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsTownOfBirth: ")
        .append(toIndentedString(customerIndividualsTownOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsStateProvinceOrCountyOfBirth: ")
        .append(toIndentedString(customerIndividualsStateProvinceOrCountyOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsCountryOfBirthOriginal: ")
        .append(toIndentedString(customerIndividualsCountryOfBirthOriginal))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressType: ")
        .append(toIndentedString(customerIndividualsSourceAddressType))
        .append("\n");
    sb
        .append("    customerIndividualsAddressFormatCode: ")
        .append(toIndentedString(customerIndividualsAddressFormatCode))
        .append("\n");
    sb
        .append("    customerIndividualsAddressLanguageTypeCode: ")
        .append(toIndentedString(customerIndividualsAddressLanguageTypeCode))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine1: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine1))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine2: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine2))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine3: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine3))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine4: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine4))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine5: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine5))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine6: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine6))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine7: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine7))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine8: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine8))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine9: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine9))
        .append("\n");
    sb
        .append("    customerIndividualsSourceAddressLine10: ")
        .append(toIndentedString(customerIndividualsSourceAddressLine10))
        .append("\n");
    sb
        .append("    customerIndividualsSourcePostalCode: ")
        .append(toIndentedString(customerIndividualsSourcePostalCode))
        .append("\n");
    sb
        .append("    customerIndividualsSourceCountry: ")
        .append(toIndentedString(customerIndividualsSourceCountry))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument1: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument1))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument2: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument2))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument3: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument3))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument4: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument4))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument5: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument5))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument6: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument6))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument7: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument7))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument8: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument8))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument9: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument9))
        .append("\n");
    sb
        .append("    customerIndividualsIdentificationDocument10: ")
        .append(toIndentedString(customerIndividualsIdentificationDocument10))
        .append("\n");
    sb
        .append("    customerIndividualsResidenceCountries: ")
        .append(toIndentedString(customerIndividualsResidenceCountries))
        .append("\n");
    sb
        .append("    customerIndividualsNationalityCitizenshipCountries: ")
        .append(toIndentedString(customerIndividualsNationalityCitizenshipCountries))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer1Details: ")
        .append(toIndentedString(customerIndividualsEmployer1Details))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer1Address1: ")
        .append(toIndentedString(customerIndividualsEmployer1Address1))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer1Address2: ")
        .append(toIndentedString(customerIndividualsEmployer1Address2))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer1Address3: ")
        .append(toIndentedString(customerIndividualsEmployer1Address3))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer2Details: ")
        .append(toIndentedString(customerIndividualsEmployer2Details))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer2Address1: ")
        .append(toIndentedString(customerIndividualsEmployer2Address1))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer2Address2: ")
        .append(toIndentedString(customerIndividualsEmployer2Address2))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer2Address3: ")
        .append(toIndentedString(customerIndividualsEmployer2Address3))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer3Details: ")
        .append(toIndentedString(customerIndividualsEmployer3Details))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer3Address1: ")
        .append(toIndentedString(customerIndividualsEmployer3Address1))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer3Address2: ")
        .append(toIndentedString(customerIndividualsEmployer3Address2))
        .append("\n");
    sb
        .append("    customerIndividualsEmployer3Address3: ")
        .append(toIndentedString(customerIndividualsEmployer3Address3))
        .append("\n");
    sb
        .append("    customerIndividualsEdqCustID: ")
        .append(toIndentedString(customerIndividualsEdqCustID))
        .append("\n");
    sb
        .append("    customerIndividualsEdqCustSubID: ")
        .append(toIndentedString(customerIndividualsEdqCustSubID))
        .append("\n");
    sb
        .append("    customerIndividualsTitleDerived: ")
        .append(toIndentedString(customerIndividualsTitleDerived))
        .append("\n");
    sb
        .append("    customerIndividualsGivenNamesDerived: ")
        .append(toIndentedString(customerIndividualsGivenNamesDerived))
        .append("\n");
    sb
        .append("    customerIndividualsFullName: ")
        .append(toIndentedString(customerIndividualsFullName))
        .append("\n");
    sb
        .append("    customerIndividualsFamilyNameDerived: ")
        .append(toIndentedString(customerIndividualsFamilyNameDerived))
        .append("\n");
    sb
        .append("    customerIndividualsInitials: ")
        .append(toIndentedString(customerIndividualsInitials))
        .append("\n");
    sb
        .append("    customerIndividualsEdqLoBCountryCode: ")
        .append(toIndentedString(customerIndividualsEdqLoBCountryCode))
        .append("\n");
    sb
        .append("    customerIndividualsCountryOfBirth: ")
        .append(toIndentedString(customerIndividualsCountryOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsAddressCountry: ")
        .append(toIndentedString(customerIndividualsAddressCountry))
        .append("\n");
    sb
        .append("    customerIndividualsEdqAddressCountryCode: ")
        .append(toIndentedString(customerIndividualsEdqAddressCountryCode))
        .append("\n");
    sb
        .append("    customerIndividualsCountryOfResidence: ")
        .append(toIndentedString(customerIndividualsCountryOfResidence))
        .append("\n");
    sb
        .append("    customerIndividualsEdqBirthCountryCode: ")
        .append(toIndentedString(customerIndividualsEdqBirthCountryCode))
        .append("\n");
    sb
        .append("    customerIndividualsEdqResidenceCountriesCode: ")
        .append(toIndentedString(customerIndividualsEdqResidenceCountriesCode))
        .append("\n");
    sb
        .append("    customerIndividualsNationalityOrCitizenship: ")
        .append(toIndentedString(customerIndividualsNationalityOrCitizenship))
        .append("\n");
    sb
        .append("    customerIndividualsNationalityCountries: ")
        .append(toIndentedString(customerIndividualsNationalityCountries))
        .append("\n");
    sb
        .append("    customerIndividualsCountriesAll: ")
        .append(toIndentedString(customerIndividualsCountriesAll))
        .append("\n");
    sb
        .append("    customerIndividualsEdqCountriesAllCodes: ")
        .append(toIndentedString(customerIndividualsEdqCountriesAllCodes))
        .append("\n");
    sb
        .append("    customerIndividualsEdqEmployerAllCountries: ")
        .append(toIndentedString(customerIndividualsEdqEmployerAllCountries))
        .append("\n");
    sb
        .append("    customerIndividualsEdqEmployerAllCountriesCodes: ")
        .append(toIndentedString(customerIndividualsEdqEmployerAllCountriesCodes))
        .append("\n");
    sb
        .append("    customerIndividualsEdqLoB: ")
        .append(toIndentedString(customerIndividualsEdqLoB))
        .append("\n");
    sb
        .append("    customerIndividualsEdqPermission: ")
        .append(toIndentedString(customerIndividualsEdqPermission))
        .append("\n");
    sb
        .append("    customerIndividualsTaxID: ")
        .append(toIndentedString(customerIndividualsTaxID))
        .append("\n");
    sb
        .append("    customerIndividualsDateOfBirth: ")
        .append(toIndentedString(customerIndividualsDateOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsDOBOriginal: ")
        .append(toIndentedString(customerIndividualsDOBOriginal))
        .append("\n");
    sb
        .append("    customerIndividualsEdqDOBString: ")
        .append(toIndentedString(customerIndividualsEdqDOBString))
        .append("\n");
    sb
        .append("    customerIndividualsYearOfBirth: ")
        .append(toIndentedString(customerIndividualsYearOfBirth))
        .append("\n");
    sb
        .append("    customerIndividualsCity: ")
        .append(toIndentedString(customerIndividualsCity))
        .append("\n");
    sb
        .append("    customerIndividualsPostalCode: ")
        .append(toIndentedString(customerIndividualsPostalCode))
        .append("\n");
    sb
        .append("    customerIndividualsProfileFullAddress: ")
        .append(toIndentedString(customerIndividualsProfileFullAddress))
        .append("\n");
    sb
        .append("    customerIndividualsGender: ")
        .append(toIndentedString(customerIndividualsGender))
        .append("\n");
    sb
        .append("    customerIndividualsGenderDerivedFlag: ")
        .append(toIndentedString(customerIndividualsGenderDerivedFlag))
        .append("\n");
    sb
        .append("    customerIndividualsProfileHyperlink: ")
        .append(toIndentedString(customerIndividualsProfileHyperlink))
        .append("\n");
    sb
        .append("    customerIndividualsSearchHyperlink: ")
        .append(toIndentedString(customerIndividualsSearchHyperlink))
        .append("\n");
    sb
        .append("    customerIndividualsEdqListKey: ")
        .append(toIndentedString(customerIndividualsEdqListKey))
        .append("\n");
    sb
        .append("    customerIndividualsPassportNumber: ")
        .append(toIndentedString(customerIndividualsPassportNumber))
        .append("\n");
    sb
        .append("    customerIndividualsPassportIssueCountry: ")
        .append(toIndentedString(customerIndividualsPassportIssueCountry))
        .append("\n");
    sb
        .append("    customerIndividualsSocialSecurityNumber: ")
        .append(toIndentedString(customerIndividualsSocialSecurityNumber))
        .append("\n");
    sb
        .append("    customerIndividualsEdqCloseOfBusinessDate: ")
        .append(toIndentedString(customerIndividualsEdqCloseOfBusinessDate))
        .append("\n");
    sb
        .append("    customerIndividualsProfileOccupation: ")
        .append(toIndentedString(customerIndividualsProfileOccupation))
        .append("\n");
    sb
        .append("    customerIndividualsPersonOrBusinessIndicator: ")
        .append(toIndentedString(customerIndividualsPersonOrBusinessIndicator))
        .append("\n");
    sb
        .append("    customerIndividualsProfileNameType: ")
        .append(toIndentedString(customerIndividualsProfileNameType))
        .append("\n");
    sb
        .append("    customerIndividualsEdqPartyRoleTypeDescription: ")
        .append(toIndentedString(customerIndividualsEdqPartyRoleTypeDescription))
        .append("\n");
    sb
        .append("    customerIndividualsEdqPartyStatusCodeDescription: ")
        .append(toIndentedString(customerIndividualsEdqPartyStatusCodeDescription))
        .append("\n");
    sb
        .append("    customerIndividualsEdqDay1SpikeFlag: ")
        .append(toIndentedString(customerIndividualsEdqDay1SpikeFlag))
        .append("\n");
    sb
        .append("    customerIndividualsOriginalScriptName: ")
        .append(toIndentedString(customerIndividualsOriginalScriptName))
        .append("\n");
    sb
        .append("    customerIndividualsAddress: ")
        .append(toIndentedString(customerIndividualsAddress))
        .append("\n");
    sb
        .append("    customerIndividualsGivenNamesOriginal: ")
        .append(toIndentedString(customerIndividualsGivenNamesOriginal))
        .append("\n");
    sb
        .append("    customerIndividualsSurname: ")
        .append(toIndentedString(customerIndividualsSurname))
        .append("\n");
    sb
        .append("    customerIndividualsEdqScreeningMode: ")
        .append(toIndentedString(customerIndividualsEdqScreeningMode))
        .append("\n");
    sb
        .append("    customerIndividualsEdqCaseKey: ")
        .append(toIndentedString(customerIndividualsEdqCaseKey))
        .append("\n");
    sb
        .append("    customerIndividualsAddressType: ")
        .append(toIndentedString(customerIndividualsAddressType))
        .append("\n");
    sb
        .append("    customerIndividualsSSCCodes: ")
        .append(toIndentedString(customerIndividualsSSCCodes))
        .append("\n");
    sb
        .append("    customerIndividualsCTRPFragment: ")
        .append(toIndentedString(customerIndividualsCTRPFragment))
        .append("\n");
    sb
        .append("    customerIndividualsRequestUserName: ")
        .append(toIndentedString(customerIndividualsRequestUserName))
        .append("\n");
    sb
        .append("    customerIndividualsReqDT: ")
        .append(toIndentedString(customerIndividualsReqDT))
        .append("\n");
    sb
        .append("    customerIndividualsAutoDiscountDecision: ")
        .append(toIndentedString(customerIndividualsAutoDiscountDecision))
        .append("\n");
    sb
        .append("    customerIndividualsRecordType: ")
        .append(toIndentedString(customerIndividualsRecordType))
        .append("\n");
    sb
        .append("    customerIndividualsDummy: ")
        .append(toIndentedString(customerIndividualsDummy))
        .append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
