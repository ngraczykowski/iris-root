package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CustomerEntities {

  private Integer caseId = null;
  private BigDecimal recordId = null;
  private String inputStream = null;
  private Integer sourceSystemHistoryId = null;
  private String closeOfBusinessDate = null;
  private Integer partitionNumber = null;
  private String sourceSystemIdentifier = null;
  private String externalProfileId = null;
  private String concatenatedProfileId = null;
  private String profileType = null;
  private String partyRoleTypeCode = null;
  private String profileStatus = null;
  private String profileSegment = null;
  private String lobName = null;
  private String lobRegion = null;
  private String lobCountry = null;
  private String hsbcLegalEntityCode = null;
  private String nameCategoryCode = null;
  private String nameTypeCode = null;
  private String nameLanguageTypeCode = null;
  private String entityNameOriginal = null;
  private String pepIndicator = null;
  private String specialCategoryCustomer = null;
  private String sourceAddressType = null;
  private String addressFormatCode = null;
  private String addressLanguageTypeCode = null;
  private String sourceAddressLine1 = null;
  private String sourceAddressLine2 = null;
  private String sourceAddressLine3 = null;
  private String sourceAddressLine4 = null;
  private String sourceAddressLine5 = null;
  private String sourceAddressLine6 = null;
  private String sourceAddressLine7 = null;
  private String sourceAddressLine8 = null;
  private String sourceAddressLine9 = null;
  private String sourceAddressLine10 = null;
  private String sourcePostalCode = null;
  private String sourceAddressCountry = null;
  private String identificationDocument1 = null;
  private String identificationDocument2 = null;
  private String identificationDocument3 = null;
  private String identificationDocument4 = null;
  private String identificationDocument5 = null;
  private String identificationDocument6 = null;
  private String identificationDocument7 = null;
  private String identificationDocument8 = null;
  private String identificationDocument9 = null;
  private String identificationDocument10 = null;
  private String tradesWithCountries = null;
  private String subsidiariesOperatesInCountries = null;
  private String exportCountries = null;
  private String importCountries = null;
  private String countriesOfIncorporation = null;
  private String countriesOfRegistrationOriginal = null;
  private String countriesOfBusiness = null;
  private String countriesOfHeadOffice = null;
  private String edqCustId = null;
  private String edqCustSubId = null;
  private String edqLobCountryCode = null;
  private String addressCountry = null;
  private String edqAddressCountryCode = null;
  private String edqTradesWithCountries = null;
  private String edqExportsCountries = null;
  private String edqImportsCountries = null;
  private String edqIncorporationCountries = null;
  private String edqIncorporationCountriesCodes = null;
  private String registrationCountry = null;
  private String edqRegiistrationCountriesCodes = null;
  private String edqBusinessCountries = null;
  private String edqHeadOfficeCountries = null;
  private String countriesAll = null;
  private String countriesAllCodes = null;
  private String edqLob = null;
  private String edqPermission = null;
  private String taxId = null;
  private String city = null;
  private String profileHyperlink = null;
  private String searchHyperlink = null;
  private String edqListKey = null;
  private String address = null;
  private String entityName = null;
  private String postalCode = null;
  private String personOrBusinessIndicator = null;
  private String edqCloseOfBusinessDate = null;
  private String operatingCountries = null;
  private String edqOperatingCountriesCodes = null;
  private String registrationNumber = null;
  private String profileNameType = null;
  private String edqPartyRoleTypeDescription = null;
  private String edqPartyStatusCodeDescription = null;
  private String edqDay1SpikeFlag = null;
  private String originalScriptName = null;
  private String profileFullAddress = null;
  private String edqScreeningMode = null;
  private String edqCaseKey = null;
  private String addressType = null;
  private String sscCodes = null;
  private String ctrpFragment = null;
  private String requestUserName = null;
  private String requestDateTime = null;

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
    this.caseId = caseId;
  }

  /**
   * Refers to a Customer name record (relationship) within an alert where there are multiple
   * Customer name records within the same alert. This is the working record ID.
   **/

  @Schema(description = "Refers to a Customer name record (relationship) within an alert where there are multiple Customer name records within the same alert. This is the working record ID.")
  @JsonProperty("recordId")
  public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  /**
   * Denotes the type of data, for example&amp;#58; Customer Data
   **/

  @Schema(description = "Denotes the type of data, for example&#58; Customer Data")
  @JsonProperty("inputStream")
  public String getInputStream() {
    return inputStream;
  }

  public void setInputStream(String inputStream) {
    this.inputStream = inputStream;
  }

  /**
   * Identifier used during reconciliation to store customer information
   **/

  @Schema(description = "Identifier used during reconciliation to store customer information")
  @JsonProperty("sourceSystemHistoryId")
  public Integer getSourceSystemHistoryId() {
    return sourceSystemHistoryId;
  }

  public void setSourceSystemHistoryId(Integer sourceSystemHistoryId) {
    this.sourceSystemHistoryId = sourceSystemHistoryId;
  }

  /**
   * The date the business ceased trading
   **/

  @Schema(description = "The date the business ceased trading")
  @JsonProperty("closeOfBusinessDate")
  public String getCloseOfBusinessDate() {
    return closeOfBusinessDate;
  }

  public void setCloseOfBusinessDate(String closeOfBusinessDate) {
    this.closeOfBusinessDate = closeOfBusinessDate;
  }

  /**
   * Where a customer feed is larger than a certain threshold it will be partitioned into sections.
   * The partion number denotes which partition this record has come from.
   **/

  @Schema(description = "Where a customer feed is larger than a certain threshold it will be partitioned into sections. The partion number denotes which partition this record has come from.")
  @JsonProperty("partitionNumber")
  public Integer getPartitionNumber() {
    return partitionNumber;
  }

  public void setPartitionNumber(Integer partitionNumber) {
    this.partitionNumber = partitionNumber;
  }

  /**
   * The Identifier used to identify where the feed originated from
   **/

  @Schema(description = "The Identifier used to identify where the feed originated from")
  @JsonProperty("sourceSystemIdentifier")
  public String getSourceSystemIdentifier() {
    return sourceSystemIdentifier;
  }

  public void setSourceSystemIdentifier(String sourceSystemIdentifier) {
    this.sourceSystemIdentifier = sourceSystemIdentifier;
  }

  /**
   * Customer ID from source system
   **/

  @Schema(description = "Customer ID from source system")
  @JsonProperty("externalProfileId")
  public String getExternalProfileId() {
    return externalProfileId;
  }

  public void setExternalProfileId(String externalProfileId) {
    this.externalProfileId = externalProfileId;
  }

  /**
   * Customer ID from source system concatenated with the source system name to ensure the customer
   * ID is unique.
   **/

  @Schema(description = "Customer ID from source system concatenated with the source system name to ensure the customer ID is unique.")
  @JsonProperty("concatenatedProfileId")
  public String getConcatenatedProfileId() {
    return concatenatedProfileId;
  }

  public void setConcatenatedProfileId(String concatenatedProfileId) {
    this.concatenatedProfileId = concatenatedProfileId;
  }

  /**
   * Party type code
   **/

  @Schema(description = "Party type code")
  @JsonProperty("profileType")
  public String getProfileType() {
    return profileType;
  }

  public void setProfileType(String profileType) {
    this.profileType = profileType;
  }

  /**
   * Provides the ID relating to the customer type
   **/

  @Schema(description = "Provides the ID relating to the customer type")
  @JsonProperty("partyRoleTypeCode")
  public String getPartyRoleTypeCode() {
    return partyRoleTypeCode;
  }

  public void setPartyRoleTypeCode(String partyRoleTypeCode) {
    this.partyRoleTypeCode = partyRoleTypeCode;
  }

  /**
   * Partyy status code
   **/

  @Schema(description = "Partyy status code")
  @JsonProperty("profileStatus")
  public String getProfileStatus() {
    return profileStatus;
  }

  public void setProfileStatus(String profileStatus) {
    this.profileStatus = profileStatus;
  }

  /**
   * Line of Business customer segment.
   **/

  @Schema(description = "Line of Business customer segment.")
  @JsonProperty("profileSegment")
  public String getProfileSegment() {
    return profileSegment;
  }

  public void setProfileSegment(String profileSegment) {
    this.profileSegment = profileSegment;
  }

  /**
   * Line of Business name
   **/

  @Schema(description = "Line of Business name")
  @JsonProperty("lobName")
  public String getLobName() {
    return lobName;
  }

  public void setLobName(String lobName) {
    this.lobName = lobName;
  }

  /**
   * Line of Business region
   **/

  @Schema(description = "Line of Business region")
  @JsonProperty("lobRegion")
  public String getLobRegion() {
    return lobRegion;
  }

  public void setLobRegion(String lobRegion) {
    this.lobRegion = lobRegion;
  }

  /**
   * Line of Business Country Code
   **/

  @Schema(description = "Line of Business Country Code")
  @JsonProperty("lobCountry")
  public String getLobCountry() {
    return lobCountry;
  }

  public void setLobCountry(String lobCountry) {
    this.lobCountry = lobCountry;
  }

  /**
   * HSBC legal entity code
   **/

  @Schema(description = "HSBC legal entity code")
  @JsonProperty("hsbcLegalEntityCode")
  public String getHsbcLegalEntityCode() {
    return hsbcLegalEntityCode;
  }

  public void setHsbcLegalEntityCode(String hsbcLegalEntityCode) {
    this.hsbcLegalEntityCode = hsbcLegalEntityCode;
  }

  /**
   * Customer name category code
   **/

  @Schema(description = "Customer name category code")
  @JsonProperty("nameCategoryCode")
  public String getNameCategoryCode() {
    return nameCategoryCode;
  }

  public void setNameCategoryCode(String nameCategoryCode) {
    this.nameCategoryCode = nameCategoryCode;
  }

  /**
   * Name Type Code
   **/

  @Schema(description = "Name Type Code")
  @JsonProperty("nameTypeCode")
  public String getNameTypeCode() {
    return nameTypeCode;
  }

  public void setNameTypeCode(String nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
  }

  /**
   * The language associated to the name type code
   **/

  @Schema(description = "The language associated to the name type code")
  @JsonProperty("nameLanguageTypeCode")
  public String getNameLanguageTypeCode() {
    return nameLanguageTypeCode;
  }

  public void setNameLanguageTypeCode(String nameLanguageTypeCode) {
    this.nameLanguageTypeCode = nameLanguageTypeCode;
  }

  /**
   * The Entity name as originally provided
   **/

  @Schema(description = "The Entity name as originally provided")
  @JsonProperty("entityNameOriginal")
  public String getEntityNameOriginal() {
    return entityNameOriginal;
  }

  public void setEntityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
  }

  /**
   * Indicates if the record is regarded as a PEP or not in the source system
   **/

  @Schema(description = "Indicates if the record is regarded as a PEP or not in the source system")
  @JsonProperty("pepIndicator")
  public String getPepIndicator() {
    return pepIndicator;
  }

  public void setPepIndicator(String pepIndicator) {
    this.pepIndicator = pepIndicator;
  }

  /**
   * Indicates if the record is regarded as a Special Chategory Person or not in the source system
   **/

  @Schema(description = "Indicates if the record is regarded as a Special Chategory Person or not in the source system")
  @JsonProperty("specialCategoryCustomer")
  public String getSpecialCategoryCustomer() {
    return specialCategoryCustomer;
  }

  public void setSpecialCategoryCustomer(String specialCategoryCustomer) {
    this.specialCategoryCustomer = specialCategoryCustomer;
  }

  /**
   * The Address Type provided for this customer record
   **/

  @Schema(description = "The Address Type provided for this customer record")
  @JsonProperty("sourceAddressType")
  public String getSourceAddressType() {
    return sourceAddressType;
  }

  public void setSourceAddressType(String sourceAddressType) {
    this.sourceAddressType = sourceAddressType;
  }

  /**
   * Indicates if the address is provided in a structured or unstructured format
   **/

  @Schema(description = "Indicates if the address is provided in a structured or unstructured format")
  @JsonProperty("addressFormatCode")
  public String getAddressFormatCode() {
    return addressFormatCode;
  }

  public void setAddressFormatCode(String addressFormatCode) {
    this.addressFormatCode = addressFormatCode;
  }

  /**
   * Indicates if the address is provided in the primary or secondary language
   **/

  @Schema(description = "Indicates if the address is provided in the primary or secondary language")
  @JsonProperty("addressLanguageTypeCode")
  public String getAddressLanguageTypeCode() {
    return addressLanguageTypeCode;
  }

  public void setAddressLanguageTypeCode(String addressLanguageTypeCode) {
    this.addressLanguageTypeCode = addressLanguageTypeCode;
  }

  /**
   * Address Line 1 as provided in the customer record
   **/

  @Schema(description = "Address Line 1 as provided in the customer record")
  @JsonProperty("sourceAddressLine1")
  public String getSourceAddressLine1() {
    return sourceAddressLine1;
  }

  public void setSourceAddressLine1(String sourceAddressLine1) {
    this.sourceAddressLine1 = sourceAddressLine1;
  }

  /**
   * Address Line 2 as provided in the customer record
   **/

  @Schema(description = "Address Line 2 as provided in the customer record")
  @JsonProperty("sourceAddressLine2")
  public String getSourceAddressLine2() {
    return sourceAddressLine2;
  }

  public void setSourceAddressLine2(String sourceAddressLine2) {
    this.sourceAddressLine2 = sourceAddressLine2;
  }

  /**
   * Address Line 3 as provided in the customer record
   **/

  @Schema(description = "Address Line 3 as provided in the customer record")
  @JsonProperty("sourceAddressLine3")
  public String getSourceAddressLine3() {
    return sourceAddressLine3;
  }

  public void setSourceAddressLine3(String sourceAddressLine3) {
    this.sourceAddressLine3 = sourceAddressLine3;
  }

  /**
   * Address Line 4 as provided in the customer record
   **/

  @Schema(description = "Address Line 4 as provided in the customer record")
  @JsonProperty("sourceAddressLine4")
  public String getSourceAddressLine4() {
    return sourceAddressLine4;
  }

  public void setSourceAddressLine4(String sourceAddressLine4) {
    this.sourceAddressLine4 = sourceAddressLine4;
  }

  /**
   * Address Line 5 as provided in the customer record
   **/

  @Schema(description = "Address Line 5 as provided in the customer record")
  @JsonProperty("sourceAddressLine5")
  public String getSourceAddressLine5() {
    return sourceAddressLine5;
  }

  public void setSourceAddressLine5(String sourceAddressLine5) {
    this.sourceAddressLine5 = sourceAddressLine5;
  }

  /**
   * Address Line 6 as provided in the customer record
   **/

  @Schema(description = "Address Line 6 as provided in the customer record")
  @JsonProperty("sourceAddressLine6")
  public String getSourceAddressLine6() {
    return sourceAddressLine6;
  }

  public void setSourceAddressLine6(String sourceAddressLine6) {
    this.sourceAddressLine6 = sourceAddressLine6;
  }

  /**
   * Address Line 7 as provided in the customer record
   **/

  @Schema(description = "Address Line 7 as provided in the customer record")
  @JsonProperty("sourceAddressLine7")
  public String getSourceAddressLine7() {
    return sourceAddressLine7;
  }

  public void setSourceAddressLine7(String sourceAddressLine7) {
    this.sourceAddressLine7 = sourceAddressLine7;
  }

  /**
   * Address Line 8 as provided in the customer record
   **/

  @Schema(description = "Address Line 8 as provided in the customer record")
  @JsonProperty("sourceAddressLine8")
  public String getSourceAddressLine8() {
    return sourceAddressLine8;
  }

  public void setSourceAddressLine8(String sourceAddressLine8) {
    this.sourceAddressLine8 = sourceAddressLine8;
  }

  /**
   * Address Line 9 as provided in the customer record
   **/

  @Schema(description = "Address Line 9 as provided in the customer record")
  @JsonProperty("sourceAddressLine9")
  public String getSourceAddressLine9() {
    return sourceAddressLine9;
  }

  public void setSourceAddressLine9(String sourceAddressLine9) {
    this.sourceAddressLine9 = sourceAddressLine9;
  }

  /**
   * Address Line 10 as provided in the customer record
   **/

  @Schema(description = "Address Line 10 as provided in the customer record")
  @JsonProperty("sourceAddressLine10")
  public String getSourceAddressLine10() {
    return sourceAddressLine10;
  }

  public void setSourceAddressLine10(String sourceAddressLine10) {
    this.sourceAddressLine10 = sourceAddressLine10;
  }

  /**
   * Address Postcode as provided in the customer record
   **/

  @Schema(description = "Address Postcode as provided in the customer record")
  @JsonProperty("sourcePostalCode")
  public String getSourcePostalCode() {
    return sourcePostalCode;
  }

  public void setSourcePostalCode(String sourcePostalCode) {
    this.sourcePostalCode = sourcePostalCode;
  }

  /**
   * Address Country as provided in the customer record
   **/

  @Schema(description = "Address Country as provided in the customer record")
  @JsonProperty("sourceAddressCountry")
  public String getSourceAddressCountry() {
    return sourceAddressCountry;
  }

  public void setSourceAddressCountry(String sourceAddressCountry) {
    this.sourceAddressCountry = sourceAddressCountry;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument1")
  public String getIdentificationDocument1() {
    return identificationDocument1;
  }

  public void setIdentificationDocument1(String identificationDocument1) {
    this.identificationDocument1 = identificationDocument1;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument2")
  public String getIdentificationDocument2() {
    return identificationDocument2;
  }

  public void setIdentificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument3")
  public String getIdentificationDocument3() {
    return identificationDocument3;
  }

  public void setIdentificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument4")
  public String getIdentificationDocument4() {
    return identificationDocument4;
  }

  public void setIdentificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument5")
  public String getIdentificationDocument5() {
    return identificationDocument5;
  }

  public void setIdentificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument6")
  public String getIdentificationDocument6() {
    return identificationDocument6;
  }

  public void setIdentificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument7")
  public String getIdentificationDocument7() {
    return identificationDocument7;
  }

  public void setIdentificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument8")
  public String getIdentificationDocument8() {
    return identificationDocument8;
  }

  public void setIdentificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument9")
  public String getIdentificationDocument9() {
    return identificationDocument9;
  }

  public void setIdentificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
  }

  /**
   * Identification document
   **/

  @Schema(description = "Identification document")
  @JsonProperty("identificationDocument10")
  public String getIdentificationDocument10() {
    return identificationDocument10;
  }

  public void setIdentificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
  }

  /**
   * List of countries the customer trades with
   **/

  @Schema(description = "List of countries the customer trades with")
  @JsonProperty("tradesWithCountries")
  public String getTradesWithCountries() {
    return tradesWithCountries;
  }

  public void setTradesWithCountries(String tradesWithCountries) {
    this.tradesWithCountries = tradesWithCountries;
  }

  /**
   * List of countries the customer&#x27;s subsidiaries operate in
   **/

  @Schema(description = "List of countries the customer's subsidiaries operate in")
  @JsonProperty("subsidiariesOperatesInCountries")
  public String getSubsidiariesOperatesInCountries() {
    return subsidiariesOperatesInCountries;
  }

  public void setSubsidiariesOperatesInCountries(String subsidiariesOperatesInCountries) {
    this.subsidiariesOperatesInCountries = subsidiariesOperatesInCountries;
  }

  /**
   * List of countries the customer exports to
   **/

  @Schema(description = "List of countries the customer exports to")
  @JsonProperty("exportCountries")
  public String getExportCountries() {
    return exportCountries;
  }

  public void setExportCountries(String exportCountries) {
    this.exportCountries = exportCountries;
  }

  /**
   * List of countries the customer imports from
   **/

  @Schema(description = "List of countries the customer imports from")
  @JsonProperty("importCountries")
  public String getImportCountries() {
    return importCountries;
  }

  public void setImportCountries(String importCountries) {
    this.importCountries = importCountries;
  }

  /**
   * List of countires where the customer was/is incorporated
   **/

  @Schema(description = "List of countires where the customer was/is incorporated")
  @JsonProperty("countriesOfIncorporation")
  public String getCountriesOfIncorporation() {
    return countriesOfIncorporation;
  }

  public void setCountriesOfIncorporation(String countriesOfIncorporation) {
    this.countriesOfIncorporation = countriesOfIncorporation;
  }

  /**
   * List of countries where the customer is registered
   **/

  @Schema(description = "List of countries where the customer is registered")
  @JsonProperty("countriesOfRegistrationOriginal")
  public String getCountriesOfRegistrationOriginal() {
    return countriesOfRegistrationOriginal;
  }

  public void setCountriesOfRegistrationOriginal(String countriesOfRegistrationOriginal) {
    this.countriesOfRegistrationOriginal = countriesOfRegistrationOriginal;
  }

  /**
   * List of countries where the customer conducts business
   **/

  @Schema(description = "List of countries where the customer conducts business")
  @JsonProperty("countriesOfBusiness")
  public String getCountriesOfBusiness() {
    return countriesOfBusiness;
  }

  public void setCountriesOfBusiness(String countriesOfBusiness) {
    this.countriesOfBusiness = countriesOfBusiness;
  }

  /**
   * List of countries where the customer has a head office
   **/

  @Schema(description = "List of countries where the customer has a head office")
  @JsonProperty("countriesOfHeadOffice")
  public String getCountriesOfHeadOffice() {
    return countriesOfHeadOffice;
  }

  public void setCountriesOfHeadOffice(String countriesOfHeadOffice) {
    this.countriesOfHeadOffice = countriesOfHeadOffice;
  }

  /**
   * Customer ID from source system concatenated with the source system name to ensure the customer
   * ID is unique
   **/

  @Schema(description = "Customer ID from source system concatenated with the source system name to ensure the customer ID is unique")
  @JsonProperty("edqCustId")
  public String getEdqCustId() {
    return edqCustId;
  }

  public void setEdqCustId(String edqCustId) {
    this.edqCustId = edqCustId;
  }

  /**
   * Customer ID from the source system
   **/

  @Schema(description = "Customer ID from the source system")
  @JsonProperty("edqCustSubId")
  public String getEdqCustSubId() {
    return edqCustSubId;
  }

  public void setEdqCustSubId(String edqCustSubId) {
    this.edqCustSubId = edqCustSubId;
  }

  /**
   * The ISO country code the the Line of Business this record is associated to
   **/

  @Schema(description = "The ISO country code the the Line of Business this record is associated to")
  @JsonProperty("edqLobCountryCode")
  public String getEdqLobCountryCode() {
    return edqLobCountryCode;
  }

  public void setEdqLobCountryCode(String edqLobCountryCode) {
    this.edqLobCountryCode = edqLobCountryCode;
  }

  /**
   * The refined and or Derived country names associated to the customer record
   **/

  @Schema(description = "The refined and or Derived country names associated to the customer record")
  @JsonProperty("addressCountry")
  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  /**
   * The ISO country code Derived from the customer&#x27;s address
   **/

  @Schema(description = "The ISO country code Derived from the customer's address")
  @JsonProperty("edqAddressCountryCode")
  public String getEdqAddressCountryCode() {
    return edqAddressCountryCode;
  }

  public void setEdqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
  }

  /**
   * A standardised list of countries the customer trades with
   **/

  @Schema(description = "A standardised list of countries the customer trades with")
  @JsonProperty("edqTradesWithCountries")
  public String getEdqTradesWithCountries() {
    return edqTradesWithCountries;
  }

  public void setEdqTradesWithCountries(String edqTradesWithCountries) {
    this.edqTradesWithCountries = edqTradesWithCountries;
  }

  /**
   * A standardised list of countries the customer exports to
   **/

  @Schema(description = "A standardised list of countries the customer exports to")
  @JsonProperty("edqExportsCountries")
  public String getEdqExportsCountries() {
    return edqExportsCountries;
  }

  public void setEdqExportsCountries(String edqExportsCountries) {
    this.edqExportsCountries = edqExportsCountries;
  }

  /**
   * A standardised list of countries the customer imports from
   **/

  @Schema(description = "A standardised list of countries the customer imports from")
  @JsonProperty("edqImportsCountries")
  public String getEdqImportsCountries() {
    return edqImportsCountries;
  }

  public void setEdqImportsCountries(String edqImportsCountries) {
    this.edqImportsCountries = edqImportsCountries;
  }

  /**
   * A standardised list of countires where the customer was/is incorporated
   **/

  @Schema(description = "A standardised list of countires where the customer was/is incorporated")
  @JsonProperty("edqIncorporationCountries")
  public String getEdqIncorporationCountries() {
    return edqIncorporationCountries;
  }

  public void setEdqIncorporationCountries(String edqIncorporationCountries) {
    this.edqIncorporationCountries = edqIncorporationCountries;
  }

  /**
   * A standardised list of country codes where the customer was/is incorporated
   **/

  @Schema(description = "A standardised list of country codes where the customer was/is incorporated")
  @JsonProperty("edqIncorporationCountriesCodes")
  public String getEdqIncorporationCountriesCodes() {
    return edqIncorporationCountriesCodes;
  }

  public void setEdqIncorporationCountriesCodes(String edqIncorporationCountriesCodes) {
    this.edqIncorporationCountriesCodes = edqIncorporationCountriesCodes;
  }

  /**
   * A standardised list of countries where the customer is registered
   **/

  @Schema(description = "A standardised list of countries where the customer is registered")
  @JsonProperty("registrationCountry")
  public String getRegistrationCountry() {
    return registrationCountry;
  }

  public void setRegistrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
  }

  /**
   * A standardised list of country codes where the customer is registered
   **/

  @Schema(description = "A standardised list of country codes where the customer is registered")
  @JsonProperty("edqRegiistrationCountriesCodes")
  public String getEdqRegiistrationCountriesCodes() {
    return edqRegiistrationCountriesCodes;
  }

  public void setEdqRegiistrationCountriesCodes(String edqRegiistrationCountriesCodes) {
    this.edqRegiistrationCountriesCodes = edqRegiistrationCountriesCodes;
  }

  /**
   * A standardised list of countries where the customer conducts business
   **/

  @Schema(description = "A standardised list of countries where the customer conducts business")
  @JsonProperty("edqBusinessCountries")
  public String getEdqBusinessCountries() {
    return edqBusinessCountries;
  }

  public void setEdqBusinessCountries(String edqBusinessCountries) {
    this.edqBusinessCountries = edqBusinessCountries;
  }

  /**
   * A standardised list of countries where the customer has a head office
   **/

  @Schema(description = "A standardised list of countries where the customer has a head office")
  @JsonProperty("edqHeadOfficeCountries")
  public String getEdqHeadOfficeCountries() {
    return edqHeadOfficeCountries;
  }

  public void setEdqHeadOfficeCountries(String edqHeadOfficeCountries) {
    this.edqHeadOfficeCountries = edqHeadOfficeCountries;
  }

  /**
   * A standardised list of all country names associated to the customer record. All standardised
   * names will ISO names.
   **/

  @Schema(description = "A standardised list of all country names associated to the customer record. All standardised names will ISO names.")
  @JsonProperty("countriesAll")
  public String getCountriesAll() {
    return countriesAll;
  }

  public void setCountriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
  }

  /**
   * A standardised list of all country names in ISO 2 character code format associated to the
   * customer record.
   **/

  @Schema(description = "A standardised list of all country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("countriesAllCodes")
  public String getCountriesAllCodes() {
    return countriesAllCodes;
  }

  public void setCountriesAllCodes(String countriesAllCodes) {
    this.countriesAllCodes = countriesAllCodes;
  }

  /**
   * The standardised Line of Business name
   **/

  @Schema(description = "The standardised Line of Business name")
  @JsonProperty("edqLob")
  public String getEdqLob() {
    return edqLob;
  }

  public void setEdqLob(String edqLob) {
    this.edqLob = edqLob;
  }

  /**
   * Record permission allows user visibility depending on permissions granted
   **/

  @Schema(description = "Record permission allows user visibility depending on permissions granted")
  @JsonProperty("edqPermission")
  public String getEdqPermission() {
    return edqPermission;
  }

  public void setEdqPermission(String edqPermission) {
    this.edqPermission = edqPermission;
  }

  /**
   * Derived TAX ID
   **/

  @Schema(description = "Derived TAX ID")
  @JsonProperty("taxId")
  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(String taxId) {
    this.taxId = taxId;
  }

  /**
   * Derived City name
   **/

  @Schema(description = "Derived City name")
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Not currently used.
   **/

  @Schema(description = "Not currently used.")
  @JsonProperty("profileHyperlink")
  public String getProfileHyperlink() {
    return profileHyperlink;
  }

  public void setProfileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
  }

  /**
   * Provides a google hyperlink for the customer record.
   **/

  @Schema(description = "Provides a google hyperlink for the customer record.")
  @JsonProperty("searchHyperlink")
  public String getSearchHyperlink() {
    return searchHyperlink;
  }

  public void setSearchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
  }

  /**
   * Indicates the record  type
   **/

  @Schema(description = "Indicates the record  type")
  @JsonProperty("edqListKey")
  public String getEdqListKey() {
    return edqListKey;
  }

  public void setEdqListKey(String edqListKey) {
    this.edqListKey = edqListKey;
  }

  /**
   * Full concatenated address post refinement
   **/

  @Schema(description = "Full concatenated address post refinement")
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Standardised Entity name
   **/

  @Schema(description = "Standardised Entity name")
  @JsonProperty("entityName")
  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Standardise Postal code
   **/

  @Schema(description = "Standardise Postal code")
  @JsonProperty("postalCode")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Standardised indicator used to identify the customer as a Person or Business
   **/

  @Schema(description = "Standardised indicator used to identify the customer as a Person or Business")
  @JsonProperty("personOrBusinessIndicator")
  public String getPersonOrBusinessIndicator() {
    return personOrBusinessIndicator;
  }

  public void setPersonOrBusinessIndicator(String personOrBusinessIndicator) {
    this.personOrBusinessIndicator = personOrBusinessIndicator;
  }

  /**
   * Standardised Close of Business date
   **/

  @Schema(description = "Standardised Close of Business date")
  @JsonProperty("edqCloseOfBusinessDate")
  public String getEdqCloseOfBusinessDate() {
    return edqCloseOfBusinessDate;
  }

  public void setEdqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
  }

  /**
   * A standardised list of countries where the customer is operates
   **/

  @Schema(description = "A standardised list of countries where the customer is operates")
  @JsonProperty("operatingCountries")
  public String getOperatingCountries() {
    return operatingCountries;
  }

  public void setOperatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
  }

  /**
   * A standardsied list of ISO country codes where the customer operates
   **/

  @Schema(description = "A standardsied list of ISO country codes where the customer operates")
  @JsonProperty("edqOperatingCountriesCodes")
  public String getEdqOperatingCountriesCodes() {
    return edqOperatingCountriesCodes;
  }

  public void setEdqOperatingCountriesCodes(String edqOperatingCountriesCodes) {
    this.edqOperatingCountriesCodes = edqOperatingCountriesCodes;
  }

  /**
   * A standardised version of the customer registration number
   **/

  @Schema(description = "A standardised version of the customer registration number")
  @JsonProperty("registrationNumber")
  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  /**
   * The description of the name type code
   **/

  @Schema(description = "The description of the name type code")
  @JsonProperty("profileNameType")
  public String getProfileNameType() {
    return profileNameType;
  }

  public void setProfileNameType(String profileNameType) {
    this.profileNameType = profileNameType;
  }

  /**
   * The description of the customer role type
   **/

  @Schema(description = "The description of the customer role type")
  @JsonProperty("edqPartyRoleTypeDescription")
  public String getEdqPartyRoleTypeDescription() {
    return edqPartyRoleTypeDescription;
  }

  public void setEdqPartyRoleTypeDescription(String edqPartyRoleTypeDescription) {
    this.edqPartyRoleTypeDescription = edqPartyRoleTypeDescription;
  }

  /**
   * The description of the customer status
   **/

  @Schema(description = "The description of the customer status")
  @JsonProperty("edqPartyStatusCodeDescription")
  public String getEdqPartyStatusCodeDescription() {
    return edqPartyStatusCodeDescription;
  }

  public void setEdqPartyStatusCodeDescription(String edqPartyStatusCodeDescription) {
    this.edqPartyStatusCodeDescription = edqPartyStatusCodeDescription;
  }

  /**
   * This should not be used see extended attributes in the CaseWithURL table for the edqDay1Spike
   * value to use.
   **/

  @Schema(description = "This should not be used see extended attributes in the CaseWithURL table for the edqDay1Spike value to use.")
  @JsonProperty("edqDay1SpikeFlag")
  public String getEdqDay1SpikeFlag() {
    return edqDay1SpikeFlag;
  }

  public void setEdqDay1SpikeFlag(String edqDay1SpikeFlag) {
    this.edqDay1SpikeFlag = edqDay1SpikeFlag;
  }

  /**
   * Entity name as originally provided
   **/

  @Schema(description = "Entity name as originally provided")
  @JsonProperty("originalScriptName")
  public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
  }

  /**
   * Original address data concatenated together
   **/

  @Schema(description = "Original address data concatenated together")
  @JsonProperty("profileFullAddress")
  public String getProfileFullAddress() {
    return profileFullAddress;
  }

  public void setProfileFullAddress(String profileFullAddress) {
    this.profileFullAddress = profileFullAddress;
  }

  /**
   * This will always be \&quot;Batch\&quot; until realtime screening is provided.
   **/

  @Schema(description = "This will always be \"Batch\" until realtime screening is provided.")
  @JsonProperty("edqScreeningMode")
  public String getEdqScreeningMode() {
    return edqScreeningMode;
  }

  public void setEdqScreeningMode(String edqScreeningMode) {
    this.edqScreeningMode = edqScreeningMode;
  }

  /**
   * The element of the CaseKey from the customer side. This is joined with the elements from the
   * list to form the KeyLabel
   **/

  @Schema(description = "The element of the CaseKey from the customer side. This is joined with the elements from the list to form the KeyLabel")
  @JsonProperty("edqCaseKey")
  public String getEdqCaseKey() {
    return edqCaseKey;
  }

  public void setEdqCaseKey(String edqCaseKey) {
    this.edqCaseKey = edqCaseKey;
  }

  /**
   * The standardise address type value
   **/

  @Schema(description = "The standardise address type value")
  @JsonProperty("addressType")
  public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  /**
   * For development use only
   **/

  @Schema(description = "For development use only")
  @JsonProperty("sscCodes")
  public String getSscCodes() {
    return sscCodes;
  }

  public void setSscCodes(String sscCodes) {
    this.sscCodes = sscCodes;
  }

  /**
   * For development use only
   **/

  @Schema(description = "For development use only")
  @JsonProperty("ctrpFragment")
  public String getCtrpFragment() {
    return ctrpFragment;
  }

  public void setCtrpFragment(String ctrpFragment) {
    this.ctrpFragment = ctrpFragment;
  }

  /**
   * N/A - For Day 1
   **/

  @Schema(description = "N/A - For Day 1")
  @JsonProperty("requestUserName")
  public String getRequestUserName() {
    return requestUserName;
  }

  public void setRequestUserName(String requestUserName) {
    this.requestUserName = requestUserName;
  }

  /**
   * N/A - For Day 1
   **/

  @Schema(description = "N/A - For Day 1")
  @JsonProperty("requestDateTime")
  public String getRequestDateTime() {
    return requestDateTime;
  }

  public void setRequestDateTime(String requestDateTime) {
    this.requestDateTime = requestDateTime;
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
    return Objects.equals(caseId, customerEntities.caseId) &&
        Objects.equals(recordId, customerEntities.recordId) &&
        Objects.equals(inputStream, customerEntities.inputStream) &&
        Objects.equals(sourceSystemHistoryId, customerEntities.sourceSystemHistoryId) &&
        Objects.equals(closeOfBusinessDate, customerEntities.closeOfBusinessDate) &&
        Objects.equals(partitionNumber, customerEntities.partitionNumber) &&
        Objects.equals(sourceSystemIdentifier, customerEntities.sourceSystemIdentifier) &&
        Objects.equals(externalProfileId, customerEntities.externalProfileId) &&
        Objects.equals(concatenatedProfileId, customerEntities.concatenatedProfileId) &&
        Objects.equals(profileType, customerEntities.profileType) &&
        Objects.equals(partyRoleTypeCode, customerEntities.partyRoleTypeCode) &&
        Objects.equals(profileStatus, customerEntities.profileStatus) &&
        Objects.equals(profileSegment, customerEntities.profileSegment) &&
        Objects.equals(lobName, customerEntities.lobName) &&
        Objects.equals(lobRegion, customerEntities.lobRegion) &&
        Objects.equals(lobCountry, customerEntities.lobCountry) &&
        Objects.equals(hsbcLegalEntityCode, customerEntities.hsbcLegalEntityCode) &&
        Objects.equals(nameCategoryCode, customerEntities.nameCategoryCode) &&
        Objects.equals(nameTypeCode, customerEntities.nameTypeCode) &&
        Objects.equals(nameLanguageTypeCode, customerEntities.nameLanguageTypeCode) &&
        Objects.equals(entityNameOriginal, customerEntities.entityNameOriginal) &&
        Objects.equals(pepIndicator, customerEntities.pepIndicator) &&
        Objects.equals(specialCategoryCustomer, customerEntities.specialCategoryCustomer) &&
        Objects.equals(sourceAddressType, customerEntities.sourceAddressType) &&
        Objects.equals(addressFormatCode, customerEntities.addressFormatCode) &&
        Objects.equals(addressLanguageTypeCode, customerEntities.addressLanguageTypeCode) &&
        Objects.equals(sourceAddressLine1, customerEntities.sourceAddressLine1) &&
        Objects.equals(sourceAddressLine2, customerEntities.sourceAddressLine2) &&
        Objects.equals(sourceAddressLine3, customerEntities.sourceAddressLine3) &&
        Objects.equals(sourceAddressLine4, customerEntities.sourceAddressLine4) &&
        Objects.equals(sourceAddressLine5, customerEntities.sourceAddressLine5) &&
        Objects.equals(sourceAddressLine6, customerEntities.sourceAddressLine6) &&
        Objects.equals(sourceAddressLine7, customerEntities.sourceAddressLine7) &&
        Objects.equals(sourceAddressLine8, customerEntities.sourceAddressLine8) &&
        Objects.equals(sourceAddressLine9, customerEntities.sourceAddressLine9) &&
        Objects.equals(sourceAddressLine10, customerEntities.sourceAddressLine10) &&
        Objects.equals(sourcePostalCode, customerEntities.sourcePostalCode) &&
        Objects.equals(sourceAddressCountry, customerEntities.sourceAddressCountry) &&
        Objects.equals(identificationDocument1, customerEntities.identificationDocument1) &&
        Objects.equals(identificationDocument2, customerEntities.identificationDocument2) &&
        Objects.equals(identificationDocument3, customerEntities.identificationDocument3) &&
        Objects.equals(identificationDocument4, customerEntities.identificationDocument4) &&
        Objects.equals(identificationDocument5, customerEntities.identificationDocument5) &&
        Objects.equals(identificationDocument6, customerEntities.identificationDocument6) &&
        Objects.equals(identificationDocument7, customerEntities.identificationDocument7) &&
        Objects.equals(identificationDocument8, customerEntities.identificationDocument8) &&
        Objects.equals(identificationDocument9, customerEntities.identificationDocument9) &&
        Objects.equals(identificationDocument10, customerEntities.identificationDocument10) &&
        Objects.equals(tradesWithCountries, customerEntities.tradesWithCountries) &&
        Objects.equals(
            subsidiariesOperatesInCountries, customerEntities.subsidiariesOperatesInCountries) &&
        Objects.equals(exportCountries, customerEntities.exportCountries) &&
        Objects.equals(importCountries, customerEntities.importCountries) &&
        Objects.equals(countriesOfIncorporation, customerEntities.countriesOfIncorporation) &&
        Objects.equals(
            countriesOfRegistrationOriginal, customerEntities.countriesOfRegistrationOriginal) &&
        Objects.equals(countriesOfBusiness, customerEntities.countriesOfBusiness) &&
        Objects.equals(countriesOfHeadOffice, customerEntities.countriesOfHeadOffice) &&
        Objects.equals(edqCustId, customerEntities.edqCustId) &&
        Objects.equals(edqCustSubId, customerEntities.edqCustSubId) &&
        Objects.equals(edqLobCountryCode, customerEntities.edqLobCountryCode) &&
        Objects.equals(addressCountry, customerEntities.addressCountry) &&
        Objects.equals(edqAddressCountryCode, customerEntities.edqAddressCountryCode) &&
        Objects.equals(edqTradesWithCountries, customerEntities.edqTradesWithCountries) &&
        Objects.equals(edqExportsCountries, customerEntities.edqExportsCountries) &&
        Objects.equals(edqImportsCountries, customerEntities.edqImportsCountries) &&
        Objects.equals(edqIncorporationCountries, customerEntities.edqIncorporationCountries) &&
        Objects.equals(
            edqIncorporationCountriesCodes, customerEntities.edqIncorporationCountriesCodes) &&
        Objects.equals(registrationCountry, customerEntities.registrationCountry) &&
        Objects.equals(
            edqRegiistrationCountriesCodes, customerEntities.edqRegiistrationCountriesCodes) &&
        Objects.equals(edqBusinessCountries, customerEntities.edqBusinessCountries) &&
        Objects.equals(edqHeadOfficeCountries, customerEntities.edqHeadOfficeCountries) &&
        Objects.equals(countriesAll, customerEntities.countriesAll) &&
        Objects.equals(countriesAllCodes, customerEntities.countriesAllCodes) &&
        Objects.equals(edqLob, customerEntities.edqLob) &&
        Objects.equals(edqPermission, customerEntities.edqPermission) &&
        Objects.equals(taxId, customerEntities.taxId) &&
        Objects.equals(city, customerEntities.city) &&
        Objects.equals(profileHyperlink, customerEntities.profileHyperlink) &&
        Objects.equals(searchHyperlink, customerEntities.searchHyperlink) &&
        Objects.equals(edqListKey, customerEntities.edqListKey) &&
        Objects.equals(address, customerEntities.address) &&
        Objects.equals(entityName, customerEntities.entityName) &&
        Objects.equals(postalCode, customerEntities.postalCode) &&
        Objects.equals(personOrBusinessIndicator, customerEntities.personOrBusinessIndicator) &&
        Objects.equals(edqCloseOfBusinessDate, customerEntities.edqCloseOfBusinessDate) &&
        Objects.equals(operatingCountries, customerEntities.operatingCountries) &&
        Objects.equals(edqOperatingCountriesCodes, customerEntities.edqOperatingCountriesCodes) &&
        Objects.equals(registrationNumber, customerEntities.registrationNumber) &&
        Objects.equals(profileNameType, customerEntities.profileNameType) &&
        Objects.equals(edqPartyRoleTypeDescription, customerEntities.edqPartyRoleTypeDescription) &&
        Objects.equals(
            edqPartyStatusCodeDescription, customerEntities.edqPartyStatusCodeDescription) &&
        Objects.equals(edqDay1SpikeFlag, customerEntities.edqDay1SpikeFlag) &&
        Objects.equals(originalScriptName, customerEntities.originalScriptName) &&
        Objects.equals(profileFullAddress, customerEntities.profileFullAddress) &&
        Objects.equals(edqScreeningMode, customerEntities.edqScreeningMode) &&
        Objects.equals(edqCaseKey, customerEntities.edqCaseKey) &&
        Objects.equals(addressType, customerEntities.addressType) &&
        Objects.equals(sscCodes, customerEntities.sscCodes) &&
        Objects.equals(ctrpFragment, customerEntities.ctrpFragment) &&
        Objects.equals(requestUserName, customerEntities.requestUserName) &&
        Objects.equals(requestDateTime, customerEntities.requestDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, sourceSystemHistoryId, closeOfBusinessDate, partitionNumber,
        sourceSystemIdentifier, externalProfileId, concatenatedProfileId, profileType,
        partyRoleTypeCode, profileStatus, profileSegment, lobName, lobRegion, lobCountry,
        hsbcLegalEntityCode, nameCategoryCode, nameTypeCode, nameLanguageTypeCode,
        entityNameOriginal, pepIndicator, specialCategoryCustomer, sourceAddressType,
        addressFormatCode, addressLanguageTypeCode, sourceAddressLine1, sourceAddressLine2,
        sourceAddressLine3, sourceAddressLine4, sourceAddressLine5, sourceAddressLine6,
        sourceAddressLine7, sourceAddressLine8, sourceAddressLine9, sourceAddressLine10,
        sourcePostalCode, sourceAddressCountry, identificationDocument1, identificationDocument2,
        identificationDocument3, identificationDocument4, identificationDocument5,
        identificationDocument6, identificationDocument7, identificationDocument8,
        identificationDocument9, identificationDocument10, tradesWithCountries,
        subsidiariesOperatesInCountries, exportCountries, importCountries, countriesOfIncorporation,
        countriesOfRegistrationOriginal, countriesOfBusiness, countriesOfHeadOffice, edqCustId,
        edqCustSubId, edqLobCountryCode, addressCountry, edqAddressCountryCode,
        edqTradesWithCountries, edqExportsCountries, edqImportsCountries, edqIncorporationCountries,
        edqIncorporationCountriesCodes, registrationCountry, edqRegiistrationCountriesCodes,
        edqBusinessCountries, edqHeadOfficeCountries, countriesAll, countriesAllCodes, edqLob,
        edqPermission, taxId, city, profileHyperlink, searchHyperlink, edqListKey, address,
        entityName, postalCode, personOrBusinessIndicator, edqCloseOfBusinessDate,
        operatingCountries, edqOperatingCountriesCodes, registrationNumber, profileNameType,
        edqPartyRoleTypeDescription, edqPartyStatusCodeDescription, edqDay1SpikeFlag,
        originalScriptName, profileFullAddress, edqScreeningMode, edqCaseKey, addressType, sscCodes,
        ctrpFragment, requestUserName, requestDateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerEntities {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb
        .append("    sourceSystemHistoryId: ")
        .append(toIndentedString(sourceSystemHistoryId))
        .append("\n");
    sb
        .append("    closeOfBusinessDate: ")
        .append(toIndentedString(closeOfBusinessDate))
        .append("\n");
    sb.append("    partitionNumber: ").append(toIndentedString(partitionNumber)).append("\n");
    sb
        .append("    sourceSystemIdentifier: ")
        .append(toIndentedString(sourceSystemIdentifier))
        .append("\n");
    sb.append("    externalProfileId: ").append(toIndentedString(externalProfileId)).append("\n");
    sb
        .append("    concatenatedProfileId: ")
        .append(toIndentedString(concatenatedProfileId))
        .append("\n");
    sb.append("    profileType: ").append(toIndentedString(profileType)).append("\n");
    sb.append("    partyRoleTypeCode: ").append(toIndentedString(partyRoleTypeCode)).append("\n");
    sb.append("    profileStatus: ").append(toIndentedString(profileStatus)).append("\n");
    sb.append("    profileSegment: ").append(toIndentedString(profileSegment)).append("\n");
    sb.append("    lobName: ").append(toIndentedString(lobName)).append("\n");
    sb.append("    lobRegion: ").append(toIndentedString(lobRegion)).append("\n");
    sb.append("    lobCountry: ").append(toIndentedString(lobCountry)).append("\n");
    sb
        .append("    hsbcLegalEntityCode: ")
        .append(toIndentedString(hsbcLegalEntityCode))
        .append("\n");
    sb.append("    nameCategoryCode: ").append(toIndentedString(nameCategoryCode)).append("\n");
    sb.append("    nameTypeCode: ").append(toIndentedString(nameTypeCode)).append("\n");
    sb
        .append("    nameLanguageTypeCode: ")
        .append(toIndentedString(nameLanguageTypeCode))
        .append("\n");
    sb.append("    entityNameOriginal: ").append(toIndentedString(entityNameOriginal)).append("\n");
    sb.append("    pepIndicator: ").append(toIndentedString(pepIndicator)).append("\n");
    sb
        .append("    specialCategoryCustomer: ")
        .append(toIndentedString(specialCategoryCustomer))
        .append("\n");
    sb.append("    sourceAddressType: ").append(toIndentedString(sourceAddressType)).append("\n");
    sb.append("    addressFormatCode: ").append(toIndentedString(addressFormatCode)).append("\n");
    sb
        .append("    addressLanguageTypeCode: ")
        .append(toIndentedString(addressLanguageTypeCode))
        .append("\n");
    sb.append("    sourceAddressLine1: ").append(toIndentedString(sourceAddressLine1)).append("\n");
    sb.append("    sourceAddressLine2: ").append(toIndentedString(sourceAddressLine2)).append("\n");
    sb.append("    sourceAddressLine3: ").append(toIndentedString(sourceAddressLine3)).append("\n");
    sb.append("    sourceAddressLine4: ").append(toIndentedString(sourceAddressLine4)).append("\n");
    sb.append("    sourceAddressLine5: ").append(toIndentedString(sourceAddressLine5)).append("\n");
    sb.append("    sourceAddressLine6: ").append(toIndentedString(sourceAddressLine6)).append("\n");
    sb.append("    sourceAddressLine7: ").append(toIndentedString(sourceAddressLine7)).append("\n");
    sb.append("    sourceAddressLine8: ").append(toIndentedString(sourceAddressLine8)).append("\n");
    sb.append("    sourceAddressLine9: ").append(toIndentedString(sourceAddressLine9)).append("\n");
    sb
        .append("    sourceAddressLine10: ")
        .append(toIndentedString(sourceAddressLine10))
        .append("\n");
    sb.append("    sourcePostalCode: ").append(toIndentedString(sourcePostalCode)).append("\n");
    sb
        .append("    sourceAddressCountry: ")
        .append(toIndentedString(sourceAddressCountry))
        .append("\n");
    sb
        .append("    identificationDocument1: ")
        .append(toIndentedString(identificationDocument1))
        .append("\n");
    sb
        .append("    identificationDocument2: ")
        .append(toIndentedString(identificationDocument2))
        .append("\n");
    sb
        .append("    identificationDocument3: ")
        .append(toIndentedString(identificationDocument3))
        .append("\n");
    sb
        .append("    identificationDocument4: ")
        .append(toIndentedString(identificationDocument4))
        .append("\n");
    sb
        .append("    identificationDocument5: ")
        .append(toIndentedString(identificationDocument5))
        .append("\n");
    sb
        .append("    identificationDocument6: ")
        .append(toIndentedString(identificationDocument6))
        .append("\n");
    sb
        .append("    identificationDocument7: ")
        .append(toIndentedString(identificationDocument7))
        .append("\n");
    sb
        .append("    identificationDocument8: ")
        .append(toIndentedString(identificationDocument8))
        .append("\n");
    sb
        .append("    identificationDocument9: ")
        .append(toIndentedString(identificationDocument9))
        .append("\n");
    sb
        .append("    identificationDocument10: ")
        .append(toIndentedString(identificationDocument10))
        .append("\n");
    sb
        .append("    tradesWithCountries: ")
        .append(toIndentedString(tradesWithCountries))
        .append("\n");
    sb
        .append("    subsidiariesOperatesInCountries: ")
        .append(toIndentedString(subsidiariesOperatesInCountries))
        .append("\n");
    sb.append("    exportCountries: ").append(toIndentedString(exportCountries)).append("\n");
    sb.append("    importCountries: ").append(toIndentedString(importCountries)).append("\n");
    sb
        .append("    countriesOfIncorporation: ")
        .append(toIndentedString(countriesOfIncorporation))
        .append("\n");
    sb
        .append("    countriesOfRegistrationOriginal: ")
        .append(toIndentedString(countriesOfRegistrationOriginal))
        .append("\n");
    sb
        .append("    countriesOfBusiness: ")
        .append(toIndentedString(countriesOfBusiness))
        .append("\n");
    sb
        .append("    countriesOfHeadOffice: ")
        .append(toIndentedString(countriesOfHeadOffice))
        .append("\n");
    sb.append("    edqCustId: ").append(toIndentedString(edqCustId)).append("\n");
    sb.append("    edqCustSubId: ").append(toIndentedString(edqCustSubId)).append("\n");
    sb.append("    edqLobCountryCode: ").append(toIndentedString(edqLobCountryCode)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb
        .append("    edqAddressCountryCode: ")
        .append(toIndentedString(edqAddressCountryCode))
        .append("\n");
    sb
        .append("    edqTradesWithCountries: ")
        .append(toIndentedString(edqTradesWithCountries))
        .append("\n");
    sb
        .append("    edqExportsCountries: ")
        .append(toIndentedString(edqExportsCountries))
        .append("\n");
    sb
        .append("    edqImportsCountries: ")
        .append(toIndentedString(edqImportsCountries))
        .append("\n");
    sb
        .append("    edqIncorporationCountries: ")
        .append(toIndentedString(edqIncorporationCountries))
        .append("\n");
    sb
        .append("    edqIncorporationCountriesCodes: ")
        .append(toIndentedString(edqIncorporationCountriesCodes))
        .append("\n");
    sb
        .append("    registrationCountry: ")
        .append(toIndentedString(registrationCountry))
        .append("\n");
    sb
        .append("    edqRegiistrationCountriesCodes: ")
        .append(toIndentedString(edqRegiistrationCountriesCodes))
        .append("\n");
    sb
        .append("    edqBusinessCountries: ")
        .append(toIndentedString(edqBusinessCountries))
        .append("\n");
    sb
        .append("    edqHeadOfficeCountries: ")
        .append(toIndentedString(edqHeadOfficeCountries))
        .append("\n");
    sb.append("    countriesAll: ").append(toIndentedString(countriesAll)).append("\n");
    sb.append("    countriesAllCodes: ").append(toIndentedString(countriesAllCodes)).append("\n");
    sb.append("    edqLob: ").append(toIndentedString(edqLob)).append("\n");
    sb.append("    edqPermission: ").append(toIndentedString(edqPermission)).append("\n");
    sb.append("    taxId: ").append(toIndentedString(taxId)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    profileHyperlink: ").append(toIndentedString(profileHyperlink)).append("\n");
    sb.append("    searchHyperlink: ").append(toIndentedString(searchHyperlink)).append("\n");
    sb.append("    edqListKey: ").append(toIndentedString(edqListKey)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    entityName: ").append(toIndentedString(entityName)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb
        .append("    personOrBusinessIndicator: ")
        .append(toIndentedString(personOrBusinessIndicator))
        .append("\n");
    sb
        .append("    edqCloseOfBusinessDate: ")
        .append(toIndentedString(edqCloseOfBusinessDate))
        .append("\n");
    sb.append("    operatingCountries: ").append(toIndentedString(operatingCountries)).append("\n");
    sb
        .append("    edqOperatingCountriesCodes: ")
        .append(toIndentedString(edqOperatingCountriesCodes))
        .append("\n");
    sb.append("    registrationNumber: ").append(toIndentedString(registrationNumber)).append("\n");
    sb.append("    profileNameType: ").append(toIndentedString(profileNameType)).append("\n");
    sb
        .append("    edqPartyRoleTypeDescription: ")
        .append(toIndentedString(edqPartyRoleTypeDescription))
        .append("\n");
    sb
        .append("    edqPartyStatusCodeDescription: ")
        .append(toIndentedString(edqPartyStatusCodeDescription))
        .append("\n");
    sb.append("    edqDay1SpikeFlag: ").append(toIndentedString(edqDay1SpikeFlag)).append("\n");
    sb.append("    originalScriptName: ").append(toIndentedString(originalScriptName)).append("\n");
    sb.append("    profileFullAddress: ").append(toIndentedString(profileFullAddress)).append("\n");
    sb.append("    edqScreeningMode: ").append(toIndentedString(edqScreeningMode)).append("\n");
    sb.append("    edqCaseKey: ").append(toIndentedString(edqCaseKey)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    sscCodes: ").append(toIndentedString(sscCodes)).append("\n");
    sb.append("    ctrpFragment: ").append(toIndentedString(ctrpFragment)).append("\n");
    sb.append("    requestUserName: ").append(toIndentedString(requestUserName)).append("\n");
    sb.append("    requestDateTime: ").append(toIndentedString(requestDateTime)).append("\n");
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
