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
 * PrivateListIndividuals
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class PrivateListIndividuals   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("recordID")
  private BigDecimal recordID = null;

  @JsonProperty("inputStream")
  private String inputStream = null;

  @JsonProperty("listKey")
  private String listKey = null;

  @JsonProperty("listSubKey")
  private String listSubKey = null;

  @JsonProperty("listRecordType")
  private String listRecordType = null;

  @JsonProperty("listRecordOrigin")
  private String listRecordOrigin = null;

  @JsonProperty("listRecordID")
  private String listRecordID = null;

  @JsonProperty("listRecordSubID")
  private String listRecordSubID = null;

  @JsonProperty("passportNumber")
  private String passportNumber = null;

  @JsonProperty("nationalId")
  private String nationalId = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("fullNameOriginal")
  private String fullNameOriginal = null;

  @JsonProperty("givenNamesOriginal")
  private String givenNamesOriginal = null;

  @JsonProperty("familyNameOriginal")
  private String familyNameOriginal = null;

  @JsonProperty("fullNameDerived")
  private String fullNameDerived = null;

  @JsonProperty("givenNamesDerived")
  private String givenNamesDerived = null;

  @JsonProperty("familyNameDerived")
  private String familyNameDerived = null;

  @JsonProperty("nameType")
  private String nameType = null;

  @JsonProperty("nameQuality")
  private String nameQuality = null;

  @JsonProperty("primaryName")
  private String primaryName = null;

  @JsonProperty("originalScriptName")
  private String originalScriptName = null;

  @JsonProperty("gender")
  private String gender = null;

  @JsonProperty("genderDerivedFlag")
  private String genderDerivedFlag = null;

  @JsonProperty("dateOfBirth")
  private String dateOfBirth = null;

  @JsonProperty("yearOfBirth")
  private Integer yearOfBirth = null;

  @JsonProperty("deceasedFlag")
  private String deceasedFlag = null;

  @JsonProperty("deceasedDate")
  private String deceasedDate = null;

  @JsonProperty("occupation")
  private String occupation = null;

  @JsonProperty("address")
  private String address = null;

  @JsonProperty("city")
  private String city = null;

  @JsonProperty("placeOfBirth")
  private String placeOfBirth = null;

  @JsonProperty("postalCode")
  private String postalCode = null;

  @JsonProperty("addressCountry")
  private String addressCountry = null;

  @JsonProperty("residencyCountry")
  private String residencyCountry = null;

  @JsonProperty("countryOfBirth")
  private String countryOfBirth = null;

  @JsonProperty("nationalities")
  private String nationalities = null;

  @JsonProperty("countryCodesAll")
  private String countryCodesAll = null;

  @JsonProperty("countriesAll")
  private String countriesAll = null;

  @JsonProperty("profileHyperlink")
  private String profileHyperlink = null;

  @JsonProperty("searchHyperlink")
  private String searchHyperlink = null;

  @JsonProperty("linkedProfiles")
  private String linkedProfiles = null;

  @JsonProperty("linkedRelationships")
  private String linkedRelationships = null;

  @JsonProperty("riskScore")
  private BigDecimal riskScore = null;

  @JsonProperty("pEPRiskScore")
  private BigDecimal pEPRiskScore = null;

  @JsonProperty("dataConfidenceScore")
  private BigDecimal dataConfidenceScore = null;

  @JsonProperty("dataConfidenceComment")
  private String dataConfidenceComment = null;

  @JsonProperty("inactiveFlag")
  private String inactiveFlag = null;

  @JsonProperty("inactiveSinceDate")
  private String inactiveSinceDate = null;

  @JsonProperty("pEPClassification")
  private String pEPClassification = null;

  @JsonProperty("addedDate")
  private String addedDate = null;

  @JsonProperty("lastUpdatedDate")
  private String lastUpdatedDate = null;

  @JsonProperty("edqAddressType")
  private String edqAddressType = null;

  @JsonProperty("edqCurrentAddress")
  private String edqCurrentAddress = null;

  @JsonProperty("edqTelephoneNumber")
  private String edqTelephoneNumber = null;

  @JsonProperty("edqCellNumberBusiness")
  private String edqCellNumberBusiness = null;

  @JsonProperty("edqCellNumberPersonal")
  private String edqCellNumberPersonal = null;

  @JsonProperty("edqTaxNumber")
  private String edqTaxNumber = null;

  @JsonProperty("edqDrivingLicence")
  private String edqDrivingLicence = null;

  @JsonProperty("edqEmailAddress")
  private String edqEmailAddress = null;

  @JsonProperty("edqNINumber")
  private String edqNINumber = null;

  @JsonProperty("edqSCION_REF")
  private String edqSCIONREF = null;

  @JsonProperty("edqListCustomerType")
  private String edqListCustomerType = null;

  @JsonProperty("edqSuffix")
  private String edqSuffix = null;

  @JsonProperty("edqRole")
  private String edqRole = null;

  @JsonProperty("edqListCategory")
  private String edqListCategory = null;

  @JsonProperty("edqFurtherInfo")
  private String edqFurtherInfo = null;

  @JsonProperty("edqSSN")
  private String edqSSN = null;

  @JsonProperty("edqCustomerID")
  private String edqCustomerID = null;

  @JsonProperty("edqListName")
  private String edqListName = null;

  @JsonProperty("edqWatchListType")
  private String edqWatchListType = null;

  @JsonProperty("middleName1Derived")
  private String middleName1Derived = null;

  @JsonProperty("middleName2Derived")
  private String middleName2Derived = null;

  @JsonProperty("middleName1Original")
  private String middleName1Original = null;

  @JsonProperty("middleName2Original")
  private String middleName2Original = null;

  @JsonProperty("currentAddress")
  private String currentAddress = null;

  @JsonProperty("recordType")
  private String recordType = null;

  @JsonProperty("dummy")
  private String dummy = null;

  public PrivateListIndividuals caseId(Integer caseId) {
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

  public PrivateListIndividuals recordID(BigDecimal recordID) {
    this.recordID = recordID;
    return this;
  }

  /**
   * Refers to a record within an Alert where there are multiple Customer name records within the same Alert
   * @return recordID
   **/
  @Schema(description = "Refers to a record within an Alert where there are multiple Customer name records within the same Alert")
  
    @Valid
    public BigDecimal getRecordID() {
    return recordID;
  }

  public void setRecordID(BigDecimal recordID) {
    this.recordID = recordID;
  }

  public PrivateListIndividuals inputStream(String inputStream) {
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

  public PrivateListIndividuals listKey(String listKey) {
    this.listKey = listKey;
    return this;
  }

  /**
   * Indicates list origin e.g. WC for world check list
   * @return listKey
   **/
  @Schema(description = "Indicates list origin e.g. WC for world check list")
  
    public String getListKey() {
    return listKey;
  }

  public void setListKey(String listKey) {
    this.listKey = listKey;
  }

  public PrivateListIndividuals listSubKey(String listSubKey) {
    this.listSubKey = listSubKey;
    return this;
  }

  /**
   * Indicates the origin of a record and its type e.g. WC-SAN
   * @return listSubKey
   **/
  @Schema(description = "Indicates the origin of a record and its type e.g. WC-SAN")
  
    public String getListSubKey() {
    return listSubKey;
  }

  public void setListSubKey(String listSubKey) {
    this.listSubKey = listSubKey;
  }

  public PrivateListIndividuals listRecordType(String listRecordType) {
    this.listRecordType = listRecordType;
    return this;
  }

  /**
   * Indicates the type of matching record e.g. SAN
   * @return listRecordType
   **/
  @Schema(description = "Indicates the type of matching record e.g. SAN")
  
    public String getListRecordType() {
    return listRecordType;
  }

  public void setListRecordType(String listRecordType) {
    this.listRecordType = listRecordType;
  }

  public PrivateListIndividuals listRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
    return this;
  }

  /**
   * List the origin where the Private List record was derived e.g. OFAC
   * @return listRecordOrigin
   **/
  @Schema(description = "List the origin where the Private List record was derived e.g. OFAC")
  
    public String getListRecordOrigin() {
    return listRecordOrigin;
  }

  public void setListRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
  }

  public PrivateListIndividuals listRecordID(String listRecordID) {
    this.listRecordID = listRecordID;
    return this;
  }

  /**
   * Unique Identifier assigned to the Private List record
   * @return listRecordID
   **/
  @Schema(description = "Unique Identifier assigned to the Private List record")
  
    public String getListRecordID() {
    return listRecordID;
  }

  public void setListRecordID(String listRecordID) {
    this.listRecordID = listRecordID;
  }

  public PrivateListIndividuals listRecordSubID(String listRecordSubID) {
    this.listRecordSubID = listRecordSubID;
    return this;
  }

  /**
   * ID for the sub records within a Private List record
   * @return listRecordSubID
   **/
  @Schema(description = "ID for the sub records within a Private List record")
  
    public String getListRecordSubID() {
    return listRecordSubID;
  }

  public void setListRecordSubID(String listRecordSubID) {
    this.listRecordSubID = listRecordSubID;
  }

  public PrivateListIndividuals passportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
    return this;
  }

  /**
   * Watchlist Entry's Passport Number (if known)
   * @return passportNumber
   **/
  @Schema(description = "Watchlist Entry's Passport Number (if known)")
  
    public String getPassportNumber() {
    return passportNumber;
  }

  public void setPassportNumber(String passportNumber) {
    this.passportNumber = passportNumber;
  }

  public PrivateListIndividuals nationalId(String nationalId) {
    this.nationalId = nationalId;
    return this;
  }

  /**
   * Watchlist Entry's National Identification Number (if known)
   * @return nationalId
   **/
  @Schema(description = "Watchlist Entry's National Identification Number (if known)")
  
    public String getNationalId() {
    return nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  public PrivateListIndividuals title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Watchlist Entry's title (if known)
   * @return title
   **/
  @Schema(description = "Watchlist Entry's title (if known)")
  
    public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PrivateListIndividuals fullNameOriginal(String fullNameOriginal) {
    this.fullNameOriginal = fullNameOriginal;
    return this;
  }

  /**
   * Watchlist Entry's Full Name
   * @return fullNameOriginal
   **/
  @Schema(description = "Watchlist Entry's Full Name")
  
    public String getFullNameOriginal() {
    return fullNameOriginal;
  }

  public void setFullNameOriginal(String fullNameOriginal) {
    this.fullNameOriginal = fullNameOriginal;
  }

  public PrivateListIndividuals givenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
    return this;
  }

  /**
   * Watchlist Entry's Given Name
   * @return givenNamesOriginal
   **/
  @Schema(description = "Watchlist Entry's Given Name")
  
    public String getGivenNamesOriginal() {
    return givenNamesOriginal;
  }

  public void setGivenNamesOriginal(String givenNamesOriginal) {
    this.givenNamesOriginal = givenNamesOriginal;
  }

  public PrivateListIndividuals familyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
    return this;
  }

  /**
   * Watchlist Entry's Family Name
   * @return familyNameOriginal
   **/
  @Schema(description = "Watchlist Entry's Family Name")
  
    public String getFamilyNameOriginal() {
    return familyNameOriginal;
  }

  public void setFamilyNameOriginal(String familyNameOriginal) {
    this.familyNameOriginal = familyNameOriginal;
  }

  public PrivateListIndividuals fullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
    return this;
  }

  /**
   * Records a Family Name where it has been derived by OWS
   * @return fullNameDerived
   **/
  @Schema(description = "Records a Family Name where it has been derived by OWS")
  
    public String getFullNameDerived() {
    return fullNameDerived;
  }

  public void setFullNameDerived(String fullNameDerived) {
    this.fullNameDerived = fullNameDerived;
  }

  public PrivateListIndividuals givenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
    return this;
  }

  /**
   * Records a Given Name where it has been derived by OWS
   * @return givenNamesDerived
   **/
  @Schema(description = "Records a Given Name where it has been derived by OWS")
  
    public String getGivenNamesDerived() {
    return givenNamesDerived;
  }

  public void setGivenNamesDerived(String givenNamesDerived) {
    this.givenNamesDerived = givenNamesDerived;
  }

  public PrivateListIndividuals familyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
    return this;
  }

  /**
   * Records a Family Name where it has been derived by OWS
   * @return familyNameDerived
   **/
  @Schema(description = "Records a Family Name where it has been derived by OWS")
  
    public String getFamilyNameDerived() {
    return familyNameDerived;
  }

  public void setFamilyNameDerived(String familyNameDerived) {
    this.familyNameDerived = familyNameDerived;
  }

  public PrivateListIndividuals nameType(String nameType) {
    this.nameType = nameType;
    return this;
  }

  /**
   * Records the type of name in use, for example&#58; Primary Name
   * @return nameType
   **/
  @Schema(description = "Records the type of name in use, for example&#58; Primary Name")
  
    public String getNameType() {
    return nameType;
  }

  public void setNameType(String nameType) {
    this.nameType = nameType;
  }

  public PrivateListIndividuals nameQuality(String nameQuality) {
    this.nameQuality = nameQuality;
    return this;
  }

  /**
   * Indicates the accuracy of the name information, Low, Medium or High
   * @return nameQuality
   **/
  @Schema(description = "Indicates the accuracy of the name information, Low, Medium or High")
  
    public String getNameQuality() {
    return nameQuality;
  }

  public void setNameQuality(String nameQuality) {
    this.nameQuality = nameQuality;
  }

  public PrivateListIndividuals primaryName(String primaryName) {
    this.primaryName = primaryName;
    return this;
  }

  /**
   * Primary Name of the Watchlist Entry
   * @return primaryName
   **/
  @Schema(description = "Primary Name of the Watchlist Entry")
  
    public String getPrimaryName() {
    return primaryName;
  }

  public void setPrimaryName(String primaryName) {
    this.primaryName = primaryName;
  }

  public PrivateListIndividuals originalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
    return this;
  }

  /**
   * Watchlist original script name
   * @return originalScriptName
   **/
  @Schema(description = "Watchlist original script name")
  
    public String getOriginalScriptName() {
    return originalScriptName;
  }

  public void setOriginalScriptName(String originalScriptName) {
    this.originalScriptName = originalScriptName;
  }

  public PrivateListIndividuals gender(String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Watchlist Entry's gender (if known)
   * @return gender
   **/
  @Schema(description = "Watchlist Entry's gender (if known)")
  
    public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public PrivateListIndividuals genderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
    return this;
  }

  /**
   * Denotes if the Watchlist Entry's gender has been derived by OWS
   * @return genderDerivedFlag
   **/
  @Schema(description = "Denotes if the Watchlist Entry's gender has been derived by OWS")
  
    public String getGenderDerivedFlag() {
    return genderDerivedFlag;
  }

  public void setGenderDerivedFlag(String genderDerivedFlag) {
    this.genderDerivedFlag = genderDerivedFlag;
  }

  public PrivateListIndividuals dateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  /**
   * Watchlist Entry's date of birth (if known)
   * @return dateOfBirth
   **/
  @Schema(description = "Watchlist Entry's date of birth (if known)")
  
    public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public PrivateListIndividuals yearOfBirth(Integer yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
    return this;
  }

  /**
   * Watchlist Entry's year of birth (if known)
   * @return yearOfBirth
   **/
  @Schema(description = "Watchlist Entry's year of birth (if known)")
  
    public Integer getYearOfBirth() {
    return yearOfBirth;
  }

  public void setYearOfBirth(Integer yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public PrivateListIndividuals deceasedFlag(String deceasedFlag) {
    this.deceasedFlag = deceasedFlag;
    return this;
  }

  /**
   * Watchlist Entry is known or believed to be deceased
   * @return deceasedFlag
   **/
  @Schema(description = "Watchlist Entry is known or believed to be deceased")
  
    public String getDeceasedFlag() {
    return deceasedFlag;
  }

  public void setDeceasedFlag(String deceasedFlag) {
    this.deceasedFlag = deceasedFlag;
  }

  public PrivateListIndividuals deceasedDate(String deceasedDate) {
    this.deceasedDate = deceasedDate;
    return this;
  }

  /**
   * Date the Watchlist Entry is known or believed to have become deceased
   * @return deceasedDate
   **/
  @Schema(description = "Date the Watchlist Entry is known or believed to have become deceased")
  
    public String getDeceasedDate() {
    return deceasedDate;
  }

  public void setDeceasedDate(String deceasedDate) {
    this.deceasedDate = deceasedDate;
  }

  public PrivateListIndividuals occupation(String occupation) {
    this.occupation = occupation;
    return this;
  }

  /**
   * Watchlist Entry's occupation (if known)
   * @return occupation
   **/
  @Schema(description = "Watchlist Entry's occupation (if known)")
  
    public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public PrivateListIndividuals address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Watchlist Entry's address (if known)
   * @return address
   **/
  @Schema(description = "Watchlist Entry's address (if known)")
  
    public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public PrivateListIndividuals city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Watchlist Entry's city (if known)
   * @return city
   **/
  @Schema(description = "Watchlist Entry's city (if known)")
  
    public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public PrivateListIndividuals placeOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
    return this;
  }

  /**
   * Watchlist Entry's Place Of Birth (if known)
   * @return placeOfBirth
   **/
  @Schema(description = "Watchlist Entry's Place Of Birth (if known)")
  
    public String getPlaceOfBirth() {
    return placeOfBirth;
  }

  public void setPlaceOfBirth(String placeOfBirth) {
    this.placeOfBirth = placeOfBirth;
  }

  public PrivateListIndividuals postalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  /**
   * Watchlist Entry's postal code (if known)
   * @return postalCode
   **/
  @Schema(description = "Watchlist Entry's postal code (if known)")
  
    public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public PrivateListIndividuals addressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
    return this;
  }

  /**
   * Watchlist Entry's address country (if known)
   * @return addressCountry
   **/
  @Schema(description = "Watchlist Entry's address country (if known)")
  
    public String getAddressCountry() {
    return addressCountry;
  }

  public void setAddressCountry(String addressCountry) {
    this.addressCountry = addressCountry;
  }

  public PrivateListIndividuals residencyCountry(String residencyCountry) {
    this.residencyCountry = residencyCountry;
    return this;
  }

  /**
   * Watchlist Entry's country of residence (if known)
   * @return residencyCountry
   **/
  @Schema(description = "Watchlist Entry's country of residence (if known)")
  
    public String getResidencyCountry() {
    return residencyCountry;
  }

  public void setResidencyCountry(String residencyCountry) {
    this.residencyCountry = residencyCountry;
  }

  public PrivateListIndividuals countryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
    return this;
  }

  /**
   * Watchlist Entry's country of birth (if known)
   * @return countryOfBirth
   **/
  @Schema(description = "Watchlist Entry's country of birth (if known)")
  
    public String getCountryOfBirth() {
    return countryOfBirth;
  }

  public void setCountryOfBirth(String countryOfBirth) {
    this.countryOfBirth = countryOfBirth;
  }

  public PrivateListIndividuals nationalities(String nationalities) {
    this.nationalities = nationalities;
    return this;
  }

  /**
   * Watchlist Entry's nationality (if known)
   * @return nationalities
   **/
  @Schema(description = "Watchlist Entry's nationality (if known)")
  
    public String getNationalities() {
    return nationalities;
  }

  public void setNationalities(String nationalities) {
    this.nationalities = nationalities;
  }

  public PrivateListIndividuals countryCodesAll(String countryCodesAll) {
    this.countryCodesAll = countryCodesAll;
    return this;
  }

  /**
   * Records all of the Countries associated with the Watchlist Entry (if known)in the ISO Country Code format
   * @return countryCodesAll
   **/
  @Schema(description = "Records all of the Countries associated with the Watchlist Entry (if known)in the ISO Country Code format")
  
    public String getCountryCodesAll() {
    return countryCodesAll;
  }

  public void setCountryCodesAll(String countryCodesAll) {
    this.countryCodesAll = countryCodesAll;
  }

  public PrivateListIndividuals countriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
    return this;
  }

  /**
   * Records all of the Countries within the Customer record that will be screened for Prohibitions
   * @return countriesAll
   **/
  @Schema(description = "Records all of the Countries within the Customer record that will be screened for Prohibitions")
  
    public String getCountriesAll() {
    return countriesAll;
  }

  public void setCountriesAll(String countriesAll) {
    this.countriesAll = countriesAll;
  }

  public PrivateListIndividuals profileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
    return this;
  }

  /**
   * Watchlist Entry address (if known)
   * @return profileHyperlink
   **/
  @Schema(description = "Watchlist Entry address (if known)")
  
    public String getProfileHyperlink() {
    return profileHyperlink;
  }

  public void setProfileHyperlink(String profileHyperlink) {
    this.profileHyperlink = profileHyperlink;
  }

  public PrivateListIndividuals searchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
    return this;
  }

  /**
   * Watchlist Entry address (if known)
   * @return searchHyperlink
   **/
  @Schema(description = "Watchlist Entry address (if known)")
  
    public String getSearchHyperlink() {
    return searchHyperlink;
  }

  public void setSearchHyperlink(String searchHyperlink) {
    this.searchHyperlink = searchHyperlink;
  }

  public PrivateListIndividuals linkedProfiles(String linkedProfiles) {
    this.linkedProfiles = linkedProfiles;
    return this;
  }

  /**
   * Any known associated Watchlist Entry profiles
   * @return linkedProfiles
   **/
  @Schema(description = "Any known associated Watchlist Entry profiles")
  
    public String getLinkedProfiles() {
    return linkedProfiles;
  }

  public void setLinkedProfiles(String linkedProfiles) {
    this.linkedProfiles = linkedProfiles;
  }

  public PrivateListIndividuals linkedRelationships(String linkedRelationships) {
    this.linkedRelationships = linkedRelationships;
    return this;
  }

  /**
   * Any known associated Watchlist Entry relationships
   * @return linkedRelationships
   **/
  @Schema(description = "Any known associated Watchlist Entry relationships")
  
    public String getLinkedRelationships() {
    return linkedRelationships;
  }

  public void setLinkedRelationships(String linkedRelationships) {
    this.linkedRelationships = linkedRelationships;
  }

  public PrivateListIndividuals riskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
    return this;
  }

  /**
   * Private List assigned risk score
   * @return riskScore
   **/
  @Schema(description = "Private List assigned risk score")
  
    @Valid
    public BigDecimal getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
  }

  public PrivateListIndividuals pEPRiskScore(BigDecimal pEPRiskScore) {
    this.pEPRiskScore = pEPRiskScore;
    return this;
  }

  /**
   * Private List assigned PEP risk score
   * @return pEPRiskScore
   **/
  @Schema(description = "Private List assigned PEP risk score")
  
    @Valid
    public BigDecimal getPEPRiskScore() {
    return pEPRiskScore;
  }

  public void setPEPRiskScore(BigDecimal pEPRiskScore) {
    this.pEPRiskScore = pEPRiskScore;
  }

  public PrivateListIndividuals dataConfidenceScore(BigDecimal dataConfidenceScore) {
    this.dataConfidenceScore = dataConfidenceScore;
    return this;
  }

  /**
   * Not Provided
   * @return dataConfidenceScore
   **/
  @Schema(description = "Not Provided")
  
    @Valid
    public BigDecimal getDataConfidenceScore() {
    return dataConfidenceScore;
  }

  public void setDataConfidenceScore(BigDecimal dataConfidenceScore) {
    this.dataConfidenceScore = dataConfidenceScore;
  }

  public PrivateListIndividuals dataConfidenceComment(String dataConfidenceComment) {
    this.dataConfidenceComment = dataConfidenceComment;
    return this;
  }

  /**
   * Not Provided
   * @return dataConfidenceComment
   **/
  @Schema(description = "Not Provided")
  
    public String getDataConfidenceComment() {
    return dataConfidenceComment;
  }

  public void setDataConfidenceComment(String dataConfidenceComment) {
    this.dataConfidenceComment = dataConfidenceComment;
  }

  public PrivateListIndividuals inactiveFlag(String inactiveFlag) {
    this.inactiveFlag = inactiveFlag;
    return this;
  }

  /**
   * Denotes if the record is considered inactive in the Private List
   * @return inactiveFlag
   **/
  @Schema(description = "Denotes if the record is considered inactive in the Private List")
  
    public String getInactiveFlag() {
    return inactiveFlag;
  }

  public void setInactiveFlag(String inactiveFlag) {
    this.inactiveFlag = inactiveFlag;
  }

  public PrivateListIndividuals inactiveSinceDate(String inactiveSinceDate) {
    this.inactiveSinceDate = inactiveSinceDate;
    return this;
  }

  /**
   * Date since the record was marked inactive
   * @return inactiveSinceDate
   **/
  @Schema(description = "Date since the record was marked inactive")
  
    public String getInactiveSinceDate() {
    return inactiveSinceDate;
  }

  public void setInactiveSinceDate(String inactiveSinceDate) {
    this.inactiveSinceDate = inactiveSinceDate;
  }

  public PrivateListIndividuals pEPClassification(String pEPClassification) {
    this.pEPClassification = pEPClassification;
    return this;
  }

  /**
   * Type of PEP, for example&#58; Local Counsellor
   * @return pEPClassification
   **/
  @Schema(description = "Type of PEP, for example&#58; Local Counsellor")
  
    public String getPEPClassification() {
    return pEPClassification;
  }

  public void setPEPClassification(String pEPClassification) {
    this.pEPClassification = pEPClassification;
  }

  public PrivateListIndividuals addedDate(String addedDate) {
    this.addedDate = addedDate;
    return this;
  }

  /**
   * Date data source was added
   * @return addedDate
   **/
  @Schema(description = "Date data source was added")
  
    public String getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(String addedDate) {
    this.addedDate = addedDate;
  }

  public PrivateListIndividuals lastUpdatedDate(String lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
    return this;
  }

  /**
   * Data since the Watchlist Entry was last updated
   * @return lastUpdatedDate
   **/
  @Schema(description = "Data since the Watchlist Entry was last updated")
  
    public String getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(String lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  public PrivateListIndividuals edqAddressType(String edqAddressType) {
    this.edqAddressType = edqAddressType;
    return this;
  }

  /**
   * Address label as provided by the private list
   * @return edqAddressType
   **/
  @Schema(description = "Address label as provided by the private list")
  
    public String getEdqAddressType() {
    return edqAddressType;
  }

  public void setEdqAddressType(String edqAddressType) {
    this.edqAddressType = edqAddressType;
  }

  public PrivateListIndividuals edqCurrentAddress(String edqCurrentAddress) {
    this.edqCurrentAddress = edqCurrentAddress;
    return this;
  }

  /**
   * Indicates with a yes or no if this is a current address
   * @return edqCurrentAddress
   **/
  @Schema(description = "Indicates with a yes or no if this is a current address")
  
    public String getEdqCurrentAddress() {
    return edqCurrentAddress;
  }

  public void setEdqCurrentAddress(String edqCurrentAddress) {
    this.edqCurrentAddress = edqCurrentAddress;
  }

  public PrivateListIndividuals edqTelephoneNumber(String edqTelephoneNumber) {
    this.edqTelephoneNumber = edqTelephoneNumber;
    return this;
  }

  /**
   * Telephone number
   * @return edqTelephoneNumber
   **/
  @Schema(description = "Telephone number")
  
    public String getEdqTelephoneNumber() {
    return edqTelephoneNumber;
  }

  public void setEdqTelephoneNumber(String edqTelephoneNumber) {
    this.edqTelephoneNumber = edqTelephoneNumber;
  }

  public PrivateListIndividuals edqCellNumberBusiness(String edqCellNumberBusiness) {
    this.edqCellNumberBusiness = edqCellNumberBusiness;
    return this;
  }

  /**
   * Telephone number
   * @return edqCellNumberBusiness
   **/
  @Schema(description = "Telephone number")
  
    public String getEdqCellNumberBusiness() {
    return edqCellNumberBusiness;
  }

  public void setEdqCellNumberBusiness(String edqCellNumberBusiness) {
    this.edqCellNumberBusiness = edqCellNumberBusiness;
  }

  public PrivateListIndividuals edqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
    return this;
  }

  /**
   * Telephone number
   * @return edqCellNumberPersonal
   **/
  @Schema(description = "Telephone number")
  
    public String getEdqCellNumberPersonal() {
    return edqCellNumberPersonal;
  }

  public void setEdqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
  }

  public PrivateListIndividuals edqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
    return this;
  }

  /**
   * Individuals TAX Number
   * @return edqTaxNumber
   **/
  @Schema(description = "Individuals TAX Number")
  
    public String getEdqTaxNumber() {
    return edqTaxNumber;
  }

  public void setEdqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
  }

  public PrivateListIndividuals edqDrivingLicence(String edqDrivingLicence) {
    this.edqDrivingLicence = edqDrivingLicence;
    return this;
  }

  /**
   * Individuals Driving Licence Number
   * @return edqDrivingLicence
   **/
  @Schema(description = "Individuals Driving Licence Number")
  
    public String getEdqDrivingLicence() {
    return edqDrivingLicence;
  }

  public void setEdqDrivingLicence(String edqDrivingLicence) {
    this.edqDrivingLicence = edqDrivingLicence;
  }

  public PrivateListIndividuals edqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
    return this;
  }

  /**
   * Email Address
   * @return edqEmailAddress
   **/
  @Schema(description = "Email Address")
  
    public String getEdqEmailAddress() {
    return edqEmailAddress;
  }

  public void setEdqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
  }

  public PrivateListIndividuals edqNINumber(String edqNINumber) {
    this.edqNINumber = edqNINumber;
    return this;
  }

  /**
   * Individuals National Insurance Number
   * @return edqNINumber
   **/
  @Schema(description = "Individuals National Insurance Number")
  
    public String getEdqNINumber() {
    return edqNINumber;
  }

  public void setEdqNINumber(String edqNINumber) {
    this.edqNINumber = edqNINumber;
  }

  public PrivateListIndividuals edqSCIONREF(String edqSCIONREF) {
    this.edqSCIONREF = edqSCIONREF;
    return this;
  }

  /**
   * SCION reference indicator
   * @return edqSCIONREF
   **/
  @Schema(description = "SCION reference indicator")
  
    public String getEdqSCIONREF() {
    return edqSCIONREF;
  }

  public void setEdqSCIONREF(String edqSCIONREF) {
    this.edqSCIONREF = edqSCIONREF;
  }

  public PrivateListIndividuals edqListCustomerType(String edqListCustomerType) {
    this.edqListCustomerType = edqListCustomerType;
    return this;
  }

  /**
   * Indicates if the record is a Person or Business, P or B respectively.
   * @return edqListCustomerType
   **/
  @Schema(description = "Indicates if the record is a Person or Business, P or B respectively.")
  
    public String getEdqListCustomerType() {
    return edqListCustomerType;
  }

  public void setEdqListCustomerType(String edqListCustomerType) {
    this.edqListCustomerType = edqListCustomerType;
  }

  public PrivateListIndividuals edqSuffix(String edqSuffix) {
    this.edqSuffix = edqSuffix;
    return this;
  }

  /**
   * Name suffix
   * @return edqSuffix
   **/
  @Schema(description = "Name suffix")
  
    public String getEdqSuffix() {
    return edqSuffix;
  }

  public void setEdqSuffix(String edqSuffix) {
    this.edqSuffix = edqSuffix;
  }

  public PrivateListIndividuals edqRole(String edqRole) {
    this.edqRole = edqRole;
    return this;
  }

  /**
   * Individuals role
   * @return edqRole
   **/
  @Schema(description = "Individuals role")
  
    public String getEdqRole() {
    return edqRole;
  }

  public void setEdqRole(String edqRole) {
    this.edqRole = edqRole;
  }

  public PrivateListIndividuals edqListCategory(String edqListCategory) {
    this.edqListCategory = edqListCategory;
    return this;
  }

  /**
   * Indicates the list record's category.
   * @return edqListCategory
   **/
  @Schema(description = "Indicates the list record's category.")
  
    public String getEdqListCategory() {
    return edqListCategory;
  }

  public void setEdqListCategory(String edqListCategory) {
    this.edqListCategory = edqListCategory;
  }

  public PrivateListIndividuals edqFurtherInfo(String edqFurtherInfo) {
    this.edqFurtherInfo = edqFurtherInfo;
    return this;
  }

  /**
   * Any further information as deemed useful
   * @return edqFurtherInfo
   **/
  @Schema(description = "Any further information as deemed useful")
  
    public String getEdqFurtherInfo() {
    return edqFurtherInfo;
  }

  public void setEdqFurtherInfo(String edqFurtherInfo) {
    this.edqFurtherInfo = edqFurtherInfo;
  }

  public PrivateListIndividuals edqSSN(String edqSSN) {
    this.edqSSN = edqSSN;
    return this;
  }

  /**
   * Individuals Social Security Number
   * @return edqSSN
   **/
  @Schema(description = "Individuals Social Security Number")
  
    public String getEdqSSN() {
    return edqSSN;
  }

  public void setEdqSSN(String edqSSN) {
    this.edqSSN = edqSSN;
  }

  public PrivateListIndividuals edqCustomerID(String edqCustomerID) {
    this.edqCustomerID = edqCustomerID;
    return this;
  }

  /**
   * Customer ID the record relates to
   * @return edqCustomerID
   **/
  @Schema(description = "Customer ID the record relates to")
  
    public String getEdqCustomerID() {
    return edqCustomerID;
  }

  public void setEdqCustomerID(String edqCustomerID) {
    this.edqCustomerID = edqCustomerID;
  }

  public PrivateListIndividuals edqListName(String edqListName) {
    this.edqListName = edqListName;
    return this;
  }

  /**
   * Full name of the SCION list
   * @return edqListName
   **/
  @Schema(description = "Full name of the SCION list")
  
    public String getEdqListName() {
    return edqListName;
  }

  public void setEdqListName(String edqListName) {
    this.edqListName = edqListName;
  }

  public PrivateListIndividuals edqWatchListType(String edqWatchListType) {
    this.edqWatchListType = edqWatchListType;
    return this;
  }

  /**
   * SCION List short form  name
   * @return edqWatchListType
   **/
  @Schema(description = "SCION List short form  name")
  
    public String getEdqWatchListType() {
    return edqWatchListType;
  }

  public void setEdqWatchListType(String edqWatchListType) {
    this.edqWatchListType = edqWatchListType;
  }

  public PrivateListIndividuals middleName1Derived(String middleName1Derived) {
    this.middleName1Derived = middleName1Derived;
    return this;
  }

  /**
   * Watchlist Entry's Derived Middle Name 1
   * @return middleName1Derived
   **/
  @Schema(description = "Watchlist Entry's Derived Middle Name 1")
  
    public String getMiddleName1Derived() {
    return middleName1Derived;
  }

  public void setMiddleName1Derived(String middleName1Derived) {
    this.middleName1Derived = middleName1Derived;
  }

  public PrivateListIndividuals middleName2Derived(String middleName2Derived) {
    this.middleName2Derived = middleName2Derived;
    return this;
  }

  /**
   * Watchlist Entry's Derived Middle Name 2
   * @return middleName2Derived
   **/
  @Schema(description = "Watchlist Entry's Derived Middle Name 2")
  
    public String getMiddleName2Derived() {
    return middleName2Derived;
  }

  public void setMiddleName2Derived(String middleName2Derived) {
    this.middleName2Derived = middleName2Derived;
  }

  public PrivateListIndividuals middleName1Original(String middleName1Original) {
    this.middleName1Original = middleName1Original;
    return this;
  }

  /**
   * Watchlist Entry's Original Middle Name 1
   * @return middleName1Original
   **/
  @Schema(description = "Watchlist Entry's Original Middle Name 1")
  
    public String getMiddleName1Original() {
    return middleName1Original;
  }

  public void setMiddleName1Original(String middleName1Original) {
    this.middleName1Original = middleName1Original;
  }

  public PrivateListIndividuals middleName2Original(String middleName2Original) {
    this.middleName2Original = middleName2Original;
    return this;
  }

  /**
   * Watchlist Entry's Original Middle Name 2
   * @return middleName2Original
   **/
  @Schema(description = "Watchlist Entry's Original Middle Name 2")
  
    public String getMiddleName2Original() {
    return middleName2Original;
  }

  public void setMiddleName2Original(String middleName2Original) {
    this.middleName2Original = middleName2Original;
  }

  public PrivateListIndividuals currentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
    return this;
  }

  /**
   * Watchlist Entry's current address (if known)
   * @return currentAddress
   **/
  @Schema(description = "Watchlist Entry's current address (if known)")
  
    public String getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
  }

  public PrivateListIndividuals recordType(String recordType) {
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

  public PrivateListIndividuals dummy(String dummy) {
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
    PrivateListIndividuals privateListIndividuals = (PrivateListIndividuals) o;
    return Objects.equals(this.caseId, privateListIndividuals.caseId) &&
        Objects.equals(this.recordID, privateListIndividuals.recordID) &&
        Objects.equals(this.inputStream, privateListIndividuals.inputStream) &&
        Objects.equals(this.listKey, privateListIndividuals.listKey) &&
        Objects.equals(this.listSubKey, privateListIndividuals.listSubKey) &&
        Objects.equals(this.listRecordType, privateListIndividuals.listRecordType) &&
        Objects.equals(this.listRecordOrigin, privateListIndividuals.listRecordOrigin) &&
        Objects.equals(this.listRecordID, privateListIndividuals.listRecordID) &&
        Objects.equals(this.listRecordSubID, privateListIndividuals.listRecordSubID) &&
        Objects.equals(this.passportNumber, privateListIndividuals.passportNumber) &&
        Objects.equals(this.nationalId, privateListIndividuals.nationalId) &&
        Objects.equals(this.title, privateListIndividuals.title) &&
        Objects.equals(this.fullNameOriginal, privateListIndividuals.fullNameOriginal) &&
        Objects.equals(this.givenNamesOriginal, privateListIndividuals.givenNamesOriginal) &&
        Objects.equals(this.familyNameOriginal, privateListIndividuals.familyNameOriginal) &&
        Objects.equals(this.fullNameDerived, privateListIndividuals.fullNameDerived) &&
        Objects.equals(this.givenNamesDerived, privateListIndividuals.givenNamesDerived) &&
        Objects.equals(this.familyNameDerived, privateListIndividuals.familyNameDerived) &&
        Objects.equals(this.nameType, privateListIndividuals.nameType) &&
        Objects.equals(this.nameQuality, privateListIndividuals.nameQuality) &&
        Objects.equals(this.primaryName, privateListIndividuals.primaryName) &&
        Objects.equals(this.originalScriptName, privateListIndividuals.originalScriptName) &&
        Objects.equals(this.gender, privateListIndividuals.gender) &&
        Objects.equals(this.genderDerivedFlag, privateListIndividuals.genderDerivedFlag) &&
        Objects.equals(this.dateOfBirth, privateListIndividuals.dateOfBirth) &&
        Objects.equals(this.yearOfBirth, privateListIndividuals.yearOfBirth) &&
        Objects.equals(this.deceasedFlag, privateListIndividuals.deceasedFlag) &&
        Objects.equals(this.deceasedDate, privateListIndividuals.deceasedDate) &&
        Objects.equals(this.occupation, privateListIndividuals.occupation) &&
        Objects.equals(this.address, privateListIndividuals.address) &&
        Objects.equals(this.city, privateListIndividuals.city) &&
        Objects.equals(this.placeOfBirth, privateListIndividuals.placeOfBirth) &&
        Objects.equals(this.postalCode, privateListIndividuals.postalCode) &&
        Objects.equals(this.addressCountry, privateListIndividuals.addressCountry) &&
        Objects.equals(this.residencyCountry, privateListIndividuals.residencyCountry) &&
        Objects.equals(this.countryOfBirth, privateListIndividuals.countryOfBirth) &&
        Objects.equals(this.nationalities, privateListIndividuals.nationalities) &&
        Objects.equals(this.countryCodesAll, privateListIndividuals.countryCodesAll) &&
        Objects.equals(this.countriesAll, privateListIndividuals.countriesAll) &&
        Objects.equals(this.profileHyperlink, privateListIndividuals.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, privateListIndividuals.searchHyperlink) &&
        Objects.equals(this.linkedProfiles, privateListIndividuals.linkedProfiles) &&
        Objects.equals(this.linkedRelationships, privateListIndividuals.linkedRelationships) &&
        Objects.equals(this.riskScore, privateListIndividuals.riskScore) &&
        Objects.equals(this.pEPRiskScore, privateListIndividuals.pEPRiskScore) &&
        Objects.equals(this.dataConfidenceScore, privateListIndividuals.dataConfidenceScore) &&
        Objects.equals(this.dataConfidenceComment, privateListIndividuals.dataConfidenceComment) &&
        Objects.equals(this.inactiveFlag, privateListIndividuals.inactiveFlag) &&
        Objects.equals(this.inactiveSinceDate, privateListIndividuals.inactiveSinceDate) &&
        Objects.equals(this.pEPClassification, privateListIndividuals.pEPClassification) &&
        Objects.equals(this.addedDate, privateListIndividuals.addedDate) &&
        Objects.equals(this.lastUpdatedDate, privateListIndividuals.lastUpdatedDate) &&
        Objects.equals(this.edqAddressType, privateListIndividuals.edqAddressType) &&
        Objects.equals(this.edqCurrentAddress, privateListIndividuals.edqCurrentAddress) &&
        Objects.equals(this.edqTelephoneNumber, privateListIndividuals.edqTelephoneNumber) &&
        Objects.equals(this.edqCellNumberBusiness, privateListIndividuals.edqCellNumberBusiness) &&
        Objects.equals(this.edqCellNumberPersonal, privateListIndividuals.edqCellNumberPersonal) &&
        Objects.equals(this.edqTaxNumber, privateListIndividuals.edqTaxNumber) &&
        Objects.equals(this.edqDrivingLicence, privateListIndividuals.edqDrivingLicence) &&
        Objects.equals(this.edqEmailAddress, privateListIndividuals.edqEmailAddress) &&
        Objects.equals(this.edqNINumber, privateListIndividuals.edqNINumber) &&
        Objects.equals(this.edqSCIONREF, privateListIndividuals.edqSCIONREF) &&
        Objects.equals(this.edqListCustomerType, privateListIndividuals.edqListCustomerType) &&
        Objects.equals(this.edqSuffix, privateListIndividuals.edqSuffix) &&
        Objects.equals(this.edqRole, privateListIndividuals.edqRole) &&
        Objects.equals(this.edqListCategory, privateListIndividuals.edqListCategory) &&
        Objects.equals(this.edqFurtherInfo, privateListIndividuals.edqFurtherInfo) &&
        Objects.equals(this.edqSSN, privateListIndividuals.edqSSN) &&
        Objects.equals(this.edqCustomerID, privateListIndividuals.edqCustomerID) &&
        Objects.equals(this.edqListName, privateListIndividuals.edqListName) &&
        Objects.equals(this.edqWatchListType, privateListIndividuals.edqWatchListType) &&
        Objects.equals(this.middleName1Derived, privateListIndividuals.middleName1Derived) &&
        Objects.equals(this.middleName2Derived, privateListIndividuals.middleName2Derived) &&
        Objects.equals(this.middleName1Original, privateListIndividuals.middleName1Original) &&
        Objects.equals(this.middleName2Original, privateListIndividuals.middleName2Original) &&
        Objects.equals(this.currentAddress, privateListIndividuals.currentAddress) &&
        Objects.equals(this.recordType, privateListIndividuals.recordType) &&
        Objects.equals(this.dummy, privateListIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordID, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin, listRecordID, listRecordSubID, passportNumber, nationalId, title, fullNameOriginal, givenNamesOriginal, familyNameOriginal, fullNameDerived, givenNamesDerived, familyNameDerived, nameType, nameQuality, primaryName, originalScriptName, gender, genderDerivedFlag, dateOfBirth, yearOfBirth, deceasedFlag, deceasedDate, occupation, address, city, placeOfBirth, postalCode, addressCountry, residencyCountry, countryOfBirth, nationalities, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink, linkedProfiles, linkedRelationships, riskScore, pEPRiskScore, dataConfidenceScore, dataConfidenceComment, inactiveFlag, inactiveSinceDate, pEPClassification, addedDate, lastUpdatedDate, edqAddressType, edqCurrentAddress, edqTelephoneNumber, edqCellNumberBusiness, edqCellNumberPersonal, edqTaxNumber, edqDrivingLicence, edqEmailAddress, edqNINumber, edqSCIONREF, edqListCustomerType, edqSuffix, edqRole, edqListCategory, edqFurtherInfo, edqSSN, edqCustomerID, edqListName, edqWatchListType, middleName1Derived, middleName2Derived, middleName1Original, middleName2Original, currentAddress, recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrivateListIndividuals {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordID: ").append(toIndentedString(recordID)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    listKey: ").append(toIndentedString(listKey)).append("\n");
    sb.append("    listSubKey: ").append(toIndentedString(listSubKey)).append("\n");
    sb.append("    listRecordType: ").append(toIndentedString(listRecordType)).append("\n");
    sb.append("    listRecordOrigin: ").append(toIndentedString(listRecordOrigin)).append("\n");
    sb.append("    listRecordID: ").append(toIndentedString(listRecordID)).append("\n");
    sb.append("    listRecordSubID: ").append(toIndentedString(listRecordSubID)).append("\n");
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
    sb.append("    linkedRelationships: ").append(toIndentedString(linkedRelationships)).append("\n");
    sb.append("    riskScore: ").append(toIndentedString(riskScore)).append("\n");
    sb.append("    pEPRiskScore: ").append(toIndentedString(pEPRiskScore)).append("\n");
    sb.append("    dataConfidenceScore: ").append(toIndentedString(dataConfidenceScore)).append("\n");
    sb.append("    dataConfidenceComment: ").append(toIndentedString(dataConfidenceComment)).append("\n");
    sb.append("    inactiveFlag: ").append(toIndentedString(inactiveFlag)).append("\n");
    sb.append("    inactiveSinceDate: ").append(toIndentedString(inactiveSinceDate)).append("\n");
    sb.append("    pEPClassification: ").append(toIndentedString(pEPClassification)).append("\n");
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    edqAddressType: ").append(toIndentedString(edqAddressType)).append("\n");
    sb.append("    edqCurrentAddress: ").append(toIndentedString(edqCurrentAddress)).append("\n");
    sb.append("    edqTelephoneNumber: ").append(toIndentedString(edqTelephoneNumber)).append("\n");
    sb.append("    edqCellNumberBusiness: ").append(toIndentedString(edqCellNumberBusiness)).append("\n");
    sb.append("    edqCellNumberPersonal: ").append(toIndentedString(edqCellNumberPersonal)).append("\n");
    sb.append("    edqTaxNumber: ").append(toIndentedString(edqTaxNumber)).append("\n");
    sb.append("    edqDrivingLicence: ").append(toIndentedString(edqDrivingLicence)).append("\n");
    sb.append("    edqEmailAddress: ").append(toIndentedString(edqEmailAddress)).append("\n");
    sb.append("    edqNINumber: ").append(toIndentedString(edqNINumber)).append("\n");
    sb.append("    edqSCIONREF: ").append(toIndentedString(edqSCIONREF)).append("\n");
    sb.append("    edqListCustomerType: ").append(toIndentedString(edqListCustomerType)).append("\n");
    sb.append("    edqSuffix: ").append(toIndentedString(edqSuffix)).append("\n");
    sb.append("    edqRole: ").append(toIndentedString(edqRole)).append("\n");
    sb.append("    edqListCategory: ").append(toIndentedString(edqListCategory)).append("\n");
    sb.append("    edqFurtherInfo: ").append(toIndentedString(edqFurtherInfo)).append("\n");
    sb.append("    edqSSN: ").append(toIndentedString(edqSSN)).append("\n");
    sb.append("    edqCustomerID: ").append(toIndentedString(edqCustomerID)).append("\n");
    sb.append("    edqListName: ").append(toIndentedString(edqListName)).append("\n");
    sb.append("    edqWatchListType: ").append(toIndentedString(edqWatchListType)).append("\n");
    sb.append("    middleName1Derived: ").append(toIndentedString(middleName1Derived)).append("\n");
    sb.append("    middleName2Derived: ").append(toIndentedString(middleName2Derived)).append("\n");
    sb.append("    middleName1Original: ").append(toIndentedString(middleName1Original)).append("\n");
    sb.append("    middleName2Original: ").append(toIndentedString(middleName2Original)).append("\n");
    sb.append("    currentAddress: ").append(toIndentedString(currentAddress)).append("\n");
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
