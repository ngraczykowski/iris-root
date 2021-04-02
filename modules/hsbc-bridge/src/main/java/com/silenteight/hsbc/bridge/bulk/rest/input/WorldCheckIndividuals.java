package com.silenteight.hsbc.bridge.bulk.rest.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class WorldCheckIndividuals {

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
  private String state = null;
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
  private String pepClassification = null;
  private String category = null;
  private String externalSources = null;
  private String linkedTo = null;
  private String companies = null;
  private String dateOfBirthOriginal = null;
  private String yearOfBirthApproximated = null;
  private String placeOfBirthOriginal = null;
  private String furtherInformation = null;
  private String countriesOriginal = null;
  private String socialSecurityNumber = null;
  private String passportCountry = null;
  private String subCategoryDescription = null;
  private String updateCategory = null;
  private String nativeAliasLanguageName = null;
  private String nativeAliasLanguageCountry = null;
  private String cachedExternalSources = null;
  private String addedDate = null;
  private String lastUpdatedDate = null;
  private String edqOwsWatchlistName = null;
  private String dobs = null;
  private String idNumbers = null;
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
   * List the origin where the World-Check record was derived e.g. OFAC
   **/

  @Schema(description = "List the origin where the World-Check record was derived e.g. OFAC")
  @JsonProperty("listRecordOrigin")
  public String getListRecordOrigin() {
    return listRecordOrigin;
  }

  public void setListRecordOrigin(String listRecordOrigin) {
    this.listRecordOrigin = listRecordOrigin;
  }

  /**
   * Unique Identifier assigned to the World-Check record
   **/

  @Schema(description = "Unique Identifier assigned to the World-Check record")
  @JsonProperty("listRecordId")
  public String getListRecordId() {
    return listRecordId;
  }

  public void setListRecordId(String listRecordId) {
    this.listRecordId = listRecordId;
  }

  /**
   * ID for the sub records within a World-Check record
   **/

  @Schema(description = "ID for the sub records within a World-Check record")
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
   * Watchlist Entry&#x27;s state (if known)
   **/

  @Schema(description = "Watchlist Entry's state (if known)")
  @JsonProperty("state")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
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
   * World-Check assigned risk score
   **/

  @Schema(description = "World-Check assigned risk score")
  @JsonProperty("riskScore")
  public BigDecimal getRiskScore() {
    return riskScore;
  }

  public void setRiskScore(BigDecimal riskScore) {
    this.riskScore = riskScore;
  }

  /**
   * World-Check assigned PEP risk score
   **/

  @Schema(description = "World-Check assigned PEP risk score")
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
   * Denotes if the record is considered inactive on World-Check
   **/

  @Schema(description = "Denotes if the record is considered inactive on World-Check")
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
  @JsonProperty("pepClassification")
  public String getPepClassification() {
    return pepClassification;
  }

  public void setPepClassification(String pepClassification) {
    this.pepClassification = pepClassification;
  }

  /**
   * Category of PEP, for example&amp;#58; Local Government
   **/

  @Schema(description = "Category of PEP, for example&#58; Local Government")
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Records any external sources used to collate information about the Watchlist Entry
   **/

  @Schema(description = "Records any external sources used to collate information about the Watchlist Entry")
  @JsonProperty("externalSources")
  public String getExternalSources() {
    return externalSources;
  }

  public void setExternalSources(String externalSources) {
    this.externalSources = externalSources;
  }

  /**
   * Associated parties ID
   **/

  @Schema(description = "Associated parties ID")
  @JsonProperty("linkedTo")
  public String getLinkedTo() {
    return linkedTo;
  }

  public void setLinkedTo(String linkedTo) {
    this.linkedTo = linkedTo;
  }

  /**
   * Companies associated with the Watchlist Entry (if known)
   **/

  @Schema(description = "Companies associated with the Watchlist Entry (if known)")
  @JsonProperty("companies")
  public String getCompanies() {
    return companies;
  }

  public void setCompanies(String companies) {
    this.companies = companies;
  }

  /**
   * The date of birth as originally provided
   **/

  @Schema(description = "The date of birth as originally provided")
  @JsonProperty("dateOfBirthOriginal")
  public String getDateOfBirthOriginal() {
    return dateOfBirthOriginal;
  }

  public void setDateOfBirthOriginal(String dateOfBirthOriginal) {
    this.dateOfBirthOriginal = dateOfBirthOriginal;
  }

  /**
   * Year of birth approximation
   **/

  @Schema(description = "Year of birth approximation")
  @JsonProperty("yearOfBirthApproximated")
  public String getYearOfBirthApproximated() {
    return yearOfBirthApproximated;
  }

  public void setYearOfBirthApproximated(String yearOfBirthApproximated) {
    this.yearOfBirthApproximated = yearOfBirthApproximated;
  }

  /**
   * Place of birth details as originally provided
   **/

  @Schema(description = "Place of birth details as originally provided")
  @JsonProperty("placeOfBirthOriginal")
  public String getPlaceOfBirthOriginal() {
    return placeOfBirthOriginal;
  }

  public void setPlaceOfBirthOriginal(String placeOfBirthOriginal) {
    this.placeOfBirthOriginal = placeOfBirthOriginal;
  }

  /**
   * Any further information as deemed useful by World-Check
   **/

  @Schema(description = "Any further information as deemed useful by World-Check")
  @JsonProperty("furtherInformation")
  public String getFurtherInformation() {
    return furtherInformation;
  }

  public void setFurtherInformation(String furtherInformation) {
    this.furtherInformation = furtherInformation;
  }

  /**
   * Associated country information as originally provided
   **/

  @Schema(description = "Associated country information as originally provided")
  @JsonProperty("countriesOriginal")
  public String getCountriesOriginal() {
    return countriesOriginal;
  }

  public void setCountriesOriginal(String countriesOriginal) {
    this.countriesOriginal = countriesOriginal;
  }

  /**
   * Social Security number
   **/

  @Schema(description = "Social Security number")
  @JsonProperty("socialSecurityNumber")
  public String getSocialSecurityNumber() {
    return socialSecurityNumber;
  }

  public void setSocialSecurityNumber(String socialSecurityNumber) {
    this.socialSecurityNumber = socialSecurityNumber;
  }

  /**
   * Passport country
   **/

  @Schema(description = "Passport country")
  @JsonProperty("passportCountry")
  public String getPassportCountry() {
    return passportCountry;
  }

  public void setPassportCountry(String passportCountry) {
    this.passportCountry = passportCountry;
  }

  /**
   * Groups PEPs according to predefined categories
   **/

  @Schema(description = "Groups PEPs according to predefined categories")
  @JsonProperty("subCategoryDescription")
  public String getSubCategoryDescription() {
    return subCategoryDescription;
  }

  public void setSubCategoryDescription(String subCategoryDescription) {
    this.subCategoryDescription = subCategoryDescription;
  }

  /**
   * Indicates the type ofchange last made, highlighting the significance of each update.
   **/

  @Schema(description = "Indicates the type ofchange last made, highlighting the significance of each update.")
  @JsonProperty("updateCategory")
  public String getUpdateCategory() {
    return updateCategory;
  }

  public void setUpdateCategory(String updateCategory) {
    this.updateCategory = updateCategory;
  }

  /**
   * Name in non-Latin character set
   **/

  @Schema(description = "Name in non-Latin character set")
  @JsonProperty("nativeAliasLanguageName")
  public String getNativeAliasLanguageName() {
    return nativeAliasLanguageName;
  }

  public void setNativeAliasLanguageName(String nativeAliasLanguageName) {
    this.nativeAliasLanguageName = nativeAliasLanguageName;
  }

  /**
   * Country in non-Latin character set
   **/

  @Schema(description = "Country in non-Latin character set")
  @JsonProperty("nativeAliasLanguageCountry")
  public String getNativeAliasLanguageCountry() {
    return nativeAliasLanguageCountry;
  }

  public void setNativeAliasLanguageCountry(String nativeAliasLanguageCountry) {
    this.nativeAliasLanguageCountry = nativeAliasLanguageCountry;
  }

  /**
   * Records any external sources used to collate information about the Watchlist Entry
   **/

  @Schema(description = "Records any external sources used to collate information about the Watchlist Entry")
  @JsonProperty("cachedExternalSources")
  public String getCachedExternalSources() {
    return cachedExternalSources;
  }

  public void setCachedExternalSources(String cachedExternalSources) {
    this.cachedExternalSources = cachedExternalSources;
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
   * Indicates the watchlist origin using the HSBC name specification
   **/

  @Schema(description = "Indicates the watchlist origin using the HSBC name specification")
  @JsonProperty("edqOwsWatchlistName")
  public String getEdqOwsWatchlistName() {
    return edqOwsWatchlistName;
  }

  public void setEdqOwsWatchlistName(String edqOwsWatchlistName) {
    this.edqOwsWatchlistName = edqOwsWatchlistName;
  }

  /**
   * List of DOBs. This is used where the DOB is not certain or there are multiple DOBs associated
   * to a record.
   **/

  @Schema(description = "List of DOBs. This is used where the DOB is not certain or there are multiple DOBs associated to a record.")
  @JsonProperty("dobs")
  public String getDobs() {
    return dobs;
  }

  public void setDobs(String dobs) {
    this.dobs = dobs;
  }

  /**
   * Identification Numbers
   **/

  @Schema(description = "Identification Numbers")
  @JsonProperty("idNumbers")
  public String getIdNumbers() {
    return idNumbers;
  }

  public void setIdNumbers(String idNumbers) {
    this.idNumbers = idNumbers;
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
    WorldCheckIndividuals worldCheckIndividuals = (WorldCheckIndividuals) o;
    return Objects.equals(caseId, worldCheckIndividuals.caseId) &&
        Objects.equals(recordId, worldCheckIndividuals.recordId) &&
        Objects.equals(inputStream, worldCheckIndividuals.inputStream) &&
        Objects.equals(listKey, worldCheckIndividuals.listKey) &&
        Objects.equals(listSubKey, worldCheckIndividuals.listSubKey) &&
        Objects.equals(listRecordType, worldCheckIndividuals.listRecordType) &&
        Objects.equals(listRecordOrigin, worldCheckIndividuals.listRecordOrigin) &&
        Objects.equals(listRecordId, worldCheckIndividuals.listRecordId) &&
        Objects.equals(listRecordSubId, worldCheckIndividuals.listRecordSubId) &&
        Objects.equals(passportNumber, worldCheckIndividuals.passportNumber) &&
        Objects.equals(nationalId, worldCheckIndividuals.nationalId) &&
        Objects.equals(title, worldCheckIndividuals.title) &&
        Objects.equals(fullNameOriginal, worldCheckIndividuals.fullNameOriginal) &&
        Objects.equals(givenNamesOriginal, worldCheckIndividuals.givenNamesOriginal) &&
        Objects.equals(familyNameOriginal, worldCheckIndividuals.familyNameOriginal) &&
        Objects.equals(fullNameDerived, worldCheckIndividuals.fullNameDerived) &&
        Objects.equals(givenNamesDerived, worldCheckIndividuals.givenNamesDerived) &&
        Objects.equals(familyNameDerived, worldCheckIndividuals.familyNameDerived) &&
        Objects.equals(nameType, worldCheckIndividuals.nameType) &&
        Objects.equals(nameQuality, worldCheckIndividuals.nameQuality) &&
        Objects.equals(primaryName, worldCheckIndividuals.primaryName) &&
        Objects.equals(originalScriptName, worldCheckIndividuals.originalScriptName) &&
        Objects.equals(gender, worldCheckIndividuals.gender) &&
        Objects.equals(genderDerivedFlag, worldCheckIndividuals.genderDerivedFlag) &&
        Objects.equals(dateOfBirth, worldCheckIndividuals.dateOfBirth) &&
        Objects.equals(yearOfBirth, worldCheckIndividuals.yearOfBirth) &&
        Objects.equals(deceasedFlag, worldCheckIndividuals.deceasedFlag) &&
        Objects.equals(deceasedDate, worldCheckIndividuals.deceasedDate) &&
        Objects.equals(occupation, worldCheckIndividuals.occupation) &&
        Objects.equals(address, worldCheckIndividuals.address) &&
        Objects.equals(city, worldCheckIndividuals.city) &&
        Objects.equals(state, worldCheckIndividuals.state) &&
        Objects.equals(postalCode, worldCheckIndividuals.postalCode) &&
        Objects.equals(addressCountry, worldCheckIndividuals.addressCountry) &&
        Objects.equals(residencyCountry, worldCheckIndividuals.residencyCountry) &&
        Objects.equals(countryOfBirth, worldCheckIndividuals.countryOfBirth) &&
        Objects.equals(nationalities, worldCheckIndividuals.nationalities) &&
        Objects.equals(countryCodesAll, worldCheckIndividuals.countryCodesAll) &&
        Objects.equals(countriesAll, worldCheckIndividuals.countriesAll) &&
        Objects.equals(profileHyperlink, worldCheckIndividuals.profileHyperlink) &&
        Objects.equals(searchHyperlink, worldCheckIndividuals.searchHyperlink) &&
        Objects.equals(linkedProfiles, worldCheckIndividuals.linkedProfiles) &&
        Objects.equals(linkedRelationships, worldCheckIndividuals.linkedRelationships) &&
        Objects.equals(riskScore, worldCheckIndividuals.riskScore) &&
        Objects.equals(pepRiskScore, worldCheckIndividuals.pepRiskScore) &&
        Objects.equals(dataConfidenceScore, worldCheckIndividuals.dataConfidenceScore) &&
        Objects.equals(dataConfidenceComment, worldCheckIndividuals.dataConfidenceComment) &&
        Objects.equals(inactiveFlag, worldCheckIndividuals.inactiveFlag) &&
        Objects.equals(inactiveSinceDate, worldCheckIndividuals.inactiveSinceDate) &&
        Objects.equals(pepClassification, worldCheckIndividuals.pepClassification) &&
        Objects.equals(category, worldCheckIndividuals.category) &&
        Objects.equals(externalSources, worldCheckIndividuals.externalSources) &&
        Objects.equals(linkedTo, worldCheckIndividuals.linkedTo) &&
        Objects.equals(companies, worldCheckIndividuals.companies) &&
        Objects.equals(dateOfBirthOriginal, worldCheckIndividuals.dateOfBirthOriginal) &&
        Objects.equals(yearOfBirthApproximated, worldCheckIndividuals.yearOfBirthApproximated) &&
        Objects.equals(placeOfBirthOriginal, worldCheckIndividuals.placeOfBirthOriginal) &&
        Objects.equals(furtherInformation, worldCheckIndividuals.furtherInformation) &&
        Objects.equals(countriesOriginal, worldCheckIndividuals.countriesOriginal) &&
        Objects.equals(socialSecurityNumber, worldCheckIndividuals.socialSecurityNumber) &&
        Objects.equals(passportCountry, worldCheckIndividuals.passportCountry) &&
        Objects.equals(subCategoryDescription, worldCheckIndividuals.subCategoryDescription) &&
        Objects.equals(updateCategory, worldCheckIndividuals.updateCategory) &&
        Objects.equals(nativeAliasLanguageName, worldCheckIndividuals.nativeAliasLanguageName) &&
        Objects.equals(nativeAliasLanguageCountry, worldCheckIndividuals.nativeAliasLanguageCountry)
        &&
        Objects.equals(cachedExternalSources, worldCheckIndividuals.cachedExternalSources) &&
        Objects.equals(addedDate, worldCheckIndividuals.addedDate) &&
        Objects.equals(lastUpdatedDate, worldCheckIndividuals.lastUpdatedDate) &&
        Objects.equals(edqOwsWatchlistName, worldCheckIndividuals.edqOwsWatchlistName) &&
        Objects.equals(dobs, worldCheckIndividuals.dobs) &&
        Objects.equals(idNumbers, worldCheckIndividuals.idNumbers) &&
        Objects.equals(recordType, worldCheckIndividuals.recordType) &&
        Objects.equals(dummy, worldCheckIndividuals.dummy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin,
        listRecordId, listRecordSubId, passportNumber, nationalId, title, fullNameOriginal,
        givenNamesOriginal, familyNameOriginal, fullNameDerived, givenNamesDerived,
        familyNameDerived, nameType, nameQuality, primaryName, originalScriptName, gender,
        genderDerivedFlag, dateOfBirth, yearOfBirth, deceasedFlag, deceasedDate, occupation,
        address, city, state, postalCode, addressCountry, residencyCountry, countryOfBirth,
        nationalities, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink,
        linkedProfiles, linkedRelationships, riskScore, pepRiskScore, dataConfidenceScore,
        dataConfidenceComment, inactiveFlag, inactiveSinceDate, pepClassification, category,
        externalSources, linkedTo, companies, dateOfBirthOriginal, yearOfBirthApproximated,
        placeOfBirthOriginal, furtherInformation, countriesOriginal, socialSecurityNumber,
        passportCountry, subCategoryDescription, updateCategory, nativeAliasLanguageName,
        nativeAliasLanguageCountry, cachedExternalSources, addedDate, lastUpdatedDate,
        edqOwsWatchlistName, dobs, idNumbers, recordType, dummy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldCheckIndividuals {\n");

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
    sb.append("    pepClassification: ").append(toIndentedString(pepClassification)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    externalSources: ").append(toIndentedString(externalSources)).append("\n");
    sb.append("    linkedTo: ").append(toIndentedString(linkedTo)).append("\n");
    sb.append("    companies: ").append(toIndentedString(companies)).append("\n");
    sb
        .append("    dateOfBirthOriginal: ")
        .append(toIndentedString(dateOfBirthOriginal))
        .append("\n");
    sb
        .append("    yearOfBirthApproximated: ")
        .append(toIndentedString(yearOfBirthApproximated))
        .append("\n");
    sb
        .append("    placeOfBirthOriginal: ")
        .append(toIndentedString(placeOfBirthOriginal))
        .append("\n");
    sb.append("    furtherInformation: ").append(toIndentedString(furtherInformation)).append("\n");
    sb.append("    countriesOriginal: ").append(toIndentedString(countriesOriginal)).append("\n");
    sb
        .append("    socialSecurityNumber: ")
        .append(toIndentedString(socialSecurityNumber))
        .append("\n");
    sb.append("    passportCountry: ").append(toIndentedString(passportCountry)).append("\n");
    sb
        .append("    subCategoryDescription: ")
        .append(toIndentedString(subCategoryDescription))
        .append("\n");
    sb.append("    updateCategory: ").append(toIndentedString(updateCategory)).append("\n");
    sb
        .append("    nativeAliasLanguageName: ")
        .append(toIndentedString(nativeAliasLanguageName))
        .append("\n");
    sb
        .append("    nativeAliasLanguageCountry: ")
        .append(toIndentedString(nativeAliasLanguageCountry))
        .append("\n");
    sb
        .append("    cachedExternalSources: ")
        .append(toIndentedString(cachedExternalSources))
        .append("\n");
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb
        .append("    edqOwsWatchlistName: ")
        .append(toIndentedString(edqOwsWatchlistName))
        .append("\n");
    sb.append("    dobs: ").append(toIndentedString(dobs)).append("\n");
    sb.append("    idNumbers: ").append(toIndentedString(idNumbers)).append("\n");
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
