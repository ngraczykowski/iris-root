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
 * WorldCheckIndividuals
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class WorldCheckIndividuals   {
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

  @JsonProperty("state")
  private String state = null;

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

  @JsonProperty("category")
  private String category = null;

  @JsonProperty("externalSources")
  private String externalSources = null;

  @JsonProperty("linkedTo")
  private String linkedTo = null;

  @JsonProperty("companies")
  private String companies = null;

  @JsonProperty("dateOfBirthOriginal")
  private String dateOfBirthOriginal = null;

  @JsonProperty("yearOfBirthApproximated")
  private String yearOfBirthApproximated = null;

  @JsonProperty("placeOfBirthOriginal")
  private String placeOfBirthOriginal = null;

  @JsonProperty("furtherInformation")
  private String furtherInformation = null;

  @JsonProperty("countriesOriginal")
  private String countriesOriginal = null;

  @JsonProperty("socialSecurityNumber")
  private String socialSecurityNumber = null;

  @JsonProperty("passportCountry")
  private String passportCountry = null;

  @JsonProperty("subCategoryDescription")
  private String subCategoryDescription = null;

  @JsonProperty("updateCategory")
  private String updateCategory = null;

  @JsonProperty("nativeAliasLanguageName")
  private String nativeAliasLanguageName = null;

  @JsonProperty("nativeAliasLanguageCountry")
  private String nativeAliasLanguageCountry = null;

  @JsonProperty("cachedExternalSources")
  private String cachedExternalSources = null;

  @JsonProperty("addedDate")
  private String addedDate = null;

  @JsonProperty("lastUpdatedDate")
  private String lastUpdatedDate = null;

  @JsonProperty("edqOWSWatchlistName")
  private String edqOWSWatchlistName = null;

  @JsonProperty("dOBs")
  private String dOBs = null;

  @JsonProperty("iDNumbers")
  private String iDNumbers = null;

  @JsonProperty("recordType")
  private String recordType = null;

  @JsonProperty("dummy")
  private String dummy = null;

  public WorldCheckIndividuals caseId(Integer caseId) {
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

  public WorldCheckIndividuals recordID(BigDecimal recordID) {
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

  public WorldCheckIndividuals inputStream(String inputStream) {
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

  public WorldCheckIndividuals listKey(String listKey) {
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

  public WorldCheckIndividuals listSubKey(String listSubKey) {
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

  public WorldCheckIndividuals listRecordType(String listRecordType) {
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

  public WorldCheckIndividuals listRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
    return this;
  }

  /**
   * List the origin where the World-Check record was derived e.g. OFAC
   * @return listRecordOrigin
   **/
  @Schema(description = "List the origin where the World-Check record was derived e.g. OFAC")
  
    public String getListRecordOrigin() {
    return listRecordOrigin;
  }

  public void setListRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
  }

  public WorldCheckIndividuals listRecordID(String listRecordID) {
    this.listRecordID = listRecordID;
    return this;
  }

  /**
   * Unique Identifier assigned to the World-Check record
   * @return listRecordID
   **/
  @Schema(description = "Unique Identifier assigned to the World-Check record")
  
    public String getListRecordID() {
    return listRecordID;
  }

  public void setListRecordID(String listRecordID) {
    this.listRecordID = listRecordID;
  }

  public WorldCheckIndividuals listRecordSubID(String listRecordSubID) {
    this.listRecordSubID = listRecordSubID;
    return this;
  }

  /**
   * ID for the sub records within a World-Check record
   * @return listRecordSubID
   **/
  @Schema(description = "ID for the sub records within a World-Check record")
  
    public String getListRecordSubID() {
    return listRecordSubID;
  }

  public void setListRecordSubID(String listRecordSubID) {
    this.listRecordSubID = listRecordSubID;
  }

  public WorldCheckIndividuals passportNumber(String passportNumber) {
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

  public WorldCheckIndividuals nationalId(String nationalId) {
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

  public WorldCheckIndividuals title(String title) {
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

  public WorldCheckIndividuals fullNameOriginal(String fullNameOriginal) {
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

  public WorldCheckIndividuals givenNamesOriginal(String givenNamesOriginal) {
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

  public WorldCheckIndividuals familyNameOriginal(String familyNameOriginal) {
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

  public WorldCheckIndividuals fullNameDerived(String fullNameDerived) {
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

  public WorldCheckIndividuals givenNamesDerived(String givenNamesDerived) {
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

  public WorldCheckIndividuals familyNameDerived(String familyNameDerived) {
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

  public WorldCheckIndividuals nameType(String nameType) {
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

  public WorldCheckIndividuals nameQuality(String nameQuality) {
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

  public WorldCheckIndividuals primaryName(String primaryName) {
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

  public WorldCheckIndividuals originalScriptName(String originalScriptName) {
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

  public WorldCheckIndividuals gender(String gender) {
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

  public WorldCheckIndividuals genderDerivedFlag(String genderDerivedFlag) {
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

  public WorldCheckIndividuals dateOfBirth(String dateOfBirth) {
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

  public WorldCheckIndividuals yearOfBirth(Integer yearOfBirth) {
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

  public WorldCheckIndividuals deceasedFlag(String deceasedFlag) {
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

  public WorldCheckIndividuals deceasedDate(String deceasedDate) {
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

  public WorldCheckIndividuals occupation(String occupation) {
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

  public WorldCheckIndividuals address(String address) {
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

  public WorldCheckIndividuals city(String city) {
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

  public WorldCheckIndividuals state(String state) {
    this.state = state;
    return this;
  }

  /**
   * Watchlist Entry's state (if known)
   * @return state
   **/
  @Schema(description = "Watchlist Entry's state (if known)")
  
    public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public WorldCheckIndividuals postalCode(String postalCode) {
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

  public WorldCheckIndividuals addressCountry(String addressCountry) {
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

  public WorldCheckIndividuals residencyCountry(String residencyCountry) {
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

  public WorldCheckIndividuals countryOfBirth(String countryOfBirth) {
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

  public WorldCheckIndividuals nationalities(String nationalities) {
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

  public WorldCheckIndividuals countryCodesAll(String countryCodesAll) {
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

  public WorldCheckIndividuals countriesAll(String countriesAll) {
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

  public WorldCheckIndividuals profileHyperlink(String profileHyperlink) {
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

  public WorldCheckIndividuals searchHyperlink(String searchHyperlink) {
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

  public WorldCheckIndividuals linkedProfiles(String linkedProfiles) {
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

  public WorldCheckIndividuals linkedRelationships(String linkedRelationships) {
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

  public WorldCheckIndividuals riskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
    return this;
  }

  /**
   * World-Check assigned risk score
   * @return riskScore
   **/
  @Schema(description = "World-Check assigned risk score")
  
    @Valid
    public BigDecimal getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
  }

  public WorldCheckIndividuals pEPRiskScore(BigDecimal pEPRiskScore) {
    this.pEPRiskScore = pEPRiskScore;
    return this;
  }

  /**
   * World-Check assigned PEP risk score
   * @return pEPRiskScore
   **/
  @Schema(description = "World-Check assigned PEP risk score")
  
    @Valid
    public BigDecimal getPEPRiskScore() {
    return pEPRiskScore;
  }

  public void setPEPRiskScore(BigDecimal pEPRiskScore) {
    this.pEPRiskScore = pEPRiskScore;
  }

  public WorldCheckIndividuals dataConfidenceScore(BigDecimal dataConfidenceScore) {
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

  public WorldCheckIndividuals dataConfidenceComment(String dataConfidenceComment) {
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

  public WorldCheckIndividuals inactiveFlag(String inactiveFlag) {
    this.inactiveFlag = inactiveFlag;
    return this;
  }

  /**
   * Denotes if the record is considered inactive on World-Check
   * @return inactiveFlag
   **/
  @Schema(description = "Denotes if the record is considered inactive on World-Check")
  
    public String getInactiveFlag() {
    return inactiveFlag;
  }

  public void setInactiveFlag(String inactiveFlag) {
    this.inactiveFlag = inactiveFlag;
  }

  public WorldCheckIndividuals inactiveSinceDate(String inactiveSinceDate) {
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

  public WorldCheckIndividuals pEPClassification(String pEPClassification) {
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

  public WorldCheckIndividuals category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Category of PEP, for example&#58; Local Government
   * @return category
   **/
  @Schema(description = "Category of PEP, for example&#58; Local Government")
  
    public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public WorldCheckIndividuals externalSources(String externalSources) {
    this.externalSources = externalSources;
    return this;
  }

  /**
   * Records any external sources used to collate information about the Watchlist Entry
   * @return externalSources
   **/
  @Schema(description = "Records any external sources used to collate information about the Watchlist Entry")
  
    public String getExternalSources() {
    return externalSources;
  }

  public void setExternalSources(String externalSources) {
    this.externalSources = externalSources;
  }

  public WorldCheckIndividuals linkedTo(String linkedTo) {
    this.linkedTo = linkedTo;
    return this;
  }

  /**
   * Associated parties ID
   * @return linkedTo
   **/
  @Schema(description = "Associated parties ID")
  
    public String getLinkedTo() {
    return linkedTo;
  }

  public void setLinkedTo(String linkedTo) {
    this.linkedTo = linkedTo;
  }

  public WorldCheckIndividuals companies(String companies) {
    this.companies = companies;
    return this;
  }

  /**
   * Companies associated with the Watchlist Entry (if known)
   * @return companies
   **/
  @Schema(description = "Companies associated with the Watchlist Entry (if known)")
  
    public String getCompanies() {
    return companies;
  }

  public void setCompanies(String companies) {
    this.companies = companies;
  }

  public WorldCheckIndividuals dateOfBirthOriginal(String dateOfBirthOriginal) {
    this.dateOfBirthOriginal = dateOfBirthOriginal;
    return this;
  }

  /**
   * The date of birth as originally provided
   * @return dateOfBirthOriginal
   **/
  @Schema(description = "The date of birth as originally provided")
  
    public String getDateOfBirthOriginal() {
    return dateOfBirthOriginal;
  }

  public void setDateOfBirthOriginal(String dateOfBirthOriginal) {
    this.dateOfBirthOriginal = dateOfBirthOriginal;
  }

  public WorldCheckIndividuals yearOfBirthApproximated(String yearOfBirthApproximated) {
    this.yearOfBirthApproximated = yearOfBirthApproximated;
    return this;
  }

  /**
   * Year of birth approximation
   * @return yearOfBirthApproximated
   **/
  @Schema(description = "Year of birth approximation")
  
    public String getYearOfBirthApproximated() {
    return yearOfBirthApproximated;
  }

  public void setYearOfBirthApproximated(String yearOfBirthApproximated) {
    this.yearOfBirthApproximated = yearOfBirthApproximated;
  }

  public WorldCheckIndividuals placeOfBirthOriginal(String placeOfBirthOriginal) {
    this.placeOfBirthOriginal = placeOfBirthOriginal;
    return this;
  }

  /**
   * Place of birth details as originally provided
   * @return placeOfBirthOriginal
   **/
  @Schema(description = "Place of birth details as originally provided")
  
    public String getPlaceOfBirthOriginal() {
    return placeOfBirthOriginal;
  }

  public void setPlaceOfBirthOriginal(String placeOfBirthOriginal) {
    this.placeOfBirthOriginal = placeOfBirthOriginal;
  }

  public WorldCheckIndividuals furtherInformation(String furtherInformation) {
    this.furtherInformation = furtherInformation;
    return this;
  }

  /**
   * Any further information as deemed useful by World-Check
   * @return furtherInformation
   **/
  @Schema(description = "Any further information as deemed useful by World-Check")
  
    public String getFurtherInformation() {
    return furtherInformation;
  }

  public void setFurtherInformation(String furtherInformation) {
    this.furtherInformation = furtherInformation;
  }

  public WorldCheckIndividuals countriesOriginal(String countriesOriginal) {
    this.countriesOriginal = countriesOriginal;
    return this;
  }

  /**
   * Associated country information as originally provided
   * @return countriesOriginal
   **/
  @Schema(description = "Associated country information as originally provided")
  
    public String getCountriesOriginal() {
    return countriesOriginal;
  }

  public void setCountriesOriginal(String countriesOriginal) {
    this.countriesOriginal = countriesOriginal;
  }

  public WorldCheckIndividuals socialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
    return this;
  }

  /**
   * Social Security number
   * @return socialSecurityNumber
   **/
  @Schema(description = "Social Security number")
  
    public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  public WorldCheckIndividuals passportCountry(String passportCountry) {
    this.passportCountry = passportCountry;
    return this;
  }

  /**
   * Passport country
   * @return passportCountry
   **/
  @Schema(description = "Passport country")
  
    public String getPassportCountry() {
    return passportCountry;
  }

  public void setPassportCountry(String passportCountry) {
    this.passportCountry = passportCountry;
  }

  public WorldCheckIndividuals subCategoryDescription(String subCategoryDescription) {
    this.subCategoryDescription = subCategoryDescription;
    return this;
  }

  /**
   * Groups PEPs according to predefined categories
   * @return subCategoryDescription
   **/
  @Schema(description = "Groups PEPs according to predefined categories")
  
    public String getSubCategoryDescription() {
    return subCategoryDescription;
  }

  public void setSubCategoryDescription(String subCategoryDescription) {
    this.subCategoryDescription = subCategoryDescription;
  }

  public WorldCheckIndividuals updateCategory(String updateCategory) {
    this.updateCategory = updateCategory;
    return this;
  }

  /**
   * Indicates the type ofchange last made, highlighting the significance of each update.
   * @return updateCategory
   **/
  @Schema(description = "Indicates the type ofchange last made, highlighting the significance of each update.")
  
    public String getUpdateCategory() {
    return updateCategory;
  }

  public void setUpdateCategory(String updateCategory) {
    this.updateCategory = updateCategory;
  }

  public WorldCheckIndividuals nativeAliasLanguageName(String nativeAliasLanguageName) {
    this.nativeAliasLanguageName = nativeAliasLanguageName;
    return this;
  }

  /**
   * Name in non-Latin character set
   * @return nativeAliasLanguageName
   **/
  @Schema(description = "Name in non-Latin character set")
  
    public String getNativeAliasLanguageName() {
    return nativeAliasLanguageName;
  }

  public void setNativeAliasLanguageName(String nativeAliasLanguageName) {
    this.nativeAliasLanguageName = nativeAliasLanguageName;
  }

  public WorldCheckIndividuals nativeAliasLanguageCountry(String nativeAliasLanguageCountry) {
    this.nativeAliasLanguageCountry = nativeAliasLanguageCountry;
    return this;
  }

  /**
   * Country in non-Latin character set
   * @return nativeAliasLanguageCountry
   **/
  @Schema(description = "Country in non-Latin character set")
  
    public String getNativeAliasLanguageCountry() {
    return nativeAliasLanguageCountry;
  }

  public void setNativeAliasLanguageCountry(String nativeAliasLanguageCountry) {
    this.nativeAliasLanguageCountry = nativeAliasLanguageCountry;
  }

  public WorldCheckIndividuals cachedExternalSources(String cachedExternalSources) {
    this.cachedExternalSources = cachedExternalSources;
    return this;
  }

  /**
   * Records any external sources used to collate information about the Watchlist Entry
   * @return cachedExternalSources
   **/
  @Schema(description = "Records any external sources used to collate information about the Watchlist Entry")
  
    public String getCachedExternalSources() {
    return cachedExternalSources;
  }

  public void setCachedExternalSources(String cachedExternalSources) {
    this.cachedExternalSources = cachedExternalSources;
  }

  public WorldCheckIndividuals addedDate(String addedDate) {
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

  public WorldCheckIndividuals lastUpdatedDate(String lastUpdatedDate) {
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

  public WorldCheckIndividuals edqOWSWatchlistName(String edqOWSWatchlistName) {
    this.edqOWSWatchlistName = edqOWSWatchlistName;
    return this;
  }

  /**
   * Indicates the watchlist origin using the HSBC name specification
   * @return edqOWSWatchlistName
   **/
  @Schema(description = "Indicates the watchlist origin using the HSBC name specification")
  
    public String getEdqOWSWatchlistName() {
    return edqOWSWatchlistName;
  }

  public void setEdqOWSWatchlistName(String edqOWSWatchlistName) {
    this.edqOWSWatchlistName = edqOWSWatchlistName;
  }

  public WorldCheckIndividuals dOBs(String dOBs) {
    this.dOBs = dOBs;
    return this;
  }

  /**
   * List of DOBs. This is used where the DOB is not certain or there are multiple DOBs associated to a record.
   * @return dOBs
   **/
  @Schema(description = "List of DOBs. This is used where the DOB is not certain or there are multiple DOBs associated to a record.")
  
    public String getDOBs() {
    return dOBs;
  }

  public void setDOBs(String dOBs) {
    this.dOBs = dOBs;
  }

  public WorldCheckIndividuals iDNumbers(String iDNumbers) {
    this.iDNumbers = iDNumbers;
    return this;
  }

  /**
   * Identification Numbers
   * @return iDNumbers
   **/
  @Schema(description = "Identification Numbers")
  
    public String getIDNumbers() {
    return iDNumbers;
  }

  public void setIDNumbers(String iDNumbers) {
    this.iDNumbers = iDNumbers;
  }

  public WorldCheckIndividuals recordType(String recordType) {
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

  public WorldCheckIndividuals dummy(String dummy) {
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
    WorldCheckIndividuals worldCheckIndividuals = (WorldCheckIndividuals) o;
    return Objects.equals(this.caseId, worldCheckIndividuals.caseId) &&
        Objects.equals(this.recordID, worldCheckIndividuals.recordID) &&
        Objects.equals(this.inputStream, worldCheckIndividuals.inputStream) &&
        Objects.equals(this.listKey, worldCheckIndividuals.listKey) &&
        Objects.equals(this.listSubKey, worldCheckIndividuals.listSubKey) &&
        Objects.equals(this.listRecordType, worldCheckIndividuals.listRecordType) &&
        Objects.equals(this.listRecordOrigin, worldCheckIndividuals.listRecordOrigin) &&
        Objects.equals(this.listRecordID, worldCheckIndividuals.listRecordID) &&
        Objects.equals(this.listRecordSubID, worldCheckIndividuals.listRecordSubID) &&
        Objects.equals(this.passportNumber, worldCheckIndividuals.passportNumber) &&
        Objects.equals(this.nationalId, worldCheckIndividuals.nationalId) &&
        Objects.equals(this.title, worldCheckIndividuals.title) &&
        Objects.equals(this.fullNameOriginal, worldCheckIndividuals.fullNameOriginal) &&
        Objects.equals(this.givenNamesOriginal, worldCheckIndividuals.givenNamesOriginal) &&
        Objects.equals(this.familyNameOriginal, worldCheckIndividuals.familyNameOriginal) &&
        Objects.equals(this.fullNameDerived, worldCheckIndividuals.fullNameDerived) &&
        Objects.equals(this.givenNamesDerived, worldCheckIndividuals.givenNamesDerived) &&
        Objects.equals(this.familyNameDerived, worldCheckIndividuals.familyNameDerived) &&
        Objects.equals(this.nameType, worldCheckIndividuals.nameType) &&
        Objects.equals(this.nameQuality, worldCheckIndividuals.nameQuality) &&
        Objects.equals(this.primaryName, worldCheckIndividuals.primaryName) &&
        Objects.equals(this.originalScriptName, worldCheckIndividuals.originalScriptName) &&
        Objects.equals(this.gender, worldCheckIndividuals.gender) &&
        Objects.equals(this.genderDerivedFlag, worldCheckIndividuals.genderDerivedFlag) &&
        Objects.equals(this.dateOfBirth, worldCheckIndividuals.dateOfBirth) &&
        Objects.equals(this.yearOfBirth, worldCheckIndividuals.yearOfBirth) &&
        Objects.equals(this.deceasedFlag, worldCheckIndividuals.deceasedFlag) &&
        Objects.equals(this.deceasedDate, worldCheckIndividuals.deceasedDate) &&
        Objects.equals(this.occupation, worldCheckIndividuals.occupation) &&
        Objects.equals(this.address, worldCheckIndividuals.address) &&
        Objects.equals(this.city, worldCheckIndividuals.city) &&
        Objects.equals(this.state, worldCheckIndividuals.state) &&
        Objects.equals(this.postalCode, worldCheckIndividuals.postalCode) &&
        Objects.equals(this.addressCountry, worldCheckIndividuals.addressCountry) &&
        Objects.equals(this.residencyCountry, worldCheckIndividuals.residencyCountry) &&
        Objects.equals(this.countryOfBirth, worldCheckIndividuals.countryOfBirth) &&
        Objects.equals(this.nationalities, worldCheckIndividuals.nationalities) &&
        Objects.equals(this.countryCodesAll, worldCheckIndividuals.countryCodesAll) &&
        Objects.equals(this.countriesAll, worldCheckIndividuals.countriesAll) &&
        Objects.equals(this.profileHyperlink, worldCheckIndividuals.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, worldCheckIndividuals.searchHyperlink) &&
        Objects.equals(this.linkedProfiles, worldCheckIndividuals.linkedProfiles) &&
        Objects.equals(this.linkedRelationships, worldCheckIndividuals.linkedRelationships) &&
        Objects.equals(this.riskScore, worldCheckIndividuals.riskScore) &&
        Objects.equals(this.pEPRiskScore, worldCheckIndividuals.pEPRiskScore) &&
        Objects.equals(this.dataConfidenceScore, worldCheckIndividuals.dataConfidenceScore) &&
        Objects.equals(this.dataConfidenceComment, worldCheckIndividuals.dataConfidenceComment) &&
        Objects.equals(this.inactiveFlag, worldCheckIndividuals.inactiveFlag) &&
        Objects.equals(this.inactiveSinceDate, worldCheckIndividuals.inactiveSinceDate) &&
        Objects.equals(this.pEPClassification, worldCheckIndividuals.pEPClassification) &&
        Objects.equals(this.category, worldCheckIndividuals.category) &&
        Objects.equals(this.externalSources, worldCheckIndividuals.externalSources) &&
        Objects.equals(this.linkedTo, worldCheckIndividuals.linkedTo) &&
        Objects.equals(this.companies, worldCheckIndividuals.companies) &&
        Objects.equals(this.dateOfBirthOriginal, worldCheckIndividuals.dateOfBirthOriginal) &&
        Objects.equals(this.yearOfBirthApproximated, worldCheckIndividuals.yearOfBirthApproximated) &&
        Objects.equals(this.placeOfBirthOriginal, worldCheckIndividuals.placeOfBirthOriginal) &&
        Objects.equals(this.furtherInformation, worldCheckIndividuals.furtherInformation) &&
        Objects.equals(this.countriesOriginal, worldCheckIndividuals.countriesOriginal) &&
        Objects.equals(this.socialSecurityNumber, worldCheckIndividuals.socialSecurityNumber) &&
        Objects.equals(this.passportCountry, worldCheckIndividuals.passportCountry) &&
        Objects.equals(this.subCategoryDescription, worldCheckIndividuals.subCategoryDescription) &&
        Objects.equals(this.updateCategory, worldCheckIndividuals.updateCategory) &&
        Objects.equals(this.nativeAliasLanguageName, worldCheckIndividuals.nativeAliasLanguageName) &&
        Objects.equals(this.nativeAliasLanguageCountry, worldCheckIndividuals.nativeAliasLanguageCountry) &&
        Objects.equals(this.cachedExternalSources, worldCheckIndividuals.cachedExternalSources) &&
        Objects.equals(this.addedDate, worldCheckIndividuals.addedDate) &&
        Objects.equals(this.lastUpdatedDate, worldCheckIndividuals.lastUpdatedDate) &&
        Objects.equals(this.edqOWSWatchlistName, worldCheckIndividuals.edqOWSWatchlistName) &&
        Objects.equals(this.dOBs, worldCheckIndividuals.dOBs) &&
        Objects.equals(this.iDNumbers, worldCheckIndividuals.iDNumbers) &&
        Objects.equals(this.recordType, worldCheckIndividuals.recordType) &&
        Objects.equals(this.dummy, worldCheckIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordID, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin, listRecordID, listRecordSubID, passportNumber, nationalId, title, fullNameOriginal, givenNamesOriginal, familyNameOriginal, fullNameDerived, givenNamesDerived, familyNameDerived, nameType, nameQuality, primaryName, originalScriptName, gender, genderDerivedFlag, dateOfBirth, yearOfBirth, deceasedFlag, deceasedDate, occupation, address, city, state, postalCode, addressCountry, residencyCountry, countryOfBirth, nationalities, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink, linkedProfiles, linkedRelationships, riskScore, pEPRiskScore, dataConfidenceScore, dataConfidenceComment, inactiveFlag, inactiveSinceDate, pEPClassification, category, externalSources, linkedTo, companies, dateOfBirthOriginal, yearOfBirthApproximated, placeOfBirthOriginal, furtherInformation, countriesOriginal, socialSecurityNumber, passportCountry, subCategoryDescription, updateCategory, nativeAliasLanguageName, nativeAliasLanguageCountry, cachedExternalSources, addedDate, lastUpdatedDate, edqOWSWatchlistName, dOBs, iDNumbers, recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldCheckIndividuals {\n");
    
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
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
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
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    externalSources: ").append(toIndentedString(externalSources)).append("\n");
    sb.append("    linkedTo: ").append(toIndentedString(linkedTo)).append("\n");
    sb.append("    companies: ").append(toIndentedString(companies)).append("\n");
    sb.append("    dateOfBirthOriginal: ").append(toIndentedString(dateOfBirthOriginal)).append("\n");
    sb.append("    yearOfBirthApproximated: ").append(toIndentedString(yearOfBirthApproximated)).append("\n");
    sb.append("    placeOfBirthOriginal: ").append(toIndentedString(placeOfBirthOriginal)).append("\n");
    sb.append("    furtherInformation: ").append(toIndentedString(furtherInformation)).append("\n");
    sb.append("    countriesOriginal: ").append(toIndentedString(countriesOriginal)).append("\n");
    sb.append("    socialSecurityNumber: ").append(toIndentedString(socialSecurityNumber)).append("\n");
    sb.append("    passportCountry: ").append(toIndentedString(passportCountry)).append("\n");
    sb.append("    subCategoryDescription: ").append(toIndentedString(subCategoryDescription)).append("\n");
    sb.append("    updateCategory: ").append(toIndentedString(updateCategory)).append("\n");
    sb.append("    nativeAliasLanguageName: ").append(toIndentedString(nativeAliasLanguageName)).append("\n");
    sb.append("    nativeAliasLanguageCountry: ").append(toIndentedString(nativeAliasLanguageCountry)).append("\n");
    sb.append("    cachedExternalSources: ").append(toIndentedString(cachedExternalSources)).append("\n");
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    edqOWSWatchlistName: ").append(toIndentedString(edqOWSWatchlistName)).append("\n");
    sb.append("    dOBs: ").append(toIndentedString(dOBs)).append("\n");
    sb.append("    iDNumbers: ").append(toIndentedString(iDNumbers)).append("\n");
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
