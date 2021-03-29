package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CustomerIndividuals {

  private int caseId;
  private BigDecimal recordId = null;
  private String inputStream = null;
  private BigDecimal sourceSystemHistoryId = null;
  private String closeOfBusinessDate = null;
  private BigDecimal partitionNumber = null;
  private BigDecimal sourceSystemIdentifier = null;
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
  private String givenName = null;
  private String middleName = null;
  private String familyNameOriginal = null;
  private String initialsOriginal = null;
  private String profileFullName = null;
  private String pepIndicator = null;
  private String specialCategoryCustomerIndicator = null;
  private String genderCode = null;
  private String birthDate = null;
  private String placeOfBirth = null;
  private String townOfBirth = null;
  private String stateProvinceOrCountyOfBirth = null;
  private String countryOfBirthOriginal = null;
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
  private String sourceCountry = null;
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
  private String residenceCountries = null;
  private String nationalityCitizenshipCountries = null;
  private String employer1Details = null;
  private String employer1Address1 = null;
  private String employer1Address2 = null;
  private String employer1Address3 = null;
  private String employer2Details = null;
  private String employer2Address1 = null;
  private String employer2Address2 = null;
  private String employer2Address3 = null;
  private String employer3Details = null;
  private String employer3Address1 = null;
  private String employer3Address2 = null;
  private String employer3Address3 = null;
  private String edqCustId = null;
  private String edqCustSubId = null;
  private String titleDerived = null;
  private String givenNamesDerived = null;
  private String fullNameDerived = null;
  private String familyNameDerived = null;
  private String initials = null;
  private String edqLobCountryCode = null;
  private String countryOfBirth = null;
  private String addressCountry = null;
  private String edqAddressCountryCode = null;
  private String countryOfResidence = null;
  private String edqBirthCountryCode = null;
  private String edqResidenceCountriesCode = null;
  private String nationalityOrCitizenship = null;
  private String nationalityCountries = null;
  private String countriesAll = null;
  private String edqCountriesAllCodes = null;
  private String edqEmployerAllCountries = null;
  private String edqEmployerAllCountriesCodes = null;
  private String edqLob = null;
  private String edqPermission = null;
  private String taxId = null;
  private String dateOfBirth = null;
  private String dobOriginal = null;
  private String edqDobString = null;
  private String yearOfBirth = null;
  private String city = null;
  private String postalCode = null;
  private String profileFullAddress = null;
  private String gender = null;
  private String genderDerivedFlag = null;
  private String profileHyperlink = null;
  private String searchHyperlink = null;
  private String edqListKey = null;
  private String passportNumber = null;
  private String passportIssueCountry = null;
  private String socialSecurityNumber = null;
  private String edqCloseOfBusinessDate = null;
  private String profileOccupation = null;
  private String personOrBusinessIndicator = null;
  private String profileNameType = null;
  private String edqPartyRoleTypeDescription = null;
  private String edqPartyStatusCodeDescription = null;
  private String edqDay1SpikeFlag = null;
  private String originalScriptName = null;
  private String address = null;
  private String givenNamesOriginal = null;
  private String surname = null;
  private String edqScreeningMode = null;
  private String edqCaseKey = null;
  private String addressType = null;
  private String sscCodes = null;
  private String ctrpFragment = null;
  private String requestUserName = null;
  private String requestDateTimeDDMMYYYY0000 = null;
  private String autoDiscountDecision = null;
  private String recordType = null;
  private String dummy = null;

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public int getCaseId() {
    return caseId;
  }

  public void setCaseId(int caseId) {
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
  public BigDecimal getSourceSystemHistoryId() {
    return sourceSystemHistoryId;
  }

  public void setSourceSystemHistoryId(BigDecimal sourceSystemHistoryId) {
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
  public BigDecimal getPartitionNumber() {
    return partitionNumber;
  }

  public void setPartitionNumber(BigDecimal partitionNumber) {
    this.partitionNumber = partitionNumber;
  }

  /**
   * The Identifier used to identify where the feed originated from
   **/

  @Schema(description = "The Identifier used to identify where the feed originated from")
  @JsonProperty("sourceSystemIdentifier")
  public BigDecimal getSourceSystemIdentifier() {
    return sourceSystemIdentifier;
  }

  public void setSourceSystemIdentifier(BigDecimal sourceSystemIdentifier) {
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
   * Customer Given Name
   **/

  @Schema(description = "Customer Given Name")
  @JsonProperty("givenName")
  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  /**
   * Customer Middle Name
   **/

  @Schema(description = "Customer Middle Name")
  @JsonProperty("middleName")
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * Customer Family Name (Original)
   **/

  @Schema(description = "Customer Family Name (Original)")
  @JsonProperty("familyNameOriginal")
  public String getFamilyNameOriginal() {
    return familyNameOriginal;
  }

  public void setFamilyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
  }

  /**
   * Initials (Original)
   **/

  @Schema(description = "Initials (Original)")
  @JsonProperty("initialsOriginal")
  public String getInitialsOriginal() {
    return initialsOriginal;
  }

  public void setInitialsOriginal(String initialsOriginal) {
    this.initialsOriginal = initialsOriginal;
  }

  /**
   * Original Unstructured Name
   **/

  @Schema(description = "Original Unstructured Name")
  @JsonProperty("profileFullName")
  public String getProfileFullName() {
    return profileFullName;
  }

  public void setProfileFullName(String profileFullName) {
    this.profileFullName = profileFullName;
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
  @JsonProperty("specialCategoryCustomerIndicator")
  public String getSpecialCategoryCustomerIndicator() {
    return specialCategoryCustomerIndicator;
  }

  public void setSpecialCategoryCustomerIndicator(String specialCategoryCustomerIndicator) {
    this.specialCategoryCustomerIndicator = specialCategoryCustomerIndicator;
  }

  /**
   * Original Customer Gender Code
   **/

  @Schema(description = "Original Customer Gender Code")
  @JsonProperty("genderCode")
  public String getGenderCode() {
    return genderCode;
  }

  public void setGenderCode(String genderCode) {
    this.genderCode = genderCode;
  }

  /**
   * Original Customer Birth Date
   **/

  @Schema(description = "Original Customer Birth Date")
  @JsonProperty("birthDate")
  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  /**
   * Original Country Of Birth Name
   **/

  @Schema(description = "Original Country Of Birth Name")
  @JsonProperty("placeOfBirth")
  public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  /**
   * Original Town Of Birth
   **/

  @Schema(description = "Original Town Of Birth")
  @JsonProperty("townOfBirth")
  public String getTownOfBirth() {
    return townOfBirth;
  }

  public void setTownOfBirth(String townOfBirth) {
    this.townOfBirth = townOfBirth;
  }

  /**
   * Original State, Province or County of Birth
   **/

  @Schema(description = "Original State, Province or County of Birth")
  @JsonProperty("stateProvinceOrCountyOfBirth")
  public String getStateProvinceOrCountyOfBirth() {
    return stateProvinceOrCountyOfBirth;
  }

  public void setStateProvinceOrCountyOfBirth(String stateProvinceOrCountyOfBirth) {
    this.stateProvinceOrCountyOfBirth = stateProvinceOrCountyOfBirth;
  }

  /**
   * Original Country Of Birth Code
   **/

  @Schema(description = "Original Country Of Birth Code")
  @JsonProperty("countryOfBirthOriginal")
  public String getCountryOfBirthOriginal() {
    return countryOfBirthOriginal;
  }

  public void setCountryOfBirthOriginal(String countryOfBirthOriginal) {
    this.countryOfBirthOriginal = countryOfBirthOriginal;
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
   * Address Postal Code as provided in the customer record
   **/

  @Schema(description = "Address Postal Code as provided in the customer record")
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
  @JsonProperty("sourceCountry")
  public String getSourceCountry() {
    return sourceCountry;
  }

  public void setSourceCountry(String sourceCountry) {
    this.sourceCountry = sourceCountry;
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
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument2")
  public String getIdentificationDocument2() {
    return identificationDocument2;
  }

  public void setIdentificationDocument2(String identificationDocument2) {
    this.identificationDocument2 = identificationDocument2;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument3")
  public String getIdentificationDocument3() {
    return identificationDocument3;
  }

  public void setIdentificationDocument3(String identificationDocument3) {
    this.identificationDocument3 = identificationDocument3;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument4")
  public String getIdentificationDocument4() {
    return identificationDocument4;
  }

  public void setIdentificationDocument4(String identificationDocument4) {
    this.identificationDocument4 = identificationDocument4;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument5")
  public String getIdentificationDocument5() {
    return identificationDocument5;
  }

  public void setIdentificationDocument5(String identificationDocument5) {
    this.identificationDocument5 = identificationDocument5;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument6")
  public String getIdentificationDocument6() {
    return identificationDocument6;
  }

  public void setIdentificationDocument6(String identificationDocument6) {
    this.identificationDocument6 = identificationDocument6;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument7")
  public String getIdentificationDocument7() {
    return identificationDocument7;
  }

  public void setIdentificationDocument7(String identificationDocument7) {
    this.identificationDocument7 = identificationDocument7;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument8")
  public String getIdentificationDocument8() {
    return identificationDocument8;
  }

  public void setIdentificationDocument8(String identificationDocument8) {
    this.identificationDocument8 = identificationDocument8;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument9")
  public String getIdentificationDocument9() {
    return identificationDocument9;
  }

  public void setIdentificationDocument9(String identificationDocument9) {
    this.identificationDocument9 = identificationDocument9;
  }

  /**
   * Identification document.
   **/

  @Schema(description = "Identification document.")
  @JsonProperty("identificationDocument10")
  public String getIdentificationDocument10() {
    return identificationDocument10;
  }

  public void setIdentificationDocument10(String identificationDocument10) {
    this.identificationDocument10 = identificationDocument10;
  }

  /**
   * List of countries where the customer has been resident
   **/

  @Schema(description = "List of countries where the customer has been resident")
  @JsonProperty("residenceCountries")
  public String getResidenceCountries() {
    return residenceCountries;
  }

  public void setResidenceCountries(String residenceCountries) {
    this.residenceCountries = residenceCountries;
  }

  /**
   * Original List of countries where the customer has nationality
   **/

  @Schema(description = "Original List of countries where the customer has nationality")
  @JsonProperty("nationalityCitizenshipCountries")
  public String getNationalityCitizenshipCountries() {
    return nationalityCitizenshipCountries;
  }

  public void setNationalityCitizenshipCountries(String nationalityCitizenshipCountries) {
    this.nationalityCitizenshipCountries = nationalityCitizenshipCountries;
  }

  /**
   * Customer Employer 1 Name
   **/

  @Schema(description = "Customer Employer 1 Name")
  @JsonProperty("employer1Details")
  public String getEmployer1Details() {
    return employer1Details;
  }

  public void setEmployer1Details(String employer1Details) {
    this.employer1Details = employer1Details;
  }

  /**
   * Customer Employer 1 Address 1
   **/

  @Schema(description = "Customer Employer 1 Address 1")
  @JsonProperty("employer1Address1")
  public String getEmployer1Address1() {
    return employer1Address1;
  }

  public void setEmployer1Address1(String employer1Address1) {
    this.employer1Address1 = employer1Address1;
  }

  /**
   * Customer Employer 1 Address 2
   **/

  @Schema(description = "Customer Employer 1 Address 2")
  @JsonProperty("employer1Address2")
  public String getEmployer1Address2() {
    return employer1Address2;
  }

  public void setEmployer1Address2(String employer1Address2) {
    this.employer1Address2 = employer1Address2;
  }

  /**
   * Customer Employer 1 Address 3
   **/

  @Schema(description = "Customer Employer 1 Address 3")
  @JsonProperty("employer1Address3")
  public String getEmployer1Address3() {
    return employer1Address3;
  }

  public void setEmployer1Address3(String employer1Address3) {
    this.employer1Address3 = employer1Address3;
  }

  /**
   * Customer Employer 2 Name
   **/

  @Schema(description = "Customer Employer 2 Name")
  @JsonProperty("employer2Details")
  public String getEmployer2Details() {
    return employer2Details;
  }

  public void setEmployer2Details(String employer2Details) {
    this.employer2Details = employer2Details;
  }

  /**
   * Customer Employer 2 Address 1
   **/

  @Schema(description = "Customer Employer 2 Address 1")
  @JsonProperty("employer2Address1")
  public String getEmployer2Address1() {
    return employer2Address1;
  }

  public void setEmployer2Address1(String employer2Address1) {
    this.employer2Address1 = employer2Address1;
  }

  /**
   * Customer Employer 2 Address 2
   **/

  @Schema(description = "Customer Employer 2 Address 2")
  @JsonProperty("employer2Address2")
  public String getEmployer2Address2() {
    return employer2Address2;
  }

  public void setEmployer2Address2(String employer2Address2) {
    this.employer2Address2 = employer2Address2;
  }

  /**
   * Customer Employer 2 Address 3
   **/

  @Schema(description = "Customer Employer 2 Address 3")
  @JsonProperty("employer2Address3")
  public String getEmployer2Address3() {
    return employer2Address3;
  }

  public void setEmployer2Address3(String employer2Address3) {
    this.employer2Address3 = employer2Address3;
  }

  /**
   * Customer Employer 3 Name
   **/

  @Schema(description = "Customer Employer 3 Name")
  @JsonProperty("employer3Details")
  public String getEmployer3Details() {
    return employer3Details;
  }

  public void setEmployer3Details(String employer3Details) {
    this.employer3Details = employer3Details;
  }

  /**
   * Customer Employer 3 Address 1
   **/

  @Schema(description = "Customer Employer 3 Address 1")
  @JsonProperty("employer3Address1")
  public String getEmployer3Address1() {
    return employer3Address1;
  }

  public void setEmployer3Address1(String employer3Address1) {
    this.employer3Address1 = employer3Address1;
  }

  /**
   * Customer Employer 3 Address 2
   **/

  @Schema(description = "Customer Employer 3 Address 2")
  @JsonProperty("employer3Address2")
  public String getEmployer3Address2() {
    return employer3Address2;
  }

  public void setEmployer3Address2(String employer3Address2) {
    this.employer3Address2 = employer3Address2;
  }

  /**
   * Customer Employer 3 Address 3
   **/

  @Schema(description = "Customer Employer 3 Address 3")
  @JsonProperty("employer3Address3")
  public String getEmployer3Address3() {
    return employer3Address3;
  }

  public void setEmployer3Address3(String employer3Address3) {
    this.employer3Address3 = employer3Address3;
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
   * Title as derived by OWS
   **/

  @Schema(description = "Title as derived by OWS")
  @JsonProperty("titleDerived")
  public String getTitleDerived() {
    return titleDerived;
  }

  public void setTitleDerived(String titleDerived) {
    this.titleDerived = titleDerived;
  }

  /**
   * Given Name as derived by OWS
   **/

  @Schema(description = "Given Name as derived by OWS")
  @JsonProperty("givenNamesDerived")
  public String getGivenNamesDerived() {
    return givenNamesDerived;
  }

  public void setGivenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
  }

  /**
   * Full Name as derived by OWS
   **/

  @Schema(description = "Full Name as derived by OWS")
  @JsonProperty("fullNameDerived")
  public String getFullNameDerived() {
    return fullNameDerived;
  }

  public void setFullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
  }

  /**
   * Family Name as derived by OWS
   **/

  @Schema(description = "Family Name as derived by OWS")
  @JsonProperty("familyNameDerived")
  public String getFamilyNameDerived() {
    return familyNameDerived;
  }

  public void setFamilyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
  }

  /**
   * Customer Initials
   **/

  @Schema(description = "Customer Initials")
  @JsonProperty("initials")
  public String getInitials() {
    return initials;
  }

  public void setInitials(String initials) {
    this.initials = initials;
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
   * A standardised list of all birth country names associated to the customer record. All
   * standardised names will ISO names.
   **/

  @Schema(description = "A standardised list of all birth country names associated to the customer record. All standardised names will ISO names.")
  @JsonProperty("countryOfBirth")
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * A standardised list of all address country names associated to the customer record. All
   * standardised names will ISO names.
   **/

  @Schema(description = "A standardised list of all address country names associated to the customer record. All standardised names will ISO names.")
  @JsonProperty("addressCountry")
  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  /**
   * A standardised list of all address country names in ISO 2 character code format associated to
   * the customer record.
   **/

  @Schema(description = "A standardised list of all address country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("edqAddressCountryCode")
  public String getEdqAddressCountryCode() {
    return edqAddressCountryCode;
  }

  public void setEdqAddressCountryCode(String edqAddressCountryCode) {
    this.edqAddressCountryCode = edqAddressCountryCode;
  }

  /**
   * A standardised list of all residency country names associated to the customer record. All
   * standardised names will ISO names.
   **/

  @Schema(description = "A standardised list of all residency country names associated to the customer record. All standardised names will ISO names.")
  @JsonProperty("countryOfResidence")
  public String getCountryOfResidence() {
    return countryOfResidence;
  }

  public void setCountryOfResidence(String countryOfResidence) {
    this.countryOfResidence = countryOfResidence;
  }

  /**
   * A standardised list of all birth country names in ISO 2 character code format associated to the
   * customer record.
   **/

  @Schema(description = "A standardised list of all birth country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("edqBirthCountryCode")
  public String getEdqBirthCountryCode() {
    return edqBirthCountryCode;
  }

  public void setEdqBirthCountryCode(String edqBirthCountryCode) {
    this.edqBirthCountryCode = edqBirthCountryCode;
  }

  /**
   * A standardised list of all residency country names in ISO 2 character code format associated to
   * the customer record.
   **/

  @Schema(description = "A standardised list of all residency country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("edqResidenceCountriesCode")
  public String getEdqResidenceCountriesCode() {
    return edqResidenceCountriesCode;
  }

  public void setEdqResidenceCountriesCode(String edqResidenceCountriesCode) {
    this.edqResidenceCountriesCode = edqResidenceCountriesCode;
  }

  /**
   * A standardised list of all country names associated to the customer record. All standardised
   * names will ISO names.
   **/

  @Schema(description = "A standardised list of all country names associated to the customer record. All standardised names will ISO names.")
  @JsonProperty("nationalityOrCitizenship")
  public String getNationalityOrCitizenship() {
    return nationalityOrCitizenship;
  }

  public void setNationalityOrCitizenship(String nationalityOrCitizenship) {
    this.nationalityOrCitizenship = nationalityOrCitizenship;
  }

  /**
   * A standardised list of all country names in ISO 2 character code format associated to the
   * customer record.
   **/

  @Schema(description = "A standardised list of all country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("nationalityCountries")
  public String getNationalityCountries() {
    return nationalityCountries;
  }

  public void setNationalityCountries(String nationalityCountries) {
    this.nationalityCountries = nationalityCountries;
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
   * A standardised list of all country names in iso 2 character code format associated to the
   * customer record.
   **/

  @Schema(description = "A standardised list of all country names in iso 2 character code format associated to the customer record.")
  @JsonProperty("edqCountriesAllCodes")
  public String getEdqCountriesAllCodes() {
    return edqCountriesAllCodes;
  }

  public void setEdqCountriesAllCodes(String edqCountriesAllCodes) {
    this.edqCountriesAllCodes = edqCountriesAllCodes;
  }

  /**
   * A standardised list of all employer country names associated to the customer record.. All
   * standardised names will ISO names.
   **/

  @Schema(description = "A standardised list of all employer country names associated to the customer record.. All standardised names will ISO names.")
  @JsonProperty("edqEmployerAllCountries")
  public String getEdqEmployerAllCountries() {
    return edqEmployerAllCountries;
  }

  public void setEdqEmployerAllCountries(String edqEmployerAllCountries) {
    this.edqEmployerAllCountries = edqEmployerAllCountries;
  }

  /**
   * A standardised list of all employer country names in ISO 2 character code format associated to
   * the customer record.
   **/

  @Schema(description = "A standardised list of all employer country names in ISO 2 character code format associated to the customer record.")
  @JsonProperty("edqEmployerAllCountriesCodes")
  public String getEdqEmployerAllCountriesCodes() {
    return edqEmployerAllCountriesCodes;
  }

  public void setEdqEmployerAllCountriesCodes(String edqEmployerAllCountriesCodes) {
    this.edqEmployerAllCountriesCodes = edqEmployerAllCountriesCodes;
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
   * For development use only
   **/

  @Schema(description = "For development use only")
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
   * Standardised Date Of Birth
   **/

  @Schema(description = "Standardised Date Of Birth")
  @JsonProperty("dateOfBirth")
  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Original Date Of Birth
   **/

  @Schema(description = "Original Date Of Birth")
  @JsonProperty("dobOriginal")
  public String getDobOriginal() {
    return dobOriginal;
  }

  public void setDobOriginal(String dobOriginal) {
    this.dobOriginal = dobOriginal;
  }

  /**
   * Standardised Date Of Birth in alternate format
   **/

  @Schema(description = "Standardised Date Of Birth in alternate format")
  @JsonProperty("edqDobString")
  public String getEdqDobString() {
    return edqDobString;
  }

  public void setEdqDobString(String edqDobString) {
    this.edqDobString = edqDobString;
  }

  /**
   * Year Of Birth
   **/

  @Schema(description = "Year Of Birth")
  @JsonProperty("yearOfBirth")
  public String getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  /**
   * Standardised City
   **/

  @Schema(description = "Standardised City")
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Standardised Postal Code
   **/

  @Schema(description = "Standardised Postal Code")
  @JsonProperty("postalCode")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
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
   * Standardised Gender
   **/

  @Schema(description = "Standardised Gender")
  @JsonProperty("gender")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Is Gender Derived?
   **/

  @Schema(description = "Is Gender Derived?")
  @JsonProperty("genderDerivedFlag")
  public String getGenderDerivedFlag() {
    return genderDerivedFlag;
  }

  public void setGenderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
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
   * Customer Passport Number
   **/

  @Schema(description = "Customer Passport Number")
  @JsonProperty("passportNumber")
  public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  /**
   * Customer Passport Issue Country
   **/

  @Schema(description = "Customer Passport Issue Country")
  @JsonProperty("passportIssueCountry")
  public String getPassportIssueCountry() {
    return passportIssueCountry;
  }

  public void setPassportIssueCountry(String passportIssueCountry) {
    this.passportIssueCountry = passportIssueCountry;
  }

  /**
   * Customer Social Security Number
   **/

  @Schema(description = "Customer Social Security Number")
  @JsonProperty("socialSecurityNumber")
  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  /**
   * Customer Close Of Business Date
   **/

  @Schema(description = "Customer Close Of Business Date")
  @JsonProperty("edqCloseOfBusinessDate")
  public String getEdqCloseOfBusinessDate() {
    return edqCloseOfBusinessDate;
  }

  public void setEdqCloseOfBusinessDate(String edqCloseOfBusinessDate) {
    this.edqCloseOfBusinessDate = edqCloseOfBusinessDate;
  }

  /**
   * Customer Occupation
   **/

  @Schema(description = "Customer Occupation")
  @JsonProperty("profileOccupation")
  public String getProfileOccupation() {
    return profileOccupation;
  }

  public void setProfileOccupation(String profileOccupation) {
    this.profileOccupation = profileOccupation;
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
   * Full name as originally provided
   **/

  @Schema(description = "Full name as originally provided")
  @JsonProperty("originalScriptName")
  public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
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
   * Original Customer Given Name
   **/

  @Schema(description = "Original Customer Given Name")
  @JsonProperty("givenNamesOriginal")
  public String getGivenNamesOriginal() {
    return givenNamesOriginal;
  }

  public void setGivenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
  }

  /**
   * Original Family name
   **/

  @Schema(description = "Original Family name")
  @JsonProperty("surname")
  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
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
  @JsonProperty("requestDateTimeDDMMYYYY0000")
  public String getRequestDateTimeDDMMYYYY0000() {
    return requestDateTimeDDMMYYYY0000;
  }

  public void setRequestDateTimeDDMMYYYY0000(String requestDateTimeDDMMYYYY0000) {
    this.requestDateTimeDDMMYYYY0000 = requestDateTimeDDMMYYYY0000;
  }

  /**
   * List ID&amp;#58;List Record Type&amp;#58;&#x3D;3 digit result code from Auto Discounting
   * process
   **/

  @Schema(description = "List ID&#58;List Record Type&#58;=3 digit result code from Auto Discounting process")
  @JsonProperty("autoDiscountDecision")
  public String getAutoDiscountDecision() {
    return autoDiscountDecision;
  }

  public void setAutoDiscountDecision(String autoDiscountDecision) {
    this.autoDiscountDecision = autoDiscountDecision;
  }

  /**
   * Indicates the type of record
   **/

  @Schema(description = "Indicates the type of record")
  @JsonProperty("recordType")
  public String getRecordType() {
    return recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  /**
   * For development use only
   **/

  @Schema(description = "For development use only")
  @JsonProperty("dummy")
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
    return Objects.equals(caseId, customerIndividuals.caseId) &&
        Objects.equals(recordId, customerIndividuals.recordId) &&
        Objects.equals(inputStream, customerIndividuals.inputStream) &&
        Objects.equals(sourceSystemHistoryId, customerIndividuals.sourceSystemHistoryId) &&
        Objects.equals(closeOfBusinessDate, customerIndividuals.closeOfBusinessDate) &&
        Objects.equals(partitionNumber, customerIndividuals.partitionNumber) &&
        Objects.equals(sourceSystemIdentifier, customerIndividuals.sourceSystemIdentifier) &&
        Objects.equals(externalProfileId, customerIndividuals.externalProfileId) &&
        Objects.equals(concatenatedProfileId, customerIndividuals.concatenatedProfileId) &&
        Objects.equals(profileType, customerIndividuals.profileType) &&
        Objects.equals(partyRoleTypeCode, customerIndividuals.partyRoleTypeCode) &&
        Objects.equals(profileStatus, customerIndividuals.profileStatus) &&
        Objects.equals(profileSegment, customerIndividuals.profileSegment) &&
        Objects.equals(lobName, customerIndividuals.lobName) &&
        Objects.equals(lobRegion, customerIndividuals.lobRegion) &&
        Objects.equals(lobCountry, customerIndividuals.lobCountry) &&
        Objects.equals(hsbcLegalEntityCode, customerIndividuals.hsbcLegalEntityCode) &&
        Objects.equals(nameCategoryCode, customerIndividuals.nameCategoryCode) &&
        Objects.equals(nameTypeCode, customerIndividuals.nameTypeCode) &&
        Objects.equals(nameLanguageTypeCode, customerIndividuals.nameLanguageTypeCode) &&
        Objects.equals(givenName, customerIndividuals.givenName) &&
        Objects.equals(middleName, customerIndividuals.middleName) &&
        Objects.equals(familyNameOriginal, customerIndividuals.familyNameOriginal) &&
        Objects.equals(initialsOriginal, customerIndividuals.initialsOriginal) &&
        Objects.equals(profileFullName, customerIndividuals.profileFullName) &&
        Objects.equals(pepIndicator, customerIndividuals.pepIndicator) &&
        Objects.equals(
            specialCategoryCustomerIndicator, customerIndividuals.specialCategoryCustomerIndicator)
        &&
        Objects.equals(genderCode, customerIndividuals.genderCode) &&
        Objects.equals(birthDate, customerIndividuals.birthDate) &&
        Objects.equals(placeOfBirth, customerIndividuals.placeOfBirth) &&
        Objects.equals(townOfBirth, customerIndividuals.townOfBirth) &&
        Objects.equals(
            stateProvinceOrCountyOfBirth, customerIndividuals.stateProvinceOrCountyOfBirth) &&
        Objects.equals(countryOfBirthOriginal, customerIndividuals.countryOfBirthOriginal) &&
        Objects.equals(sourceAddressType, customerIndividuals.sourceAddressType) &&
        Objects.equals(addressFormatCode, customerIndividuals.addressFormatCode) &&
        Objects.equals(addressLanguageTypeCode, customerIndividuals.addressLanguageTypeCode) &&
        Objects.equals(sourceAddressLine1, customerIndividuals.sourceAddressLine1) &&
        Objects.equals(sourceAddressLine2, customerIndividuals.sourceAddressLine2) &&
        Objects.equals(sourceAddressLine3, customerIndividuals.sourceAddressLine3) &&
        Objects.equals(sourceAddressLine4, customerIndividuals.sourceAddressLine4) &&
        Objects.equals(sourceAddressLine5, customerIndividuals.sourceAddressLine5) &&
        Objects.equals(sourceAddressLine6, customerIndividuals.sourceAddressLine6) &&
        Objects.equals(sourceAddressLine7, customerIndividuals.sourceAddressLine7) &&
        Objects.equals(sourceAddressLine8, customerIndividuals.sourceAddressLine8) &&
        Objects.equals(sourceAddressLine9, customerIndividuals.sourceAddressLine9) &&
        Objects.equals(sourceAddressLine10, customerIndividuals.sourceAddressLine10) &&
        Objects.equals(sourcePostalCode, customerIndividuals.sourcePostalCode) &&
        Objects.equals(sourceCountry, customerIndividuals.sourceCountry) &&
        Objects.equals(identificationDocument1, customerIndividuals.identificationDocument1) &&
        Objects.equals(identificationDocument2, customerIndividuals.identificationDocument2) &&
        Objects.equals(identificationDocument3, customerIndividuals.identificationDocument3) &&
        Objects.equals(identificationDocument4, customerIndividuals.identificationDocument4) &&
        Objects.equals(identificationDocument5, customerIndividuals.identificationDocument5) &&
        Objects.equals(identificationDocument6, customerIndividuals.identificationDocument6) &&
        Objects.equals(identificationDocument7, customerIndividuals.identificationDocument7) &&
        Objects.equals(identificationDocument8, customerIndividuals.identificationDocument8) &&
        Objects.equals(identificationDocument9, customerIndividuals.identificationDocument9) &&
        Objects.equals(identificationDocument10, customerIndividuals.identificationDocument10) &&
        Objects.equals(residenceCountries, customerIndividuals.residenceCountries) &&
        Objects.equals(
            nationalityCitizenshipCountries, customerIndividuals.nationalityCitizenshipCountries) &&
        Objects.equals(employer1Details, customerIndividuals.employer1Details) &&
        Objects.equals(employer1Address1, customerIndividuals.employer1Address1) &&
        Objects.equals(employer1Address2, customerIndividuals.employer1Address2) &&
        Objects.equals(employer1Address3, customerIndividuals.employer1Address3) &&
        Objects.equals(employer2Details, customerIndividuals.employer2Details) &&
        Objects.equals(employer2Address1, customerIndividuals.employer2Address1) &&
        Objects.equals(employer2Address2, customerIndividuals.employer2Address2) &&
        Objects.equals(employer2Address3, customerIndividuals.employer2Address3) &&
        Objects.equals(employer3Details, customerIndividuals.employer3Details) &&
        Objects.equals(employer3Address1, customerIndividuals.employer3Address1) &&
        Objects.equals(employer3Address2, customerIndividuals.employer3Address2) &&
        Objects.equals(employer3Address3, customerIndividuals.employer3Address3) &&
        Objects.equals(edqCustId, customerIndividuals.edqCustId) &&
        Objects.equals(edqCustSubId, customerIndividuals.edqCustSubId) &&
        Objects.equals(titleDerived, customerIndividuals.titleDerived) &&
        Objects.equals(givenNamesDerived, customerIndividuals.givenNamesDerived) &&
        Objects.equals(fullNameDerived, customerIndividuals.fullNameDerived) &&
        Objects.equals(familyNameDerived, customerIndividuals.familyNameDerived) &&
        Objects.equals(initials, customerIndividuals.initials) &&
        Objects.equals(edqLobCountryCode, customerIndividuals.edqLobCountryCode) &&
        Objects.equals(countryOfBirth, customerIndividuals.countryOfBirth) &&
        Objects.equals(addressCountry, customerIndividuals.addressCountry) &&
        Objects.equals(edqAddressCountryCode, customerIndividuals.edqAddressCountryCode) &&
        Objects.equals(countryOfResidence, customerIndividuals.countryOfResidence) &&
        Objects.equals(edqBirthCountryCode, customerIndividuals.edqBirthCountryCode) &&
        Objects.equals(edqResidenceCountriesCode, customerIndividuals.edqResidenceCountriesCode) &&
        Objects.equals(nationalityOrCitizenship, customerIndividuals.nationalityOrCitizenship) &&
        Objects.equals(nationalityCountries, customerIndividuals.nationalityCountries) &&
        Objects.equals(countriesAll, customerIndividuals.countriesAll) &&
        Objects.equals(edqCountriesAllCodes, customerIndividuals.edqCountriesAllCodes) &&
        Objects.equals(edqEmployerAllCountries, customerIndividuals.edqEmployerAllCountries) &&
        Objects.equals(
            edqEmployerAllCountriesCodes, customerIndividuals.edqEmployerAllCountriesCodes) &&
        Objects.equals(edqLob, customerIndividuals.edqLob) &&
        Objects.equals(edqPermission, customerIndividuals.edqPermission) &&
        Objects.equals(taxId, customerIndividuals.taxId) &&
        Objects.equals(dateOfBirth, customerIndividuals.dateOfBirth) &&
        Objects.equals(dobOriginal, customerIndividuals.dobOriginal) &&
        Objects.equals(edqDobString, customerIndividuals.edqDobString) &&
        Objects.equals(yearOfBirth, customerIndividuals.yearOfBirth) &&
        Objects.equals(city, customerIndividuals.city) &&
        Objects.equals(postalCode, customerIndividuals.postalCode) &&
        Objects.equals(profileFullAddress, customerIndividuals.profileFullAddress) &&
        Objects.equals(gender, customerIndividuals.gender) &&
        Objects.equals(genderDerivedFlag, customerIndividuals.genderDerivedFlag) &&
        Objects.equals(profileHyperlink, customerIndividuals.profileHyperlink) &&
        Objects.equals(searchHyperlink, customerIndividuals.searchHyperlink) &&
        Objects.equals(edqListKey, customerIndividuals.edqListKey) &&
        Objects.equals(passportNumber, customerIndividuals.passportNumber) &&
        Objects.equals(passportIssueCountry, customerIndividuals.passportIssueCountry) &&
        Objects.equals(socialSecurityNumber, customerIndividuals.socialSecurityNumber) &&
        Objects.equals(edqCloseOfBusinessDate, customerIndividuals.edqCloseOfBusinessDate) &&
        Objects.equals(profileOccupation, customerIndividuals.profileOccupation) &&
        Objects.equals(personOrBusinessIndicator, customerIndividuals.personOrBusinessIndicator) &&
        Objects.equals(profileNameType, customerIndividuals.profileNameType) &&
        Objects.equals(edqPartyRoleTypeDescription, customerIndividuals.edqPartyRoleTypeDescription)
        &&
        Objects.equals(
            edqPartyStatusCodeDescription, customerIndividuals.edqPartyStatusCodeDescription) &&
        Objects.equals(edqDay1SpikeFlag, customerIndividuals.edqDay1SpikeFlag) &&
        Objects.equals(originalScriptName, customerIndividuals.originalScriptName) &&
        Objects.equals(address, customerIndividuals.address) &&
        Objects.equals(givenNamesOriginal, customerIndividuals.givenNamesOriginal) &&
        Objects.equals(surname, customerIndividuals.surname) &&
        Objects.equals(edqScreeningMode, customerIndividuals.edqScreeningMode) &&
        Objects.equals(edqCaseKey, customerIndividuals.edqCaseKey) &&
        Objects.equals(addressType, customerIndividuals.addressType) &&
        Objects.equals(sscCodes, customerIndividuals.sscCodes) &&
        Objects.equals(ctrpFragment, customerIndividuals.ctrpFragment) &&
        Objects.equals(requestUserName, customerIndividuals.requestUserName) &&
        Objects.equals(requestDateTimeDDMMYYYY0000, customerIndividuals.requestDateTimeDDMMYYYY0000)
        &&
        Objects.equals(autoDiscountDecision, customerIndividuals.autoDiscountDecision) &&
        Objects.equals(recordType, customerIndividuals.recordType) &&
        Objects.equals(dummy, customerIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, sourceSystemHistoryId, closeOfBusinessDate, partitionNumber,
        sourceSystemIdentifier, externalProfileId, concatenatedProfileId, profileType,
        partyRoleTypeCode, profileStatus, profileSegment, lobName, lobRegion, lobCountry,
        hsbcLegalEntityCode, nameCategoryCode, nameTypeCode, nameLanguageTypeCode, givenName,
        middleName, familyNameOriginal, initialsOriginal, profileFullName, pepIndicator,
        specialCategoryCustomerIndicator, genderCode, birthDate, placeOfBirth, townOfBirth,
        stateProvinceOrCountyOfBirth, countryOfBirthOriginal, sourceAddressType, addressFormatCode,
        addressLanguageTypeCode, sourceAddressLine1, sourceAddressLine2, sourceAddressLine3,
        sourceAddressLine4, sourceAddressLine5, sourceAddressLine6, sourceAddressLine7,
        sourceAddressLine8, sourceAddressLine9, sourceAddressLine10, sourcePostalCode,
        sourceCountry, identificationDocument1, identificationDocument2, identificationDocument3,
        identificationDocument4, identificationDocument5, identificationDocument6,
        identificationDocument7, identificationDocument8, identificationDocument9,
        identificationDocument10, residenceCountries, nationalityCitizenshipCountries,
        employer1Details, employer1Address1, employer1Address2, employer1Address3, employer2Details,
        employer2Address1, employer2Address2, employer2Address3, employer3Details,
        employer3Address1, employer3Address2, employer3Address3, edqCustId, edqCustSubId,
        titleDerived, givenNamesDerived, fullNameDerived, familyNameDerived, initials,
        edqLobCountryCode, countryOfBirth, addressCountry, edqAddressCountryCode,
        countryOfResidence, edqBirthCountryCode, edqResidenceCountriesCode,
        nationalityOrCitizenship, nationalityCountries, countriesAll, edqCountriesAllCodes,
        edqEmployerAllCountries, edqEmployerAllCountriesCodes, edqLob, edqPermission, taxId,
        dateOfBirth, dobOriginal, edqDobString, yearOfBirth, city, postalCode, profileFullAddress,
        gender, genderDerivedFlag, profileHyperlink, searchHyperlink, edqListKey, passportNumber,
        passportIssueCountry, socialSecurityNumber, edqCloseOfBusinessDate, profileOccupation,
        personOrBusinessIndicator, profileNameType, edqPartyRoleTypeDescription,
        edqPartyStatusCodeDescription, edqDay1SpikeFlag, originalScriptName, address,
        givenNamesOriginal, surname, edqScreeningMode, edqCaseKey, addressType, sscCodes,
        ctrpFragment, requestUserName, requestDateTimeDDMMYYYY0000, autoDiscountDecision,
        recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerIndividuals {\n");

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
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    familyNameOriginal: ").append(toIndentedString(familyNameOriginal)).append("\n");
    sb.append("    initialsOriginal: ").append(toIndentedString(initialsOriginal)).append("\n");
    sb.append("    profileFullName: ").append(toIndentedString(profileFullName)).append("\n");
    sb.append("    pepIndicator: ").append(toIndentedString(pepIndicator)).append("\n");
    sb
        .append("    specialCategoryCustomerIndicator: ")
        .append(toIndentedString(specialCategoryCustomerIndicator))
        .append("\n");
    sb.append("    genderCode: ").append(toIndentedString(genderCode)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    placeOfBirth: ").append(toIndentedString(placeOfBirth)).append("\n");
    sb.append("    townOfBirth: ").append(toIndentedString(townOfBirth)).append("\n");
    sb
        .append("    stateProvinceOrCountyOfBirth: ")
        .append(toIndentedString(stateProvinceOrCountyOfBirth))
        .append("\n");
    sb
        .append("    countryOfBirthOriginal: ")
        .append(toIndentedString(countryOfBirthOriginal))
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
    sb.append("    sourceCountry: ").append(toIndentedString(sourceCountry)).append("\n");
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
    sb.append("    residenceCountries: ").append(toIndentedString(residenceCountries)).append("\n");
    sb
        .append("    nationalityCitizenshipCountries: ")
        .append(toIndentedString(nationalityCitizenshipCountries))
        .append("\n");
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
    sb.append("    edqCustId: ").append(toIndentedString(edqCustId)).append("\n");
    sb.append("    edqCustSubId: ").append(toIndentedString(edqCustSubId)).append("\n");
    sb.append("    titleDerived: ").append(toIndentedString(titleDerived)).append("\n");
    sb.append("    givenNamesDerived: ").append(toIndentedString(givenNamesDerived)).append("\n");
    sb.append("    fullNameDerived: ").append(toIndentedString(fullNameDerived)).append("\n");
    sb.append("    familyNameDerived: ").append(toIndentedString(familyNameDerived)).append("\n");
    sb.append("    initials: ").append(toIndentedString(initials)).append("\n");
    sb.append("    edqLobCountryCode: ").append(toIndentedString(edqLobCountryCode)).append("\n");
    sb.append("    countryOfBirth: ").append(toIndentedString(countryOfBirth)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb
        .append("    edqAddressCountryCode: ")
        .append(toIndentedString(edqAddressCountryCode))
        .append("\n");
    sb.append("    countryOfResidence: ").append(toIndentedString(countryOfResidence)).append("\n");
    sb
        .append("    edqBirthCountryCode: ")
        .append(toIndentedString(edqBirthCountryCode))
        .append("\n");
    sb
        .append("    edqResidenceCountriesCode: ")
        .append(toIndentedString(edqResidenceCountriesCode))
        .append("\n");
    sb
        .append("    nationalityOrCitizenship: ")
        .append(toIndentedString(nationalityOrCitizenship))
        .append("\n");
    sb
        .append("    nationalityCountries: ")
        .append(toIndentedString(nationalityCountries))
        .append("\n");
    sb.append("    countriesAll: ").append(toIndentedString(countriesAll)).append("\n");
    sb
        .append("    edqCountriesAllCodes: ")
        .append(toIndentedString(edqCountriesAllCodes))
        .append("\n");
    sb
        .append("    edqEmployerAllCountries: ")
        .append(toIndentedString(edqEmployerAllCountries))
        .append("\n");
    sb
        .append("    edqEmployerAllCountriesCodes: ")
        .append(toIndentedString(edqEmployerAllCountriesCodes))
        .append("\n");
    sb.append("    edqLob: ").append(toIndentedString(edqLob)).append("\n");
    sb.append("    edqPermission: ").append(toIndentedString(edqPermission)).append("\n");
    sb.append("    taxId: ").append(toIndentedString(taxId)).append("\n");
    sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
    sb.append("    dobOriginal: ").append(toIndentedString(dobOriginal)).append("\n");
    sb.append("    edqDobString: ").append(toIndentedString(edqDobString)).append("\n");
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
    sb
        .append("    passportIssueCountry: ")
        .append(toIndentedString(passportIssueCountry))
        .append("\n");
    sb
        .append("    socialSecurityNumber: ")
        .append(toIndentedString(socialSecurityNumber))
        .append("\n");
    sb
        .append("    edqCloseOfBusinessDate: ")
        .append(toIndentedString(edqCloseOfBusinessDate))
        .append("\n");
    sb.append("    profileOccupation: ").append(toIndentedString(profileOccupation)).append("\n");
    sb
        .append("    personOrBusinessIndicator: ")
        .append(toIndentedString(personOrBusinessIndicator))
        .append("\n");
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
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    givenNamesOriginal: ").append(toIndentedString(givenNamesOriginal)).append("\n");
    sb.append("    surname: ").append(toIndentedString(surname)).append("\n");
    sb.append("    edqScreeningMode: ").append(toIndentedString(edqScreeningMode)).append("\n");
    sb.append("    edqCaseKey: ").append(toIndentedString(edqCaseKey)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    sscCodes: ").append(toIndentedString(sscCodes)).append("\n");
    sb.append("    ctrpFragment: ").append(toIndentedString(ctrpFragment)).append("\n");
    sb.append("    requestUserName: ").append(toIndentedString(requestUserName)).append("\n");
    sb
        .append("    requestDateTimeDDMMYYYY0000: ")
        .append(toIndentedString(requestDateTimeDDMMYYYY0000))
        .append("\n");
    sb
        .append("    autoDiscountDecision: ")
        .append(toIndentedString(autoDiscountDecision))
        .append("\n");
    sb.append("    recordType: ").append(toIndentedString(recordType)).append("\n");
    sb.append("    dummy: ").append(toIndentedString(dummy)).append("\n");
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
