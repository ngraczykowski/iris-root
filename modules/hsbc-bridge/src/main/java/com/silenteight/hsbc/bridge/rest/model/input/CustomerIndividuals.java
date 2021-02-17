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
 * CustomerIndividuals
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CustomerIndividuals   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("recordId")
  private BigDecimal recordId = null;

  @JsonProperty("inputStream")
  private String inputStream = null;

  @JsonProperty("sourceSystemHistoryID")
  private BigDecimal sourceSystemHistoryID = null;

  @JsonProperty("closeOfBusinessDate")
  private String closeOfBusinessDate = null;

  @JsonProperty("partitionNumber")
  private BigDecimal partitionNumber = null;

  @JsonProperty("sourceSystemIdentifier")
  private BigDecimal sourceSystemIdentifier = null;

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

  @JsonProperty("loBName")
  private String loBName = null;

  @JsonProperty("loBRegion")
  private String loBRegion = null;

  @JsonProperty("loBCountry")
  private String loBCountry = null;

  @JsonProperty("hSBCLegalEntityCode")
  private String hSBCLegalEntityCode = null;

  @JsonProperty("nameCategoryCode")
  private String nameCategoryCode = null;

  @JsonProperty("nameTypeCode")
  private String nameTypeCode = null;

  @JsonProperty("nameLanguageTypeCode")
  private String nameLanguageTypeCode = null;

  @JsonProperty("givenName")
  private String givenName = null;

  @JsonProperty("middleName")
  private String middleName = null;

  @JsonProperty("familyNameOriginal")
  private String familyNameOriginal = null;

  @JsonProperty("initialsOriginal")
  private String initialsOriginal = null;

  @JsonProperty("profileFullName")
  private String profileFullName = null;

  @JsonProperty("pEPIndicator")
  private String pEPIndicator = null;

  @JsonProperty("specialCategoryCustomerIndicator")
  private String specialCategoryCustomerIndicator = null;

  @JsonProperty("genderCode")
  private String genderCode = null;

  @JsonProperty("birthDate")
  private String birthDate = null;

  @JsonProperty("placeOfBirth")
  private String placeOfBirth = null;

  @JsonProperty("townOfBirth")
  private String townOfBirth = null;

  @JsonProperty("state,ProvinceOrCountyOfBirth")
  private String stateProvinceOrCountyOfBirth = null;

  @JsonProperty("countryOfBirthOriginal")
  private String countryOfBirthOriginal = null;

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

  @JsonProperty("sourceCountry")
  private String sourceCountry = null;

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

  @JsonProperty("residenceCountries")
  private String residenceCountries = null;

  @JsonProperty("nationalityCitizenshipCountries")
  private String nationalityCitizenshipCountries = null;

  @JsonProperty("employer1Details")
  private String employer1Details = null;

  @JsonProperty("employer1Address1")
  private String employer1Address1 = null;

  @JsonProperty("employer1Address2")
  private String employer1Address2 = null;

  @JsonProperty("employer1Address3")
  private String employer1Address3 = null;

  @JsonProperty("employer2Details")
  private String employer2Details = null;

  @JsonProperty("employer2Address1")
  private String employer2Address1 = null;

  @JsonProperty("employer2Address2")
  private String employer2Address2 = null;

  @JsonProperty("employer2Address3")
  private String employer2Address3 = null;

  @JsonProperty("employer3Details")
  private String employer3Details = null;

  @JsonProperty("employer3Address1")
  private String employer3Address1 = null;

  @JsonProperty("employer3Address2")
  private String employer3Address2 = null;

  @JsonProperty("employer3Address3")
  private String employer3Address3 = null;

  @JsonProperty("edqCustID")
  private String edqCustID = null;

  @JsonProperty("edqCustSubID")
  private String edqCustSubID = null;

  @JsonProperty("titleDerived")
  private String titleDerived = null;

  @JsonProperty("givenNamesDerived")
  private String givenNamesDerived = null;

  @JsonProperty("fullNameDerived")
  private String fullNameDerived = null;

  @JsonProperty("familyNameDerived")
  private String familyNameDerived = null;

  @JsonProperty("initials")
  private String initials = null;

  @JsonProperty("edqLoBCountryCode")
  private String edqLoBCountryCode = null;

  @JsonProperty("countryOfBirth")
  private String countryOfBirth = null;

  @JsonProperty("addressCountry")
  private String addressCountry = null;

  @JsonProperty("edqAddressCountryCode")
  private String edqAddressCountryCode = null;

  @JsonProperty("countryOfResidence")
  private String countryOfResidence = null;

  @JsonProperty("edqBirthCountryCode")
  private String edqBirthCountryCode = null;

  @JsonProperty("edqResidenceCountriesCode")
  private String edqResidenceCountriesCode = null;

  @JsonProperty("nationalityOrCitizenship")
  private String nationalityOrCitizenship = null;

  @JsonProperty("nationalityCountries")
  private String nationalityCountries = null;

  @JsonProperty("countriesAll")
  private String countriesAll = null;

  @JsonProperty("edqCountriesAllCodes")
  private String edqCountriesAllCodes = null;

  @JsonProperty("edqEmployerAllCountries")
  private String edqEmployerAllCountries = null;

  @JsonProperty("edqEmployerAllCountriesCodes")
  private String edqEmployerAllCountriesCodes = null;

  @JsonProperty("edqLoB")
  private String edqLoB = null;

  @JsonProperty("edqPermission")
  private String edqPermission = null;

  @JsonProperty("taxID")
  private String taxID = null;

  @JsonProperty("dateOfBirth")
  private String dateOfBirth = null;

  @JsonProperty("dOBOriginal")
  private String dOBOriginal = null;

  @JsonProperty("edqDOBString")
  private String edqDOBString = null;

  @JsonProperty("yearOfBirth")
  private String yearOfBirth = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("postalCode")
  private String postalCode = null;

  @JsonProperty("profileFullAddress")
  private String profileFullAddress = null;

  @JsonProperty("gender")
  private String gender = null;

  @JsonProperty("genderDerivedFlag")
  private String genderDerivedFlag = null;

  @JsonProperty("profileHyperlink")
  private String profileHyperlink = null;

  @JsonProperty("searchHyperlink")
  private String searchHyperlink = null;

  @JsonProperty("edqListKey")
  private String edqListKey = null;

  @JsonProperty("passportNumber")
  private String passportNumber = null;

  @JsonProperty("passportIssueCountry")
  private String passportIssueCountry = null;

  @JsonProperty("socialSecurityNumber")
  private String socialSecurityNumber = null;

  @JsonProperty("edqCloseOfBusinessDate")
  private String edqCloseOfBusinessDate = null;

  @JsonProperty("profileOccupation")
  private String profileOccupation = null;

  @JsonProperty("personOrBusinessIndicator")
  private String personOrBusinessIndicator = null;

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

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("givenNamesOriginal")
  private String givenNamesOriginal = null;

  @JsonProperty("surname")
  private String surname = null;

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

  @JsonProperty("requestDateTimeDDMMYYYY0000")
  private String requestDateTimeDDMMYYYY0000 = null;

  @JsonProperty("autoDiscountDecision")
  private String autoDiscountDecision = null;

  @JsonProperty("recordType")
  private String recordType = null;

  @JsonProperty("dummy")
  private String dummy = null;

  public CustomerIndividuals caseId(Integer caseId) {
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

  public CustomerIndividuals recordId(BigDecimal recordId) {
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

  public CustomerIndividuals inputStream(String inputStream) {
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

  public CustomerIndividuals sourceSystemHistoryID(BigDecimal sourceSystemHistoryID) {
    this.sourceSystemHistoryID = sourceSystemHistoryID;
    return this;
  }

  /**
   * Identifier used during reconciliation to store customer information
   * @return sourceSystemHistoryID
   **/
  @Schema(description = "Identifier used during reconciliation to store customer information")
  
    @Valid
    public BigDecimal getSourceSystemHistoryID() {
    return sourceSystemHistoryID;
  }

  public void setSourceSystemHistoryID(BigDecimal sourceSystemHistoryID) {
    this.sourceSystemHistoryID = sourceSystemHistoryID;
  }

  public CustomerIndividuals closeOfBusinessDate(String closeOfBusinessDate) {
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

  public CustomerIndividuals partitionNumber(BigDecimal partitionNumber) {
    this.partitionNumber = partitionNumber;
    return this;
  }

  /**
   * Where a customer feed is larger than a certain threshold it will be partitioned into sections. The partion number denotes which partition this record has come from.
   * @return partitionNumber
   **/
  @Schema(description = "Where a customer feed is larger than a certain threshold it will be partitioned into sections. The partion number denotes which partition this record has come from.")
  
    @Valid
    public BigDecimal getPartitionNumber() {
    return partitionNumber;
  }

  public void setPartitionNumber(BigDecimal partitionNumber) {
    this.partitionNumber = partitionNumber;
  }

  public CustomerIndividuals sourceSystemIdentifier(BigDecimal sourceSystemIdentifier) {
    this.sourceSystemIdentifier = sourceSystemIdentifier;
    return this;
  }

  /**
   * The Identifier used to identify where the feed originated from
   * @return sourceSystemIdentifier
   **/
  @Schema(description = "The Identifier used to identify where the feed originated from")
  
    @Valid
    public BigDecimal getSourceSystemIdentifier() {
    return sourceSystemIdentifier;
  }

  public void setSourceSystemIdentifier(BigDecimal sourceSystemIdentifier) {
    this.sourceSystemIdentifier = sourceSystemIdentifier;
  }

  public CustomerIndividuals externalProfileID(String externalProfileID) {
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

  public CustomerIndividuals concatenatedProfileID(String concatenatedProfileID) {
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

  public CustomerIndividuals profileType(String profileType) {
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

  public CustomerIndividuals partyRoleTypeCode(String partyRoleTypeCode) {
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

  public CustomerIndividuals profileStatus(String profileStatus) {
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

  public CustomerIndividuals profileSegment(String profileSegment) {
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

  public CustomerIndividuals loBName(String loBName) {
    this.loBName = loBName;
    return this;
  }

  /**
   * Line of Business name
   * @return loBName
   **/
  @Schema(description = "Line of Business name")
  
    public String getLoBName() {
    return loBName;
  }

  public void setLoBName(String loBName) {
    this.loBName = loBName;
  }

  public CustomerIndividuals loBRegion(String loBRegion) {
    this.loBRegion = loBRegion;
    return this;
  }

  /**
   * Line of Business region
   * @return loBRegion
   **/
  @Schema(description = "Line of Business region")
  
    public String getLoBRegion() {
    return loBRegion;
  }

  public void setLoBRegion(String loBRegion) {
    this.loBRegion = loBRegion;
  }

  public CustomerIndividuals loBCountry(String loBCountry) {
    this.loBCountry = loBCountry;
    return this;
  }

  /**
   * Line of Business Country Code
   * @return loBCountry
   **/
  @Schema(description = "Line of Business Country Code")
  
    public String getLoBCountry() {
    return loBCountry;
  }

  public void setLoBCountry(String loBCountry) {
    this.loBCountry = loBCountry;
  }

  public CustomerIndividuals hSBCLegalEntityCode(String hSBCLegalEntityCode) {
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

  public CustomerIndividuals nameCategoryCode(String nameCategoryCode) {
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

  public CustomerIndividuals nameTypeCode(String nameTypeCode) {
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

  public CustomerIndividuals nameLanguageTypeCode(String nameLanguageTypeCode) {
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

  public CustomerIndividuals givenName(String givenName) {
    this.givenName = givenName;
    return this;
  }

  /**
   * Customer Given Name
   * @return givenName
   **/
  @Schema(description = "Customer Given Name")
  
    public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public CustomerIndividuals middleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  /**
   * Customer Middle Name
   * @return middleName
   **/
  @Schema(description = "Customer Middle Name")
  
    public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public CustomerIndividuals familyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
    return this;
  }

  /**
   * Customer Family Name (Original)
   * @return familyNameOriginal
   **/
  @Schema(description = "Customer Family Name (Original)")
  
    public String getFamilyNameOriginal() {
    return familyNameOriginal;
  }

  public void setFamilyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
  }

  public CustomerIndividuals initialsOriginal(String initialsOriginal) {
    this.initialsOriginal = initialsOriginal;
    return this;
  }

  /**
   * Initials (Original)
   * @return initialsOriginal
   **/
  @Schema(description = "Initials (Original)")
  
    public String getInitialsOriginal() {
    return initialsOriginal;
  }

  public void setInitialsOriginal(String initialsOriginal) {
    this.initialsOriginal = initialsOriginal;
  }

  public CustomerIndividuals profileFullName(String profileFullName) {
    this.profileFullName = profileFullName;
    return this;
  }

  /**
   * Original Unstructured Name
   * @return profileFullName
   **/
  @Schema(description = "Original Unstructured Name")
  
    public String getProfileFullName() {
    return profileFullName;
  }

  public void setProfileFullName(String profileFullName) {
    this.profileFullName = profileFullName;
  }

  public CustomerIndividuals pEPIndicator(String pEPIndicator) {
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

  public CustomerIndividuals specialCategoryCustomerIndicator(String specialCategoryCustomerIndicator) {
    this.specialCategoryCustomerIndicator = specialCategoryCustomerIndicator;
    return this;
  }

  /**
   * Indicates if the record is regarded as a Special Chategory Person or not in the source system
   * @return specialCategoryCustomerIndicator
   **/
  @Schema(description = "Indicates if the record is regarded as a Special Chategory Person or not in the source system")
  
    public String getSpecialCategoryCustomerIndicator() {
    return specialCategoryCustomerIndicator;
  }

  public void setSpecialCategoryCustomerIndicator(String specialCategoryCustomerIndicator) {
    this.specialCategoryCustomerIndicator = specialCategoryCustomerIndicator;
  }

  public CustomerIndividuals genderCode(String genderCode) {
    this.genderCode = genderCode;
    return this;
  }

  /**
   * Original Customer Gender Code
   * @return genderCode
   **/
  @Schema(description = "Original Customer Gender Code")
  
    public String getGenderCode() {
    return genderCode;
  }

  public void setGenderCode(String genderCode) {
    this.genderCode = genderCode;
  }

  public CustomerIndividuals birthDate(String birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  /**
   * Original Customer Birth Date
   * @return birthDate
   **/
  @Schema(description = "Original Customer Birth Date")
  
    public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public CustomerIndividuals placeOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
    return this;
  }

  /**
   * Original Country Of Birth Name
   * @return placeOfBirth
   **/
  @Schema(description = "Original Country Of Birth Name")
  
    public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  public CustomerIndividuals townOfBirth(String townOfBirth) {
    this.townOfBirth = townOfBirth;
    return this;
  }

  /**
   * Original Town Of Birth
   * @return townOfBirth
   **/
  @Schema(description = "Original Town Of Birth")
  
    public String getTownOfBirth() {
    return townOfBirth;
  }

  public void setTownOfBirth(String townOfBirth) {
    this.townOfBirth = townOfBirth;
  }

  public CustomerIndividuals stateProvinceOrCountyOfBirth(String stateProvinceOrCountyOfBirth) {
    this.stateProvinceOrCountyOfBirth = stateProvinceOrCountyOfBirth;
    return this;
  }

  /**
   * Original State, Province or County of Birth
   * @return stateProvinceOrCountyOfBirth
   **/
  @Schema(description = "Original State, Province or County of Birth")
  
    public String getStateProvinceOrCountyOfBirth() {
    return stateProvinceOrCountyOfBirth;
  }

  public void setStateProvinceOrCountyOfBirth(String stateProvinceOrCountyOfBirth) {
    this.stateProvinceOrCountyOfBirth = stateProvinceOrCountyOfBirth;
  }

  public CustomerIndividuals countryOfBirthOriginal(String countryOfBirthOriginal) {
    this.countryOfBirthOriginal = countryOfBirthOriginal;
    return this;
  }

  /**
   * Original Country Of Birth Code
   * @return countryOfBirthOriginal
   **/
  @Schema(description = "Original Country Of Birth Code")
  
    public String getCountryOfBirthOriginal() {
    return countryOfBirthOriginal;
  }

  public void setCountryOfBirthOriginal(String countryOfBirthOriginal) {
    this.countryOfBirthOriginal = countryOfBirthOriginal;
  }

  public CustomerIndividuals sourceAddressType(String sourceAddressType) {
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

  public CustomerIndividuals addressFormatCode(String addressFormatCode) {
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

  public CustomerIndividuals addressLanguageTypeCode(String addressLanguageTypeCode) {
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

  public CustomerIndividuals sourceAddressLine1(String sourceAddressLine1) {
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

  public CustomerIndividuals sourceAddressLine2(String sourceAddressLine2) {
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

  public CustomerIndividuals sourceAddressLine3(String sourceAddressLine3) {
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

  public CustomerIndividuals sourceAddressLine4(String sourceAddressLine4) {
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

  public CustomerIndividuals sourceAddressLine5(String sourceAddressLine5) {
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

  public CustomerIndividuals sourceAddressLine6(String sourceAddressLine6) {
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

  public CustomerIndividuals sourceAddressLine7(String sourceAddressLine7) {
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

  public CustomerIndividuals sourceAddressLine8(String sourceAddressLine8) {
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

  public CustomerIndividuals sourceAddressLine9(String sourceAddressLine9) {
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

  public CustomerIndividuals sourceAddressLine10(String sourceAddressLine10) {
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

  public CustomerIndividuals sourcePostalCode(String sourcePostalCode) {
    this.sourcePostalCode = sourcePostalCode;
    return this;
  }

  /**
   * Address Postal Code as provided in the customer record
   * @return sourcePostalCode
   **/
  @Schema(description = "Address Postal Code as provided in the customer record")
  
    public String getSourcePostalCode() {
    return sourcePostalCode;
  }

  public void setSourcePostalCode(String sourcePostalCode) {
    this.sourcePostalCode = sourcePostalCode;
  }

  public CustomerIndividuals sourceCountry(String sourceCountry) {
    this.sourceCountry = sourceCountry;
    return this;
  }

  /**
   * Address Country as provided in the customer record
   * @return sourceCountry
   **/
  @Schema(description = "Address Country as provided in the customer record")
  
    public String getSourceCountry() {
    return sourceCountry;
  }

  public void setSourceCountry(String sourceCountry) {
    this.sourceCountry = sourceCountry;
  }

  public CustomerIndividuals identificationDocument1(String identificationDocument1) {
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

  public CustomerIndividuals identificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument2
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument2() {
    return identificationDocument2;
  }

  public void setIdentificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
  }

  public CustomerIndividuals identificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument3
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument3() {
    return identificationDocument3;
  }

  public void setIdentificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
  }

  public CustomerIndividuals identificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument4
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument4() {
    return identificationDocument4;
  }

  public void setIdentificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
  }

  public CustomerIndividuals identificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument5
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument5() {
    return identificationDocument5;
  }

  public void setIdentificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
  }

  public CustomerIndividuals identificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument6
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument6() {
    return identificationDocument6;
  }

  public void setIdentificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
  }

  public CustomerIndividuals identificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument7
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument7() {
    return identificationDocument7;
  }

  public void setIdentificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
  }

  public CustomerIndividuals identificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument8
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument8() {
    return identificationDocument8;
  }

  public void setIdentificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
  }

  public CustomerIndividuals identificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument9
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument9() {
    return identificationDocument9;
  }

  public void setIdentificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
  }

  public CustomerIndividuals identificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
    return this;
  }

  /**
   * Identification document.
   * @return identificationDocument10
   **/
  @Schema(description = "Identification document.")
  
    public String getIdentificationDocument10() {
    return identificationDocument10;
  }

  public void setIdentificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
  }

  public CustomerIndividuals residenceCountries(String residenceCountries) {
    this.residenceCountries = residenceCountries;
    return this;
  }

  /**
   * List of countries where the customer has been resident
   * @return residenceCountries
   **/
  @Schema(description = "List of countries where the customer has been resident")
  
    public String getResidenceCountries() {
    return residenceCountries;
  }

  public void setResidenceCountries(String residenceCountries) {
    this.residenceCountries = residenceCountries;
  }

  public CustomerIndividuals nationalityCitizenshipCountries(String nationalityCitizenshipCountries) {
    this.nationalityCitizenshipCountries = nationalityCitizenshipCountries;
    return this;
  }

  /**
   * Original List of countries where the customer has nationality
   * @return nationalityCitizenshipCountries
   **/
  @Schema(description = "Original List of countries where the customer has nationality")
  
    public String getNationalityCitizenshipCountries() {
    return nationalityCitizenshipCountries;
  }

  public void setNationalityCitizenshipCountries(String nationalityCitizenshipCountries) {
    this.nationalityCitizenshipCountries = nationalityCitizenshipCountries;
  }

  public CustomerIndividuals employer1Details(String employer1Details) {
    this.employer1Details = employer1Details;
    return this;
  }

  /**
   * Customer Employer 1 Name
   * @return employer1Details
   **/
  @Schema(description = "Customer Employer 1 Name")
  
    public String getEmployer1Details() {
    return employer1Details;
  }

  public void setEmployer1Details(String employer1Details) {
    this.employer1Details = employer1Details;
  }

  public CustomerIndividuals employer1Address1(String employer1Address1) {
    this.employer1Address1 = employer1Address1;
    return this;
  }

  /**
   * Customer Employer 1 Address 1
   * @return employer1Address1
   **/
  @Schema(description = "Customer Employer 1 Address 1")
  
    public String getEmployer1Address1() {
    return employer1Address1;
  }

  public void setEmployer1Address1(String employer1Address1) {
    this.employer1Address1 = employer1Address1;
  }

  public CustomerIndividuals employer1Address2(String employer1Address2) {
    this.employer1Address2 = employer1Address2;
    return this;
  }

  /**
   * Customer Employer 1 Address 2
   * @return employer1Address2
   **/
  @Schema(description = "Customer Employer 1 Address 2")
  
    public String getEmployer1Address2() {
    return employer1Address2;
  }

  public void setEmployer1Address2(String employer1Address2) {
    this.employer1Address2 = employer1Address2;
  }

  public CustomerIndividuals employer1Address3(String employer1Address3) {
    this.employer1Address3 = employer1Address3;
    return this;
  }

  /**
   * Customer Employer 1 Address 3
   * @return employer1Address3
   **/
  @Schema(description = "Customer Employer 1 Address 3")
  
    public String getEmployer1Address3() {
    return employer1Address3;
  }

  public void setEmployer1Address3(String employer1Address3) {
    this.employer1Address3 = employer1Address3;
  }

  public CustomerIndividuals employer2Details(String employer2Details) {
    this.employer2Details = employer2Details;
    return this;
  }

  /**
   * Customer Employer 2 Name
   * @return employer2Details
   **/
  @Schema(description = "Customer Employer 2 Name")
  
    public String getEmployer2Details() {
    return employer2Details;
  }

  public void setEmployer2Details(String employer2Details) {
    this.employer2Details = employer2Details;
  }

  public CustomerIndividuals employer2Address1(String employer2Address1) {
    this.employer2Address1 = employer2Address1;
    return this;
  }

  /**
   * Customer Employer 2 Address 1
   * @return employer2Address1
   **/
  @Schema(description = "Customer Employer 2 Address 1")
  
    public String getEmployer2Address1() {
    return employer2Address1;
  }

  public void setEmployer2Address1(String employer2Address1) {
    this.employer2Address1 = employer2Address1;
  }

  public CustomerIndividuals employer2Address2(String employer2Address2) {
    this.employer2Address2 = employer2Address2;
    return this;
  }

  /**
   * Customer Employer 2 Address 2
   * @return employer2Address2
   **/
  @Schema(description = "Customer Employer 2 Address 2")
  
    public String getEmployer2Address2() {
    return employer2Address2;
  }

  public void setEmployer2Address2(String employer2Address2) {
    this.employer2Address2 = employer2Address2;
  }

  public CustomerIndividuals employer2Address3(String employer2Address3) {
    this.employer2Address3 = employer2Address3;
    return this;
  }

  /**
   * Customer Employer 2 Address 3
   * @return employer2Address3
   **/
  @Schema(description = "Customer Employer 2 Address 3")
  
    public String getEmployer2Address3() {
    return employer2Address3;
  }

  public void setEmployer2Address3(String employer2Address3) {
    this.employer2Address3 = employer2Address3;
  }

  public CustomerIndividuals employer3Details(String employer3Details) {
    this.employer3Details = employer3Details;
    return this;
  }

  /**
   * Customer Employer 3 Name
   * @return employer3Details
   **/
  @Schema(description = "Customer Employer 3 Name")
  
    public String getEmployer3Details() {
    return employer3Details;
  }

  public void setEmployer3Details(String employer3Details) {
    this.employer3Details = employer3Details;
  }

  public CustomerIndividuals employer3Address1(String employer3Address1) {
    this.employer3Address1 = employer3Address1;
    return this;
  }

  /**
   * Customer Employer 3 Address 1
   * @return employer3Address1
   **/
  @Schema(description = "Customer Employer 3 Address 1")
  
    public String getEmployer3Address1() {
    return employer3Address1;
  }

  public void setEmployer3Address1(String employer3Address1) {
    this.employer3Address1 = employer3Address1;
  }

  public CustomerIndividuals employer3Address2(String employer3Address2) {
    this.employer3Address2 = employer3Address2;
    return this;
  }

  /**
   * Customer Employer 3 Address 2
   * @return employer3Address2
   **/
  @Schema(description = "Customer Employer 3 Address 2")
  
    public String getEmployer3Address2() {
    return employer3Address2;
  }

  public void setEmployer3Address2(String employer3Address2) {
    this.employer3Address2 = employer3Address2;
  }

  public CustomerIndividuals employer3Address3(String employer3Address3) {
    this.employer3Address3 = employer3Address3;
    return this;
  }

  /**
   * Customer Employer 3 Address 3
   * @return employer3Address3
   **/
  @Schema(description = "Customer Employer 3 Address 3")
  
    public String getEmployer3Address3() {
    return employer3Address3;
  }

  public void setEmployer3Address3(String employer3Address3) {
    this.employer3Address3 = employer3Address3;
  }

  public CustomerIndividuals edqCustID(String edqCustID) {
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

  public CustomerIndividuals edqCustSubID(String edqCustSubID) {
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

  public CustomerIndividuals titleDerived(String titleDerived) {
    this.titleDerived = titleDerived;
    return this;
  }

  /**
   * Title as derived by OWS
   * @return titleDerived
   **/
  @Schema(description = "Title as derived by OWS")
  
    public String getTitleDerived() {
    return titleDerived;
  }

  public void setTitleDerived(String titleDerived) {
    this.titleDerived = titleDerived;
  }

  public CustomerIndividuals givenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
    return this;
  }

  /**
   * Given Name as derived by OWS
   * @return givenNamesDerived
   **/
  @Schema(description = "Given Name as derived by OWS")
  
    public String getGivenNamesDerived() {
    return givenNamesDerived;
  }

  public void setGivenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
  }

  public CustomerIndividuals fullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
    return this;
  }

  /**
   * Full Name as derived by OWS
   * @return fullNameDerived
   **/
  @Schema(description = "Full Name as derived by OWS")
  
    public String getFullNameDerived() {
    return fullNameDerived;
  }

  public void setFullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
  }

  public CustomerIndividuals familyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
    return this;
  }

  /**
   * Family Name as derived by OWS
   * @return familyNameDerived
   **/
  @Schema(description = "Family Name as derived by OWS")
  
    public String getFamilyNameDerived() {
    return familyNameDerived;
  }

  public void setFamilyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
  }

  public CustomerIndividuals initials(String initials) {
    this.initials = initials;
    return this;
  }

  /**
   * Customer Initials
   * @return initials
   **/
  @Schema(description = "Customer Initials")
  
    public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public CustomerIndividuals edqLoBCountryCode(String edqLoBCountryCode) {
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

  public CustomerIndividuals countryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
    return this;
  }

  /**
   * A standardised list of all birth country names associated to the customer record. All standardised names will ISO names.
   * @return countryOfBirth
   **/
  @Schema(description = "A standardised list of all birth country names associated to the customer record. All standardised names will ISO names.")
  
    public String getCountryOfBirth() {
    return countryOfBirth;
  }

  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  public CustomerIndividuals addressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
    return this;
  }

  /**
   * A standardised list of all address country names associated to the customer record. All standardised names will ISO names.
   * @return addressCountry
   **/
  @Schema(description = "A standardised list of all address country names associated to the customer record. All standardised names will ISO names.")
  
    public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public CustomerIndividuals edqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
    return this;
  }

  /**
   * A standardised list of all address country names in ISO 2 character code format associated to the customer record.
   * @return edqAddressCountryCode
   **/
  @Schema(description = "A standardised list of all address country names in ISO 2 character code format associated to the customer record.")
  
    public String getEdqAddressCountryCode() {
    return edqAddressCountryCode;
  }

  public void setEdqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
  }

  public CustomerIndividuals countryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
    return this;
  }

  /**
   * A standardised list of all residency country names associated to the customer record. All standardised names will ISO names.
   * @return countryOfResidence
   **/
  @Schema(description = "A standardised list of all residency country names associated to the customer record. All standardised names will ISO names.")
  
    public String getCountryOfResidence() {
    return countryOfResidence;
  }

  public void setCountryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
  }

  public CustomerIndividuals edqBirthCountryCode(String edqBirthCountryCode) {
    this.edqBirthCountryCode = edqBirthCountryCode;
    return this;
  }

  /**
   * A standardised list of all birth country names in ISO 2 character code format associated to the customer record.
   * @return edqBirthCountryCode
   **/
  @Schema(description = "A standardised list of all birth country names in ISO 2 character code format associated to the customer record.")
  
    public String getEdqBirthCountryCode() {
    return edqBirthCountryCode;
  }

  public void setEdqBirthCountryCode(String edqBirthCountryCode) {
    this.edqBirthCountryCode = edqBirthCountryCode;
  }

  public CustomerIndividuals edqResidenceCountriesCode(String edqResidenceCountriesCode) {
    this.edqResidenceCountriesCode = edqResidenceCountriesCode;
    return this;
  }

  /**
   * A standardised list of all residency country names in ISO 2 character code format associated to the customer record.
   * @return edqResidenceCountriesCode
   **/
  @Schema(description = "A standardised list of all residency country names in ISO 2 character code format associated to the customer record.")
  
    public String getEdqResidenceCountriesCode() {
    return edqResidenceCountriesCode;
  }

  public void setEdqResidenceCountriesCode(String edqResidenceCountriesCode) {
    this.edqResidenceCountriesCode = edqResidenceCountriesCode;
  }

  public CustomerIndividuals nationalityOrCitizenship(String nationalityOrCitizenship) {
    this.nationalityOrCitizenship = nationalityOrCitizenship;
    return this;
  }

  /**
   * A standardised list of all country names associated to the customer record. All standardised names will ISO names.
   * @return nationalityOrCitizenship
   **/
  @Schema(description = "A standardised list of all country names associated to the customer record. All standardised names will ISO names.")
  
    public String getNationalityOrCitizenship() {
    return nationalityOrCitizenship;
  }

  public void setNationalityOrCitizenship(String nationalityOrCitizenship) {
    this.nationalityOrCitizenship = nationalityOrCitizenship;
  }

  public CustomerIndividuals nationalityCountries(String nationalityCountries) {
    this.nationalityCountries = nationalityCountries;
    return this;
  }

  /**
   * A standardised list of all country names in ISO 2 character code format associated to the customer record.
   * @return nationalityCountries
   **/
  @Schema(description = "A standardised list of all country names in ISO 2 character code format associated to the customer record.")
  
    public String getNationalityCountries() {
    return nationalityCountries;
  }

  public void setNationalityCountries(String nationalityCountries) {
    this.nationalityCountries = nationalityCountries;
  }

  public CustomerIndividuals countriesAll(String countriesAll) {
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

  public CustomerIndividuals edqCountriesAllCodes(String edqCountriesAllCodes) {
    this.edqCountriesAllCodes = edqCountriesAllCodes;
    return this;
  }

  /**
   * A standardised list of all country names in iso 2 character code format associated to the customer record.
   * @return edqCountriesAllCodes
   **/
  @Schema(description = "A standardised list of all country names in iso 2 character code format associated to the customer record.")
  
    public String getEdqCountriesAllCodes() {
    return edqCountriesAllCodes;
  }

  public void setEdqCountriesAllCodes(String edqCountriesAllCodes) {
    this.edqCountriesAllCodes = edqCountriesAllCodes;
  }

  public CustomerIndividuals edqEmployerAllCountries(String edqEmployerAllCountries) {
    this.edqEmployerAllCountries = edqEmployerAllCountries;
    return this;
  }

  /**
   * A standardised list of all employer country names associated to the customer record.. All standardised names will ISO names.
   * @return edqEmployerAllCountries
   **/
  @Schema(description = "A standardised list of all employer country names associated to the customer record.. All standardised names will ISO names.")
  
    public String getEdqEmployerAllCountries() {
    return edqEmployerAllCountries;
  }

  public void setEdqEmployerAllCountries(String edqEmployerAllCountries) {
    this.edqEmployerAllCountries = edqEmployerAllCountries;
  }

  public CustomerIndividuals edqEmployerAllCountriesCodes(String edqEmployerAllCountriesCodes) {
    this.edqEmployerAllCountriesCodes = edqEmployerAllCountriesCodes;
    return this;
  }

  /**
   * A standardised list of all employer country names in ISO 2 character code format associated to the customer record.
   * @return edqEmployerAllCountriesCodes
   **/
  @Schema(description = "A standardised list of all employer country names in ISO 2 character code format associated to the customer record.")
  
    public String getEdqEmployerAllCountriesCodes() {
    return edqEmployerAllCountriesCodes;
  }

  public void setEdqEmployerAllCountriesCodes(String edqEmployerAllCountriesCodes) {
    this.edqEmployerAllCountriesCodes = edqEmployerAllCountriesCodes;
  }

  public CustomerIndividuals edqLoB(String edqLoB) {
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

  public CustomerIndividuals edqPermission(String edqPermission) {
    this.edqPermission = edqPermission;
    return this;
  }

  /**
   * For development use only
   * @return edqPermission
   **/
  @Schema(description = "For development use only")
  
    public String getEdqPermission() {
    return edqPermission;
  }

  public void setEdqPermission(String edqPermission) {
    this.edqPermission = edqPermission;
  }

  public CustomerIndividuals taxID(String taxID) {
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

  public CustomerIndividuals dateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  /**
   * Standardised Date Of Birth
   * @return dateOfBirth
   **/
  @Schema(description = "Standardised Date Of Birth")
  
    public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public CustomerIndividuals dOBOriginal(String dOBOriginal) {
    this.dOBOriginal = dOBOriginal;
    return this;
  }

  /**
   * Original Date Of Birth
   * @return dOBOriginal
   **/
  @Schema(description = "Original Date Of Birth")
  
    public String getDOBOriginal() {
    return dOBOriginal;
  }

  public void setDOBOriginal(String dOBOriginal) {
    this.dOBOriginal = dOBOriginal;
  }

  public CustomerIndividuals edqDOBString(String edqDOBString) {
    this.edqDOBString = edqDOBString;
    return this;
  }

  /**
   * Standardised Date Of Birth in alternate format
   * @return edqDOBString
   **/
  @Schema(description = "Standardised Date Of Birth in alternate format")
  
    public String getEdqDOBString() {
    return edqDOBString;
  }

  public void setEdqDOBString(String edqDOBString) {
    this.edqDOBString = edqDOBString;
  }

  public CustomerIndividuals yearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
    return this;
  }

  /**
   * Year Of Birth
   * @return yearOfBirth
   **/
  @Schema(description = "Year Of Birth")
  
    public String getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public CustomerIndividuals city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Standardised City
   * @return city
   **/
  @Schema(description = "Standardised City")
  
    public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public CustomerIndividuals postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  /**
   * Standardised Postal Code
   * @return postalCode
   **/
  @Schema(description = "Standardised Postal Code")
  
    public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public CustomerIndividuals profileFullAddress(String profileFullAddress) {
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

  public CustomerIndividuals gender(String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Standardised Gender
   * @return gender
   **/
  @Schema(description = "Standardised Gender")
  
    public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public CustomerIndividuals genderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
    return this;
  }

  /**
   * Is Gender Derived?
   * @return genderDerivedFlag
   **/
  @Schema(description = "Is Gender Derived?")
  
    public String getGenderDerivedFlag() {
    return genderDerivedFlag;
  }

  public void setGenderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
  }

  public CustomerIndividuals profileHyperlink(String profileHyperlink) {
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

  public CustomerIndividuals searchHyperlink(String searchHyperlink) {
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

  public CustomerIndividuals edqListKey(String edqListKey) {
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

  public CustomerIndividuals passportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
    return this;
  }

  /**
   * Customer Passport Number
   * @return passportNumber
   **/
  @Schema(description = "Customer Passport Number")
  
    public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public CustomerIndividuals passportIssueCountry(String passportIssueCountry) {
    this.passportIssueCountry = passportIssueCountry;
    return this;
  }

  /**
   * Customer Passport Issue Country
   * @return passportIssueCountry
   **/
  @Schema(description = "Customer Passport Issue Country")
  
    public String getPassportIssueCountry() {
    return passportIssueCountry;
  }

  public void setPassportIssueCountry(String passportIssueCountry) {
    this.passportIssueCountry = passportIssueCountry;
  }

  public CustomerIndividuals socialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
    return this;
  }

  /**
   * Customer Social Security Number
   * @return socialSecurityNumber
   **/
  @Schema(description = "Customer Social Security Number")
  
    public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public CustomerIndividuals edqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
    return this;
  }

  /**
   * Customer Close Of Business Date
   * @return edqCloseOfBusinessDate
   **/
  @Schema(description = "Customer Close Of Business Date")
  
    public String getEdqCloseOfBusinessDate() {
    return edqCloseOfBusinessDate;
  }

  public void setEdqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
  }

  public CustomerIndividuals profileOccupation(String profileOccupation) {
    this.profileOccupation = profileOccupation;
    return this;
  }

  /**
   * Customer Occupation
   * @return profileOccupation
   **/
  @Schema(description = "Customer Occupation")
  
    public String getProfileOccupation() {
    return profileOccupation;
  }

  public void setProfileOccupation(String profileOccupation) {
    this.profileOccupation = profileOccupation;
  }

  public CustomerIndividuals personOrBusinessIndicator(String personOrBusinessIndicator) {
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

  public CustomerIndividuals profileNameType(String profileNameType) {
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

  public CustomerIndividuals edqPartyRoleTypeDescription(String edqPartyRoleTypeDescription) {
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

  public CustomerIndividuals edqPartyStatusCodeDescription(String edqPartyStatusCodeDescription) {
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

  public CustomerIndividuals edqDay1SpikeFlag(String edqDay1SpikeFlag) {
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

  public CustomerIndividuals originalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
    return this;
  }

  /**
   * Full name as originally provided
   * @return originalScriptName
   **/
  @Schema(description = "Full name as originally provided")
  
    public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
  }

  public CustomerIndividuals address(String address) {
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

  public CustomerIndividuals givenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
    return this;
  }

  /**
   * Original Customer Given Name
   * @return givenNamesOriginal
   **/
  @Schema(description = "Original Customer Given Name")
  
    public String getGivenNamesOriginal() {
    return givenNamesOriginal;
  }

  public void setGivenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
  }

  public CustomerIndividuals surname(String surname) {
    this.surname = surname;
    return this;
  }

  /**
   * Original Family name
   * @return surname
   **/
  @Schema(description = "Original Family name")
  
    public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public CustomerIndividuals edqScreeningMode(String edqScreeningMode) {
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

  public CustomerIndividuals edqCaseKey(String edqCaseKey) {
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

  public CustomerIndividuals addressType(String addressType) {
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

  public CustomerIndividuals sSCCodes(String sSCCodes) {
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

  public CustomerIndividuals cTRPFragment(String cTRPFragment) {
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

  public CustomerIndividuals requestUserName(String requestUserName) {
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

  public CustomerIndividuals requestDateTimeDDMMYYYY0000(String requestDateTimeDDMMYYYY0000) {
    this.requestDateTimeDDMMYYYY0000 = requestDateTimeDDMMYYYY0000;
    return this;
  }

  /**
   * N/A - For Day 1
   * @return requestDateTimeDDMMYYYY0000
   **/
  @Schema(description = "N/A - For Day 1")
  
    public String getRequestDateTimeDDMMYYYY0000() {
    return requestDateTimeDDMMYYYY0000;
  }

  public void setRequestDateTimeDDMMYYYY0000(String requestDateTimeDDMMYYYY0000) {
    this.requestDateTimeDDMMYYYY0000 = requestDateTimeDDMMYYYY0000;
  }

  public CustomerIndividuals autoDiscountDecision(String autoDiscountDecision) {
    this.autoDiscountDecision = autoDiscountDecision;
    return this;
  }

  /**
   * List ID&#58;List Record Type&#58;=3 digit result code from Auto Discounting process
   * @return autoDiscountDecision
   **/
  @Schema(description = "List ID&#58;List Record Type&#58;=3 digit result code from Auto Discounting process")
  
    public String getAutoDiscountDecision() {
    return autoDiscountDecision;
  }

  public void setAutoDiscountDecision(String autoDiscountDecision) {
    this.autoDiscountDecision = autoDiscountDecision;
  }

  public CustomerIndividuals recordType(String recordType) {
    this.recordType = recordType;
    return this;
  }

  /**
   * Indicates the type of record
   * @return recordType
   **/
  @Schema(description = "Indicates the type of record")
  
    public String getRecordType() {
    return recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  public CustomerIndividuals dummy(String dummy) {
    this.dummy = dummy;
    return this;
  }

  /**
   * For development use only
   * @return dummy
   **/
  @Schema(description = "For development use only")
  
    public String getDummy() {
    return dummy;
  }

  public void setDummy(String dummy) {
    this.dummy = dummy;
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
    return Objects.equals(this.caseId, customerIndividuals.caseId) &&
        Objects.equals(this.recordId, customerIndividuals.recordId) &&
        Objects.equals(this.inputStream, customerIndividuals.inputStream) &&
        Objects.equals(this.sourceSystemHistoryID, customerIndividuals.sourceSystemHistoryID) &&
        Objects.equals(this.closeOfBusinessDate, customerIndividuals.closeOfBusinessDate) &&
        Objects.equals(this.partitionNumber, customerIndividuals.partitionNumber) &&
        Objects.equals(this.sourceSystemIdentifier, customerIndividuals.sourceSystemIdentifier) &&
        Objects.equals(this.externalProfileID, customerIndividuals.externalProfileID) &&
        Objects.equals(this.concatenatedProfileID, customerIndividuals.concatenatedProfileID) &&
        Objects.equals(this.profileType, customerIndividuals.profileType) &&
        Objects.equals(this.partyRoleTypeCode, customerIndividuals.partyRoleTypeCode) &&
        Objects.equals(this.profileStatus, customerIndividuals.profileStatus) &&
        Objects.equals(this.profileSegment, customerIndividuals.profileSegment) &&
        Objects.equals(this.loBName, customerIndividuals.loBName) &&
        Objects.equals(this.loBRegion, customerIndividuals.loBRegion) &&
        Objects.equals(this.loBCountry, customerIndividuals.loBCountry) &&
        Objects.equals(this.hSBCLegalEntityCode, customerIndividuals.hSBCLegalEntityCode) &&
        Objects.equals(this.nameCategoryCode, customerIndividuals.nameCategoryCode) &&
        Objects.equals(this.nameTypeCode, customerIndividuals.nameTypeCode) &&
        Objects.equals(this.nameLanguageTypeCode, customerIndividuals.nameLanguageTypeCode) &&
        Objects.equals(this.givenName, customerIndividuals.givenName) &&
        Objects.equals(this.middleName, customerIndividuals.middleName) &&
        Objects.equals(this.familyNameOriginal, customerIndividuals.familyNameOriginal) &&
        Objects.equals(this.initialsOriginal, customerIndividuals.initialsOriginal) &&
        Objects.equals(this.profileFullName, customerIndividuals.profileFullName) &&
        Objects.equals(this.pEPIndicator, customerIndividuals.pEPIndicator) &&
        Objects.equals(this.specialCategoryCustomerIndicator, customerIndividuals.specialCategoryCustomerIndicator) &&
        Objects.equals(this.genderCode, customerIndividuals.genderCode) &&
        Objects.equals(this.birthDate, customerIndividuals.birthDate) &&
        Objects.equals(this.placeOfBirth, customerIndividuals.placeOfBirth) &&
        Objects.equals(this.townOfBirth, customerIndividuals.townOfBirth) &&
        Objects.equals(this.stateProvinceOrCountyOfBirth, customerIndividuals.stateProvinceOrCountyOfBirth) &&
        Objects.equals(this.countryOfBirthOriginal, customerIndividuals.countryOfBirthOriginal) &&
        Objects.equals(this.sourceAddressType, customerIndividuals.sourceAddressType) &&
        Objects.equals(this.addressFormatCode, customerIndividuals.addressFormatCode) &&
        Objects.equals(this.addressLanguageTypeCode, customerIndividuals.addressLanguageTypeCode) &&
        Objects.equals(this.sourceAddressLine1, customerIndividuals.sourceAddressLine1) &&
        Objects.equals(this.sourceAddressLine2, customerIndividuals.sourceAddressLine2) &&
        Objects.equals(this.sourceAddressLine3, customerIndividuals.sourceAddressLine3) &&
        Objects.equals(this.sourceAddressLine4, customerIndividuals.sourceAddressLine4) &&
        Objects.equals(this.sourceAddressLine5, customerIndividuals.sourceAddressLine5) &&
        Objects.equals(this.sourceAddressLine6, customerIndividuals.sourceAddressLine6) &&
        Objects.equals(this.sourceAddressLine7, customerIndividuals.sourceAddressLine7) &&
        Objects.equals(this.sourceAddressLine8, customerIndividuals.sourceAddressLine8) &&
        Objects.equals(this.sourceAddressLine9, customerIndividuals.sourceAddressLine9) &&
        Objects.equals(this.sourceAddressLine10, customerIndividuals.sourceAddressLine10) &&
        Objects.equals(this.sourcePostalCode, customerIndividuals.sourcePostalCode) &&
        Objects.equals(this.sourceCountry, customerIndividuals.sourceCountry) &&
        Objects.equals(this.identificationDocument1, customerIndividuals.identificationDocument1) &&
        Objects.equals(this.identificationDocument2, customerIndividuals.identificationDocument2) &&
        Objects.equals(this.identificationDocument3, customerIndividuals.identificationDocument3) &&
        Objects.equals(this.identificationDocument4, customerIndividuals.identificationDocument4) &&
        Objects.equals(this.identificationDocument5, customerIndividuals.identificationDocument5) &&
        Objects.equals(this.identificationDocument6, customerIndividuals.identificationDocument6) &&
        Objects.equals(this.identificationDocument7, customerIndividuals.identificationDocument7) &&
        Objects.equals(this.identificationDocument8, customerIndividuals.identificationDocument8) &&
        Objects.equals(this.identificationDocument9, customerIndividuals.identificationDocument9) &&
        Objects.equals(this.identificationDocument10, customerIndividuals.identificationDocument10) &&
        Objects.equals(this.residenceCountries, customerIndividuals.residenceCountries) &&
        Objects.equals(this.nationalityCitizenshipCountries, customerIndividuals.nationalityCitizenshipCountries) &&
        Objects.equals(this.employer1Details, customerIndividuals.employer1Details) &&
        Objects.equals(this.employer1Address1, customerIndividuals.employer1Address1) &&
        Objects.equals(this.employer1Address2, customerIndividuals.employer1Address2) &&
        Objects.equals(this.employer1Address3, customerIndividuals.employer1Address3) &&
        Objects.equals(this.employer2Details, customerIndividuals.employer2Details) &&
        Objects.equals(this.employer2Address1, customerIndividuals.employer2Address1) &&
        Objects.equals(this.employer2Address2, customerIndividuals.employer2Address2) &&
        Objects.equals(this.employer2Address3, customerIndividuals.employer2Address3) &&
        Objects.equals(this.employer3Details, customerIndividuals.employer3Details) &&
        Objects.equals(this.employer3Address1, customerIndividuals.employer3Address1) &&
        Objects.equals(this.employer3Address2, customerIndividuals.employer3Address2) &&
        Objects.equals(this.employer3Address3, customerIndividuals.employer3Address3) &&
        Objects.equals(this.edqCustID, customerIndividuals.edqCustID) &&
        Objects.equals(this.edqCustSubID, customerIndividuals.edqCustSubID) &&
        Objects.equals(this.titleDerived, customerIndividuals.titleDerived) &&
        Objects.equals(this.givenNamesDerived, customerIndividuals.givenNamesDerived) &&
        Objects.equals(this.fullNameDerived, customerIndividuals.fullNameDerived) &&
        Objects.equals(this.familyNameDerived, customerIndividuals.familyNameDerived) &&
        Objects.equals(this.initials, customerIndividuals.initials) &&
        Objects.equals(this.edqLoBCountryCode, customerIndividuals.edqLoBCountryCode) &&
        Objects.equals(this.countryOfBirth, customerIndividuals.countryOfBirth) &&
        Objects.equals(this.addressCountry, customerIndividuals.addressCountry) &&
        Objects.equals(this.edqAddressCountryCode, customerIndividuals.edqAddressCountryCode) &&
        Objects.equals(this.countryOfResidence, customerIndividuals.countryOfResidence) &&
        Objects.equals(this.edqBirthCountryCode, customerIndividuals.edqBirthCountryCode) &&
        Objects.equals(this.edqResidenceCountriesCode, customerIndividuals.edqResidenceCountriesCode) &&
        Objects.equals(this.nationalityOrCitizenship, customerIndividuals.nationalityOrCitizenship) &&
        Objects.equals(this.nationalityCountries, customerIndividuals.nationalityCountries) &&
        Objects.equals(this.countriesAll, customerIndividuals.countriesAll) &&
        Objects.equals(this.edqCountriesAllCodes, customerIndividuals.edqCountriesAllCodes) &&
        Objects.equals(this.edqEmployerAllCountries, customerIndividuals.edqEmployerAllCountries) &&
        Objects.equals(this.edqEmployerAllCountriesCodes, customerIndividuals.edqEmployerAllCountriesCodes) &&
        Objects.equals(this.edqLoB, customerIndividuals.edqLoB) &&
        Objects.equals(this.edqPermission, customerIndividuals.edqPermission) &&
        Objects.equals(this.taxID, customerIndividuals.taxID) &&
        Objects.equals(this.dateOfBirth, customerIndividuals.dateOfBirth) &&
        Objects.equals(this.dOBOriginal, customerIndividuals.dOBOriginal) &&
        Objects.equals(this.edqDOBString, customerIndividuals.edqDOBString) &&
        Objects.equals(this.yearOfBirth, customerIndividuals.yearOfBirth) &&
        Objects.equals(this.city, customerIndividuals.city) &&
        Objects.equals(this.postalCode, customerIndividuals.postalCode) &&
        Objects.equals(this.profileFullAddress, customerIndividuals.profileFullAddress) &&
        Objects.equals(this.gender, customerIndividuals.gender) &&
        Objects.equals(this.genderDerivedFlag, customerIndividuals.genderDerivedFlag) &&
        Objects.equals(this.profileHyperlink, customerIndividuals.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, customerIndividuals.searchHyperlink) &&
        Objects.equals(this.edqListKey, customerIndividuals.edqListKey) &&
        Objects.equals(this.passportNumber, customerIndividuals.passportNumber) &&
        Objects.equals(this.passportIssueCountry, customerIndividuals.passportIssueCountry) &&
        Objects.equals(this.socialSecurityNumber, customerIndividuals.socialSecurityNumber) &&
        Objects.equals(this.edqCloseOfBusinessDate, customerIndividuals.edqCloseOfBusinessDate) &&
        Objects.equals(this.profileOccupation, customerIndividuals.profileOccupation) &&
        Objects.equals(this.personOrBusinessIndicator, customerIndividuals.personOrBusinessIndicator) &&
        Objects.equals(this.profileNameType, customerIndividuals.profileNameType) &&
        Objects.equals(this.edqPartyRoleTypeDescription, customerIndividuals.edqPartyRoleTypeDescription) &&
        Objects.equals(this.edqPartyStatusCodeDescription, customerIndividuals.edqPartyStatusCodeDescription) &&
        Objects.equals(this.edqDay1SpikeFlag, customerIndividuals.edqDay1SpikeFlag) &&
        Objects.equals(this.originalScriptName, customerIndividuals.originalScriptName) &&
        Objects.equals(this.address, customerIndividuals.address) &&
        Objects.equals(this.givenNamesOriginal, customerIndividuals.givenNamesOriginal) &&
        Objects.equals(this.surname, customerIndividuals.surname) &&
        Objects.equals(this.edqScreeningMode, customerIndividuals.edqScreeningMode) &&
        Objects.equals(this.edqCaseKey, customerIndividuals.edqCaseKey) &&
        Objects.equals(this.addressType, customerIndividuals.addressType) &&
        Objects.equals(this.sSCCodes, customerIndividuals.sSCCodes) &&
        Objects.equals(this.cTRPFragment, customerIndividuals.cTRPFragment) &&
        Objects.equals(this.requestUserName, customerIndividuals.requestUserName) &&
        Objects.equals(this.requestDateTimeDDMMYYYY0000, customerIndividuals.requestDateTimeDDMMYYYY0000) &&
        Objects.equals(this.autoDiscountDecision, customerIndividuals.autoDiscountDecision) &&
        Objects.equals(this.recordType, customerIndividuals.recordType) &&
        Objects.equals(this.dummy, customerIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordId, inputStream, sourceSystemHistoryID, closeOfBusinessDate, partitionNumber, sourceSystemIdentifier, externalProfileID, concatenatedProfileID, profileType, partyRoleTypeCode, profileStatus, profileSegment, loBName, loBRegion, loBCountry, hSBCLegalEntityCode, nameCategoryCode, nameTypeCode, nameLanguageTypeCode, givenName, middleName, familyNameOriginal, initialsOriginal, profileFullName, pEPIndicator, specialCategoryCustomerIndicator, genderCode, birthDate, placeOfBirth, townOfBirth, stateProvinceOrCountyOfBirth, countryOfBirthOriginal, sourceAddressType, addressFormatCode, addressLanguageTypeCode, sourceAddressLine1, sourceAddressLine2, sourceAddressLine3, sourceAddressLine4, sourceAddressLine5, sourceAddressLine6, sourceAddressLine7, sourceAddressLine8, sourceAddressLine9, sourceAddressLine10, sourcePostalCode, sourceCountry, identificationDocument1, identificationDocument2, identificationDocument3, identificationDocument4, identificationDocument5, identificationDocument6, identificationDocument7, identificationDocument8, identificationDocument9, identificationDocument10, residenceCountries, nationalityCitizenshipCountries, employer1Details, employer1Address1, employer1Address2, employer1Address3, employer2Details, employer2Address1, employer2Address2, employer2Address3, employer3Details, employer3Address1, employer3Address2, employer3Address3, edqCustID, edqCustSubID, titleDerived, givenNamesDerived, fullNameDerived, familyNameDerived, initials, edqLoBCountryCode, countryOfBirth, addressCountry, edqAddressCountryCode, countryOfResidence, edqBirthCountryCode, edqResidenceCountriesCode, nationalityOrCitizenship, nationalityCountries, countriesAll, edqCountriesAllCodes, edqEmployerAllCountries, edqEmployerAllCountriesCodes, edqLoB, edqPermission, taxID, dateOfBirth, dOBOriginal, edqDOBString, yearOfBirth, city, postalCode, profileFullAddress, gender, genderDerivedFlag, profileHyperlink, searchHyperlink, edqListKey, passportNumber, passportIssueCountry, socialSecurityNumber, edqCloseOfBusinessDate, profileOccupation, personOrBusinessIndicator, profileNameType, edqPartyRoleTypeDescription, edqPartyStatusCodeDescription, edqDay1SpikeFlag, originalScriptName, address, givenNamesOriginal, surname, edqScreeningMode, edqCaseKey, addressType, sSCCodes, cTRPFragment, requestUserName, requestDateTimeDDMMYYYY0000, autoDiscountDecision, recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerIndividuals {\n");
    
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
    sb.append("    loBName: ").append(toIndentedString(loBName)).append("\n");
    sb.append("    loBRegion: ").append(toIndentedString(loBRegion)).append("\n");
    sb.append("    loBCountry: ").append(toIndentedString(loBCountry)).append("\n");
    sb.append("    hSBCLegalEntityCode: ").append(toIndentedString(hSBCLegalEntityCode)).append("\n");
    sb.append("    nameCategoryCode: ").append(toIndentedString(nameCategoryCode)).append("\n");
    sb.append("    nameTypeCode: ").append(toIndentedString(nameTypeCode)).append("\n");
    sb.append("    nameLanguageTypeCode: ").append(toIndentedString(nameLanguageTypeCode)).append("\n");
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    familyNameOriginal: ").append(toIndentedString(familyNameOriginal)).append("\n");
    sb.append("    initialsOriginal: ").append(toIndentedString(initialsOriginal)).append("\n");
    sb.append("    profileFullName: ").append(toIndentedString(profileFullName)).append("\n");
    sb.append("    pEPIndicator: ").append(toIndentedString(pEPIndicator)).append("\n");
    sb.append("    specialCategoryCustomerIndicator: ").append(toIndentedString(specialCategoryCustomerIndicator)).append("\n");
    sb.append("    genderCode: ").append(toIndentedString(genderCode)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    placeOfBirth: ").append(toIndentedString(placeOfBirth)).append("\n");
    sb.append("    townOfBirth: ").append(toIndentedString(townOfBirth)).append("\n");
    sb.append("    stateProvinceOrCountyOfBirth: ").append(toIndentedString(stateProvinceOrCountyOfBirth)).append("\n");
    sb.append("    countryOfBirthOriginal: ").append(toIndentedString(countryOfBirthOriginal)).append("\n");
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
    sb.append("    sourceCountry: ").append(toIndentedString(sourceCountry)).append("\n");
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
    sb.append("    residenceCountries: ").append(toIndentedString(residenceCountries)).append("\n");
    sb.append("    nationalityCitizenshipCountries: ").append(toIndentedString(nationalityCitizenshipCountries)).append("\n");
    sb.append("    employer1Details: ").append(toIndentedString(employer1Details)).append("\n");
    sb.append("    employer1Address1: ").append(toIndentedString(employer1Address1)).append("\n");
    sb.append("    employer1Address2: ").append(toIndentedString(employer1Address2)).append("\n");
    sb.append("    employer1Address3: ").append(toIndentedString(employer1Address3)).append("\n");
    sb.append("    employer2Details: ").append(toIndentedString(employer2Details)).append("\n");
    sb.append("    employer2Address1: ").append(toIndentedString(employer2Address1)).append("\n");
    sb.append("    employer2Address2: ").append(toIndentedString(employer2Address2)).append("\n");
    sb.append("    employer2Address3: ").append(toIndentedString(employer2Address3)).append("\n");
    sb.append("    employer3Details: ").append(toIndentedString(employer3Details)).append("\n");
    sb.append("    employer3Address1: ").append(toIndentedString(employer3Address1)).append("\n");
    sb.append("    employer3Address2: ").append(toIndentedString(employer3Address2)).append("\n");
    sb.append("    employer3Address3: ").append(toIndentedString(employer3Address3)).append("\n");
    sb.append("    edqCustID: ").append(toIndentedString(edqCustID)).append("\n");
    sb.append("    edqCustSubID: ").append(toIndentedString(edqCustSubID)).append("\n");
    sb.append("    titleDerived: ").append(toIndentedString(titleDerived)).append("\n");
    sb.append("    givenNamesDerived: ").append(toIndentedString(givenNamesDerived)).append("\n");
    sb.append("    fullNameDerived: ").append(toIndentedString(fullNameDerived)).append("\n");
    sb.append("    familyNameDerived: ").append(toIndentedString(familyNameDerived)).append("\n");
    sb.append("    initials: ").append(toIndentedString(initials)).append("\n");
    sb.append("    edqLoBCountryCode: ").append(toIndentedString(edqLoBCountryCode)).append("\n");
    sb.append("    countryOfBirth: ").append(toIndentedString(countryOfBirth)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb.append("    edqAddressCountryCode: ").append(toIndentedString(edqAddressCountryCode)).append("\n");
    sb.append("    countryOfResidence: ").append(toIndentedString(countryOfResidence)).append("\n");
    sb.append("    edqBirthCountryCode: ").append(toIndentedString(edqBirthCountryCode)).append("\n");
    sb.append("    edqResidenceCountriesCode: ").append(toIndentedString(edqResidenceCountriesCode)).append("\n");
    sb.append("    nationalityOrCitizenship: ").append(toIndentedString(nationalityOrCitizenship)).append("\n");
    sb.append("    nationalityCountries: ").append(toIndentedString(nationalityCountries)).append("\n");
    sb.append("    countriesAll: ").append(toIndentedString(countriesAll)).append("\n");
    sb.append("    edqCountriesAllCodes: ").append(toIndentedString(edqCountriesAllCodes)).append("\n");
    sb.append("    edqEmployerAllCountries: ").append(toIndentedString(edqEmployerAllCountries)).append("\n");
    sb.append("    edqEmployerAllCountriesCodes: ").append(toIndentedString(edqEmployerAllCountriesCodes)).append("\n");
    sb.append("    edqLoB: ").append(toIndentedString(edqLoB)).append("\n");
    sb.append("    edqPermission: ").append(toIndentedString(edqPermission)).append("\n");
    sb.append("    taxID: ").append(toIndentedString(taxID)).append("\n");
    sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
    sb.append("    dOBOriginal: ").append(toIndentedString(dOBOriginal)).append("\n");
    sb.append("    edqDOBString: ").append(toIndentedString(edqDOBString)).append("\n");
    sb.append("    yearOfBirth: ").append(toIndentedString(yearOfBirth)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    profileFullAddress: ").append(toIndentedString(profileFullAddress)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    genderDerivedFlag: ").append(toIndentedString(genderDerivedFlag)).append("\n");
    sb.append("    profileHyperlink: ").append(toIndentedString(profileHyperlink)).append("\n");
    sb.append("    searchHyperlink: ").append(toIndentedString(searchHyperlink)).append("\n");
    sb.append("    edqListKey: ").append(toIndentedString(edqListKey)).append("\n");
    sb.append("    passportNumber: ").append(toIndentedString(passportNumber)).append("\n");
    sb.append("    passportIssueCountry: ").append(toIndentedString(passportIssueCountry)).append("\n");
    sb.append("    socialSecurityNumber: ").append(toIndentedString(socialSecurityNumber)).append("\n");
    sb.append("    edqCloseOfBusinessDate: ").append(toIndentedString(edqCloseOfBusinessDate)).append("\n");
    sb.append("    profileOccupation: ").append(toIndentedString(profileOccupation)).append("\n");
    sb.append("    personOrBusinessIndicator: ").append(toIndentedString(personOrBusinessIndicator)).append("\n");
    sb.append("    profileNameType: ").append(toIndentedString(profileNameType)).append("\n");
    sb.append("    edqPartyRoleTypeDescription: ").append(toIndentedString(edqPartyRoleTypeDescription)).append("\n");
    sb.append("    edqPartyStatusCodeDescription: ").append(toIndentedString(edqPartyStatusCodeDescription)).append("\n");
    sb.append("    edqDay1SpikeFlag: ").append(toIndentedString(edqDay1SpikeFlag)).append("\n");
    sb.append("    originalScriptName: ").append(toIndentedString(originalScriptName)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    givenNamesOriginal: ").append(toIndentedString(givenNamesOriginal)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    edqScreeningMode: ").append(toIndentedString(edqScreeningMode)).append("\n");
    sb.append("    edqCaseKey: ").append(toIndentedString(edqCaseKey)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    sSCCodes: ").append(toIndentedString(sSCCodes)).append("\n");
    sb.append("    cTRPFragment: ").append(toIndentedString(cTRPFragment)).append("\n");
    sb.append("    requestUserName: ").append(toIndentedString(requestUserName)).append("\n");
    sb.append("    requestDateTimeDDMMYYYY0000: ").append(toIndentedString(requestDateTimeDDMMYYYY0000)).append("\n");
    sb.append("    autoDiscountDecision: ").append(toIndentedString(autoDiscountDecision)).append("\n");
    sb.append("    recordType: ").append(toIndentedString(recordType)).append("\n");
    sb.append("    dummy: ").append(toIndentedString(dummy)).append("\n");
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
