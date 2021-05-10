package com.silenteight.hsbc.bridge.json.external.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen", date = "2021-04-30T09:49:39.903Z[GMT]")
public class CustomerEntities {

  private String customerEntitiesRecordId = null;
  private String customerEntitiesInputStream = null;
  private String customerEntitiesSourceSystemHistoryID = null;
  private String customerEntitiesCloseOfBusinessDate = null;
  private String customerEntitiesPartitionNumber = null;
  private String customerEntitiesSourceSystemIdentifier = null;
  private String customerEntitiesExternalProfileID = null;
  private String customerEntitiesConcatenatedProfileID = null;
  private String customerEntitiesProfileType = null;
  private String customerEntitiesPartyRoleTypeCode = null;
  private String customerEntitiesProfileStatus = null;
  private String customerEntitiesProfileSegment = null;
  private String customerEntitiesLoBName = null;
  private String customerEntitiesLoBRegion = null;
  private String customerEntitiesLoBCountry = null;
  private String customerEntitiesHSBCLegalEntityCode = null;
  private String customerEntitiesNameCategoryCode = null;
  private String customerEntitiesNameTypeCode = null;
  private String customerEntitiesNameLanguageTypeCode = null;
  private String customerEntitiesEntityNameOriginal = null;
  private String customerEntitiesPEPIndicator = null;
  private String customerEntitiesSpecialCategoryCustomer = null;
  private String customerEntitiesSourceAddressType = null;
  private String customerEntitiesAddressFormatCode = null;
  private String customerEntitiesAddressLanguageTypeCode = null;
  private String customerEntitiesSourceAddressLine1 = null;
  private String customerEntitiesSourceAddressLine2 = null;
  private String customerEntitiesSourceAddressLine3 = null;
  private String customerEntitiesSourceAddressLine4 = null;
  private String customerEntitiesSourceAddressLine5 = null;
  private String customerEntitiesSourceAddressLine6 = null;
  private String customerEntitiesSourceAddressLine7 = null;
  private String customerEntitiesSourceAddressLine8 = null;
  private String customerEntitiesSourceAddressLine9 = null;
  private String customerEntitiesSourceAddressLine10 = null;
  private String customerEntitiesSourcePostalCode = null;
  private String customerEntitiesSourceAddressCountry = null;
  private String customerEntitiesIdentificationDocument1 = null;
  private String customerEntitiesIdentificationDocument2 = null;
  private String customerEntitiesIdentificationDocument3 = null;
  private String customerEntitiesIdentificationDocument4 = null;
  private String customerEntitiesIdentificationDocument5 = null;
  private String customerEntitiesIdentificationDocument6 = null;
  private String customerEntitiesIdentificationDocument7 = null;
  private String customerEntitiesIdentificationDocument8 = null;
  private String customerEntitiesIdentificationDocument9 = null;
  private String customerEntitiesIdentificationDocument10 = null;
  private String customerEntitiesTradesWithCountries = null;
  private String customerEntitiesSubsidiariesOperatesInCountries = null;
  private String customerEntitiesExportCountries = null;
  private String customerEntitiesImportCountries = null;
  private String customerEntitiesCountriesOfIncorporation = null;
  private String customerEntitiesCountriesOfRegistrationOriginal = null;
  private String customerEntitiesCountriesOfBusiness = null;
  private String customerEntitiesCountriesOfHeadOffice = null;
  private String customerEntitiesEdqCustID = null;
  private String customerEntitiesEdqCustSubID = null;
  private String customerEntitiesEdqLoBCountryCode = null;
  private String customerEntitiesAddressCountry = null;
  private String customerEntitiesEdqAddressCountryCode = null;
  private String customerEntitiesEdqTradesWithCountries = null;
  private String customerEntitiesEdqExportsCountries = null;
  private String customerEntitiesEdqImportsCountries = null;
  private String customerEntitiesEdqIncorporationCountries = null;
  private String customerEntitiesEdqIncorporationCountriesCodes = null;
  private String customerEntitiesRegistrationCountry = null;
  private String customerEntitiesEdqRegiistrationCountriesCodes = null;
  private String customerEntitiesEdqBusinessCountries = null;
  private String customerEntitiesEdqHeadOfficeCountries = null;
  private String customerEntitiesCountriesAll = null;
  private String customerEntitiesCountriesAllCodes = null;
  private String customerEntitiesEdqLoB = null;
  private String customerEntitiesEdqPermission = null;
  private String customerEntitiesTaxID = null;
  private String customerEntitiesCity = null;
  private String customerEntitiesProfileHyperlink = null;
  private String customerEntitiesSearchHyperlink = null;
  private String customerEntitiesEdqListKey = null;
  private String customerEntitiesAddress = null;
  private String customerEntitiesEntityName = null;
  private String customerEntitiesPostalCode = null;
  private String customerEntitiesPersonOrBusinessIndicator = null;
  private String customerEntitiesEdqCloseOfBusinessDate = null;
  private String customerEntitiesOperatingCountries = null;
  private String customerEntitiesEdqOperatingCountriesCodes = null;
  private String customerEntitiesRegistrationNumber = null;
  private String customerEntitiesProfileNameType = null;
  private String customerEntitiesEdqPartyRoleTypeDescription = null;
  private String customerEntitiesEdqPartyStatusCodeDescription = null;
  private String customerEntitiesEdqDay1SpikeFlag = null;
  private String customerEntitiesOriginalScriptName = null;
  private String customerEntitiesProfileFullAddress = null;
  private String customerEntitiesEdqScreeningMode = null;
  private String customerEntitiesEdqCaseKey = null;
  private String customerEntitiesAddressType = null;
  private String customerEntitiesSSCCodes = null;
  private String customerEntitiesCTRPFragment = null;
  private String customerEntitiesRequestUserName = null;
  private String customerEntitiesRequestDateTime = null;

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Record Id")
  public String getCustomerEntitiesRecordId() {
    return customerEntitiesRecordId;
  }

