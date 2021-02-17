package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CustomerEntities
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CustomerEntities   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("recordId")
  private BigDecimal recordId = null;

  @JsonProperty("inputStream")
  private String inputStream = null;

  @JsonProperty("sourceSystemHistoryID")
  private Integer sourceSystemHistoryID = null;

  @JsonProperty("closeOfBusinessDate")
  private String closeOfBusinessDate = null;

  @JsonProperty("partitionNumber")
  private Integer partitionNumber = null;

  @JsonProperty("sourceSystemIdentifier")
  private String sourceSystemIdentifier = null;

  @JsonProperty("externalProfileID")
  private String externalProfileID = null;

  @JsonProperty("concatenatedProfileID")
  private String concatenatedProfileID = null;

  @JsonProperty("profileType")
  private String profileType = null;

  @JsonProperty("partyRoleTypeCode")
  private String partyRoleTypeCode = null;

  @JsonProperty("profileStatus")
  private String profileStatus = null;

  @JsonProperty("profileSegment")
  private String profileSegment = null;

  @JsonProperty("lOBName")
  private String lOBName = null;

  @JsonProperty("lOBRegion")
  private String lOBRegion = null;

  @JsonProperty("lOBCountry")
  private String lOBCountry = null;

  @JsonProperty("hSBCLegalEntityCode")
  private String hSBCLegalEntityCode = null;

  @JsonProperty("nameCategoryCode")
  private String nameCategoryCode = null;

  @JsonProperty("nameTypeCode")
  private String nameTypeCode = null;

  @JsonProperty("nameLanguageTypeCode")
  private String nameLanguageTypeCode = null;

  @JsonProperty("entityNameOriginal")
  private String entityNameOriginal = null;

  @JsonProperty("pEPIndicator")
  private String pEPIndicator = null;

  @JsonProperty("specialCategoryCustomer")
  private String specialCategoryCustomer = null;

  @JsonProperty("sourceAddressType")
  private String sourceAddressType = null;

  @JsonProperty("addressFormatCode")
  private String addressFormatCode = null;

  @JsonProperty("addressLanguageTypeCode")
  private String addressLanguageTypeCode = null;

  @JsonProperty("sourceAddressLine1")
  private String sourceAddressLine1 = null;

  @JsonProperty("sourceAddressLine2")
  private String sourceAddressLine2 = null;

  @JsonProperty("sourceAddressLine3")
  private String sourceAddressLine3 = null;

  @JsonProperty("sourceAddressLine4")
  private String sourceAddressLine4 = null;

  @JsonProperty("sourceAddressLine5")
  private String sourceAddressLine5 = null;

  @JsonProperty("sourceAddressLine6")
  private String sourceAddressLine6 = null;

  @JsonProperty("sourceAddressLine7")
  private String sourceAddressLine7 = null;

  @JsonProperty("sourceAddressLine8")
  private String sourceAddressLine8 = null;

  @JsonProperty("sourceAddressLine9")
  private String sourceAddressLine9 = null;

  @JsonProperty("sourceAddressLine10")
  private String sourceAddressLine10 = null;

  @JsonProperty("sourcePostalCode")
  private String sourcePostalCode = null;

  @JsonProperty("sourceAddressCountry")
  private String sourceAddressCountry = null;

  @JsonProperty("identificationDocument1")
  private String identificationDocument1 = null;

  @JsonProperty("identificationDocument2")
  private String identificationDocument2 = null;

  @JsonProperty("identificationDocument3")
  private String identificationDocument3 = null;

  @JsonProperty("identificationDocument4")
  private String identificationDocument4 = null;

  @JsonProperty("identificationDocument5")
  private String identificationDocument5 = null;

  @JsonProperty("identificationDocument6")
  private String identificationDocument6 = null;

  @JsonProperty("identificationDocument7")
  private String identificationDocument7 = null;

  @JsonProperty("identificationDocument8")
  private String identificationDocument8 = null;

  @JsonProperty("identificationDocument9")
  private String identificationDocument9 = null;

  @JsonProperty("identificationDocument10")
  private String identificationDocument10 = null;

  @JsonProperty("tradesWithCountries")
  private String tradesWithCountries = null;

  @JsonProperty("subsidiariesOperatesInCountries")
  private String subsidiariesOperatesInCountries = null;

  @JsonProperty("exportCountries")
  private String exportCountries = null;

  @JsonProperty("importCountries")
  private String importCountries = null;

  @JsonProperty("countriesOfIncorporation")
  private String countriesOfIncorporation = null;

  @JsonProperty("countriesOfRegistrationOriginal")
  private String countriesOfRegistrationOriginal = null;

  @JsonProperty("countriesOfBusiness")
  private String countriesOfBusiness = null;

  @JsonProperty("countriesOfHeadOffice")
  private String countriesOfHeadOffice = null;

  @JsonProperty("edqCustID")
  private String edqCustID = null;

  @JsonProperty("edqCustSubID")
  private String edqCustSubID = null;

  @JsonProperty("edqLoBCountryCode")
  private String edqLoBCountryCode = null;

  @JsonProperty("addressCountry")
  private String addressCountry = null;

  @JsonProperty("edqAddressCountryCode")
  private String edqAddressCountryCode = null;

  @JsonProperty("edqTradesWithCountries")
  private String edqTradesWithCountries = null;

  @JsonProperty("edqExportsCountries")
  private String edqExportsCountries = null;

  @JsonProperty("edqImportsCountries")
  private String edqImportsCountries = null;

  @JsonProperty("edqIncorporationCountries")
  private String edqIncorporationCountries = null;

  @JsonProperty("edqIncorporationCountriesCodes")
  private String edqIncorporationCountriesCodes = null;

  @JsonProperty("registrationCountry")
  private String registrationCountry = null;

  @JsonProperty("edqRegiistrationCountriesCodes")
  private String edqRegiistrationCountriesCodes = null;

  @JsonProperty("edqBusinessCountries")
  private String edqBusinessCountries = null;

  @JsonProperty("edqHeadOfficeCountries")
  private String edqHeadOfficeCountries = null;

  @JsonProperty("countriesAll")
  private String countriesAll = null;

  @JsonProperty("countriesAllCodes")
  private String countriesAllCodes = null;

  @JsonProperty("edqLoB")
  private String edqLoB = null;

  @JsonProperty("edqPermission")
  private String edqPermission = null;

  @JsonProperty("taxID")
  private String taxID = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("profileHyperlink")
  private String profileHyperlink = null;

  @JsonProperty("searchHyperlink")
  private String searchHyperlink = null;

  @JsonProperty("edqListKey")
  private String edqListKey = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("entityName")
  private String entityName = null;

  @JsonProperty("postalCode")
  private String postalCode = null;

  @JsonProperty("personOrBusinessIndicator")
  private String personOrBusinessIndicator = null;

  @JsonProperty("edqCloseOfBusinessDate")
  private String edqCloseOfBusinessDate = null;

  @JsonProperty("operatingCountries")
  private String operatingCountries = null;

  @JsonProperty("edqOperatingCountriesCodes")
  private String edqOperatingCountriesCodes = null;

  @JsonProperty("registrationNumber")
  private String registrationNumber = null;

  @JsonProperty("profileNameType")
  private String profileNameType = null;

  @JsonProperty("edqPartyRoleTypeDescription")
  private String edqPartyRoleTypeDescription = null;

  @JsonProperty("edqPartyStatusCodeDescription")
  private String edqPartyStatusCodeDescription = null;

  @JsonProperty("edqDay1SpikeFlag")
  private String edqDay1SpikeFlag = null;

  @JsonProperty("originalScriptName")
  private String originalScriptName = null;

  @JsonProperty("profileFullAddress")
  private String profileFullAddress = null;

  @JsonProperty("edqScreeningMode")
  private String edqScreeningMode = null;

  @JsonProperty("edqCaseKey")
  private String edqCaseKey = null;

  @JsonProperty("addressType")
  private String addressType = null;

  @JsonProperty("sSCCodes")
  private String sSCCodes = null;

  @JsonProperty("cTRPFragment")
  private String cTRPFragment = null;

  @JsonProperty("requestUserName")
  private String requestUserName = null;

  @JsonProperty("requestDateTime")
  private String requestDateTime = null;

  public CustomerEntities caseId(Integer caseId) {
    this.caseId = caseId;
    return this;
  }

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   * @return caseId
   **/
  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  
    public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
    this.caseId = caseId;
  }

  public CustomerEntities recordId(BigDecimal recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * Refers to a Customer name record (relationship) within an alert where there are multiple Customer name records within the same alert. This is the working record ID.
   * @return recordId
   **/
  @Schema(description = "Refers to a Customer name record (relationship) within an alert where there are multiple Customer name records within the same alert. This is the working record ID.")
  
    @Valid
    public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  public CustomerEntities inputStream(String inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  /**
   * Denotes the type of data, for example&#58; Customer Data
   * @return inputStream
   **/
  @Schema(description = "Denotes the type of data, for example&#58; Customer Data")
  
    public String getInputStream() {
    return inputStream;
  }

  public void setInputStream(String inputStream) {
    this.inputStream = inputStream;
  }

  public CustomerEntities sourceSystemHistoryID(Integer sourceSystemHistoryID) {
    this.sourceSystemHistoryID = sourceSystemHistoryID;
    return this;
  }

  /**
   * Identifier used during reconciliation to store customer information
   * @return sourceSystemHistoryID
   **/
  @Schema(description = "Identifier used during reconciliation to store customer information")
  
    public Integer getSourceSystemHistoryID() {
    return sourceSystemHistoryID;
  }

  public void setSourceSystemHistoryID(Integer sourceSystemHistoryID) {
    this.sourceSystemHistoryID = sourceSystemHistoryID;
  }

  public CustomerEntities closeOfBusinessDate(String closeOfBusinessDate) {
    this.closeOfBusinessDate = closeOfBusinessDate;
    return this;
  }

  /**
   * The date the business ceased trading
   * @return closeOfBusinessDate
   **/
  @Schema(description = "The date the business ceased trading")
  
    public String getCloseOfBusinessDate() {
    return closeOfBusinessDate;
  }

  public void setCloseOfBusinessDate(String closeOfBusinessDate) {
    this.closeOfBusinessDate = closeOfBusinessDate;
  }

  public CustomerEntities partitionNumber(Integer partitionNumber) {
    this.partitionNumber = partitionNumber;
    return this;
  }

  /**
   * Where a customer feed is larger than a certain threshold it will be partitioned into sections. The partion number denotes which partition this record has come from.
   * @return partitionNumber
   **/
  @Schema(description = "Where a customer feed is larger than a certain threshold it will be partitioned into sections. The partion number denotes which partition this record has come from.")
  
    public Integer getPartitionNumber() {
    return partitionNumber;
  }

  public void setPartitionNumber(Integer partitionNumber) {
    this.partitionNumber = partitionNumber;
  }

  public CustomerEntities sourceSystemIdentifier(String sourceSystemIdentifier) {
    this.sourceSystemIdentifier = sourceSystemIdentifier;
    return this;
  }

  /**
   * The Identifier used to identify where the feed originated from
   * @return sourceSystemIdentifier
   **/
  @Schema(description = "The Identifier used to identify where the feed originated from")
  
    public String getSourceSystemIdentifier() {
    return sourceSystemIdentifier;
  }

  public void setSourceSystemIdentifier(String sourceSystemIdentifier) {
    this.sourceSystemIdentifier = sourceSystemIdentifier;
  }

  public CustomerEntities externalProfileID(String externalProfileID) {
    this.externalProfileID = externalProfileID;
    return this;
  }

  /**
   * Customer ID from source system
   * @return externalProfileID
   **/
  @Schema(description = "Customer ID from source system")
  
    public String getExternalProfileID() {
    return externalProfileID;
  }

  public void setExternalProfileID(String externalProfileID) {
    this.externalProfileID = externalProfileID;
  }

  public CustomerEntities concatenatedProfileID(String concatenatedProfileID) {
    this.concatenatedProfileID = concatenatedProfileID;
    return this;
  }

  /**
   * Customer ID from source system concatenated with the source system name to ensure the customer ID is unique.
   * @return concatenatedProfileID
   **/
  @Schema(description = "Customer ID from source system concatenated with the source system name to ensure the customer ID is unique.")
  
    public String getConcatenatedProfileID() {
    return concatenatedProfileID;
  }

  public void setConcatenatedProfileID(String concatenatedProfileID) {
    this.concatenatedProfileID = concatenatedProfileID;
  }

  public CustomerEntities profileType(String profileType) {
    this.profileType = profileType;
    return this;
  }

  /**
   * Party type code
   * @return profileType
   **/
  @Schema(description = "Party type code")
  
    public String getProfileType() {
    return profileType;
  }

  public void setProfileType(String profileType) {
    this.profileType = profileType;
  }

  public CustomerEntities partyRoleTypeCode(String partyRoleTypeCode) {
    this.partyRoleTypeCode = partyRoleTypeCode;
    return this;
  }

  /**
   * Provides the ID relating to the customer type
   * @return partyRoleTypeCode
   **/
  @Schema(description = "Provides the ID relating to the customer type")
  
    public String getPartyRoleTypeCode() {
    return partyRoleTypeCode;
  }

  public void setPartyRoleTypeCode(String partyRoleTypeCode) {
    this.partyRoleTypeCode = partyRoleTypeCode;
  }

  public CustomerEntities profileStatus(String profileStatus) {
    this.profileStatus = profileStatus;
    return this;
  }

  /**
   * Partyy status code
   * @return profileStatus
   **/
  @Schema(description = "Partyy status code")
  
    public String getProfileStatus() {
    return profileStatus;
  }

  public void setProfileStatus(String profileStatus) {
    this.profileStatus = profileStatus;
  }

  public CustomerEntities profileSegment(String profileSegment) {
    this.profileSegment = profileSegment;
    return this;
  }

  /**
   * Line of Business customer segment.
   * @return profileSegment
   **/
  @Schema(description = "Line of Business customer segment.")
  
    public String getProfileSegment() {
    return profileSegment;
  }

  public void setProfileSegment(String profileSegment) {
    this.profileSegment = profileSegment;
  }

  public CustomerEntities lOBName(String lOBName) {
    this.lOBName = lOBName;
    return this;
  }

  /**
   * Line of Business name
   * @return lOBName
   **/
  @Schema(description = "Line of Business name")
  
    public String getLOBName() {
    return lOBName;
  }

  public void setLOBName(String lOBName) {
    this.lOBName = lOBName;
  }

  public CustomerEntities lOBRegion(String lOBRegion) {
    this.lOBRegion = lOBRegion;
    return this;
  }

  /**
   * Line of Business region
   * @return lOBRegion
   **/
  @Schema(description = "Line of Business region")
  
    public String getLOBRegion() {
    return lOBRegion;
  }

  public void setLOBRegion(String lOBRegion) {
    this.lOBRegion = lOBRegion;
  }

  public CustomerEntities lOBCountry(String lOBCountry) {
    this.lOBCountry = lOBCountry;
    return this;
  }

  /**
   * Line of Business Country Code
   * @return lOBCountry
   **/
  @Schema(description = "Line of Business Country Code")
  
    public String getLOBCountry() {
    return lOBCountry;
  }

  public void setLOBCountry(String lOBCountry) {
    this.lOBCountry = lOBCountry;
  }

  public CustomerEntities hSBCLegalEntityCode(String hSBCLegalEntityCode) {
    this.hSBCLegalEntityCode = hSBCLegalEntityCode;
    return this;
  }

  /**
   * HSBC legal entity code
   * @return hSBCLegalEntityCode
   **/
  @Schema(description = "HSBC legal entity code")
  
    public String getHSBCLegalEntityCode() {
    return hSBCLegalEntityCode;
  }

  public void setHSBCLegalEntityCode(String hSBCLegalEntityCode) {
    this.hSBCLegalEntityCode = hSBCLegalEntityCode;
  }

  public CustomerEntities nameCategoryCode(String nameCategoryCode) {
    this.nameCategoryCode = nameCategoryCode;
    return this;
  }

  /**
   * Customer name category code
   * @return nameCategoryCode
   **/
  @Schema(description = "Customer name category code")
  
    public String getNameCategoryCode() {
    return nameCategoryCode;
  }

  public void setNameCategoryCode(String nameCategoryCode) {
    this.nameCategoryCode = nameCategoryCode;
  }

  public CustomerEntities nameTypeCode(String nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
    return this;
  }

  /**
   * Name Type Code
   * @return nameTypeCode
   **/
  @Schema(description = "Name Type Code")
  
    public String getNameTypeCode() {
    return nameTypeCode;
  }

  public void setNameTypeCode(String nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
  }

  public CustomerEntities nameLanguageTypeCode(String nameLanguageTypeCode) {
    this.nameLanguageTypeCode = nameLanguageTypeCode;
    return this;
  }

  /**
   * The language associated to the name type code
   * @return nameLanguageTypeCode
   **/
  @Schema(description = "The language associated to the name type code")
  
    public String getNameLanguageTypeCode() {
    return nameLanguageTypeCode;
  }

  public void setNameLanguageTypeCode(String nameLanguageTypeCode) {
    this.nameLanguageTypeCode = nameLanguageTypeCode;
  }

  public CustomerEntities entityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
    return this;
  }

  /**
   * The Entity name as originally provided
   * @return entityNameOriginal
   **/
  @Schema(description = "The Entity name as originally provided")
  
    public String getEntityNameOriginal() {
    return entityNameOriginal;
  }

  public void setEntityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
  }

  public CustomerEntities pEPIndicator(String pEPIndicator) {
    this.pEPIndicator = pEPIndicator;
    return this;
  }

  /**
   * Indicates if the record is regarded as a PEP or not in the source system
   * @return pEPIndicator
   **/
  @Schema(description = "Indicates if the record is regarded as a PEP or not in the source system")
  
    public String getPEPIndicator() {
    return pEPIndicator;
  }

  public void setPEPIndicator(String pEPIndicator) {
    this.pEPIndicator = pEPIndicator;
  }

  public CustomerEntities specialCategoryCustomer(String specialCategoryCustomer) {
    this.specialCategoryCustomer = specialCategoryCustomer;
    return this;
  }

  /**
   * Indicates if the record is regarded as a Special Chategory Person or not in the source system
   * @return specialCategoryCustomer
   **/
  @Schema(description = "Indicates if the record is regarded as a Special Chategory Person or not in the source system")
  
    public String getSpecialCategoryCustomer() {
    return specialCategoryCustomer;
  }

  public void setSpecialCategoryCustomer(String specialCategoryCustomer) {
    this.specialCategoryCustomer = specialCategoryCustomer;
  }

  public CustomerEntities sourceAddressType(String sourceAddressType) {
    this.sourceAddressType = sourceAddressType;
    return this;
  }

  /**
   * The Address Type provided for this customer record
   * @return sourceAddressType
   **/
  @Schema(description = "The Address Type provided for this customer record")
  
    public String getSourceAddressType() {
    return sourceAddressType;
  }

  public void setSourceAddressType(String sourceAddressType) {
    this.sourceAddressType = sourceAddressType;
  }

  public CustomerEntities addressFormatCode(String addressFormatCode) {
    this.addressFormatCode = addressFormatCode;
    return this;
  }

  /**
   * Indicates if the address is provided in a structured or unstructured format
   * @return addressFormatCode
   **/
  @Schema(description = "Indicates if the address is provided in a structured or unstructured format")
  
    public String getAddressFormatCode() {
    return addressFormatCode;
  }

  public void setAddressFormatCode(String addressFormatCode) {
    this.addressFormatCode = addressFormatCode;
  }

  public CustomerEntities addressLanguageTypeCode(String addressLanguageTypeCode) {
    this.addressLanguageTypeCode = addressLanguageTypeCode;
    return this;
  }

  /**
   * Indicates if the address is provided in the primary or secondary language
   * @return addressLanguageTypeCode
   **/
  @Schema(description = "Indicates if the address is provided in the primary or secondary language")
  
    public String getAddressLanguageTypeCode() {
    return addressLanguageTypeCode;
  }

  public void setAddressLanguageTypeCode(String addressLanguageTypeCode) {
    this.addressLanguageTypeCode = addressLanguageTypeCode;
  }

  public CustomerEntities sourceAddressLine1(String sourceAddressLine1) {
    this.sourceAddressLine1 = sourceAddressLine1;
    return this;
  }

  /**
   * Address Line 1 as provided in the customer record
   * @return sourceAddressLine1
   **/
  @Schema(description = "Address Line 1 as provided in the customer record")
  
    public String getSourceAddressLine1() {
    return sourceAddressLine1;
  }

  public void setSourceAddressLine1(String sourceAddressLine1) {
    this.sourceAddressLine1 = sourceAddressLine1;
  }

  public CustomerEntities sourceAddressLine2(String sourceAddressLine2) {
    this.sourceAddressLine2 = sourceAddressLine2;
    return this;
  }

  /**
   * Address Line 2 as provided in the customer record
   * @return sourceAddressLine2
   **/
  @Schema(description = "Address Line 2 as provided in the customer record")
  
    public String getSourceAddressLine2() {
    return sourceAddressLine2;
  }

  public void setSourceAddressLine2(String sourceAddressLine2) {
    this.sourceAddressLine2 = sourceAddressLine2;
  }

  public CustomerEntities sourceAddressLine3(String sourceAddressLine3) {
    this.sourceAddressLine3 = sourceAddressLine3;
    return this;
  }

  /**
   * Address Line 3 as provided in the customer record
   * @return sourceAddressLine3
   **/
  @Schema(description = "Address Line 3 as provided in the customer record")
  
    public String getSourceAddressLine3() {
    return sourceAddressLine3;
  }

  public void setSourceAddressLine3(String sourceAddressLine3) {
    this.sourceAddressLine3 = sourceAddressLine3;
  }

  public CustomerEntities sourceAddressLine4(String sourceAddressLine4) {
    this.sourceAddressLine4 = sourceAddressLine4;
    return this;
  }

  /**
   * Address Line 4 as provided in the customer record
   * @return sourceAddressLine4
   **/
  @Schema(description = "Address Line 4 as provided in the customer record")
  
    public String getSourceAddressLine4() {
    return sourceAddressLine4;
  }

  public void setSourceAddressLine4(String sourceAddressLine4) {
    this.sourceAddressLine4 = sourceAddressLine4;
  }

  public CustomerEntities sourceAddressLine5(String sourceAddressLine5) {
    this.sourceAddressLine5 = sourceAddressLine5;
    return this;
  }

  /**
   * Address Line 5 as provided in the customer record
   * @return sourceAddressLine5
   **/
  @Schema(description = "Address Line 5 as provided in the customer record")
  
    public String getSourceAddressLine5() {
    return sourceAddressLine5;
  }

  public void setSourceAddressLine5(String sourceAddressLine5) {
    this.sourceAddressLine5 = sourceAddressLine5;
  }

  public CustomerEntities sourceAddressLine6(String sourceAddressLine6) {
    this.sourceAddressLine6 = sourceAddressLine6;
    return this;
  }

  /**
   * Address Line 6 as provided in the customer record
   * @return sourceAddressLine6
   **/
  @Schema(description = "Address Line 6 as provided in the customer record")
  
    public String getSourceAddressLine6() {
    return sourceAddressLine6;
  }

  public void setSourceAddressLine6(String sourceAddressLine6) {
    this.sourceAddressLine6 = sourceAddressLine6;
  }

  public CustomerEntities sourceAddressLine7(String sourceAddressLine7) {
    this.sourceAddressLine7 = sourceAddressLine7;
    return this;
  }

  /**
   * Address Line 7 as provided in the customer record
   * @return sourceAddressLine7
   **/
  @Schema(description = "Address Line 7 as provided in the customer record")
  
    public String getSourceAddressLine7() {
    return sourceAddressLine7;
  }

  public void setSourceAddressLine7(String sourceAddressLine7) {
    this.sourceAddressLine7 = sourceAddressLine7;
  }

  public CustomerEntities sourceAddressLine8(String sourceAddressLine8) {
    this.sourceAddressLine8 = sourceAddressLine8;
    return this;
  }

  /**
   * Address Line 8 as provided in the customer record
   * @return sourceAddressLine8
   **/
  @Schema(description = "Address Line 8 as provided in the customer record")
  
    public String getSourceAddressLine8() {
    return sourceAddressLine8;
  }

  public void setSourceAddressLine8(String sourceAddressLine8) {
    this.sourceAddressLine8 = sourceAddressLine8;
  }

  public CustomerEntities sourceAddressLine9(String sourceAddressLine9) {
    this.sourceAddressLine9 = sourceAddressLine9;
    return this;
  }

  /**
   * Address Line 9 as provided in the customer record
   * @return sourceAddressLine9
   **/
  @Schema(description = "Address Line 9 as provided in the customer record")
  
    public String getSourceAddressLine9() {
    return sourceAddressLine9;
  }

  public void setSourceAddressLine9(String sourceAddressLine9) {
    this.sourceAddressLine9 = sourceAddressLine9;
  }

  public CustomerEntities sourceAddressLine10(String sourceAddressLine10) {
    this.sourceAddressLine10 = sourceAddressLine10;
    return this;
  }

  /**
   * Address Line 10 as provided in the customer record
   * @return sourceAddressLine10
   **/
  @Schema(description = "Address Line 10 as provided in the customer record")
  
    public String getSourceAddressLine10() {
    return sourceAddressLine10;
  }

  public void setSourceAddressLine10(String sourceAddressLine10) {
    this.sourceAddressLine10 = sourceAddressLine10;
  }

  public CustomerEntities sourcePostalCode(String sourcePostalCode) {
    this.sourcePostalCode = sourcePostalCode;
    return this;
  }

  /**
   * Address Postcode as provided in the customer record
   * @return sourcePostalCode
   **/
  @Schema(description = "Address Postcode as provided in the customer record")
  
    public String getSourcePostalCode() {
    return sourcePostalCode;
  }

  public void setSourcePostalCode(String sourcePostalCode) {
    this.sourcePostalCode = sourcePostalCode;
  }

  public CustomerEntities sourceAddressCountry(String sourceAddressCountry) {
    this.sourceAddressCountry = sourceAddressCountry;
    return this;
  }

  /**
   * Address Country as provided in the customer record
   * @return sourceAddressCountry
   **/
  @Schema(description = "Address Country as provided in the customer record")
  
    public String getSourceAddressCountry() {
    return sourceAddressCountry;
  }

  public void setSourceAddressCountry(String sourceAddressCountry) {
    this.sourceAddressCountry = sourceAddressCountry;
  }

  public CustomerEntities identificationDocument1(String identificationDocument1) {
    this.identificationDocument1 = identificationDocument1;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument1
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument1() {
    return identificationDocument1;
  }

  public void setIdentificationDocument1(String identificationDocument1) {
    this.identificationDocument1 = identificationDocument1;
  }

  public CustomerEntities identificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument2
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument2() {
    return identificationDocument2;
  }

  public void setIdentificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
  }

  public CustomerEntities identificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument3
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument3() {
    return identificationDocument3;
  }

  public void setIdentificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
  }

  public CustomerEntities identificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument4
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument4() {
    return identificationDocument4;
  }

  public void setIdentificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
  }

  public CustomerEntities identificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument5
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument5() {
    return identificationDocument5;
  }

  public void setIdentificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
  }

  public CustomerEntities identificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument6
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument6() {
    return identificationDocument6;
  }

  public void setIdentificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
  }

  public CustomerEntities identificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument7
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument7() {
    return identificationDocument7;
  }

  public void setIdentificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
  }

  public CustomerEntities identificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument8
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument8() {
    return identificationDocument8;
  }

  public void setIdentificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
  }

  public CustomerEntities identificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument9
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument9() {
    return identificationDocument9;
  }

  public void setIdentificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
  }

  public CustomerEntities identificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
    return this;
  }

  /**
   * Identification document
   * @return identificationDocument10
   **/
  @Schema(description = "Identification document")
  
    public String getIdentificationDocument10() {
    return identificationDocument10;
  }

  public void setIdentificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
  }

  public CustomerEntities tradesWithCountries(String tradesWithCountries) {
    this.tradesWithCountries = tradesWithCountries;
    return this;
  }

  /**
   * List of countries the customer trades with
   * @return tradesWithCountries
   **/
  @Schema(description = "List of countries the customer trades with")
  
    public String getTradesWithCountries() {
    return tradesWithCountries;
  }

  public void setTradesWithCountries(String tradesWithCountries) {
    this.tradesWithCountries = tradesWithCountries;
  }

  public CustomerEntities subsidiariesOperatesInCountries(String subsidiariesOperatesInCountries) {
    this.subsidiariesOperatesInCountries = subsidiariesOperatesInCountries;
    return this;
  }

  /**
   * List of countries the customer's subsidiaries operate in
   * @return subsidiariesOperatesInCountries
   **/
  @Schema(description = "List of countries the customer's subsidiaries operate in")
  
    public String getSubsidiariesOperatesInCountries() {
    return subsidiariesOperatesInCountries;
  }

  public void setSubsidiariesOperatesInCountries(String subsidiariesOperatesInCountries) {
    this.subsidiariesOperatesInCountries = subsidiariesOperatesInCountries;
  }

  public CustomerEntities exportCountries(String exportCountries) {
    this.exportCountries = exportCountries;
    return this;
  }

  /**
   * List of countries the customer exports to
   * @return exportCountries
   **/
  @Schema(description = "List of countries the customer exports to")
  
    public String getExportCountries() {
    return exportCountries;
  }

  public void setExportCountries(String exportCountries) {
    this.exportCountries = exportCountries;
  }

  public CustomerEntities importCountries(String importCountries) {
    this.importCountries = importCountries;
    return this;
  }

  /**
   * List of countries the customer imports from
   * @return importCountries
   **/
  @Schema(description = "List of countries the customer imports from")
  
    public String getImportCountries() {
    return importCountries;
  }

  public void setImportCountries(String importCountries) {
    this.importCountries = importCountries;
  }

  public CustomerEntities countriesOfIncorporation(String countriesOfIncorporation) {
    this.countriesOfIncorporation = countriesOfIncorporation;
    return this;
  }

  /**
   * List of countires where the customer was/is incorporated
   * @return countriesOfIncorporation
   **/
  @Schema(description = "List of countires where the customer was/is incorporated")
  
    public String getCountriesOfIncorporation() {
    return countriesOfIncorporation;
  }

  public void setCountriesOfIncorporation(String countriesOfIncorporation) {
    this.countriesOfIncorporation = countriesOfIncorporation;
  }

  public CustomerEntities countriesOfRegistrationOriginal(String countriesOfRegistrationOriginal) {
    this.countriesOfRegistrationOriginal = countriesOfRegistrationOriginal;
    return this;
  }

  /**
   * List of countries where the customer is registered
   * @return countriesOfRegistrationOriginal
   **/
  @Schema(description = "List of countries where the customer is registered")
  
    public String getCountriesOfRegistrationOriginal() {
    return countriesOfRegistrationOriginal;
  }

  public void setCountriesOfRegistrationOriginal(String countriesOfRegistrationOriginal) {
    this.countriesOfRegistrationOriginal = countriesOfRegistrationOriginal;
  }

  public CustomerEntities countriesOfBusiness(String countriesOfBusiness) {
    this.countriesOfBusiness = countriesOfBusiness;
    return this;
  }

  /**
   * List of countries where the customer conducts business
   * @return countriesOfBusiness
   **/
  @Schema(description = "List of countries where the customer conducts business")
  
    public String getCountriesOfBusiness() {
    return countriesOfBusiness;
  }

  public void setCountriesOfBusiness(String countriesOfBusiness) {
    this.countriesOfBusiness = countriesOfBusiness;
  }

  public CustomerEntities countriesOfHeadOffice(String countriesOfHeadOffice) {
    this.countriesOfHeadOffice = countriesOfHeadOffice;
    return this;
  }

  /**
   * List of countries where the customer has a head office
   * @return countriesOfHeadOffice
   **/
  @Schema(description = "List of countries where the customer has a head office")
  
    public String getCountriesOfHeadOffice() {
    return countriesOfHeadOffice;
  }

  public void setCountriesOfHeadOffice(String countriesOfHeadOffice) {
    this.countriesOfHeadOffice = countriesOfHeadOffice;
  }

  public CustomerEntities edqCustID(String edqCustID) {
    this.edqCustID = edqCustID;
    return this;
  }

  /**
   * Customer ID from source system concatenated with the source system name to ensure the customer ID is unique
   * @return edqCustID
   **/
  @Schema(description = "Customer ID from source system concatenated with the source system name to ensure the customer ID is unique")
  
    public String getEdqCustID() {
    return edqCustID;
  }

  public void setEdqCustID(String edqCustID) {
    this.edqCustID = edqCustID;
  }

  public CustomerEntities edqCustSubID(String edqCustSubID) {
    this.edqCustSubID = edqCustSubID;
    return this;
  }

  /**
   * Customer ID from the source system
   * @return edqCustSubID
   **/
  @Schema(description = "Customer ID from the source system")
  
    public String getEdqCustSubID() {
    return edqCustSubID;
  }

  public void setEdqCustSubID(String edqCustSubID) {
    this.edqCustSubID = edqCustSubID;
  }

  public CustomerEntities edqLoBCountryCode(String edqLoBCountryCode) {
    this.edqLoBCountryCode = edqLoBCountryCode;
    return this;
  }

  /**
   * The ISO country code the the Line of Business this record is associated to
   * @return edqLoBCountryCode
   **/
  @Schema(description = "The ISO country code the the Line of Business this record is associated to")
  
    public String getEdqLoBCountryCode() {
    return edqLoBCountryCode;
  }

  public void setEdqLoBCountryCode(String edqLoBCountryCode) {
    this.edqLoBCountryCode = edqLoBCountryCode;
  }

  public CustomerEntities addressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
    return this;
  }

  /**
   * The refined and or Derived country names associated to the customer record
   * @return addressCountry
   **/
  @Schema(description = "The refined and or Derived country names associated to the customer record")
  
    public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public CustomerEntities edqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
    return this;
  }

  /**
   * The ISO country code Derived from the customer's address
   * @return edqAddressCountryCode
   **/
  @Schema(description = "The ISO country code Derived from the customer's address")
  
    public String getEdqAddressCountryCode() {
    return edqAddressCountryCode;
  }

  public void setEdqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
  }

  public CustomerEntities edqTradesWithCountries(String edqTradesWithCountries) {
    this.edqTradesWithCountries = edqTradesWithCountries;
    return this;
  }

  /**
   * A standardised list of countries the customer trades with
   * @return edqTradesWithCountries
   **/
  @Schema(description = "A standardised list of countries the customer trades with")
  
    public String getEdqTradesWithCountries() {
    return edqTradesWithCountries;
  }

  public void setEdqTradesWithCountries(String edqTradesWithCountries) {
    this.edqTradesWithCountries = edqTradesWithCountries;
  }

  public CustomerEntities edqExportsCountries(String edqExportsCountries) {
    this.edqExportsCountries = edqExportsCountries;
    return this;
  }

  /**
   * A standardised list of countries the customer exports to
   * @return edqExportsCountries
   **/
  @Schema(description = "A standardised list of countries the customer exports to")
  
    public String getEdqExportsCountries() {
    return edqExportsCountries;
  }

  public void setEdqExportsCountries(String edqExportsCountries) {
    this.edqExportsCountries = edqExportsCountries;
  }

  public CustomerEntities edqImportsCountries(String edqImportsCountries) {
    this.edqImportsCountries = edqImportsCountries;
    return this;
  }

  /**
   * A standardised list of countries the customer imports from
   * @return edqImportsCountries
   **/
  @Schema(description = "A standardised list of countries the customer imports from")
  
    public String getEdqImportsCountries() {
    return edqImportsCountries;
  }

  public void setEdqImportsCountries(String edqImportsCountries) {
    this.edqImportsCountries = edqImportsCountries;
  }

  public CustomerEntities edqIncorporationCountries(String edqIncorporationCountries) {
    this.edqIncorporationCountries = edqIncorporationCountries;
    return this;
  }

  /**
   * A standardised list of countires where the customer was/is incorporated
   * @return edqIncorporationCountries
   **/
  @Schema(description = "A standardised list of countires where the customer was/is incorporated")
  
    public String getEdqIncorporationCountries() {
    return edqIncorporationCountries;
  }

  public void setEdqIncorporationCountries(String edqIncorporationCountries) {
    this.edqIncorporationCountries = edqIncorporationCountries;
  }

  public CustomerEntities edqIncorporationCountriesCodes(String edqIncorporationCountriesCodes) {
    this.edqIncorporationCountriesCodes = edqIncorporationCountriesCodes;
    return this;
  }

  /**
   * A standardised list of country codes where the customer was/is incorporated
   * @return edqIncorporationCountriesCodes
   **/
  @Schema(description = "A standardised list of country codes where the customer was/is incorporated")
  
    public String getEdqIncorporationCountriesCodes() {
    return edqIncorporationCountriesCodes;
  }

  public void setEdqIncorporationCountriesCodes(String edqIncorporationCountriesCodes) {
    this.edqIncorporationCountriesCodes = edqIncorporationCountriesCodes;
  }

  public CustomerEntities registrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
    return this;
  }

  /**
   * A standardised list of countries where the customer is registered
   * @return registrationCountry
   **/
  @Schema(description = "A standardised list of countries where the customer is registered")
  
    public String getRegistrationCountry() {
    return registrationCountry;
  }

  public void setRegistrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
  }

  public CustomerEntities edqRegiistrationCountriesCodes(String edqRegiistrationCountriesCodes) {
    this.edqRegiistrationCountriesCodes = edqRegiistrationCountriesCodes;
    return this;
  }

  /**
   * A standardised list of country codes where the customer is registered
   * @return edqRegiistrationCountriesCodes
   **/
  @Schema(description = "A standardised list of country codes where the customer is registered")
  
    public String getEdqRegiistrationCountriesCodes() {
    return edqRegiistrationCountriesCodes;
  }

  public void setEdqRegiistrationCountriesCodes(String edqRegiistrationCountriesCodes) {
    this.edqRegiistrationCountriesCodes = edqRegiistrationCountriesCodes;
  }

  public CustomerEntities edqBusinessCountries(String edqBusinessCountries) {
    this.edqBusinessCountries = edqBusinessCountries;
    return this;
  }

  /**
   * A standardised list of countries where the customer conducts business
   * @return edqBusinessCountries
   **/
  @Schema(description = "A standardised list of countries where the customer conducts business")
  
    public String getEdqBusinessCountries() {
    return edqBusinessCountries;
  }

  public void setEdqBusinessCountries(String edqBusinessCountries) {
    this.edqBusinessCountries = edqBusinessCountries;
  }

  public CustomerEntities edqHeadOfficeCountries(String edqHeadOfficeCountries) {
    this.edqHeadOfficeCountries = edqHeadOfficeCountries;
    return this;
  }

  /**
   * A standardised list of countries where the customer has a head office
   * @return edqHeadOfficeCountries
   **/
  @Schema(description = "A standardised list of countries where the customer has a head office")
  
    public String getEdqHeadOfficeCountries() {
    return edqHeadOfficeCountries;
  }

  public void setEdqHeadOfficeCountries(String edqHeadOfficeCountries) {
    this.edqHeadOfficeCountries = edqHeadOfficeCountries;
  }

  public CustomerEntities countriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
    return this;
  }

  /**
   * A standardised list of all country names associated to the customer record. All standardised names will ISO names.
   * @return countriesAll
   **/
  @Schema(description = "A standardised list of all country names associated to the customer record. All standardised names will ISO names.")
  
    public String getCountriesAll() {
    return countriesAll;
  }

  public void setCountriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
  }

  public CustomerEntities countriesAllCodes(String countriesAllCodes) {
    this.countriesAllCodes = countriesAllCodes;
    return this;
  }

  /**
   * A standardised list of all country names in ISO 2 character code format associated to the customer record.
   * @return countriesAllCodes
   **/
  @Schema(description = "A standardised list of all country names in ISO 2 character code format associated to the customer record.")
  
    public String getCountriesAllCodes() {
    return countriesAllCodes;
  }

  public void setCountriesAllCodes(String countriesAllCodes) {
    this.countriesAllCodes = countriesAllCodes;
  }

  public CustomerEntities edqLoB(String edqLoB) {
    this.edqLoB = edqLoB;
    return this;
  }

  /**
   * The standardised Line of Business name
   * @return edqLoB
   **/
  @Schema(description = "The standardised Line of Business name")
  
    public String getEdqLoB() {
    return edqLoB;
  }

  public void setEdqLoB(String edqLoB) {
    this.edqLoB = edqLoB;
  }

  public CustomerEntities edqPermission(String edqPermission) {
    this.edqPermission = edqPermission;
    return this;
  }

  /**
   * Record permission allows user visibility depending on permissions granted
   * @return edqPermission
   **/
  @Schema(description = "Record permission allows user visibility depending on permissions granted")
  
    public String getEdqPermission() {
    return edqPermission;
  }

  public void setEdqPermission(String edqPermission) {
    this.edqPermission = edqPermission;
  }

  public CustomerEntities taxID(String taxID) {
    this.taxID = taxID;
    return this;
  }

  /**
   * Derived TAX ID
   * @return taxID
   **/
  @Schema(description = "Derived TAX ID")
  
    public String getTaxID() {
    return taxID;
  }

  public void setTaxID(String taxID) {
    this.taxID = taxID;
  }

  public CustomerEntities city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Derived City name
   * @return city
   **/
  @Schema(description = "Derived City name")
  
    public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public CustomerEntities profileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
    return this;
  }

  /**
   * Not currently used.
   * @return profileHyperlink
   **/
  @Schema(description = "Not currently used.")
  
    public String getProfileHyperlink() {
    return profileHyperlink;
  }

  public void setProfileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
  }

  public CustomerEntities searchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
    return this;
  }

  /**
   * Provides a google hyperlink for the customer record.
   * @return searchHyperlink
   **/
  @Schema(description = "Provides a google hyperlink for the customer record.")
  
    public String getSearchHyperlink() {
    return searchHyperlink;
  }

  public void setSearchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
  }

  public CustomerEntities edqListKey(String edqListKey) {
    this.edqListKey = edqListKey;
    return this;
  }

  /**
   * Indicates the record  type
   * @return edqListKey
   **/
  @Schema(description = "Indicates the record  type")
  
    public String getEdqListKey() {
    return edqListKey;
  }

  public void setEdqListKey(String edqListKey) {
    this.edqListKey = edqListKey;
  }

  public CustomerEntities address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Full concatenated address post refinement
   * @return address
   **/
  @Schema(description = "Full concatenated address post refinement")
  
    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public CustomerEntities entityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

  /**
   * Standardised Entity name
   * @return entityName
   **/
  @Schema(description = "Standardised Entity name")
  
    public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public CustomerEntities postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  /**
   * Standardise Postal code
   * @return postalCode
   **/
  @Schema(description = "Standardise Postal code")
  
    public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public CustomerEntities personOrBusinessIndicator(String personOrBusinessIndicator) {
    this.personOrBusinessIndicator = personOrBusinessIndicator;
    return this;
  }

  /**
   * Standardised indicator used to identify the customer as a Person or Business
   * @return personOrBusinessIndicator
   **/
  @Schema(description = "Standardised indicator used to identify the customer as a Person or Business")
  
    public String getPersonOrBusinessIndicator() {
    return personOrBusinessIndicator;
  }

  public void setPersonOrBusinessIndicator(String personOrBusinessIndicator) {
    this.personOrBusinessIndicator = personOrBusinessIndicator;
  }

  public CustomerEntities edqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
    return this;
  }

  /**
   * Standardised Close of Business date
   * @return edqCloseOfBusinessDate
   **/
  @Schema(description = "Standardised Close of Business date")
  
    public String getEdqCloseOfBusinessDate() {
    return edqCloseOfBusinessDate;
  }

  public void setEdqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
  }

  public CustomerEntities operatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
    return this;
  }

  /**
   * A standardised list of countries where the customer is operates
   * @return operatingCountries
   **/
  @Schema(description = "A standardised list of countries where the customer is operates")
  
    public String getOperatingCountries() {
    return operatingCountries;
  }

  public void setOperatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
  }

  public CustomerEntities edqOperatingCountriesCodes(String edqOperatingCountriesCodes) {
    this.edqOperatingCountriesCodes = edqOperatingCountriesCodes;
    return this;
  }

  /**
   * A standardsied list of ISO country codes where the customer operates
   * @return edqOperatingCountriesCodes
   **/
  @Schema(description = "A standardsied list of ISO country codes where the customer operates")
  
    public String getEdqOperatingCountriesCodes() {
    return edqOperatingCountriesCodes;
  }

  public void setEdqOperatingCountriesCodes(String edqOperatingCountriesCodes) {
    this.edqOperatingCountriesCodes = edqOperatingCountriesCodes;
  }

  public CustomerEntities registrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
    return this;
  }

  /**
   * A standardised version of the customer registration number
   * @return registrationNumber
   **/
  @Schema(description = "A standardised version of the customer registration number")
  
    public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public CustomerEntities profileNameType(String profileNameType) {
    this.profileNameType = profileNameType;
    return this;
  }

  /**
   * The description of the name type code
   * @return profileNameType
   **/
  @Schema(description = "The description of the name type code")
  
    public String getProfileNameType() {
    return profileNameType;
  }

  public void setProfileNameType(String profileNameType) {
    this.profileNameType = profileNameType;
  }

  public CustomerEntities edqPartyRoleTypeDescription(String edqPartyRoleTypeDescription) {
    this.edqPartyRoleTypeDescription = edqPartyRoleTypeDescription;
    return this;
  }

  /**
   * The description of the customer role type
   * @return edqPartyRoleTypeDescription
   **/
  @Schema(description = "The description of the customer role type")
  
    public String getEdqPartyRoleTypeDescription() {
    return edqPartyRoleTypeDescription;
  }

  public void setEdqPartyRoleTypeDescription(String edqPartyRoleTypeDescription) {
    this.edqPartyRoleTypeDescription = edqPartyRoleTypeDescription;
  }

  public CustomerEntities edqPartyStatusCodeDescription(String edqPartyStatusCodeDescription) {
    this.edqPartyStatusCodeDescription = edqPartyStatusCodeDescription;
    return this;
  }

  /**
   * The description of the customer status
   * @return edqPartyStatusCodeDescription
   **/
  @Schema(description = "The description of the customer status")
  
    public String getEdqPartyStatusCodeDescription() {
    return edqPartyStatusCodeDescription;
  }

  public void setEdqPartyStatusCodeDescription(String edqPartyStatusCodeDescription) {
    this.edqPartyStatusCodeDescription = edqPartyStatusCodeDescription;
  }

  public CustomerEntities edqDay1SpikeFlag(String edqDay1SpikeFlag) {
    this.edqDay1SpikeFlag = edqDay1SpikeFlag;
    return this;
  }

  /**
   * This should not be used see extended attributes in the CaseWithURL table for the edqDay1Spike value to use.
   * @return edqDay1SpikeFlag
   **/
  @Schema(description = "This should not be used see extended attributes in the CaseWithURL table for the edqDay1Spike value to use.")
  
    public String getEdqDay1SpikeFlag() {
    return edqDay1SpikeFlag;
  }

  public void setEdqDay1SpikeFlag(String edqDay1SpikeFlag) {
    this.edqDay1SpikeFlag = edqDay1SpikeFlag;
  }

  public CustomerEntities originalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
    return this;
  }

  /**
   * Entity name as originally provided
   * @return originalScriptName
   **/
  @Schema(description = "Entity name as originally provided")
  
    public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
  }

  public CustomerEntities profileFullAddress(String profileFullAddress) {
    this.profileFullAddress = profileFullAddress;
    return this;
  }

  /**
   * Original address data concatenated together
   * @return profileFullAddress
   **/
  @Schema(description = "Original address data concatenated together")
  
    public String getProfileFullAddress() {
    return profileFullAddress;
  }

  public void setProfileFullAddress(String profileFullAddress) {
    this.profileFullAddress = profileFullAddress;
  }

  public CustomerEntities edqScreeningMode(String edqScreeningMode) {
    this.edqScreeningMode = edqScreeningMode;
    return this;
  }

  /**
   * This will always be \"Batch\" until realtime screening is provided.
   * @return edqScreeningMode
   **/
  @Schema(description = "This will always be \"Batch\" until realtime screening is provided.")
  
    public String getEdqScreeningMode() {
    return edqScreeningMode;
  }

  public void setEdqScreeningMode(String edqScreeningMode) {
    this.edqScreeningMode = edqScreeningMode;
  }

  public CustomerEntities edqCaseKey(String edqCaseKey) {
    this.edqCaseKey = edqCaseKey;
    return this;
  }

  /**
   * The element of the CaseKey from the customer side. This is joined with the elements from the list to form the KeyLabel
   * @return edqCaseKey
   **/
  @Schema(description = "The element of the CaseKey from the customer side. This is joined with the elements from the list to form the KeyLabel")
  
    public String getEdqCaseKey() {
    return edqCaseKey;
  }

  public void setEdqCaseKey(String edqCaseKey) {
    this.edqCaseKey = edqCaseKey;
  }

  public CustomerEntities addressType(String addressType) {
    this.addressType = addressType;
    return this;
  }

  /**
   * The standardise address type value
   * @return addressType
   **/
  @Schema(description = "The standardise address type value")
  
    public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public CustomerEntities sSCCodes(String sSCCodes) {
    this.sSCCodes = sSCCodes;
    return this;
  }

  /**
   * For development use only
   * @return sSCCodes
   **/
  @Schema(description = "For development use only")
  
    public String getSSCCodes() {
    return sSCCodes;
  }

  public void setSSCCodes(String sSCCodes) {
    this.sSCCodes = sSCCodes;
  }

  public CustomerEntities cTRPFragment(String cTRPFragment) {
    this.cTRPFragment = cTRPFragment;
    return this;
  }

  /**
   * For development use only
   * @return cTRPFragment
   **/
  @Schema(description = "For development use only")
  
    public String getCTRPFragment() {
    return cTRPFragment;
  }

  public void setCTRPFragment(String cTRPFragment) {
    this.cTRPFragment = cTRPFragment;
  }

  public CustomerEntities requestUserName(String requestUserName) {
    this.requestUserName = requestUserName;
    return this;
  }

  /**
   * N/A - For Day 1
   * @return requestUserName
   **/
  @Schema(description = "N/A - For Day 1")
  
    public String getRequestUserName() {
    return requestUserName;
  }

  public void setRequestUserName(String requestUserName) {
    this.requestUserName = requestUserName;
  }

  public CustomerEntities requestDateTime(String requestDateTime) {
    this.requestDateTime = requestDateTime;
    return this;
  }

  /**
   * N/A - For Day 1
   * @return requestDateTime
   **/
  @Schema(description = "N/A - For Day 1")
  
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
    return Objects.equals(this.caseId, customerEntities.caseId) &&
        Objects.equals(this.recordId, customerEntities.recordId) &&
        Objects.equals(this.inputStream, customerEntities.inputStream) &&
        Objects.equals(this.sourceSystemHistoryID, customerEntities.sourceSystemHistoryID) &&
        Objects.equals(this.closeOfBusinessDate, customerEntities.closeOfBusinessDate) &&
        Objects.equals(this.partitionNumber, customerEntities.partitionNumber) &&
        Objects.equals(this.sourceSystemIdentifier, customerEntities.sourceSystemIdentifier) &&
        Objects.equals(this.externalProfileID, customerEntities.externalProfileID) &&
        Objects.equals(this.concatenatedProfileID, customerEntities.concatenatedProfileID) &&
        Objects.equals(this.profileType, customerEntities.profileType) &&
        Objects.equals(this.partyRoleTypeCode, customerEntities.partyRoleTypeCode) &&
        Objects.equals(this.profileStatus, customerEntities.profileStatus) &&
        Objects.equals(this.profileSegment, customerEntities.profileSegment) &&
        Objects.equals(this.lOBName, customerEntities.lOBName) &&
        Objects.equals(this.lOBRegion, customerEntities.lOBRegion) &&
        Objects.equals(this.lOBCountry, customerEntities.lOBCountry) &&
        Objects.equals(this.hSBCLegalEntityCode, customerEntities.hSBCLegalEntityCode) &&
        Objects.equals(this.nameCategoryCode, customerEntities.nameCategoryCode) &&
        Objects.equals(this.nameTypeCode, customerEntities.nameTypeCode) &&
        Objects.equals(this.nameLanguageTypeCode, customerEntities.nameLanguageTypeCode) &&
        Objects.equals(this.entityNameOriginal, customerEntities.entityNameOriginal) &&
        Objects.equals(this.pEPIndicator, customerEntities.pEPIndicator) &&
        Objects.equals(this.specialCategoryCustomer, customerEntities.specialCategoryCustomer) &&
        Objects.equals(this.sourceAddressType, customerEntities.sourceAddressType) &&
        Objects.equals(this.addressFormatCode, customerEntities.addressFormatCode) &&
        Objects.equals(this.addressLanguageTypeCode, customerEntities.addressLanguageTypeCode) &&
        Objects.equals(this.sourceAddressLine1, customerEntities.sourceAddressLine1) &&
        Objects.equals(this.sourceAddressLine2, customerEntities.sourceAddressLine2) &&
        Objects.equals(this.sourceAddressLine3, customerEntities.sourceAddressLine3) &&
        Objects.equals(this.sourceAddressLine4, customerEntities.sourceAddressLine4) &&
        Objects.equals(this.sourceAddressLine5, customerEntities.sourceAddressLine5) &&
        Objects.equals(this.sourceAddressLine6, customerEntities.sourceAddressLine6) &&
        Objects.equals(this.sourceAddressLine7, customerEntities.sourceAddressLine7) &&
        Objects.equals(this.sourceAddressLine8, customerEntities.sourceAddressLine8) &&
        Objects.equals(this.sourceAddressLine9, customerEntities.sourceAddressLine9) &&
        Objects.equals(this.sourceAddressLine10, customerEntities.sourceAddressLine10) &&
        Objects.equals(this.sourcePostalCode, customerEntities.sourcePostalCode) &&
        Objects.equals(this.sourceAddressCountry, customerEntities.sourceAddressCountry) &&
        Objects.equals(this.identificationDocument1, customerEntities.identificationDocument1) &&
        Objects.equals(this.identificationDocument2, customerEntities.identificationDocument2) &&
        Objects.equals(this.identificationDocument3, customerEntities.identificationDocument3) &&
        Objects.equals(this.identificationDocument4, customerEntities.identificationDocument4) &&
        Objects.equals(this.identificationDocument5, customerEntities.identificationDocument5) &&
        Objects.equals(this.identificationDocument6, customerEntities.identificationDocument6) &&
        Objects.equals(this.identificationDocument7, customerEntities.identificationDocument7) &&
        Objects.equals(this.identificationDocument8, customerEntities.identificationDocument8) &&
        Objects.equals(this.identificationDocument9, customerEntities.identificationDocument9) &&
        Objects.equals(this.identificationDocument10, customerEntities.identificationDocument10) &&
        Objects.equals(this.tradesWithCountries, customerEntities.tradesWithCountries) &&
        Objects.equals(this.subsidiariesOperatesInCountries, customerEntities.subsidiariesOperatesInCountries) &&
        Objects.equals(this.exportCountries, customerEntities.exportCountries) &&
        Objects.equals(this.importCountries, customerEntities.importCountries) &&
        Objects.equals(this.countriesOfIncorporation, customerEntities.countriesOfIncorporation) &&
        Objects.equals(this.countriesOfRegistrationOriginal, customerEntities.countriesOfRegistrationOriginal) &&
        Objects.equals(this.countriesOfBusiness, customerEntities.countriesOfBusiness) &&
        Objects.equals(this.countriesOfHeadOffice, customerEntities.countriesOfHeadOffice) &&
        Objects.equals(this.edqCustID, customerEntities.edqCustID) &&
        Objects.equals(this.edqCustSubID, customerEntities.edqCustSubID) &&
        Objects.equals(this.edqLoBCountryCode, customerEntities.edqLoBCountryCode) &&
        Objects.equals(this.addressCountry, customerEntities.addressCountry) &&
        Objects.equals(this.edqAddressCountryCode, customerEntities.edqAddressCountryCode) &&
        Objects.equals(this.edqTradesWithCountries, customerEntities.edqTradesWithCountries) &&
        Objects.equals(this.edqExportsCountries, customerEntities.edqExportsCountries) &&
        Objects.equals(this.edqImportsCountries, customerEntities.edqImportsCountries) &&
        Objects.equals(this.edqIncorporationCountries, customerEntities.edqIncorporationCountries) &&
        Objects.equals(this.edqIncorporationCountriesCodes, customerEntities.edqIncorporationCountriesCodes) &&
        Objects.equals(this.registrationCountry, customerEntities.registrationCountry) &&
        Objects.equals(this.edqRegiistrationCountriesCodes, customerEntities.edqRegiistrationCountriesCodes) &&
        Objects.equals(this.edqBusinessCountries, customerEntities.edqBusinessCountries) &&
        Objects.equals(this.edqHeadOfficeCountries, customerEntities.edqHeadOfficeCountries) &&
        Objects.equals(this.countriesAll, customerEntities.countriesAll) &&
        Objects.equals(this.countriesAllCodes, customerEntities.countriesAllCodes) &&
        Objects.equals(this.edqLoB, customerEntities.edqLoB) &&
        Objects.equals(this.edqPermission, customerEntities.edqPermission) &&
        Objects.equals(this.taxID, customerEntities.taxID) &&
        Objects.equals(this.city, customerEntities.city) &&
        Objects.equals(this.profileHyperlink, customerEntities.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, customerEntities.searchHyperlink) &&
        Objects.equals(this.edqListKey, customerEntities.edqListKey) &&
        Objects.equals(this.address, customerEntities.address) &&
        Objects.equals(this.entityName, customerEntities.entityName) &&
        Objects.equals(this.postalCode, customerEntities.postalCode) &&
        Objects.equals(this.personOrBusinessIndicator, customerEntities.personOrBusinessIndicator) &&
        Objects.equals(this.edqCloseOfBusinessDate, customerEntities.edqCloseOfBusinessDate) &&
        Objects.equals(this.operatingCountries, customerEntities.operatingCountries) &&
        Objects.equals(this.edqOperatingCountriesCodes, customerEntities.edqOperatingCountriesCodes) &&
        Objects.equals(this.registrationNumber, customerEntities.registrationNumber) &&
        Objects.equals(this.profileNameType, customerEntities.profileNameType) &&
        Objects.equals(this.edqPartyRoleTypeDescription, customerEntities.edqPartyRoleTypeDescription) &&
        Objects.equals(this.edqPartyStatusCodeDescription, customerEntities.edqPartyStatusCodeDescription) &&
        Objects.equals(this.edqDay1SpikeFlag, customerEntities.edqDay1SpikeFlag) &&
        Objects.equals(this.originalScriptName, customerEntities.originalScriptName) &&
        Objects.equals(this.profileFullAddress, customerEntities.profileFullAddress) &&
        Objects.equals(this.edqScreeningMode, customerEntities.edqScreeningMode) &&
        Objects.equals(this.edqCaseKey, customerEntities.edqCaseKey) &&
        Objects.equals(this.addressType, customerEntities.addressType) &&
        Objects.equals(this.sSCCodes, customerEntities.sSCCodes) &&
        Objects.equals(this.cTRPFragment, customerEntities.cTRPFragment) &&
        Objects.equals(this.requestUserName, customerEntities.requestUserName) &&
        Objects.equals(this.requestDateTime, customerEntities.requestDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordId, inputStream, sourceSystemHistoryID, closeOfBusinessDate, partitionNumber, sourceSystemIdentifier, externalProfileID, concatenatedProfileID, profileType, partyRoleTypeCode, profileStatus, profileSegment, lOBName, lOBRegion, lOBCountry, hSBCLegalEntityCode, nameCategoryCode, nameTypeCode, nameLanguageTypeCode, entityNameOriginal, pEPIndicator, specialCategoryCustomer, sourceAddressType, addressFormatCode, addressLanguageTypeCode, sourceAddressLine1, sourceAddressLine2, sourceAddressLine3, sourceAddressLine4, sourceAddressLine5, sourceAddressLine6, sourceAddressLine7, sourceAddressLine8, sourceAddressLine9, sourceAddressLine10, sourcePostalCode, sourceAddressCountry, identificationDocument1, identificationDocument2, identificationDocument3, identificationDocument4, identificationDocument5, identificationDocument6, identificationDocument7, identificationDocument8, identificationDocument9, identificationDocument10, tradesWithCountries, subsidiariesOperatesInCountries, exportCountries, importCountries, countriesOfIncorporation, countriesOfRegistrationOriginal, countriesOfBusiness, countriesOfHeadOffice, edqCustID, edqCustSubID, edqLoBCountryCode, addressCountry, edqAddressCountryCode, edqTradesWithCountries, edqExportsCountries, edqImportsCountries, edqIncorporationCountries, edqIncorporationCountriesCodes, registrationCountry, edqRegiistrationCountriesCodes, edqBusinessCountries, edqHeadOfficeCountries, countriesAll, countriesAllCodes, edqLoB, edqPermission, taxID, city, profileHyperlink, searchHyperlink, edqListKey, address, entityName, postalCode, personOrBusinessIndicator, edqCloseOfBusinessDate, operatingCountries, edqOperatingCountriesCodes, registrationNumber, profileNameType, edqPartyRoleTypeDescription, edqPartyStatusCodeDescription, edqDay1SpikeFlag, originalScriptName, profileFullAddress, edqScreeningMode, edqCaseKey, addressType, sSCCodes, cTRPFragment, requestUserName, requestDateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerEntities {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    sourceSystemHistoryID: ").append(toIndentedString(sourceSystemHistoryID)).append("\n");
    sb.append("    closeOfBusinessDate: ").append(toIndentedString(closeOfBusinessDate)).append("\n");
    sb.append("    partitionNumber: ").append(toIndentedString(partitionNumber)).append("\n");
    sb.append("    sourceSystemIdentifier: ").append(toIndentedString(sourceSystemIdentifier)).append("\n");
    sb.append("    externalProfileID: ").append(toIndentedString(externalProfileID)).append("\n");
    sb.append("    concatenatedProfileID: ").append(toIndentedString(concatenatedProfileID)).append("\n");
    sb.append("    profileType: ").append(toIndentedString(profileType)).append("\n");
    sb.append("    partyRoleTypeCode: ").append(toIndentedString(partyRoleTypeCode)).append("\n");
    sb.append("    profileStatus: ").append(toIndentedString(profileStatus)).append("\n");
    sb.append("    profileSegment: ").append(toIndentedString(profileSegment)).append("\n");
    sb.append("    lOBName: ").append(toIndentedString(lOBName)).append("\n");
    sb.append("    lOBRegion: ").append(toIndentedString(lOBRegion)).append("\n");
    sb.append("    lOBCountry: ").append(toIndentedString(lOBCountry)).append("\n");
    sb.append("    hSBCLegalEntityCode: ").append(toIndentedString(hSBCLegalEntityCode)).append("\n");
    sb.append("    nameCategoryCode: ").append(toIndentedString(nameCategoryCode)).append("\n");
    sb.append("    nameTypeCode: ").append(toIndentedString(nameTypeCode)).append("\n");
    sb.append("    nameLanguageTypeCode: ").append(toIndentedString(nameLanguageTypeCode)).append("\n");
    sb.append("    entityNameOriginal: ").append(toIndentedString(entityNameOriginal)).append("\n");
    sb.append("    pEPIndicator: ").append(toIndentedString(pEPIndicator)).append("\n");
    sb.append("    specialCategoryCustomer: ").append(toIndentedString(specialCategoryCustomer)).append("\n");
    sb.append("    sourceAddressType: ").append(toIndentedString(sourceAddressType)).append("\n");
    sb.append("    addressFormatCode: ").append(toIndentedString(addressFormatCode)).append("\n");
    sb.append("    addressLanguageTypeCode: ").append(toIndentedString(addressLanguageTypeCode)).append("\n");
    sb.append("    sourceAddressLine1: ").append(toIndentedString(sourceAddressLine1)).append("\n");
    sb.append("    sourceAddressLine2: ").append(toIndentedString(sourceAddressLine2)).append("\n");
    sb.append("    sourceAddressLine3: ").append(toIndentedString(sourceAddressLine3)).append("\n");
    sb.append("    sourceAddressLine4: ").append(toIndentedString(sourceAddressLine4)).append("\n");
    sb.append("    sourceAddressLine5: ").append(toIndentedString(sourceAddressLine5)).append("\n");
    sb.append("    sourceAddressLine6: ").append(toIndentedString(sourceAddressLine6)).append("\n");
    sb.append("    sourceAddressLine7: ").append(toIndentedString(sourceAddressLine7)).append("\n");
    sb.append("    sourceAddressLine8: ").append(toIndentedString(sourceAddressLine8)).append("\n");
    sb.append("    sourceAddressLine9: ").append(toIndentedString(sourceAddressLine9)).append("\n");
    sb.append("    sourceAddressLine10: ").append(toIndentedString(sourceAddressLine10)).append("\n");
    sb.append("    sourcePostalCode: ").append(toIndentedString(sourcePostalCode)).append("\n");
    sb.append("    sourceAddressCountry: ").append(toIndentedString(sourceAddressCountry)).append("\n");
    sb.append("    identificationDocument1: ").append(toIndentedString(identificationDocument1)).append("\n");
    sb.append("    identificationDocument2: ").append(toIndentedString(identificationDocument2)).append("\n");
    sb.append("    identificationDocument3: ").append(toIndentedString(identificationDocument3)).append("\n");
    sb.append("    identificationDocument4: ").append(toIndentedString(identificationDocument4)).append("\n");
    sb.append("    identificationDocument5: ").append(toIndentedString(identificationDocument5)).append("\n");
    sb.append("    identificationDocument6: ").append(toIndentedString(identificationDocument6)).append("\n");
    sb.append("    identificationDocument7: ").append(toIndentedString(identificationDocument7)).append("\n");
    sb.append("    identificationDocument8: ").append(toIndentedString(identificationDocument8)).append("\n");
    sb.append("    identificationDocument9: ").append(toIndentedString(identificationDocument9)).append("\n");
    sb.append("    identificationDocument10: ").append(toIndentedString(identificationDocument10)).append("\n");
    sb.append("    tradesWithCountries: ").append(toIndentedString(tradesWithCountries)).append("\n");
    sb.append("    subsidiariesOperatesInCountries: ").append(toIndentedString(subsidiariesOperatesInCountries)).append("\n");
    sb.append("    exportCountries: ").append(toIndentedString(exportCountries)).append("\n");
    sb.append("    importCountries: ").append(toIndentedString(importCountries)).append("\n");
    sb.append("    countriesOfIncorporation: ").append(toIndentedString(countriesOfIncorporation)).append("\n");
    sb.append("    countriesOfRegistrationOriginal: ").append(toIndentedString(countriesOfRegistrationOriginal)).append("\n");
    sb.append("    countriesOfBusiness: ").append(toIndentedString(countriesOfBusiness)).append("\n");
    sb.append("    countriesOfHeadOffice: ").append(toIndentedString(countriesOfHeadOffice)).append("\n");
    sb.append("    edqCustID: ").append(toIndentedString(edqCustID)).append("\n");
    sb.append("    edqCustSubID: ").append(toIndentedString(edqCustSubID)).append("\n");
    sb.append("    edqLoBCountryCode: ").append(toIndentedString(edqLoBCountryCode)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb.append("    edqAddressCountryCode: ").append(toIndentedString(edqAddressCountryCode)).append("\n");
    sb.append("    edqTradesWithCountries: ").append(toIndentedString(edqTradesWithCountries)).append("\n");
    sb.append("    edqExportsCountries: ").append(toIndentedString(edqExportsCountries)).append("\n");
    sb.append("    edqImportsCountries: ").append(toIndentedString(edqImportsCountries)).append("\n");
    sb.append("    edqIncorporationCountries: ").append(toIndentedString(edqIncorporationCountries)).append("\n");
    sb.append("    edqIncorporationCountriesCodes: ").append(toIndentedString(edqIncorporationCountriesCodes)).append("\n");
    sb.append("    registrationCountry: ").append(toIndentedString(registrationCountry)).append("\n");
    sb.append("    edqRegiistrationCountriesCodes: ").append(toIndentedString(edqRegiistrationCountriesCodes)).append("\n");
    sb.append("    edqBusinessCountries: ").append(toIndentedString(edqBusinessCountries)).append("\n");
    sb.append("    edqHeadOfficeCountries: ").append(toIndentedString(edqHeadOfficeCountries)).append("\n");
    sb.append("    countriesAll: ").append(toIndentedString(countriesAll)).append("\n");
    sb.append("    countriesAllCodes: ").append(toIndentedString(countriesAllCodes)).append("\n");
    sb.append("    edqLoB: ").append(toIndentedString(edqLoB)).append("\n");
    sb.append("    edqPermission: ").append(toIndentedString(edqPermission)).append("\n");
    sb.append("    taxID: ").append(toIndentedString(taxID)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    profileHyperlink: ").append(toIndentedString(profileHyperlink)).append("\n");
    sb.append("    searchHyperlink: ").append(toIndentedString(searchHyperlink)).append("\n");
    sb.append("    edqListKey: ").append(toIndentedString(edqListKey)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    entityName: ").append(toIndentedString(entityName)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    personOrBusinessIndicator: ").append(toIndentedString(personOrBusinessIndicator)).append("\n");
    sb.append("    edqCloseOfBusinessDate: ").append(toIndentedString(edqCloseOfBusinessDate)).append("\n");
    sb.append("    operatingCountries: ").append(toIndentedString(operatingCountries)).append("\n");
    sb.append("    edqOperatingCountriesCodes: ").append(toIndentedString(edqOperatingCountriesCodes)).append("\n");
    sb.append("    registrationNumber: ").append(toIndentedString(registrationNumber)).append("\n");
    sb.append("    profileNameType: ").append(toIndentedString(profileNameType)).append("\n");
    sb.append("    edqPartyRoleTypeDescription: ").append(toIndentedString(edqPartyRoleTypeDescription)).append("\n");
    sb.append("    edqPartyStatusCodeDescription: ").append(toIndentedString(edqPartyStatusCodeDescription)).append("\n");
    sb.append("    edqDay1SpikeFlag: ").append(toIndentedString(edqDay1SpikeFlag)).append("\n");
    sb.append("    originalScriptName: ").append(toIndentedString(originalScriptName)).append("\n");
    sb.append("    profileFullAddress: ").append(toIndentedString(profileFullAddress)).append("\n");
    sb.append("    edqScreeningMode: ").append(toIndentedString(edqScreeningMode)).append("\n");
    sb.append("    edqCaseKey: ").append(toIndentedString(edqCaseKey)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    sSCCodes: ").append(toIndentedString(sSCCodes)).append("\n");
    sb.append("    cTRPFragment: ").append(toIndentedString(cTRPFragment)).append("\n");
    sb.append("    requestUserName: ").append(toIndentedString(requestUserName)).append("\n");
    sb.append("    requestDateTime: ").append(toIndentedString(requestDateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
