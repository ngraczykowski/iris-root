package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class PrivateListIndividuals {

  private int caseId;
  private BigDecimal recordId = null;
  private String inputStream = null;
  private String listKey = null;
  private String listSubKey = null;
  private String listRecordType = null;
  private String listRecordOrigin = null;
  private String listRecordId = null;
  private String listRecordSubId = null;
  private String passportNumber = null;
  private String nationalId = null;
  private String title = null;
  private String fullNameOriginal = null;
  private String givenNamesOriginal = null;
  private String familyNameOriginal = null;
  private String fullNameDerived = null;
  private String givenNamesDerived = null;
  private String familyNameDerived = null;
  private String nameType = null;
  private String nameQuality = null;
  private String primaryName = null;
  private String originalScriptName = null;
  private String gender = null;
  private String genderDerivedFlag = null;
  private String dateOfBirth = null;
  private Integer yearOfBirth = null;
  private String deceasedFlag = null;
  private String deceasedDate = null;
  private String occupation = null;
  private String address = null;
  private String city = null;
  private String placeOfBirth = null;
  private String postalCode = null;
  private String addressCountry = null;
  private String residencyCountry = null;
  private String countryOfBirth = null;
  private String nationalities = null;
  private String countryCodesAll = null;
  private String countriesAll = null;
  private String profileHyperlink = null;
  private String searchHyperlink = null;
  private String linkedProfiles = null;
  private String linkedRelationships = null;
  private BigDecimal riskScore = null;
  private BigDecimal pepRiskScore = null;
  private BigDecimal dataConfidenceScore = null;
  private String dataConfidenceComment = null;
  private String inactiveFlag = null;
  private String inactiveSinceDate = null;
  private String pEPClassification = null;
  private String addedDate = null;
  private String lastUpdatedDate = null;
  private String edqAddressType = null;
  private String edqCurrentAddress = null;
  private String edqTelephoneNumber = null;
  private String edqCellNumberBusiness = null;
  private String edqCellNumberPersonal = null;
  private String edqTaxNumber = null;
  private String edqDrivingLicence = null;
  private String edqEmailAddress = null;
  private String edqNiNumber = null;
  private String edqScionRef = null;
  private String edqListCustomerType = null;
  private String edqSuffix = null;
  private String edqRole = null;
  private String edqListCategory = null;
  private String edqFurtherInfo = null;
  private String edqSsn = null;
  private String edqCustomerId = null;
  private String edqListName = null;
  private String edqWatchListType = null;
  private String middleName1Derived = null;
  private String middleName2Derived = null;
  private String middleName1Original = null;
  private String middleName2Original = null;
  private String currentAddress = null;
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
   * Refers to a record within an Alert where there are multiple Customer name records within the
   * same Alert
   **/

  @Schema(description = "Refers to a record within an Alert where there are multiple Customer name records within the same Alert")
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
   * Indicates list origin e.g. WC for world check list
   **/

  @Schema(description = "Indicates list origin e.g. WC for world check list")
  @JsonProperty("listKey")
  public String getListKey() {
    return listKey;
  }

  public void setListKey(String listKey) {
    this.listKey = listKey;
  }

  /**
   * Indicates the origin of a record and its type e.g. WC-SAN
   **/

  @Schema(description = "Indicates the origin of a record and its type e.g. WC-SAN")
  @JsonProperty("listSubKey")
  public String getListSubKey() {
    return listSubKey;
  }

  public void setListSubKey(String listSubKey) {
    this.listSubKey = listSubKey;
  }

  /**
   * Indicates the type of matching record e.g. SAN
   **/

  @Schema(description = "Indicates the type of matching record e.g. SAN")
  @JsonProperty("listRecordType")
  public String getListRecordType() {
    return listRecordType;
  }

  public void setListRecordType(String listRecordType) {
    this.listRecordType = listRecordType;
  }

  /**
   * List the origin where the Private List record was derived e.g. OFAC
   **/

  @Schema(description = "List the origin where the Private List record was derived e.g. OFAC")
  @JsonProperty("listRecordOrigin")
  public String getListRecordOrigin() {
    return listRecordOrigin;
  }

  public void setListRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
  }

  /**
   * Unique Identifier assigned to the Private List record
   **/

  @Schema(description = "Unique Identifier assigned to the Private List record")
  @JsonProperty("listRecordId")
  public String getListRecordId() {
    return listRecordId;
  }

  public void setListRecordId(String listRecordId) {
    this.listRecordId = listRecordId;
  }

  /**
   * ID for the sub records within a Private List record
   **/

  @Schema(description = "ID for the sub records within a Private List record")
  @JsonProperty("listRecordSubId")
  public String getListRecordSubId() {
    return listRecordSubId;
  }

  public void setListRecordSubId(String listRecordSubId) {
    this.listRecordSubId = listRecordSubId;
  }

  /**
   * Watchlist Entry&#x27;s Passport Number (if known)
   **/

  @Schema(description = "Watchlist Entry's Passport Number (if known)")
  @JsonProperty("passportNumber")
  public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  /**
   * Watchlist Entry&#x27;s National Identification Number (if known)
   **/

  @Schema(description = "Watchlist Entry's National Identification Number (if known)")
  @JsonProperty("nationalId")
  public String getNationalId() {
    return nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  /**
   * Watchlist Entry&#x27;s title (if known)
   **/

  @Schema(description = "Watchlist Entry's title (if known)")
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Watchlist Entry&#x27;s Full Name
   **/

  @Schema(description = "Watchlist Entry's Full Name")
  @JsonProperty("fullNameOriginal")
  public String getFullNameOriginal() {
    return fullNameOriginal;
  }

  public void setFullNameOriginal(String fullNameOriginal) {
    this.fullNameOriginal = fullNameOriginal;
  }

  /**
   * Watchlist Entry&#x27;s Given Name
   **/

  @Schema(description = "Watchlist Entry's Given Name")
  @JsonProperty("givenNamesOriginal")
  public String getGivenNamesOriginal() {
    return givenNamesOriginal;
  }

  public void setGivenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
  }

  /**
   * Watchlist Entry&#x27;s Family Name
   **/

  @Schema(description = "Watchlist Entry's Family Name")
  @JsonProperty("familyNameOriginal")
  public String getFamilyNameOriginal() {
    return familyNameOriginal;
  }

  public void setFamilyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
  }

  /**
   * Records a Family Name where it has been derived by OWS
   **/

  @Schema(description = "Records a Family Name where it has been derived by OWS")
  @JsonProperty("fullNameDerived")
  public String getFullNameDerived() {
    return fullNameDerived;
  }

  public void setFullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
  }

  /**
   * Records a Given Name where it has been derived by OWS
   **/

  @Schema(description = "Records a Given Name where it has been derived by OWS")
  @JsonProperty("givenNamesDerived")
  public String getGivenNamesDerived() {
    return givenNamesDerived;
  }

  public void setGivenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
  }

  /**
   * Records a Family Name where it has been derived by OWS
   **/

  @Schema(description = "Records a Family Name where it has been derived by OWS")
  @JsonProperty("familyNameDerived")
  public String getFamilyNameDerived() {
    return familyNameDerived;
  }

  public void setFamilyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
  }

  /**
   * Records the type of name in use, for example&amp;#58; Primary Name
   **/

  @Schema(description = "Records the type of name in use, for example&#58; Primary Name")
  @JsonProperty("nameType")
  public String getNameType() {
    return nameType;
  }

  public void setNameType(String nameType) {
    this.nameType = nameType;
  }

  /**
   * Indicates the accuracy of the name information, Low, Medium or High
   **/

  @Schema(description = "Indicates the accuracy of the name information, Low, Medium or High")
  @JsonProperty("nameQuality")
  public String getNameQuality() {
    return nameQuality;
  }

  public void setNameQuality(String nameQuality) {
    this.nameQuality = nameQuality;
  }

  /**
   * Primary Name of the Watchlist Entry
   **/

  @Schema(description = "Primary Name of the Watchlist Entry")
  @JsonProperty("primaryName")
  public String getPrimaryName() {
    return primaryName;
  }

  public void setPrimaryName(String primaryName) {
    this.primaryName = primaryName;
  }

  /**
   * Watchlist original script name
   **/

  @Schema(description = "Watchlist original script name")
  @JsonProperty("originalScriptName")
  public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
  }

  /**
   * Watchlist Entry&#x27;s gender (if known)
   **/

  @Schema(description = "Watchlist Entry's gender (if known)")
  @JsonProperty("gender")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Denotes if the Watchlist Entry&#x27;s gender has been derived by OWS
   **/

  @Schema(description = "Denotes if the Watchlist Entry's gender has been derived by OWS")
  @JsonProperty("genderDerivedFlag")
  public String getGenderDerivedFlag() {
    return genderDerivedFlag;
  }

  public void setGenderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
  }

  /**
   * Watchlist Entry&#x27;s date of birth (if known)
   **/

  @Schema(description = "Watchlist Entry's date of birth (if known)")
  @JsonProperty("dateOfBirth")
  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Watchlist Entry&#x27;s year of birth (if known)
   **/

  @Schema(description = "Watchlist Entry's year of birth (if known)")
  @JsonProperty("yearOfBirth")
  public Integer getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(Integer yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  /**
   * Watchlist Entry is known or believed to be deceased
   **/

  @Schema(description = "Watchlist Entry is known or believed to be deceased")
  @JsonProperty("deceasedFlag")
  public String getDeceasedFlag() {
    return deceasedFlag;
  }

  public void setDeceasedFlag(String deceasedFlag) {
    this.deceasedFlag = deceasedFlag;
  }

  /**
   * Date the Watchlist Entry is known or believed to have become deceased
   **/

  @Schema(description = "Date the Watchlist Entry is known or believed to have become deceased")
  @JsonProperty("deceasedDate")
  public String getDeceasedDate() {
    return deceasedDate;
  }

  public void setDeceasedDate(String deceasedDate) {
    this.deceasedDate = deceasedDate;
  }

  /**
   * Watchlist Entry&#x27;s occupation (if known)
   **/

  @Schema(description = "Watchlist Entry's occupation (if known)")
  @JsonProperty("occupation")
  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  /**
   * Watchlist Entry&#x27;s address (if known)
   **/

  @Schema(description = "Watchlist Entry's address (if known)")
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Watchlist Entry&#x27;s city (if known)
   **/

  @Schema(description = "Watchlist Entry's city (if known)")
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Watchlist Entry&#x27;s Place Of Birth (if known)
   **/

  @Schema(description = "Watchlist Entry's Place Of Birth (if known)")
  @JsonProperty("placeOfBirth")
  public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  /**
   * Watchlist Entry&#x27;s postal code (if known)
   **/

  @Schema(description = "Watchlist Entry's postal code (if known)")
  @JsonProperty("postalCode")
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Watchlist Entry&#x27;s address country (if known)
   **/

  @Schema(description = "Watchlist Entry's address country (if known)")
  @JsonProperty("addressCountry")
  public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  /**
   * Watchlist Entry&#x27;s country of residence (if known)
   **/

  @Schema(description = "Watchlist Entry's country of residence (if known)")
  @JsonProperty("residencyCountry")
  public String getResidencyCountry() {
    return residencyCountry;
  }

  public void setResidencyCountry(String residencyCountry) {
    this.residencyCountry = residencyCountry;
  }

  /**
   * Watchlist Entry&#x27;s country of birth (if known)
   **/

  @Schema(description = "Watchlist Entry's country of birth (if known)")
  @JsonProperty("countryOfBirth")
  public String getCountryOfBirth() {
    return countryOfBirth;
  }

  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  /**
   * Watchlist Entry&#x27;s nationality (if known)
   **/

  @Schema(description = "Watchlist Entry's nationality (if known)")
  @JsonProperty("nationalities")
  public String getNationalities() {
    return nationalities;
  }

  public void setNationalities(String nationalities) {
    this.nationalities = nationalities;
  }

  /**
   * Records all of the Countries associated with the Watchlist Entry (if known)in the ISO Country
   * Code format
   **/

  @Schema(description = "Records all of the Countries associated with the Watchlist Entry (if known)in the ISO Country Code format")
  @JsonProperty("countryCodesAll")
  public String getCountryCodesAll() {
    return countryCodesAll;
  }

  public void setCountryCodesAll(String countryCodesAll) {
    this.countryCodesAll = countryCodesAll;
  }

  /**
   * Records all of the Countries within the Customer record that will be screened for Prohibitions
   **/

  @Schema(description = "Records all of the Countries within the Customer record that will be screened for Prohibitions")
  @JsonProperty("countriesAll")
  public String getCountriesAll() {
    return countriesAll;
  }

  public void setCountriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
  }

  /**
   * Watchlist Entry address (if known)
   **/

  @Schema(description = "Watchlist Entry address (if known)")
  @JsonProperty("profileHyperlink")
  public String getProfileHyperlink() {
    return profileHyperlink;
  }

  public void setProfileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
  }

  /**
   * Watchlist Entry address (if known)
   **/

  @Schema(description = "Watchlist Entry address (if known)")
  @JsonProperty("searchHyperlink")
  public String getSearchHyperlink() {
    return searchHyperlink;
  }

  public void setSearchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
  }

  /**
   * Any known associated Watchlist Entry profiles
   **/

  @Schema(description = "Any known associated Watchlist Entry profiles")
  @JsonProperty("linkedProfiles")
  public String getLinkedProfiles() {
    return linkedProfiles;
  }

  public void setLinkedProfiles(String linkedProfiles) {
    this.linkedProfiles = linkedProfiles;
  }

  /**
   * Any known associated Watchlist Entry relationships
   **/

  @Schema(description = "Any known associated Watchlist Entry relationships")
  @JsonProperty("linkedRelationships")
  public String getLinkedRelationships() {
    return linkedRelationships;
  }

  public void setLinkedRelationships(String linkedRelationships) {
    this.linkedRelationships = linkedRelationships;
  }

  /**
   * Private List assigned risk score
   **/

  @Schema(description = "Private List assigned risk score")
  @JsonProperty("riskScore")
  public BigDecimal getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
  }

  /**
   * Private List assigned PEP risk score
   **/

  @Schema(description = "Private List assigned PEP risk score")
  @JsonProperty("pepRiskScore")
  public BigDecimal getPepRiskScore() {
    return pepRiskScore;
  }

  public void setPepRiskScore(BigDecimal pepRiskScore) {
    this.pepRiskScore = pepRiskScore;
  }

  /**
   * Not Provided
   **/

  @Schema(description = "Not Provided")
  @JsonProperty("dataConfidenceScore")
  public BigDecimal getDataConfidenceScore() {
    return dataConfidenceScore;
  }

  public void setDataConfidenceScore(BigDecimal dataConfidenceScore) {
    this.dataConfidenceScore = dataConfidenceScore;
  }

  /**
   * Not Provided
   **/

  @Schema(description = "Not Provided")
  @JsonProperty("dataConfidenceComment")
  public String getDataConfidenceComment() {
    return dataConfidenceComment;
  }

  public void setDataConfidenceComment(String dataConfidenceComment) {
    this.dataConfidenceComment = dataConfidenceComment;
  }

  /**
   * Denotes if the record is considered inactive in the Private List
   **/

  @Schema(description = "Denotes if the record is considered inactive in the Private List")
  @JsonProperty("inactiveFlag")
  public String getInactiveFlag() {
    return inactiveFlag;
  }

  public void setInactiveFlag(String inactiveFlag) {
    this.inactiveFlag = inactiveFlag;
  }

  /**
   * Date since the record was marked inactive
   **/

  @Schema(description = "Date since the record was marked inactive")
  @JsonProperty("inactiveSinceDate")
  public String getInactiveSinceDate() {
    return inactiveSinceDate;
  }

  public void setInactiveSinceDate(String inactiveSinceDate) {
    this.inactiveSinceDate = inactiveSinceDate;
  }

  /**
   * Type of PEP, for example&amp;#58; Local Counsellor
   **/

  @Schema(description = "Type of PEP, for example&#58; Local Counsellor")
  @JsonProperty("pEPClassification")
  public String getPEPClassification() {
    return pEPClassification;
  }

  public void setPEPClassification(String pEPClassification) {
    this.pEPClassification = pEPClassification;
  }

  /**
   * Date data source was added
   **/

  @Schema(description = "Date data source was added")
  @JsonProperty("addedDate")
  public String getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(String addedDate) {
    this.addedDate = addedDate;
  }

  /**
   * Data since the Watchlist Entry was last updated
   **/

  @Schema(description = "Data since the Watchlist Entry was last updated")
  @JsonProperty("lastUpdatedDate")
  public String getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(String lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  /**
   * Address label as provided by the private list
   **/

  @Schema(description = "Address label as provided by the private list")
  @JsonProperty("edqAddressType")
  public String getEdqAddressType() {
    return edqAddressType;
  }

  public void setEdqAddressType(String edqAddressType) {
    this.edqAddressType = edqAddressType;
  }

  /**
   * Indicates with a yes or no if this is a current address
   **/

  @Schema(description = "Indicates with a yes or no if this is a current address")
  @JsonProperty("edqCurrentAddress")
  public String getEdqCurrentAddress() {
    return edqCurrentAddress;
  }

  public void setEdqCurrentAddress(String edqCurrentAddress) {
    this.edqCurrentAddress = edqCurrentAddress;
  }

  /**
   * Telephone number
   **/

  @Schema(description = "Telephone number")
  @JsonProperty("edqTelephoneNumber")
  public String getEdqTelephoneNumber() {
    return edqTelephoneNumber;
  }

  public void setEdqTelephoneNumber(String edqTelephoneNumber) {
    this.edqTelephoneNumber = edqTelephoneNumber;
  }

  /**
   * Telephone number
   **/

  @Schema(description = "Telephone number")
  @JsonProperty("edqCellNumberBusiness")
  public String getEdqCellNumberBusiness() {
    return edqCellNumberBusiness;
  }

  public void setEdqCellNumberBusiness(String edqCellNumberBusiness) {
    this.edqCellNumberBusiness = edqCellNumberBusiness;
  }

  /**
   * Telephone number
   **/

  @Schema(description = "Telephone number")
  @JsonProperty("edqCellNumberPersonal")
  public String getEdqCellNumberPersonal() {
    return edqCellNumberPersonal;
  }

  public void setEdqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
  }

  /**
   * Individuals TAX Number
   **/

  @Schema(description = "Individuals TAX Number")
  @JsonProperty("edqTaxNumber")
  public String getEdqTaxNumber() {
    return edqTaxNumber;
  }

  public void setEdqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
  }

  /**
   * Individuals Driving Licence Number
   **/

  @Schema(description = "Individuals Driving Licence Number")
  @JsonProperty("edqDrivingLicence")
  public String getEdqDrivingLicence() {
    return edqDrivingLicence;
  }

  public void setEdqDrivingLicence(String edqDrivingLicence) {
    this.edqDrivingLicence = edqDrivingLicence;
  }

  /**
   * Email Address
   **/

  @Schema(description = "Email Address")
  @JsonProperty("edqEmailAddress")
  public String getEdqEmailAddress() {
    return edqEmailAddress;
  }

  public void setEdqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
  }

  /**
   * Individuals National Insurance Number
   **/

  @Schema(description = "Individuals National Insurance Number")
  @JsonProperty("edqNiNumber")
  public String getEdqNiNumber() {
    return edqNiNumber;
  }

  public void setEdqNiNumber(String edqNiNumber) {
    this.edqNiNumber = edqNiNumber;
  }

  /**
   * SCION reference indicator
   **/

  @Schema(description = "SCION reference indicator")
  @JsonProperty("edqScionRef")
  public String getEdqScionRef() {
    return edqScionRef;
  }

  public void setEdqScionRef(String edqScionRef) {
    this.edqScionRef = edqScionRef;
  }

  /**
   * Indicates if the record is a Person or Business, P or B respectively.
   **/

  @Schema(description = "Indicates if the record is a Person or Business, P or B respectively.")
  @JsonProperty("edqListCustomerType")
  public String getEdqListCustomerType() {
    return edqListCustomerType;
  }

  public void setEdqListCustomerType(String edqListCustomerType) {
    this.edqListCustomerType = edqListCustomerType;
  }

  /**
   * Name suffix
   **/

  @Schema(description = "Name suffix")
  @JsonProperty("edqSuffix")
  public String getEdqSuffix() {
    return edqSuffix;
  }

  public void setEdqSuffix(String edqSuffix) {
    this.edqSuffix = edqSuffix;
  }

  /**
   * Individuals role
   **/

  @Schema(description = "Individuals role")
  @JsonProperty("edqRole")
  public String getEdqRole() {
    return edqRole;
  }

  public void setEdqRole(String edqRole) {
    this.edqRole = edqRole;
  }

  /**
   * Indicates the list record&#x27;s category.
   **/

  @Schema(description = "Indicates the list record's category.")
  @JsonProperty("edqListCategory")
  public String getEdqListCategory() {
    return edqListCategory;
  }

  public void setEdqListCategory(String edqListCategory) {
    this.edqListCategory = edqListCategory;
  }

  /**
   * Any further information as deemed useful
   **/

  @Schema(description = "Any further information as deemed useful")
  @JsonProperty("edqFurtherInfo")
  public String getEdqFurtherInfo() {
    return edqFurtherInfo;
  }

  public void setEdqFurtherInfo(String edqFurtherInfo) {
    this.edqFurtherInfo = edqFurtherInfo;
  }

  /**
   * Individuals Social Security Number
   **/

  @Schema(description = "Individuals Social Security Number")
  @JsonProperty("edqSsn")
  public String getEdqSsn() {
    return edqSsn;
  }

  public void setEdqSsn(String edqSsn) {
    this.edqSsn = edqSsn;
  }

  /**
   * Customer ID the record relates to
   **/

  @Schema(description = "Customer ID the record relates to")
  @JsonProperty("edqCustomerId")
  public String getEdqCustomerId() {
    return edqCustomerId;
  }

  public void setEdqCustomerId(String edqCustomerId) {
    this.edqCustomerId = edqCustomerId;
  }

  /**
   * Full name of the SCION list
   **/

  @Schema(description = "Full name of the SCION list")
  @JsonProperty("edqListName")
  public String getEdqListName() {
    return edqListName;
  }

  public void setEdqListName(String edqListName) {
    this.edqListName = edqListName;
  }

  /**
   * SCION List short form  name
   **/

  @Schema(description = "SCION List short form  name")
  @JsonProperty("edqWatchListType")
  public String getEdqWatchListType() {
    return edqWatchListType;
  }

  public void setEdqWatchListType(String edqWatchListType) {
    this.edqWatchListType = edqWatchListType;
  }

  /**
   * Watchlist Entry&#x27;s Derived Middle Name 1
   **/

  @Schema(description = "Watchlist Entry's Derived Middle Name 1")
  @JsonProperty("middleName1Derived")
  public String getMiddleName1Derived() {
    return middleName1Derived;
  }

  public void setMiddleName1Derived(String middleName1Derived) {
    this.middleName1Derived = middleName1Derived;
  }

  /**
   * Watchlist Entry&#x27;s Derived Middle Name 2
   **/

  @Schema(description = "Watchlist Entry's Derived Middle Name 2")
  @JsonProperty("middleName2Derived")
  public String getMiddleName2Derived() {
    return middleName2Derived;
  }

  public void setMiddleName2Derived(String middleName2Derived) {
    this.middleName2Derived = middleName2Derived;
  }

  /**
   * Watchlist Entry&#x27;s Original Middle Name 1
   **/

  @Schema(description = "Watchlist Entry's Original Middle Name 1")
  @JsonProperty("middleName1Original")
  public String getMiddleName1Original() {
    return middleName1Original;
  }

  public void setMiddleName1Original(String middleName1Original) {
    this.middleName1Original = middleName1Original;
  }

  /**
   * Watchlist Entry&#x27;s Original Middle Name 2
   **/

  @Schema(description = "Watchlist Entry's Original Middle Name 2")
  @JsonProperty("middleName2Original")
  public String getMiddleName2Original() {
    return middleName2Original;
  }

  public void setMiddleName2Original(String middleName2Original) {
    this.middleName2Original = middleName2Original;
  }

  /**
   * Watchlist Entry&#x27;s current address (if known)
   **/

  @Schema(description = "Watchlist Entry's current address (if known)")
  @JsonProperty("currentAddress")
  public String getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
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
    PrivateListIndividuals privateListIndividuals = (PrivateListIndividuals) o;
    return Objects.equals(caseId, privateListIndividuals.caseId) &&
        Objects.equals(recordId, privateListIndividuals.recordId) &&
        Objects.equals(inputStream, privateListIndividuals.inputStream) &&
        Objects.equals(listKey, privateListIndividuals.listKey) &&
        Objects.equals(listSubKey, privateListIndividuals.listSubKey) &&
        Objects.equals(listRecordType, privateListIndividuals.listRecordType) &&
        Objects.equals(listRecordOrigin, privateListIndividuals.listRecordOrigin) &&
        Objects.equals(listRecordId, privateListIndividuals.listRecordId) &&
        Objects.equals(listRecordSubId, privateListIndividuals.listRecordSubId) &&
        Objects.equals(passportNumber, privateListIndividuals.passportNumber) &&
        Objects.equals(nationalId, privateListIndividuals.nationalId) &&
        Objects.equals(title, privateListIndividuals.title) &&
        Objects.equals(fullNameOriginal, privateListIndividuals.fullNameOriginal) &&
        Objects.equals(givenNamesOriginal, privateListIndividuals.givenNamesOriginal) &&
        Objects.equals(familyNameOriginal, privateListIndividuals.familyNameOriginal) &&
        Objects.equals(fullNameDerived, privateListIndividuals.fullNameDerived) &&
        Objects.equals(givenNamesDerived, privateListIndividuals.givenNamesDerived) &&
        Objects.equals(familyNameDerived, privateListIndividuals.familyNameDerived) &&
        Objects.equals(nameType, privateListIndividuals.nameType) &&
        Objects.equals(nameQuality, privateListIndividuals.nameQuality) &&
        Objects.equals(primaryName, privateListIndividuals.primaryName) &&
        Objects.equals(originalScriptName, privateListIndividuals.originalScriptName) &&
        Objects.equals(gender, privateListIndividuals.gender) &&
        Objects.equals(genderDerivedFlag, privateListIndividuals.genderDerivedFlag) &&
        Objects.equals(dateOfBirth, privateListIndividuals.dateOfBirth) &&
        Objects.equals(yearOfBirth, privateListIndividuals.yearOfBirth) &&
        Objects.equals(deceasedFlag, privateListIndividuals.deceasedFlag) &&
        Objects.equals(deceasedDate, privateListIndividuals.deceasedDate) &&
        Objects.equals(occupation, privateListIndividuals.occupation) &&
        Objects.equals(address, privateListIndividuals.address) &&
        Objects.equals(city, privateListIndividuals.city) &&
        Objects.equals(placeOfBirth, privateListIndividuals.placeOfBirth) &&
        Objects.equals(postalCode, privateListIndividuals.postalCode) &&
        Objects.equals(addressCountry, privateListIndividuals.addressCountry) &&
        Objects.equals(residencyCountry, privateListIndividuals.residencyCountry) &&
        Objects.equals(countryOfBirth, privateListIndividuals.countryOfBirth) &&
        Objects.equals(nationalities, privateListIndividuals.nationalities) &&
        Objects.equals(countryCodesAll, privateListIndividuals.countryCodesAll) &&
        Objects.equals(countriesAll, privateListIndividuals.countriesAll) &&
        Objects.equals(profileHyperlink, privateListIndividuals.profileHyperlink) &&
        Objects.equals(searchHyperlink, privateListIndividuals.searchHyperlink) &&
        Objects.equals(linkedProfiles, privateListIndividuals.linkedProfiles) &&
        Objects.equals(linkedRelationships, privateListIndividuals.linkedRelationships) &&
        Objects.equals(riskScore, privateListIndividuals.riskScore) &&
        Objects.equals(pepRiskScore, privateListIndividuals.pepRiskScore) &&
        Objects.equals(dataConfidenceScore, privateListIndividuals.dataConfidenceScore) &&
        Objects.equals(dataConfidenceComment, privateListIndividuals.dataConfidenceComment) &&
        Objects.equals(inactiveFlag, privateListIndividuals.inactiveFlag) &&
        Objects.equals(inactiveSinceDate, privateListIndividuals.inactiveSinceDate) &&
        Objects.equals(pEPClassification, privateListIndividuals.pEPClassification) &&
        Objects.equals(addedDate, privateListIndividuals.addedDate) &&
        Objects.equals(lastUpdatedDate, privateListIndividuals.lastUpdatedDate) &&
        Objects.equals(edqAddressType, privateListIndividuals.edqAddressType) &&
        Objects.equals(edqCurrentAddress, privateListIndividuals.edqCurrentAddress) &&
        Objects.equals(edqTelephoneNumber, privateListIndividuals.edqTelephoneNumber) &&
        Objects.equals(edqCellNumberBusiness, privateListIndividuals.edqCellNumberBusiness) &&
        Objects.equals(edqCellNumberPersonal, privateListIndividuals.edqCellNumberPersonal) &&
        Objects.equals(edqTaxNumber, privateListIndividuals.edqTaxNumber) &&
        Objects.equals(edqDrivingLicence, privateListIndividuals.edqDrivingLicence) &&
        Objects.equals(edqEmailAddress, privateListIndividuals.edqEmailAddress) &&
        Objects.equals(edqNiNumber, privateListIndividuals.edqNiNumber) &&
        Objects.equals(edqScionRef, privateListIndividuals.edqScionRef) &&
        Objects.equals(edqListCustomerType, privateListIndividuals.edqListCustomerType) &&
        Objects.equals(edqSuffix, privateListIndividuals.edqSuffix) &&
        Objects.equals(edqRole, privateListIndividuals.edqRole) &&
        Objects.equals(edqListCategory, privateListIndividuals.edqListCategory) &&
        Objects.equals(edqFurtherInfo, privateListIndividuals.edqFurtherInfo) &&
        Objects.equals(edqSsn, privateListIndividuals.edqSsn) &&
        Objects.equals(edqCustomerId, privateListIndividuals.edqCustomerId) &&
        Objects.equals(edqListName, privateListIndividuals.edqListName) &&
        Objects.equals(edqWatchListType, privateListIndividuals.edqWatchListType) &&
        Objects.equals(middleName1Derived, privateListIndividuals.middleName1Derived) &&
        Objects.equals(middleName2Derived, privateListIndividuals.middleName2Derived) &&
        Objects.equals(middleName1Original, privateListIndividuals.middleName1Original) &&
        Objects.equals(middleName2Original, privateListIndividuals.middleName2Original) &&
        Objects.equals(currentAddress, privateListIndividuals.currentAddress) &&
        Objects.equals(recordType, privateListIndividuals.recordType) &&
        Objects.equals(dummy, privateListIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin,
        listRecordId, listRecordSubId, passportNumber, nationalId, title, fullNameOriginal,
        givenNamesOriginal, familyNameOriginal, fullNameDerived, givenNamesDerived,
        familyNameDerived, nameType, nameQuality, primaryName, originalScriptName, gender,
        genderDerivedFlag, dateOfBirth, yearOfBirth, deceasedFlag, deceasedDate, occupation,
        address, city, placeOfBirth, postalCode, addressCountry, residencyCountry, countryOfBirth,
        nationalities, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink,
        linkedProfiles, linkedRelationships, riskScore, pepRiskScore, dataConfidenceScore,
        dataConfidenceComment, inactiveFlag, inactiveSinceDate, pEPClassification, addedDate,
        lastUpdatedDate, edqAddressType, edqCurrentAddress, edqTelephoneNumber,
        edqCellNumberBusiness, edqCellNumberPersonal, edqTaxNumber, edqDrivingLicence,
        edqEmailAddress, edqNiNumber, edqScionRef, edqListCustomerType, edqSuffix, edqRole,
        edqListCategory, edqFurtherInfo, edqSsn, edqCustomerId, edqListName, edqWatchListType,
        middleName1Derived, middleName2Derived, middleName1Original, middleName2Original,
        currentAddress, recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrivateListIndividuals {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    listKey: ").append(toIndentedString(listKey)).append("\n");
    sb.append("    listSubKey: ").append(toIndentedString(listSubKey)).append("\n");
    sb.append("    listRecordType: ").append(toIndentedString(listRecordType)).append("\n");
    sb.append("    listRecordOrigin: ").append(toIndentedString(listRecordOrigin)).append("\n");
    sb.append("    listRecordId: ").append(toIndentedString(listRecordId)).append("\n");
    sb.append("    listRecordSubId: ").append(toIndentedString(listRecordSubId)).append("\n");
    sb.append("    passportNumber: ").append(toIndentedString(passportNumber)).append("\n");
    sb.append("    nationalId: ").append(toIndentedString(nationalId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    fullNameOriginal: ").append(toIndentedString(fullNameOriginal)).append("\n");
    sb.append("    givenNamesOriginal: ").append(toIndentedString(givenNamesOriginal)).append("\n");
    sb.append("    familyNameOriginal: ").append(toIndentedString(familyNameOriginal)).append("\n");
    sb.append("    fullNameDerived: ").append(toIndentedString(fullNameDerived)).append("\n");
    sb.append("    givenNamesDerived: ").append(toIndentedString(givenNamesDerived)).append("\n");
    sb.append("    familyNameDerived: ").append(toIndentedString(familyNameDerived)).append("\n");
    sb.append("    nameType: ").append(toIndentedString(nameType)).append("\n");
    sb.append("    nameQuality: ").append(toIndentedString(nameQuality)).append("\n");
    sb.append("    primaryName: ").append(toIndentedString(primaryName)).append("\n");
    sb.append("    originalScriptName: ").append(toIndentedString(originalScriptName)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    genderDerivedFlag: ").append(toIndentedString(genderDerivedFlag)).append("\n");
    sb.append("    dateOfBirth: ").append(toIndentedString(dateOfBirth)).append("\n");
    sb.append("    yearOfBirth: ").append(toIndentedString(yearOfBirth)).append("\n");
    sb.append("    deceasedFlag: ").append(toIndentedString(deceasedFlag)).append("\n");
    sb.append("    deceasedDate: ").append(toIndentedString(deceasedDate)).append("\n");
    sb.append("    occupation: ").append(toIndentedString(occupation)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    placeOfBirth: ").append(toIndentedString(placeOfBirth)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb.append("    residencyCountry: ").append(toIndentedString(residencyCountry)).append("\n");
    sb.append("    countryOfBirth: ").append(toIndentedString(countryOfBirth)).append("\n");
    sb.append("    nationalities: ").append(toIndentedString(nationalities)).append("\n");
    sb.append("    countryCodesAll: ").append(toIndentedString(countryCodesAll)).append("\n");
    sb.append("    countriesAll: ").append(toIndentedString(countriesAll)).append("\n");
    sb.append("    profileHyperlink: ").append(toIndentedString(profileHyperlink)).append("\n");
    sb.append("    searchHyperlink: ").append(toIndentedString(searchHyperlink)).append("\n");
    sb.append("    linkedProfiles: ").append(toIndentedString(linkedProfiles)).append("\n");
    sb
        .append("    linkedRelationships: ")
        .append(toIndentedString(linkedRelationships))
        .append("\n");
    sb.append("    riskScore: ").append(toIndentedString(riskScore)).append("\n");
    sb.append("    pepRiskScore: ").append(toIndentedString(pepRiskScore)).append("\n");
    sb
        .append("    dataConfidenceScore: ")
        .append(toIndentedString(dataConfidenceScore))
        .append("\n");
    sb
        .append("    dataConfidenceComment: ")
        .append(toIndentedString(dataConfidenceComment))
        .append("\n");
    sb.append("    inactiveFlag: ").append(toIndentedString(inactiveFlag)).append("\n");
    sb.append("    inactiveSinceDate: ").append(toIndentedString(inactiveSinceDate)).append("\n");
    sb.append("    pEPClassification: ").append(toIndentedString(pEPClassification)).append("\n");
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    edqAddressType: ").append(toIndentedString(edqAddressType)).append("\n");
    sb.append("    edqCurrentAddress: ").append(toIndentedString(edqCurrentAddress)).append("\n");
    sb.append("    edqTelephoneNumber: ").append(toIndentedString(edqTelephoneNumber)).append("\n");
    sb
        .append("    edqCellNumberBusiness: ")
        .append(toIndentedString(edqCellNumberBusiness))
        .append("\n");
    sb
        .append("    edqCellNumberPersonal: ")
        .append(toIndentedString(edqCellNumberPersonal))
        .append("\n");
    sb.append("    edqTaxNumber: ").append(toIndentedString(edqTaxNumber)).append("\n");
    sb.append("    edqDrivingLicence: ").append(toIndentedString(edqDrivingLicence)).append("\n");
    sb.append("    edqEmailAddress: ").append(toIndentedString(edqEmailAddress)).append("\n");
    sb.append("    edqNiNumber: ").append(toIndentedString(edqNiNumber)).append("\n");
    sb.append("    edqScionRef: ").append(toIndentedString(edqScionRef)).append("\n");
    sb
        .append("    edqListCustomerType: ")
        .append(toIndentedString(edqListCustomerType))
        .append("\n");
    sb.append("    edqSuffix: ").append(toIndentedString(edqSuffix)).append("\n");
    sb.append("    edqRole: ").append(toIndentedString(edqRole)).append("\n");
    sb.append("    edqListCategory: ").append(toIndentedString(edqListCategory)).append("\n");
    sb.append("    edqFurtherInfo: ").append(toIndentedString(edqFurtherInfo)).append("\n");
    sb.append("    edqSsn: ").append(toIndentedString(edqSsn)).append("\n");
    sb.append("    edqCustomerId: ").append(toIndentedString(edqCustomerId)).append("\n");
    sb.append("    edqListName: ").append(toIndentedString(edqListName)).append("\n");
    sb.append("    edqWatchListType: ").append(toIndentedString(edqWatchListType)).append("\n");
    sb.append("    middleName1Derived: ").append(toIndentedString(middleName1Derived)).append("\n");
    sb.append("    middleName2Derived: ").append(toIndentedString(middleName2Derived)).append("\n");
    sb
        .append("    middleName1Original: ")
        .append(toIndentedString(middleName1Original))
        .append("\n");
    sb
        .append("    middleName2Original: ")
        .append(toIndentedString(middleName2Original))
        .append("\n");
    sb.append("    currentAddress: ").append(toIndentedString(currentAddress)).append("\n");
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