  public void setCustomerEntitiesRecordId(String customerEntitiesRecordId) {
    this.customerEntitiesRecordId = customerEntitiesRecordId;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Input Stream")
  public String getCustomerEntitiesInputStream() {
    return customerEntitiesInputStream;
  }

  public void setCustomerEntitiesInputStream(String customerEntitiesInputStream) {
    this.customerEntitiesInputStream = customerEntitiesInputStream;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source System History ID")
  public String getCustomerEntitiesSourceSystemHistoryID() {
    return customerEntitiesSourceSystemHistoryID;
  }

  public void setCustomerEntitiesSourceSystemHistoryID(
      String customerEntitiesSourceSystemHistoryID) {
    this.customerEntitiesSourceSystemHistoryID = customerEntitiesSourceSystemHistoryID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Close Of Business Date")
  public String getCustomerEntitiesCloseOfBusinessDate() {
    return customerEntitiesCloseOfBusinessDate;
  }

  public void setCustomerEntitiesCloseOfBusinessDate(String customerEntitiesCloseOfBusinessDate) {
    this.customerEntitiesCloseOfBusinessDate = customerEntitiesCloseOfBusinessDate;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Partition Number")
  public String getCustomerEntitiesPartitionNumber() {
    return customerEntitiesPartitionNumber;
  }

  public void setCustomerEntitiesPartitionNumber(String customerEntitiesPartitionNumber) {
    this.customerEntitiesPartitionNumber = customerEntitiesPartitionNumber;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source System Identifier")
  public String getCustomerEntitiesSourceSystemIdentifier() {
    return customerEntitiesSourceSystemIdentifier;
  }

  public void setCustomerEntitiesSourceSystemIdentifier(
      String customerEntitiesSourceSystemIdentifier) {
    this.customerEntitiesSourceSystemIdentifier = customerEntitiesSourceSystemIdentifier;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.External Profile ID")
  public String getCustomerEntitiesExternalProfileID() {
    return customerEntitiesExternalProfileID;
  }

  public void setCustomerEntitiesExternalProfileID(String customerEntitiesExternalProfileID) {
    this.customerEntitiesExternalProfileID = customerEntitiesExternalProfileID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Concatenated Profile ID")
  public String getCustomerEntitiesConcatenatedProfileID() {
    return customerEntitiesConcatenatedProfileID;
  }

  public void setCustomerEntitiesConcatenatedProfileID(
      String customerEntitiesConcatenatedProfileID) {
    this.customerEntitiesConcatenatedProfileID = customerEntitiesConcatenatedProfileID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Type")
  public String getCustomerEntitiesProfileType() {
    return customerEntitiesProfileType;
  }

  public void setCustomerEntitiesProfileType(String customerEntitiesProfileType) {
    this.customerEntitiesProfileType = customerEntitiesProfileType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Party Role Type Code")
  public String getCustomerEntitiesPartyRoleTypeCode() {
    return customerEntitiesPartyRoleTypeCode;
  }

  public void setCustomerEntitiesPartyRoleTypeCode(String customerEntitiesPartyRoleTypeCode) {
    this.customerEntitiesPartyRoleTypeCode = customerEntitiesPartyRoleTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Status")
  public String getCustomerEntitiesProfileStatus() {
    return customerEntitiesProfileStatus;
  }

  public void setCustomerEntitiesProfileStatus(String customerEntitiesProfileStatus) {
    this.customerEntitiesProfileStatus = customerEntitiesProfileStatus;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Segment")
  public String getCustomerEntitiesProfileSegment() {
    return customerEntitiesProfileSegment;
  }

  public void setCustomerEntitiesProfileSegment(String customerEntitiesProfileSegment) {
    this.customerEntitiesProfileSegment = customerEntitiesProfileSegment;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.LoB Name")
  public String getCustomerEntitiesLoBName() {
    return customerEntitiesLoBName;
  }

  public void setCustomerEntitiesLoBName(String customerEntitiesLoBName) {
    this.customerEntitiesLoBName = customerEntitiesLoBName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.LoB Region")
  public String getCustomerEntitiesLoBRegion() {
    return customerEntitiesLoBRegion;
  }

  public void setCustomerEntitiesLoBRegion(String customerEntitiesLoBRegion) {
    this.customerEntitiesLoBRegion = customerEntitiesLoBRegion;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.LoB Country")
  public String getCustomerEntitiesLoBCountry() {
    return customerEntitiesLoBCountry;
  }

  public void setCustomerEntitiesLoBCountry(String customerEntitiesLoBCountry) {
    this.customerEntitiesLoBCountry = customerEntitiesLoBCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.HSBC Legal Entity Code")
  public String getCustomerEntitiesHSBCLegalEntityCode() {
    return customerEntitiesHSBCLegalEntityCode;
  }

  public void setCustomerEntitiesHSBCLegalEntityCode(String customerEntitiesHSBCLegalEntityCode) {
    this.customerEntitiesHSBCLegalEntityCode = customerEntitiesHSBCLegalEntityCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Name Category Code")
  public String getCustomerEntitiesNameCategoryCode() {
    return customerEntitiesNameCategoryCode;
  }

  public void setCustomerEntitiesNameCategoryCode(String customerEntitiesNameCategoryCode) {
    this.customerEntitiesNameCategoryCode = customerEntitiesNameCategoryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.NameTypeCode")
  public String getCustomerEntitiesNameTypeCode() {
    return customerEntitiesNameTypeCode;
  }

  public void setCustomerEntitiesNameTypeCode(String customerEntitiesNameTypeCode) {
    this.customerEntitiesNameTypeCode = customerEntitiesNameTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Name Language Type Code")
  public String getCustomerEntitiesNameLanguageTypeCode() {
    return customerEntitiesNameLanguageTypeCode;
  }

  public void setCustomerEntitiesNameLanguageTypeCode(String customerEntitiesNameLanguageTypeCode) {
    this.customerEntitiesNameLanguageTypeCode = customerEntitiesNameLanguageTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Entity Name (Original)")
  public String getCustomerEntitiesEntityNameOriginal() {
    return customerEntitiesEntityNameOriginal;
  }

  public void setCustomerEntitiesEntityNameOriginal(String customerEntitiesEntityNameOriginal) {
    this.customerEntitiesEntityNameOriginal = customerEntitiesEntityNameOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.PEP Indicator")
  public String getCustomerEntitiesPEPIndicator() {
    return customerEntitiesPEPIndicator;
  }

  public void setCustomerEntitiesPEPIndicator(String customerEntitiesPEPIndicator) {
    this.customerEntitiesPEPIndicator = customerEntitiesPEPIndicator;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Special Category Customer")
  public String getCustomerEntitiesSpecialCategoryCustomer() {
    return customerEntitiesSpecialCategoryCustomer;
  }

  public void setCustomerEntitiesSpecialCategoryCustomer(
      String customerEntitiesSpecialCategoryCustomer) {
    this.customerEntitiesSpecialCategoryCustomer = customerEntitiesSpecialCategoryCustomer;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Type")
  public String getCustomerEntitiesSourceAddressType() {
    return customerEntitiesSourceAddressType;
  }

  public void setCustomerEntitiesSourceAddressType(String customerEntitiesSourceAddressType) {
    this.customerEntitiesSourceAddressType = customerEntitiesSourceAddressType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.AddressFormatCode")
  public String getCustomerEntitiesAddressFormatCode() {
    return customerEntitiesAddressFormatCode;
  }

  public void setCustomerEntitiesAddressFormatCode(String customerEntitiesAddressFormatCode) {
    this.customerEntitiesAddressFormatCode = customerEntitiesAddressFormatCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Address Language Type Code")
  public String getCustomerEntitiesAddressLanguageTypeCode() {
    return customerEntitiesAddressLanguageTypeCode;
  }

  public void setCustomerEntitiesAddressLanguageTypeCode(
      String customerEntitiesAddressLanguageTypeCode) {
    this.customerEntitiesAddressLanguageTypeCode = customerEntitiesAddressLanguageTypeCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 1")
  public String getCustomerEntitiesSourceAddressLine1() {
    return customerEntitiesSourceAddressLine1;
  }

  public void setCustomerEntitiesSourceAddressLine1(String customerEntitiesSourceAddressLine1) {
    this.customerEntitiesSourceAddressLine1 = customerEntitiesSourceAddressLine1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 2")
  public String getCustomerEntitiesSourceAddressLine2() {
    return customerEntitiesSourceAddressLine2;
  }

  public void setCustomerEntitiesSourceAddressLine2(String customerEntitiesSourceAddressLine2) {
    this.customerEntitiesSourceAddressLine2 = customerEntitiesSourceAddressLine2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 3")
  public String getCustomerEntitiesSourceAddressLine3() {
    return customerEntitiesSourceAddressLine3;
  }

  public void setCustomerEntitiesSourceAddressLine3(String customerEntitiesSourceAddressLine3) {
    this.customerEntitiesSourceAddressLine3 = customerEntitiesSourceAddressLine3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 4")
  public String getCustomerEntitiesSourceAddressLine4() {
    return customerEntitiesSourceAddressLine4;
  }

  public void setCustomerEntitiesSourceAddressLine4(String customerEntitiesSourceAddressLine4) {
    this.customerEntitiesSourceAddressLine4 = customerEntitiesSourceAddressLine4;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 5")
  public String getCustomerEntitiesSourceAddressLine5() {
    return customerEntitiesSourceAddressLine5;
  }

  public void setCustomerEntitiesSourceAddressLine5(String customerEntitiesSourceAddressLine5) {
    this.customerEntitiesSourceAddressLine5 = customerEntitiesSourceAddressLine5;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 6")
  public String getCustomerEntitiesSourceAddressLine6() {
    return customerEntitiesSourceAddressLine6;
  }

  public void setCustomerEntitiesSourceAddressLine6(String customerEntitiesSourceAddressLine6) {
    this.customerEntitiesSourceAddressLine6 = customerEntitiesSourceAddressLine6;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 7")
  public String getCustomerEntitiesSourceAddressLine7() {
    return customerEntitiesSourceAddressLine7;
  }

  public void setCustomerEntitiesSourceAddressLine7(String customerEntitiesSourceAddressLine7) {
    this.customerEntitiesSourceAddressLine7 = customerEntitiesSourceAddressLine7;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 8")
  public String getCustomerEntitiesSourceAddressLine8() {
    return customerEntitiesSourceAddressLine8;
  }

  public void setCustomerEntitiesSourceAddressLine8(String customerEntitiesSourceAddressLine8) {
    this.customerEntitiesSourceAddressLine8 = customerEntitiesSourceAddressLine8;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 9")
  public String getCustomerEntitiesSourceAddressLine9() {
    return customerEntitiesSourceAddressLine9;
  }

  public void setCustomerEntitiesSourceAddressLine9(String customerEntitiesSourceAddressLine9) {
    this.customerEntitiesSourceAddressLine9 = customerEntitiesSourceAddressLine9;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Line 10")
  public String getCustomerEntitiesSourceAddressLine10() {
    return customerEntitiesSourceAddressLine10;
  }

  public void setCustomerEntitiesSourceAddressLine10(String customerEntitiesSourceAddressLine10) {
    this.customerEntitiesSourceAddressLine10 = customerEntitiesSourceAddressLine10;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Postal Code")
  public String getCustomerEntitiesSourcePostalCode() {
    return customerEntitiesSourcePostalCode;
  }

  public void setCustomerEntitiesSourcePostalCode(String customerEntitiesSourcePostalCode) {
    this.customerEntitiesSourcePostalCode = customerEntitiesSourcePostalCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Source Address Country")
  public String getCustomerEntitiesSourceAddressCountry() {
    return customerEntitiesSourceAddressCountry;
  }

  public void setCustomerEntitiesSourceAddressCountry(String customerEntitiesSourceAddressCountry) {
    this.customerEntitiesSourceAddressCountry = customerEntitiesSourceAddressCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 1")
  public String getCustomerEntitiesIdentificationDocument1() {
    return customerEntitiesIdentificationDocument1;
  }

  public void setCustomerEntitiesIdentificationDocument1(
      String customerEntitiesIdentificationDocument1) {
    this.customerEntitiesIdentificationDocument1 = customerEntitiesIdentificationDocument1;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 2")
  public String getCustomerEntitiesIdentificationDocument2() {
    return customerEntitiesIdentificationDocument2;
  }

  public void setCustomerEntitiesIdentificationDocument2(
      String customerEntitiesIdentificationDocument2) {
    this.customerEntitiesIdentificationDocument2 = customerEntitiesIdentificationDocument2;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 3")
  public String getCustomerEntitiesIdentificationDocument3() {
    return customerEntitiesIdentificationDocument3;
  }

  public void setCustomerEntitiesIdentificationDocument3(
      String customerEntitiesIdentificationDocument3) {
    this.customerEntitiesIdentificationDocument3 = customerEntitiesIdentificationDocument3;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 4")
  public String getCustomerEntitiesIdentificationDocument4() {
    return customerEntitiesIdentificationDocument4;
  }

  public void setCustomerEntitiesIdentificationDocument4(
      String customerEntitiesIdentificationDocument4) {
    this.customerEntitiesIdentificationDocument4 = customerEntitiesIdentificationDocument4;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 5")
  public String getCustomerEntitiesIdentificationDocument5() {
    return customerEntitiesIdentificationDocument5;
  }

  public void setCustomerEntitiesIdentificationDocument5(
      String customerEntitiesIdentificationDocument5) {
    this.customerEntitiesIdentificationDocument5 = customerEntitiesIdentificationDocument5;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 6")
  public String getCustomerEntitiesIdentificationDocument6() {
    return customerEntitiesIdentificationDocument6;
  }

  public void setCustomerEntitiesIdentificationDocument6(
      String customerEntitiesIdentificationDocument6) {
    this.customerEntitiesIdentificationDocument6 = customerEntitiesIdentificationDocument6;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 7")
  public String getCustomerEntitiesIdentificationDocument7() {
    return customerEntitiesIdentificationDocument7;
  }

  public void setCustomerEntitiesIdentificationDocument7(
      String customerEntitiesIdentificationDocument7) {
    this.customerEntitiesIdentificationDocument7 = customerEntitiesIdentificationDocument7;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 8")
  public String getCustomerEntitiesIdentificationDocument8() {
    return customerEntitiesIdentificationDocument8;
  }

  public void setCustomerEntitiesIdentificationDocument8(
      String customerEntitiesIdentificationDocument8) {
    this.customerEntitiesIdentificationDocument8 = customerEntitiesIdentificationDocument8;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 9")
  public String getCustomerEntitiesIdentificationDocument9() {
    return customerEntitiesIdentificationDocument9;
  }

  public void setCustomerEntitiesIdentificationDocument9(
      String customerEntitiesIdentificationDocument9) {
    this.customerEntitiesIdentificationDocument9 = customerEntitiesIdentificationDocument9;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Identification Document 10")
  public String getCustomerEntitiesIdentificationDocument10() {
    return customerEntitiesIdentificationDocument10;
  }

  public void setCustomerEntitiesIdentificationDocument10(
      String customerEntitiesIdentificationDocument10) {
    this.customerEntitiesIdentificationDocument10 = customerEntitiesIdentificationDocument10;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Trades With Countries")
  public String getCustomerEntitiesTradesWithCountries() {
    return customerEntitiesTradesWithCountries;
  }

  public void setCustomerEntitiesTradesWithCountries(String customerEntitiesTradesWithCountries) {
    this.customerEntitiesTradesWithCountries = customerEntitiesTradesWithCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Subsidiaries Operates in Countries")
  public String getCustomerEntitiesSubsidiariesOperatesInCountries() {
    return customerEntitiesSubsidiariesOperatesInCountries;
  }

  public void setCustomerEntitiesSubsidiariesOperatesInCountries(
      String customerEntitiesSubsidiariesOperatesInCountries) {
    this.customerEntitiesSubsidiariesOperatesInCountries =
        customerEntitiesSubsidiariesOperatesInCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Export Countries")
  public String getCustomerEntitiesExportCountries() {
    return customerEntitiesExportCountries;
  }

  public void setCustomerEntitiesExportCountries(String customerEntitiesExportCountries) {
    this.customerEntitiesExportCountries = customerEntitiesExportCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Import Countries")
  public String getCustomerEntitiesImportCountries() {
    return customerEntitiesImportCountries;
  }

  public void setCustomerEntitiesImportCountries(String customerEntitiesImportCountries) {
    this.customerEntitiesImportCountries = customerEntitiesImportCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries of Incorporation")
  public String getCustomerEntitiesCountriesOfIncorporation() {
    return customerEntitiesCountriesOfIncorporation;
  }

  public void setCustomerEntitiesCountriesOfIncorporation(
      String customerEntitiesCountriesOfIncorporation) {
    this.customerEntitiesCountriesOfIncorporation = customerEntitiesCountriesOfIncorporation;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries of Registration (Original)")
  public String getCustomerEntitiesCountriesOfRegistrationOriginal() {
    return customerEntitiesCountriesOfRegistrationOriginal;
  }

  public void setCustomerEntitiesCountriesOfRegistrationOriginal(
      String customerEntitiesCountriesOfRegistrationOriginal) {
    this.customerEntitiesCountriesOfRegistrationOriginal =
        customerEntitiesCountriesOfRegistrationOriginal;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries of Business")
  public String getCustomerEntitiesCountriesOfBusiness() {
    return customerEntitiesCountriesOfBusiness;
  }

  public void setCustomerEntitiesCountriesOfBusiness(String customerEntitiesCountriesOfBusiness) {
    this.customerEntitiesCountriesOfBusiness = customerEntitiesCountriesOfBusiness;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries of Head Office")
  public String getCustomerEntitiesCountriesOfHeadOffice() {
    return customerEntitiesCountriesOfHeadOffice;
  }

  public void setCustomerEntitiesCountriesOfHeadOffice(
      String customerEntitiesCountriesOfHeadOffice) {
    this.customerEntitiesCountriesOfHeadOffice = customerEntitiesCountriesOfHeadOffice;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqCustID")
  public String getCustomerEntitiesEdqCustID() {
    return customerEntitiesEdqCustID;
  }

  public void setCustomerEntitiesEdqCustID(String customerEntitiesEdqCustID) {
    this.customerEntitiesEdqCustID = customerEntitiesEdqCustID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqCustSubID")
  public String getCustomerEntitiesEdqCustSubID() {
    return customerEntitiesEdqCustSubID;
  }

  public void setCustomerEntitiesEdqCustSubID(String customerEntitiesEdqCustSubID) {
    this.customerEntitiesEdqCustSubID = customerEntitiesEdqCustSubID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqLoBCountryCode")
  public String getCustomerEntitiesEdqLoBCountryCode() {
    return customerEntitiesEdqLoBCountryCode;
  }

  public void setCustomerEntitiesEdqLoBCountryCode(String customerEntitiesEdqLoBCountryCode) {
    this.customerEntitiesEdqLoBCountryCode = customerEntitiesEdqLoBCountryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Address Country")
  public String getCustomerEntitiesAddressCountry() {
    return customerEntitiesAddressCountry;
  }

  public void setCustomerEntitiesAddressCountry(String customerEntitiesAddressCountry) {
    this.customerEntitiesAddressCountry = customerEntitiesAddressCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqAddressCountryCode")
  public String getCustomerEntitiesEdqAddressCountryCode() {
    return customerEntitiesEdqAddressCountryCode;
  }

  public void setCustomerEntitiesEdqAddressCountryCode(
      String customerEntitiesEdqAddressCountryCode) {
    this.customerEntitiesEdqAddressCountryCode = customerEntitiesEdqAddressCountryCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqTradesWithCountries")
  public String getCustomerEntitiesEdqTradesWithCountries() {
    return customerEntitiesEdqTradesWithCountries;
  }

  public void setCustomerEntitiesEdqTradesWithCountries(
      String customerEntitiesEdqTradesWithCountries) {
    this.customerEntitiesEdqTradesWithCountries = customerEntitiesEdqTradesWithCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqExportsCountries")
  public String getCustomerEntitiesEdqExportsCountries() {
    return customerEntitiesEdqExportsCountries;
  }

  public void setCustomerEntitiesEdqExportsCountries(String customerEntitiesEdqExportsCountries) {
    this.customerEntitiesEdqExportsCountries = customerEntitiesEdqExportsCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqImportsCountries")
  public String getCustomerEntitiesEdqImportsCountries() {
    return customerEntitiesEdqImportsCountries;
  }

  public void setCustomerEntitiesEdqImportsCountries(String customerEntitiesEdqImportsCountries) {
    this.customerEntitiesEdqImportsCountries = customerEntitiesEdqImportsCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqIncorporationCountries")
  public String getCustomerEntitiesEdqIncorporationCountries() {
    return customerEntitiesEdqIncorporationCountries;
  }

  public void setCustomerEntitiesEdqIncorporationCountries(
      String customerEntitiesEdqIncorporationCountries) {
    this.customerEntitiesEdqIncorporationCountries = customerEntitiesEdqIncorporationCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqIncorporationCountriesCodes")
  public String getCustomerEntitiesEdqIncorporationCountriesCodes() {
    return customerEntitiesEdqIncorporationCountriesCodes;
  }

  public void setCustomerEntitiesEdqIncorporationCountriesCodes(
      String customerEntitiesEdqIncorporationCountriesCodes) {
    this.customerEntitiesEdqIncorporationCountriesCodes =
        customerEntitiesEdqIncorporationCountriesCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Registration Country")
  public String getCustomerEntitiesRegistrationCountry() {
    return customerEntitiesRegistrationCountry;
  }

  public void setCustomerEntitiesRegistrationCountry(String customerEntitiesRegistrationCountry) {
    this.customerEntitiesRegistrationCountry = customerEntitiesRegistrationCountry;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqRegiistrationCountriesCodes")
  public String getCustomerEntitiesEdqRegiistrationCountriesCodes() {
    return customerEntitiesEdqRegiistrationCountriesCodes;
  }

  public void setCustomerEntitiesEdqRegiistrationCountriesCodes(
      String customerEntitiesEdqRegiistrationCountriesCodes) {
    this.customerEntitiesEdqRegiistrationCountriesCodes =
        customerEntitiesEdqRegiistrationCountriesCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqBusinessCountries")
  public String getCustomerEntitiesEdqBusinessCountries() {
    return customerEntitiesEdqBusinessCountries;
  }

  public void setCustomerEntitiesEdqBusinessCountries(String customerEntitiesEdqBusinessCountries) {
    this.customerEntitiesEdqBusinessCountries = customerEntitiesEdqBusinessCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqHeadOfficeCountries")
  public String getCustomerEntitiesEdqHeadOfficeCountries() {
    return customerEntitiesEdqHeadOfficeCountries;
  }

  public void setCustomerEntitiesEdqHeadOfficeCountries(
      String customerEntitiesEdqHeadOfficeCountries) {
    this.customerEntitiesEdqHeadOfficeCountries = customerEntitiesEdqHeadOfficeCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries (All)")
  public String getCustomerEntitiesCountriesAll() {
    return customerEntitiesCountriesAll;
  }

  public void setCustomerEntitiesCountriesAll(String customerEntitiesCountriesAll) {
    this.customerEntitiesCountriesAll = customerEntitiesCountriesAll;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Countries (All) Codes")
  public String getCustomerEntitiesCountriesAllCodes() {
    return customerEntitiesCountriesAllCodes;
  }

  public void setCustomerEntitiesCountriesAllCodes(String customerEntitiesCountriesAllCodes) {
    this.customerEntitiesCountriesAllCodes = customerEntitiesCountriesAllCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqLoB")
  public String getCustomerEntitiesEdqLoB() {
    return customerEntitiesEdqLoB;
  }

  public void setCustomerEntitiesEdqLoB(String customerEntitiesEdqLoB) {
    this.customerEntitiesEdqLoB = customerEntitiesEdqLoB;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqPermission")
  public String getCustomerEntitiesEdqPermission() {
    return customerEntitiesEdqPermission;
  }

  public void setCustomerEntitiesEdqPermission(String customerEntitiesEdqPermission) {
    this.customerEntitiesEdqPermission = customerEntitiesEdqPermission;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Tax ID")
  public String getCustomerEntitiesTaxID() {
    return customerEntitiesTaxID;
  }

  public void setCustomerEntitiesTaxID(String customerEntitiesTaxID) {
    this.customerEntitiesTaxID = customerEntitiesTaxID;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.City")
  public String getCustomerEntitiesCity() {
    return customerEntitiesCity;
  }

  public void setCustomerEntitiesCity(String customerEntitiesCity) {
    this.customerEntitiesCity = customerEntitiesCity;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Hyperlink")
  public String getCustomerEntitiesProfileHyperlink() {
    return customerEntitiesProfileHyperlink;
  }

  public void setCustomerEntitiesProfileHyperlink(String customerEntitiesProfileHyperlink) {
    this.customerEntitiesProfileHyperlink = customerEntitiesProfileHyperlink;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Search Hyperlink")
  public String getCustomerEntitiesSearchHyperlink() {
    return customerEntitiesSearchHyperlink;
  }

  public void setCustomerEntitiesSearchHyperlink(String customerEntitiesSearchHyperlink) {
    this.customerEntitiesSearchHyperlink = customerEntitiesSearchHyperlink;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqListKey")
  public String getCustomerEntitiesEdqListKey() {
    return customerEntitiesEdqListKey;
  }

  public void setCustomerEntitiesEdqListKey(String customerEntitiesEdqListKey) {
    this.customerEntitiesEdqListKey = customerEntitiesEdqListKey;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Address")
  public String getCustomerEntitiesAddress() {
    return customerEntitiesAddress;
  }

  public void setCustomerEntitiesAddress(String customerEntitiesAddress) {
    this.customerEntitiesAddress = customerEntitiesAddress;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Entity Name")
  public String getCustomerEntitiesEntityName() {
    return customerEntitiesEntityName;
  }

  public void setCustomerEntitiesEntityName(String customerEntitiesEntityName) {
    this.customerEntitiesEntityName = customerEntitiesEntityName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Postal Code")
  public String getCustomerEntitiesPostalCode() {
    return customerEntitiesPostalCode;
  }

  public void setCustomerEntitiesPostalCode(String customerEntitiesPostalCode) {
    this.customerEntitiesPostalCode = customerEntitiesPostalCode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Person or Business Indicator")
  public String getCustomerEntitiesPersonOrBusinessIndicator() {
    return customerEntitiesPersonOrBusinessIndicator;
  }

  public void setCustomerEntitiesPersonOrBusinessIndicator(
      String customerEntitiesPersonOrBusinessIndicator) {
    this.customerEntitiesPersonOrBusinessIndicator = customerEntitiesPersonOrBusinessIndicator;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqCloseOfBusinessDate")
  public String getCustomerEntitiesEdqCloseOfBusinessDate() {
    return customerEntitiesEdqCloseOfBusinessDate;
  }

  public void setCustomerEntitiesEdqCloseOfBusinessDate(
      String customerEntitiesEdqCloseOfBusinessDate) {
    this.customerEntitiesEdqCloseOfBusinessDate = customerEntitiesEdqCloseOfBusinessDate;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Operating Countries")
  public String getCustomerEntitiesOperatingCountries() {
    return customerEntitiesOperatingCountries;
  }

  public void setCustomerEntitiesOperatingCountries(String customerEntitiesOperatingCountries) {
    this.customerEntitiesOperatingCountries = customerEntitiesOperatingCountries;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqOperatingCountriesCodes")
  public String getCustomerEntitiesEdqOperatingCountriesCodes() {
    return customerEntitiesEdqOperatingCountriesCodes;
  }

  public void setCustomerEntitiesEdqOperatingCountriesCodes(
      String customerEntitiesEdqOperatingCountriesCodes) {
    this.customerEntitiesEdqOperatingCountriesCodes = customerEntitiesEdqOperatingCountriesCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Registration Number")
  public String getCustomerEntitiesRegistrationNumber() {
    return customerEntitiesRegistrationNumber;
  }

  public void setCustomerEntitiesRegistrationNumber(String customerEntitiesRegistrationNumber) {
    this.customerEntitiesRegistrationNumber = customerEntitiesRegistrationNumber;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Name Type")
  public String getCustomerEntitiesProfileNameType() {
    return customerEntitiesProfileNameType;
  }

  public void setCustomerEntitiesProfileNameType(String customerEntitiesProfileNameType) {
    this.customerEntitiesProfileNameType = customerEntitiesProfileNameType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqPartyRoleTypeDescription")
  public String getCustomerEntitiesEdqPartyRoleTypeDescription() {
    return customerEntitiesEdqPartyRoleTypeDescription;
  }

  public void setCustomerEntitiesEdqPartyRoleTypeDescription(
      String customerEntitiesEdqPartyRoleTypeDescription) {
    this.customerEntitiesEdqPartyRoleTypeDescription = customerEntitiesEdqPartyRoleTypeDescription;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqPartyStatusCodeDescription")
  public String getCustomerEntitiesEdqPartyStatusCodeDescription() {
    return customerEntitiesEdqPartyStatusCodeDescription;
  }

  public void setCustomerEntitiesEdqPartyStatusCodeDescription(
      String customerEntitiesEdqPartyStatusCodeDescription) {
    this.customerEntitiesEdqPartyStatusCodeDescription =
        customerEntitiesEdqPartyStatusCodeDescription;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqDay1SpikeFlag")
  public String getCustomerEntitiesEdqDay1SpikeFlag() {
    return customerEntitiesEdqDay1SpikeFlag;
  }

  public void setCustomerEntitiesEdqDay1SpikeFlag(String customerEntitiesEdqDay1SpikeFlag) {
    this.customerEntitiesEdqDay1SpikeFlag = customerEntitiesEdqDay1SpikeFlag;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Original Script Name")
  public String getCustomerEntitiesOriginalScriptName() {
    return customerEntitiesOriginalScriptName;
  }

  public void setCustomerEntitiesOriginalScriptName(String customerEntitiesOriginalScriptName) {
    this.customerEntitiesOriginalScriptName = customerEntitiesOriginalScriptName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Profile Full Address")
  public String getCustomerEntitiesProfileFullAddress() {
    return customerEntitiesProfileFullAddress;
  }

  public void setCustomerEntitiesProfileFullAddress(String customerEntitiesProfileFullAddress) {
    this.customerEntitiesProfileFullAddress = customerEntitiesProfileFullAddress;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqScreeningMode")
  public String getCustomerEntitiesEdqScreeningMode() {
    return customerEntitiesEdqScreeningMode;
  }

  public void setCustomerEntitiesEdqScreeningMode(String customerEntitiesEdqScreeningMode) {
    this.customerEntitiesEdqScreeningMode = customerEntitiesEdqScreeningMode;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.edqCaseKey")
  public String getCustomerEntitiesEdqCaseKey() {
    return customerEntitiesEdqCaseKey;
  }

  public void setCustomerEntitiesEdqCaseKey(String customerEntitiesEdqCaseKey) {
    this.customerEntitiesEdqCaseKey = customerEntitiesEdqCaseKey;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Address Type")
  public String getCustomerEntitiesAddressType() {
    return customerEntitiesAddressType;
  }

  public void setCustomerEntitiesAddressType(String customerEntitiesAddressType) {
    this.customerEntitiesAddressType = customerEntitiesAddressType;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.SSC Codes")
  public String getCustomerEntitiesSSCCodes() {
    return customerEntitiesSSCCodes;
  }

  public void setCustomerEntitiesSSCCodes(String customerEntitiesSSCCodes) {
    this.customerEntitiesSSCCodes = customerEntitiesSSCCodes;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.CTRP Fragment")
  public String getCustomerEntitiesCTRPFragment() {
    return customerEntitiesCTRPFragment;
  }

  public void setCustomerEntitiesCTRPFragment(String customerEntitiesCTRPFragment) {
    this.customerEntitiesCTRPFragment = customerEntitiesCTRPFragment;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Request User Name")
  public String getCustomerEntitiesRequestUserName() {
    return customerEntitiesRequestUserName;
  }

  public void setCustomerEntitiesRequestUserName(String customerEntitiesRequestUserName) {
    this.customerEntitiesRequestUserName = customerEntitiesRequestUserName;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("CustomerEntities.Request Date/Time")
  public String getCustomerEntitiesRequestDateTime() {
    return customerEntitiesRequestDateTime;
  }

  public void setCustomerEntitiesRequestDateTime(String customerEntitiesRequestDateTime) {
    this.customerEntitiesRequestDateTime = customerEntitiesRequestDateTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerEntities customerEntities = (CustomerEntities) o;
    return Objects.equals(customerEntitiesRecordId, customerEntities.customerEntitiesRecordId) &&
        Objects.equals(customerEntitiesInputStream, customerEntities.customerEntitiesInputStream) &&
        Objects.equals(
            customerEntitiesSourceSystemHistoryID,
            customerEntities.customerEntitiesSourceSystemHistoryID) &&
        Objects.equals(
            customerEntitiesCloseOfBusinessDate,
            customerEntities.customerEntitiesCloseOfBusinessDate) &&
        Objects.equals(
            customerEntitiesPartitionNumber, customerEntities.customerEntitiesPartitionNumber) &&
        Objects.equals(
            customerEntitiesSourceSystemIdentifier,
            customerEntities.customerEntitiesSourceSystemIdentifier) &&
        Objects.equals(
            customerEntitiesExternalProfileID, customerEntities.customerEntitiesExternalProfileID)
        &&
        Objects.equals(
            customerEntitiesConcatenatedProfileID,
            customerEntities.customerEntitiesConcatenatedProfileID) &&
        Objects.equals(customerEntitiesProfileType, customerEntities.customerEntitiesProfileType) &&
        Objects.equals(
            customerEntitiesPartyRoleTypeCode, customerEntities.customerEntitiesPartyRoleTypeCode)
        &&
        Objects.equals(
            customerEntitiesProfileStatus, customerEntities.customerEntitiesProfileStatus) &&
        Objects.equals(
            customerEntitiesProfileSegment, customerEntities.customerEntitiesProfileSegment) &&
        Objects.equals(customerEntitiesLoBName, customerEntities.customerEntitiesLoBName) &&
        Objects.equals(customerEntitiesLoBRegion, customerEntities.customerEntitiesLoBRegion) &&
        Objects.equals(customerEntitiesLoBCountry, customerEntities.customerEntitiesLoBCountry) &&
        Objects.equals(
            customerEntitiesHSBCLegalEntityCode,
            customerEntities.customerEntitiesHSBCLegalEntityCode) &&
        Objects.equals(
            customerEntitiesNameCategoryCode, customerEntities.customerEntitiesNameCategoryCode) &&
        Objects.equals(customerEntitiesNameTypeCode, customerEntities.customerEntitiesNameTypeCode)
        &&
        Objects.equals(
            customerEntitiesNameLanguageTypeCode,
            customerEntities.customerEntitiesNameLanguageTypeCode) &&
        Objects.equals(
            customerEntitiesEntityNameOriginal, customerEntities.customerEntitiesEntityNameOriginal)
        &&
        Objects.equals(customerEntitiesPEPIndicator, customerEntities.customerEntitiesPEPIndicator)
        &&
        Objects.equals(
            customerEntitiesSpecialCategoryCustomer,
            customerEntities.customerEntitiesSpecialCategoryCustomer) &&
        Objects.equals(
            customerEntitiesSourceAddressType, customerEntities.customerEntitiesSourceAddressType)
        &&
        Objects.equals(
            customerEntitiesAddressFormatCode, customerEntities.customerEntitiesAddressFormatCode)
        &&
        Objects.equals(
            customerEntitiesAddressLanguageTypeCode,
            customerEntities.customerEntitiesAddressLanguageTypeCode) &&
        Objects.equals(
            customerEntitiesSourceAddressLine1, customerEntities.customerEntitiesSourceAddressLine1)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine2, customerEntities.customerEntitiesSourceAddressLine2)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine3, customerEntities.customerEntitiesSourceAddressLine3)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine4, customerEntities.customerEntitiesSourceAddressLine4)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine5, customerEntities.customerEntitiesSourceAddressLine5)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine6, customerEntities.customerEntitiesSourceAddressLine6)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine7, customerEntities.customerEntitiesSourceAddressLine7)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine8, customerEntities.customerEntitiesSourceAddressLine8)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine9, customerEntities.customerEntitiesSourceAddressLine9)
        &&
        Objects.equals(
            customerEntitiesSourceAddressLine10,
            customerEntities.customerEntitiesSourceAddressLine10) &&
        Objects.equals(
            customerEntitiesSourcePostalCode, customerEntities.customerEntitiesSourcePostalCode) &&
        Objects.equals(
            customerEntitiesSourceAddressCountry,
            customerEntities.customerEntitiesSourceAddressCountry) &&
        Objects.equals(
            customerEntitiesIdentificationDocument1,
            customerEntities.customerEntitiesIdentificationDocument1) &&
        Objects.equals(
            customerEntitiesIdentificationDocument2,
            customerEntities.customerEntitiesIdentificationDocument2) &&
        Objects.equals(
            customerEntitiesIdentificationDocument3,
            customerEntities.customerEntitiesIdentificationDocument3) &&
        Objects.equals(
            customerEntitiesIdentificationDocument4,
            customerEntities.customerEntitiesIdentificationDocument4) &&
        Objects.equals(
            customerEntitiesIdentificationDocument5,
            customerEntities.customerEntitiesIdentificationDocument5) &&
        Objects.equals(
            customerEntitiesIdentificationDocument6,
            customerEntities.customerEntitiesIdentificationDocument6) &&
        Objects.equals(
            customerEntitiesIdentificationDocument7,
            customerEntities.customerEntitiesIdentificationDocument7) &&
        Objects.equals(
            customerEntitiesIdentificationDocument8,
            customerEntities.customerEntitiesIdentificationDocument8) &&
        Objects.equals(
            customerEntitiesIdentificationDocument9,
            customerEntities.customerEntitiesIdentificationDocument9) &&
        Objects.equals(
            customerEntitiesIdentificationDocument10,
            customerEntities.customerEntitiesIdentificationDocument10) &&
        Objects.equals(
            customerEntitiesTradesWithCountries,
            customerEntities.customerEntitiesTradesWithCountries) &&
        Objects.equals(
            customerEntitiesSubsidiariesOperatesInCountries,
            customerEntities.customerEntitiesSubsidiariesOperatesInCountries) &&
        Objects.equals(
            customerEntitiesExportCountries, customerEntities.customerEntitiesExportCountries) &&
        Objects.equals(
            customerEntitiesImportCountries, customerEntities.customerEntitiesImportCountries) &&
        Objects.equals(
            customerEntitiesCountriesOfIncorporation,
            customerEntities.customerEntitiesCountriesOfIncorporation) &&
        Objects.equals(
            customerEntitiesCountriesOfRegistrationOriginal,
            customerEntities.customerEntitiesCountriesOfRegistrationOriginal) &&
        Objects.equals(
            customerEntitiesCountriesOfBusiness,
            customerEntities.customerEntitiesCountriesOfBusiness) &&
        Objects.equals(
            customerEntitiesCountriesOfHeadOffice,
            customerEntities.customerEntitiesCountriesOfHeadOffice) &&
        Objects.equals(customerEntitiesEdqCustID, customerEntities.customerEntitiesEdqCustID) &&
        Objects.equals(customerEntitiesEdqCustSubID, customerEntities.customerEntitiesEdqCustSubID)
        &&
        Objects.equals(
            customerEntitiesEdqLoBCountryCode, customerEntities.customerEntitiesEdqLoBCountryCode)
        &&
        Objects.equals(
            customerEntitiesAddressCountry, customerEntities.customerEntitiesAddressCountry) &&
        Objects.equals(
            customerEntitiesEdqAddressCountryCode,
            customerEntities.customerEntitiesEdqAddressCountryCode) &&
        Objects.equals(
            customerEntitiesEdqTradesWithCountries,
            customerEntities.customerEntitiesEdqTradesWithCountries) &&
        Objects.equals(
            customerEntitiesEdqExportsCountries,
            customerEntities.customerEntitiesEdqExportsCountries) &&
        Objects.equals(
            customerEntitiesEdqImportsCountries,
            customerEntities.customerEntitiesEdqImportsCountries) &&
        Objects.equals(
            customerEntitiesEdqIncorporationCountries,
            customerEntities.customerEntitiesEdqIncorporationCountries) &&
        Objects.equals(
            customerEntitiesEdqIncorporationCountriesCodes,
            customerEntities.customerEntitiesEdqIncorporationCountriesCodes) &&
        Objects.equals(
            customerEntitiesRegistrationCountry,
            customerEntities.customerEntitiesRegistrationCountry) &&
        Objects.equals(
            customerEntitiesEdqRegiistrationCountriesCodes,
            customerEntities.customerEntitiesEdqRegiistrationCountriesCodes) &&
        Objects.equals(
            customerEntitiesEdqBusinessCountries,
            customerEntities.customerEntitiesEdqBusinessCountries) &&
        Objects.equals(
            customerEntitiesEdqHeadOfficeCountries,
            customerEntities.customerEntitiesEdqHeadOfficeCountries) &&
        Objects.equals(customerEntitiesCountriesAll, customerEntities.customerEntitiesCountriesAll)
        &&
        Objects.equals(
            customerEntitiesCountriesAllCodes, customerEntities.customerEntitiesCountriesAllCodes)
        &&
        Objects.equals(customerEntitiesEdqLoB, customerEntities.customerEntitiesEdqLoB) &&
        Objects.equals(
            customerEntitiesEdqPermission, customerEntities.customerEntitiesEdqPermission) &&
        Objects.equals(customerEntitiesTaxID, customerEntities.customerEntitiesTaxID) &&
        Objects.equals(customerEntitiesCity, customerEntities.customerEntitiesCity) &&
        Objects.equals(
            customerEntitiesProfileHyperlink, customerEntities.customerEntitiesProfileHyperlink) &&
        Objects.equals(
            customerEntitiesSearchHyperlink, customerEntities.customerEntitiesSearchHyperlink) &&
        Objects.equals(customerEntitiesEdqListKey, customerEntities.customerEntitiesEdqListKey) &&
        Objects.equals(customerEntitiesAddress, customerEntities.customerEntitiesAddress) &&
        Objects.equals(customerEntitiesEntityName, customerEntities.customerEntitiesEntityName) &&
        Objects.equals(customerEntitiesPostalCode, customerEntities.customerEntitiesPostalCode) &&
        Objects.equals(
            customerEntitiesPersonOrBusinessIndicator,
            customerEntities.customerEntitiesPersonOrBusinessIndicator) &&
        Objects.equals(
            customerEntitiesEdqCloseOfBusinessDate,
            customerEntities.customerEntitiesEdqCloseOfBusinessDate) &&
        Objects.equals(
            customerEntitiesOperatingCountries, customerEntities.customerEntitiesOperatingCountries)
        &&
        Objects.equals(
            customerEntitiesEdqOperatingCountriesCodes,
            customerEntities.customerEntitiesEdqOperatingCountriesCodes) &&
        Objects.equals(
            customerEntitiesRegistrationNumber, customerEntities.customerEntitiesRegistrationNumber)
        &&
        Objects.equals(
            customerEntitiesProfileNameType, customerEntities.customerEntitiesProfileNameType) &&
        Objects.equals(
            customerEntitiesEdqPartyRoleTypeDescription,
            customerEntities.customerEntitiesEdqPartyRoleTypeDescription) &&
        Objects.equals(
            customerEntitiesEdqPartyStatusCodeDescription,
            customerEntities.customerEntitiesEdqPartyStatusCodeDescription) &&
        Objects.equals(
            customerEntitiesEdqDay1SpikeFlag, customerEntities.customerEntitiesEdqDay1SpikeFlag) &&
        Objects.equals(
            customerEntitiesOriginalScriptName, customerEntities.customerEntitiesOriginalScriptName)
        &&
        Objects.equals(
            customerEntitiesProfileFullAddress, customerEntities.customerEntitiesProfileFullAddress)
        &&
        Objects.equals(
            customerEntitiesEdqScreeningMode, customerEntities.customerEntitiesEdqScreeningMode) &&
        Objects.equals(customerEntitiesEdqCaseKey, customerEntities.customerEntitiesEdqCaseKey) &&
        Objects.equals(customerEntitiesAddressType, customerEntities.customerEntitiesAddressType) &&
        Objects.equals(customerEntitiesSSCCodes, customerEntities.customerEntitiesSSCCodes) &&
        Objects.equals(customerEntitiesCTRPFragment, customerEntities.customerEntitiesCTRPFragment)
        &&
        Objects.equals(
            customerEntitiesRequestUserName, customerEntities.customerEntitiesRequestUserName) &&
        Objects.equals(
            customerEntitiesRequestDateTime, customerEntities.customerEntitiesRequestDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        customerEntitiesRecordId, customerEntitiesInputStream,
        customerEntitiesSourceSystemHistoryID, customerEntitiesCloseOfBusinessDate,
        customerEntitiesPartitionNumber, customerEntitiesSourceSystemIdentifier,
        customerEntitiesExternalProfileID, customerEntitiesConcatenatedProfileID,
        customerEntitiesProfileType, customerEntitiesPartyRoleTypeCode,
        customerEntitiesProfileStatus, customerEntitiesProfileSegment, customerEntitiesLoBName,
        customerEntitiesLoBRegion, customerEntitiesLoBCountry, customerEntitiesHSBCLegalEntityCode,
        customerEntitiesNameCategoryCode, customerEntitiesNameTypeCode,
        customerEntitiesNameLanguageTypeCode, customerEntitiesEntityNameOriginal,
        customerEntitiesPEPIndicator, customerEntitiesSpecialCategoryCustomer,
        customerEntitiesSourceAddressType, customerEntitiesAddressFormatCode,
        customerEntitiesAddressLanguageTypeCode, customerEntitiesSourceAddressLine1,
        customerEntitiesSourceAddressLine2, customerEntitiesSourceAddressLine3,
        customerEntitiesSourceAddressLine4, customerEntitiesSourceAddressLine5,
        customerEntitiesSourceAddressLine6, customerEntitiesSourceAddressLine7,
        customerEntitiesSourceAddressLine8, customerEntitiesSourceAddressLine9,
        customerEntitiesSourceAddressLine10, customerEntitiesSourcePostalCode,
        customerEntitiesSourceAddressCountry, customerEntitiesIdentificationDocument1,
        customerEntitiesIdentificationDocument2, customerEntitiesIdentificationDocument3,
        customerEntitiesIdentificationDocument4, customerEntitiesIdentificationDocument5,
        customerEntitiesIdentificationDocument6, customerEntitiesIdentificationDocument7,
        customerEntitiesIdentificationDocument8, customerEntitiesIdentificationDocument9,
        customerEntitiesIdentificationDocument10, customerEntitiesTradesWithCountries,
        customerEntitiesSubsidiariesOperatesInCountries, customerEntitiesExportCountries,
        customerEntitiesImportCountries, customerEntitiesCountriesOfIncorporation,
        customerEntitiesCountriesOfRegistrationOriginal, customerEntitiesCountriesOfBusiness,
        customerEntitiesCountriesOfHeadOffice, customerEntitiesEdqCustID,
        customerEntitiesEdqCustSubID, customerEntitiesEdqLoBCountryCode,
        customerEntitiesAddressCountry, customerEntitiesEdqAddressCountryCode,
        customerEntitiesEdqTradesWithCountries, customerEntitiesEdqExportsCountries,
        customerEntitiesEdqImportsCountries, customerEntitiesEdqIncorporationCountries,
        customerEntitiesEdqIncorporationCountriesCodes, customerEntitiesRegistrationCountry,
        customerEntitiesEdqRegiistrationCountriesCodes, customerEntitiesEdqBusinessCountries,
        customerEntitiesEdqHeadOfficeCountries, customerEntitiesCountriesAll,
        customerEntitiesCountriesAllCodes, customerEntitiesEdqLoB, customerEntitiesEdqPermission,
        customerEntitiesTaxID, customerEntitiesCity, customerEntitiesProfileHyperlink,
        customerEntitiesSearchHyperlink, customerEntitiesEdqListKey, customerEntitiesAddress,
        customerEntitiesEntityName, customerEntitiesPostalCode,
        customerEntitiesPersonOrBusinessIndicator, customerEntitiesEdqCloseOfBusinessDate,
        customerEntitiesOperatingCountries, customerEntitiesEdqOperatingCountriesCodes,
        customerEntitiesRegistrationNumber, customerEntitiesProfileNameType,
        customerEntitiesEdqPartyRoleTypeDescription, customerEntitiesEdqPartyStatusCodeDescription,
        customerEntitiesEdqDay1SpikeFlag, customerEntitiesOriginalScriptName,
        customerEntitiesProfileFullAddress, customerEntitiesEdqScreeningMode,
        customerEntitiesEdqCaseKey, customerEntitiesAddressType, customerEntitiesSSCCodes,
        customerEntitiesCTRPFragment, customerEntitiesRequestUserName,
        customerEntitiesRequestDateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerEntities {\n");

    sb
        .append("    customerEntitiesRecordId: ")
        .append(toIndentedString(customerEntitiesRecordId))
        .append("\n");
    sb
        .append("    customerEntitiesInputStream: ")
        .append(toIndentedString(customerEntitiesInputStream))
        .append("\n");
    sb
        .append("    customerEntitiesSourceSystemHistoryID: ")
        .append(toIndentedString(customerEntitiesSourceSystemHistoryID))
        .append("\n");
    sb
        .append("    customerEntitiesCloseOfBusinessDate: ")
        .append(toIndentedString(customerEntitiesCloseOfBusinessDate))
        .append("\n");
    sb
        .append("    customerEntitiesPartitionNumber: ")
        .append(toIndentedString(customerEntitiesPartitionNumber))
        .append("\n");
    sb
        .append("    customerEntitiesSourceSystemIdentifier: ")
        .append(toIndentedString(customerEntitiesSourceSystemIdentifier))
        .append("\n");
    sb
        .append("    customerEntitiesExternalProfileID: ")
        .append(toIndentedString(customerEntitiesExternalProfileID))
        .append("\n");
    sb
        .append("    customerEntitiesConcatenatedProfileID: ")
        .append(toIndentedString(customerEntitiesConcatenatedProfileID))
        .append("\n");
    sb
        .append("    customerEntitiesProfileType: ")
        .append(toIndentedString(customerEntitiesProfileType))
        .append("\n");
    sb
        .append("    customerEntitiesPartyRoleTypeCode: ")
        .append(toIndentedString(customerEntitiesPartyRoleTypeCode))
        .append("\n");
    sb
        .append("    customerEntitiesProfileStatus: ")
        .append(toIndentedString(customerEntitiesProfileStatus))
        .append("\n");
    sb
        .append("    customerEntitiesProfileSegment: ")
        .append(toIndentedString(customerEntitiesProfileSegment))
        .append("\n");
    sb
        .append("    customerEntitiesLoBName: ")
        .append(toIndentedString(customerEntitiesLoBName))
        .append("\n");
    sb
        .append("    customerEntitiesLoBRegion: ")
        .append(toIndentedString(customerEntitiesLoBRegion))
        .append("\n");
    sb
        .append("    customerEntitiesLoBCountry: ")
        .append(toIndentedString(customerEntitiesLoBCountry))
        .append("\n");
    sb
        .append("    customerEntitiesHSBCLegalEntityCode: ")
        .append(toIndentedString(customerEntitiesHSBCLegalEntityCode))
        .append("\n");
    sb
        .append("    customerEntitiesNameCategoryCode: ")
        .append(toIndentedString(customerEntitiesNameCategoryCode))
        .append("\n");
    sb
        .append("    customerEntitiesNameTypeCode: ")
        .append(toIndentedString(customerEntitiesNameTypeCode))
        .append("\n");
    sb
        .append("    customerEntitiesNameLanguageTypeCode: ")
        .append(toIndentedString(customerEntitiesNameLanguageTypeCode))
        .append("\n");
    sb
        .append("    customerEntitiesEntityNameOriginal: ")
        .append(toIndentedString(customerEntitiesEntityNameOriginal))
        .append("\n");
    sb
        .append("    customerEntitiesPEPIndicator: ")
        .append(toIndentedString(customerEntitiesPEPIndicator))
        .append("\n");
    sb
        .append("    customerEntitiesSpecialCategoryCustomer: ")
        .append(toIndentedString(customerEntitiesSpecialCategoryCustomer))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressType: ")
        .append(toIndentedString(customerEntitiesSourceAddressType))
        .append("\n");
    sb
        .append("    customerEntitiesAddressFormatCode: ")
        .append(toIndentedString(customerEntitiesAddressFormatCode))
        .append("\n");
    sb
        .append("    customerEntitiesAddressLanguageTypeCode: ")
        .append(toIndentedString(customerEntitiesAddressLanguageTypeCode))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine1: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine1))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine2: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine2))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine3: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine3))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine4: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine4))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine5: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine5))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine6: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine6))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine7: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine7))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine8: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine8))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine9: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine9))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressLine10: ")
        .append(toIndentedString(customerEntitiesSourceAddressLine10))
        .append("\n");
    sb
        .append("    customerEntitiesSourcePostalCode: ")
        .append(toIndentedString(customerEntitiesSourcePostalCode))
        .append("\n");
    sb
        .append("    customerEntitiesSourceAddressCountry: ")
        .append(toIndentedString(customerEntitiesSourceAddressCountry))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument1: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument1))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument2: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument2))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument3: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument3))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument4: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument4))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument5: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument5))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument6: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument6))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument7: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument7))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument8: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument8))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument9: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument9))
        .append("\n");
    sb
        .append("    customerEntitiesIdentificationDocument10: ")
        .append(toIndentedString(customerEntitiesIdentificationDocument10))
        .append("\n");
    sb
        .append("    customerEntitiesTradesWithCountries: ")
        .append(toIndentedString(customerEntitiesTradesWithCountries))
        .append("\n");
    sb
        .append("    customerEntitiesSubsidiariesOperatesInCountries: ")
        .append(toIndentedString(customerEntitiesSubsidiariesOperatesInCountries))
        .append("\n");
    sb
        .append("    customerEntitiesExportCountries: ")
        .append(toIndentedString(customerEntitiesExportCountries))
        .append("\n");
    sb
        .append("    customerEntitiesImportCountries: ")
        .append(toIndentedString(customerEntitiesImportCountries))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesOfIncorporation: ")
        .append(toIndentedString(customerEntitiesCountriesOfIncorporation))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesOfRegistrationOriginal: ")
        .append(toIndentedString(customerEntitiesCountriesOfRegistrationOriginal))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesOfBusiness: ")
        .append(toIndentedString(customerEntitiesCountriesOfBusiness))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesOfHeadOffice: ")
        .append(toIndentedString(customerEntitiesCountriesOfHeadOffice))
        .append("\n");
    sb
        .append("    customerEntitiesEdqCustID: ")
        .append(toIndentedString(customerEntitiesEdqCustID))
        .append("\n");
    sb
        .append("    customerEntitiesEdqCustSubID: ")
        .append(toIndentedString(customerEntitiesEdqCustSubID))
        .append("\n");
    sb
        .append("    customerEntitiesEdqLoBCountryCode: ")
        .append(toIndentedString(customerEntitiesEdqLoBCountryCode))
        .append("\n");
    sb
        .append("    customerEntitiesAddressCountry: ")
        .append(toIndentedString(customerEntitiesAddressCountry))
        .append("\n");
    sb
        .append("    customerEntitiesEdqAddressCountryCode: ")
        .append(toIndentedString(customerEntitiesEdqAddressCountryCode))
        .append("\n");
    sb
        .append("    customerEntitiesEdqTradesWithCountries: ")
        .append(toIndentedString(customerEntitiesEdqTradesWithCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqExportsCountries: ")
        .append(toIndentedString(customerEntitiesEdqExportsCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqImportsCountries: ")
        .append(toIndentedString(customerEntitiesEdqImportsCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqIncorporationCountries: ")
        .append(toIndentedString(customerEntitiesEdqIncorporationCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqIncorporationCountriesCodes: ")
        .append(toIndentedString(customerEntitiesEdqIncorporationCountriesCodes))
        .append("\n");
    sb
        .append("    customerEntitiesRegistrationCountry: ")
        .append(toIndentedString(customerEntitiesRegistrationCountry))
        .append("\n");
    sb
        .append("    customerEntitiesEdqRegiistrationCountriesCodes: ")
        .append(toIndentedString(customerEntitiesEdqRegiistrationCountriesCodes))
        .append("\n");
    sb
        .append("    customerEntitiesEdqBusinessCountries: ")
        .append(toIndentedString(customerEntitiesEdqBusinessCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqHeadOfficeCountries: ")
        .append(toIndentedString(customerEntitiesEdqHeadOfficeCountries))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesAll: ")
        .append(toIndentedString(customerEntitiesCountriesAll))
        .append("\n");
    sb
        .append("    customerEntitiesCountriesAllCodes: ")
        .append(toIndentedString(customerEntitiesCountriesAllCodes))
        .append("\n");
    sb
        .append("    customerEntitiesEdqLoB: ")
        .append(toIndentedString(customerEntitiesEdqLoB))
        .append("\n");
    sb
        .append("    customerEntitiesEdqPermission: ")
        .append(toIndentedString(customerEntitiesEdqPermission))
        .append("\n");
    sb
        .append("    customerEntitiesTaxID: ")
        .append(toIndentedString(customerEntitiesTaxID))
        .append("\n");
    sb
        .append("    customerEntitiesCity: ")
        .append(toIndentedString(customerEntitiesCity))
        .append("\n");
    sb
        .append("    customerEntitiesProfileHyperlink: ")
        .append(toIndentedString(customerEntitiesProfileHyperlink))
        .append("\n");
    sb
        .append("    customerEntitiesSearchHyperlink: ")
        .append(toIndentedString(customerEntitiesSearchHyperlink))
        .append("\n");
    sb
        .append("    customerEntitiesEdqListKey: ")
        .append(toIndentedString(customerEntitiesEdqListKey))
        .append("\n");
    sb
        .append("    customerEntitiesAddress: ")
        .append(toIndentedString(customerEntitiesAddress))
        .append("\n");
    sb
        .append("    customerEntitiesEntityName: ")
        .append(toIndentedString(customerEntitiesEntityName))
        .append("\n");
    sb
        .append("    customerEntitiesPostalCode: ")
        .append(toIndentedString(customerEntitiesPostalCode))
        .append("\n");
    sb
        .append("    customerEntitiesPersonOrBusinessIndicator: ")
        .append(toIndentedString(customerEntitiesPersonOrBusinessIndicator))
        .append("\n");
    sb
        .append("    customerEntitiesEdqCloseOfBusinessDate: ")
        .append(toIndentedString(customerEntitiesEdqCloseOfBusinessDate))
        .append("\n");
    sb
        .append("    customerEntitiesOperatingCountries: ")
        .append(toIndentedString(customerEntitiesOperatingCountries))
        .append("\n");
    sb
        .append("    customerEntitiesEdqOperatingCountriesCodes: ")
        .append(toIndentedString(customerEntitiesEdqOperatingCountriesCodes))
        .append("\n");
    sb
        .append("    customerEntitiesRegistrationNumber: ")
        .append(toIndentedString(customerEntitiesRegistrationNumber))
        .append("\n");
    sb
        .append("    customerEntitiesProfileNameType: ")
        .append(toIndentedString(customerEntitiesProfileNameType))
        .append("\n");
    sb
        .append("    customerEntitiesEdqPartyRoleTypeDescription: ")
        .append(toIndentedString(customerEntitiesEdqPartyRoleTypeDescription))
        .append("\n");
    sb
        .append("    customerEntitiesEdqPartyStatusCodeDescription: ")
        .append(toIndentedString(customerEntitiesEdqPartyStatusCodeDescription))
        .append("\n");
    sb
        .append("    customerEntitiesEdqDay1SpikeFlag: ")
        .append(toIndentedString(customerEntitiesEdqDay1SpikeFlag))
        .append("\n");
    sb
        .append("    customerEntitiesOriginalScriptName: ")
        .append(toIndentedString(customerEntitiesOriginalScriptName))
        .append("\n");
    sb
        .append("    customerEntitiesProfileFullAddress: ")
        .append(toIndentedString(customerEntitiesProfileFullAddress))
        .append("\n");
    sb
        .append("    customerEntitiesEdqScreeningMode: ")
        .append(toIndentedString(customerEntitiesEdqScreeningMode))
        .append("\n");
    sb
        .append("    customerEntitiesEdqCaseKey: ")
        .append(toIndentedString(customerEntitiesEdqCaseKey))
        .append("\n");
    sb
        .append("    customerEntitiesAddressType: ")
        .append(toIndentedString(customerEntitiesAddressType))
        .append("\n");
    sb
        .append("    customerEntitiesSSCCodes: ")
        .append(toIndentedString(customerEntitiesSSCCodes))
        .append("\n");
    sb
        .append("    customerEntitiesCTRPFragment: ")
        .append(toIndentedString(customerEntitiesCTRPFragment))
        .append("\n");
    sb
        .append("    customerEntitiesRequestUserName: ")
        .append(toIndentedString(customerEntitiesRequestUserName))
        .append("\n");
    sb
        .append("    customerEntitiesRequestDateTime: ")
        .append(toIndentedString(customerEntitiesRequestDateTime))
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
