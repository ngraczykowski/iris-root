package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * WorldCheckEntities
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class WorldCheckEntities   {
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

  @JsonProperty("registrationNumber")
  private String registrationNumber = null;

  @JsonProperty("entityNameOriginal")
  private String entityNameOriginal = null;

  @JsonProperty("entityNameDerived")
  private String entityNameDerived = null;

  @JsonProperty("nameType")
  private String nameType = null;

  @JsonProperty("nameQuality")
  private String nameQuality = null;

  @JsonProperty("primaryName")
  private String primaryName = null;

  @JsonProperty("originalScriptName")
  private String originalScriptName = null;

  @JsonProperty("aliasIsAcronym")
  private String aliasIsAcronym = null;

  @JsonProperty("vesselIndicator")
  private String vesselIndicator = null;

  @JsonProperty("vesselInfo")
  private String vesselInfo = null;

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

  @JsonProperty("registrationCountry")
  private String registrationCountry = null;

  @JsonProperty("operatingCountries")
  private String operatingCountries = null;

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

  @JsonProperty("furtherInformation")
  private String furtherInformation = null;

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

  @JsonProperty("edqWCListType")
  private String edqWCListType = null;

  @JsonProperty("edqOWSWatchlistName")
  private String edqOWSWatchlistName = null;

  public WorldCheckEntities caseId(Integer caseId) {
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

  public WorldCheckEntities recordID(BigDecimal recordID) {
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

  public WorldCheckEntities inputStream(String inputStream) {
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

  public WorldCheckEntities listKey(String listKey) {
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

  public WorldCheckEntities listSubKey(String listSubKey) {
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

  public WorldCheckEntities listRecordType(String listRecordType) {
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

  public WorldCheckEntities listRecordOrigin(String listRecordOrigin) {
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

  public WorldCheckEntities listRecordID(String listRecordID) {
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

  public WorldCheckEntities listRecordSubID(String listRecordSubID) {
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

  public WorldCheckEntities registrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
    return this;
  }

  /**
   * Entity Registration Number
   * @return registrationNumber
   **/
  @Schema(description = "Entity Registration Number")
  
    public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  public WorldCheckEntities entityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
    return this;
  }

  /**
   * Entity Name
   * @return entityNameOriginal
   **/
  @Schema(description = "Entity Name")
  
    public String getEntityNameOriginal() {
    return entityNameOriginal;
  }

  public void setEntityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
  }

  public WorldCheckEntities entityNameDerived(String entityNameDerived) {
    this.entityNameDerived = entityNameDerived;
    return this;
  }

  /**
   * Records the Entity Name where it has been derived by OWS
   * @return entityNameDerived
   **/
  @Schema(description = "Records the Entity Name where it has been derived by OWS")
  
    public String getEntityNameDerived() {
    return entityNameDerived;
  }

  public void setEntityNameDerived(String entityNameDerived) {
    this.entityNameDerived = entityNameDerived;
  }

  public WorldCheckEntities nameType(String nameType) {
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

  public WorldCheckEntities nameQuality(String nameQuality) {
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

  public WorldCheckEntities primaryName(String primaryName) {
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

  public WorldCheckEntities originalScriptName(String originalScriptName) {
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

  public WorldCheckEntities aliasIsAcronym(String aliasIsAcronym) {
    this.aliasIsAcronym = aliasIsAcronym;
    return this;
  }

  /**
   * Indicates is the name is an alias or primary name
   * @return aliasIsAcronym
   **/
  @Schema(description = "Indicates is the name is an alias or primary name")
  
    public String getAliasIsAcronym() {
    return aliasIsAcronym;
  }

  public void setAliasIsAcronym(String aliasIsAcronym) {
    this.aliasIsAcronym = aliasIsAcronym;
  }

  public WorldCheckEntities vesselIndicator(String vesselIndicator) {
    this.vesselIndicator = vesselIndicator;
    return this;
  }

  /**
   * Marks if the Watchlist Entry is a Vessel
   * @return vesselIndicator
   **/
  @Schema(description = "Marks if the Watchlist Entry is a Vessel")
  
    public String getVesselIndicator() {
    return vesselIndicator;
  }

  public void setVesselIndicator(String vesselIndicator) {
    this.vesselIndicator = vesselIndicator;
  }

  public WorldCheckEntities vesselInfo(String vesselInfo) {
    this.vesselInfo = vesselInfo;
    return this;
  }

  /**
   * Information relating to the Vessel
   * @return vesselInfo
   **/
  @Schema(description = "Information relating to the Vessel")
  
    public String getVesselInfo() {
    return vesselInfo;
  }

  public void setVesselInfo(String vesselInfo) {
    this.vesselInfo = vesselInfo;
  }

  public WorldCheckEntities address(String address) {
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

  public WorldCheckEntities city(String city) {
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

  public WorldCheckEntities state(String state) {
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

  public WorldCheckEntities postalCode(String postalCode) {
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

  public WorldCheckEntities addressCountry(String addressCountry) {
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

  public WorldCheckEntities registrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
    return this;
  }

  /**
   * Watchlist Entry's registration country (if known)
   * @return registrationCountry
   **/
  @Schema(description = "Watchlist Entry's registration country (if known)")
  
    public String getRegistrationCountry() {
    return registrationCountry;
  }

  public void setRegistrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
  }

  public WorldCheckEntities operatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
    return this;
  }

  /**
   * Watchlist Entry operating country (if known)
   * @return operatingCountries
   **/
  @Schema(description = "Watchlist Entry operating country (if known)")
  
    public String getOperatingCountries() {
    return operatingCountries;
  }

  public void setOperatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
  }

  public WorldCheckEntities countryCodesAll(String countryCodesAll) {
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

  public WorldCheckEntities countriesAll(String countriesAll) {
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

  public WorldCheckEntities profileHyperlink(String profileHyperlink) {
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

  public WorldCheckEntities searchHyperlink(String searchHyperlink) {
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

  public WorldCheckEntities linkedProfiles(String linkedProfiles) {
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

  public WorldCheckEntities linkedRelationships(String linkedRelationships) {
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

  public WorldCheckEntities riskScore(BigDecimal riskScore) {
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

  public WorldCheckEntities pEPRiskScore(BigDecimal pEPRiskScore) {
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

  public WorldCheckEntities dataConfidenceScore(BigDecimal dataConfidenceScore) {
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

  public WorldCheckEntities dataConfidenceComment(String dataConfidenceComment) {
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

  public WorldCheckEntities inactiveFlag(String inactiveFlag) {
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

  public WorldCheckEntities inactiveSinceDate(String inactiveSinceDate) {
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

  public WorldCheckEntities pEPClassification(String pEPClassification) {
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

  public WorldCheckEntities category(String category) {
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

  public WorldCheckEntities externalSources(String externalSources) {
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

  public WorldCheckEntities linkedTo(String linkedTo) {
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

  public WorldCheckEntities companies(String companies) {
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

  public WorldCheckEntities furtherInformation(String furtherInformation) {
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

  public WorldCheckEntities subCategoryDescription(String subCategoryDescription) {
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

  public WorldCheckEntities updateCategory(String updateCategory) {
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

  public WorldCheckEntities nativeAliasLanguageName(String nativeAliasLanguageName) {
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

  public WorldCheckEntities nativeAliasLanguageCountry(String nativeAliasLanguageCountry) {
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

  public WorldCheckEntities cachedExternalSources(String cachedExternalSources) {
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

  public WorldCheckEntities addedDate(String addedDate) {
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

  public WorldCheckEntities lastUpdatedDate(String lastUpdatedDate) {
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

  public WorldCheckEntities edqWCListType(String edqWCListType) {
    this.edqWCListType = edqWCListType;
    return this;
  }

  /**
   * Indicates the watchlist origin using the HSBC name specification
   * @return edqWCListType
   **/
  @Schema(description = "Indicates the watchlist origin using the HSBC name specification")
  
    public String getEdqWCListType() {
    return edqWCListType;
  }

  public void setEdqWCListType(String edqWCListType) {
    this.edqWCListType = edqWCListType;
  }

  public WorldCheckEntities edqOWSWatchlistName(String edqOWSWatchlistName) {
    this.edqOWSWatchlistName = edqOWSWatchlistName;
    return this;
  }

  /**
   * The world check watchlist name is transposed into a HSBC equivalent.
   * @return edqOWSWatchlistName
   **/
  @Schema(description = "The world check watchlist name is transposed into a HSBC equivalent.")
  
    public String getEdqOWSWatchlistName() {
    return edqOWSWatchlistName;
  }

  public void setEdqOWSWatchlistName(String edqOWSWatchlistName) {
    this.edqOWSWatchlistName = edqOWSWatchlistName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldCheckEntities worldCheckEntities = (WorldCheckEntities) o;
    return Objects.equals(this.caseId, worldCheckEntities.caseId) &&
        Objects.equals(this.recordID, worldCheckEntities.recordID) &&
        Objects.equals(this.inputStream, worldCheckEntities.inputStream) &&
        Objects.equals(this.listKey, worldCheckEntities.listKey) &&
        Objects.equals(this.listSubKey, worldCheckEntities.listSubKey) &&
        Objects.equals(this.listRecordType, worldCheckEntities.listRecordType) &&
        Objects.equals(this.listRecordOrigin, worldCheckEntities.listRecordOrigin) &&
        Objects.equals(this.listRecordID, worldCheckEntities.listRecordID) &&
        Objects.equals(this.listRecordSubID, worldCheckEntities.listRecordSubID) &&
        Objects.equals(this.registrationNumber, worldCheckEntities.registrationNumber) &&
        Objects.equals(this.entityNameOriginal, worldCheckEntities.entityNameOriginal) &&
        Objects.equals(this.entityNameDerived, worldCheckEntities.entityNameDerived) &&
        Objects.equals(this.nameType, worldCheckEntities.nameType) &&
        Objects.equals(this.nameQuality, worldCheckEntities.nameQuality) &&
        Objects.equals(this.primaryName, worldCheckEntities.primaryName) &&
        Objects.equals(this.originalScriptName, worldCheckEntities.originalScriptName) &&
        Objects.equals(this.aliasIsAcronym, worldCheckEntities.aliasIsAcronym) &&
        Objects.equals(this.vesselIndicator, worldCheckEntities.vesselIndicator) &&
        Objects.equals(this.vesselInfo, worldCheckEntities.vesselInfo) &&
        Objects.equals(this.address, worldCheckEntities.address) &&
        Objects.equals(this.city, worldCheckEntities.city) &&
        Objects.equals(this.state, worldCheckEntities.state) &&
        Objects.equals(this.postalCode, worldCheckEntities.postalCode) &&
        Objects.equals(this.addressCountry, worldCheckEntities.addressCountry) &&
        Objects.equals(this.registrationCountry, worldCheckEntities.registrationCountry) &&
        Objects.equals(this.operatingCountries, worldCheckEntities.operatingCountries) &&
        Objects.equals(this.countryCodesAll, worldCheckEntities.countryCodesAll) &&
        Objects.equals(this.countriesAll, worldCheckEntities.countriesAll) &&
        Objects.equals(this.profileHyperlink, worldCheckEntities.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, worldCheckEntities.searchHyperlink) &&
        Objects.equals(this.linkedProfiles, worldCheckEntities.linkedProfiles) &&
        Objects.equals(this.linkedRelationships, worldCheckEntities.linkedRelationships) &&
        Objects.equals(this.riskScore, worldCheckEntities.riskScore) &&
        Objects.equals(this.pEPRiskScore, worldCheckEntities.pEPRiskScore) &&
        Objects.equals(this.dataConfidenceScore, worldCheckEntities.dataConfidenceScore) &&
        Objects.equals(this.dataConfidenceComment, worldCheckEntities.dataConfidenceComment) &&
        Objects.equals(this.inactiveFlag, worldCheckEntities.inactiveFlag) &&
        Objects.equals(this.inactiveSinceDate, worldCheckEntities.inactiveSinceDate) &&
        Objects.equals(this.pEPClassification, worldCheckEntities.pEPClassification) &&
        Objects.equals(this.category, worldCheckEntities.category) &&
        Objects.equals(this.externalSources, worldCheckEntities.externalSources) &&
        Objects.equals(this.linkedTo, worldCheckEntities.linkedTo) &&
        Objects.equals(this.companies, worldCheckEntities.companies) &&
        Objects.equals(this.furtherInformation, worldCheckEntities.furtherInformation) &&
        Objects.equals(this.subCategoryDescription, worldCheckEntities.subCategoryDescription) &&
        Objects.equals(this.updateCategory, worldCheckEntities.updateCategory) &&
        Objects.equals(this.nativeAliasLanguageName, worldCheckEntities.nativeAliasLanguageName) &&
        Objects.equals(this.nativeAliasLanguageCountry, worldCheckEntities.nativeAliasLanguageCountry) &&
        Objects.equals(this.cachedExternalSources, worldCheckEntities.cachedExternalSources) &&
        Objects.equals(this.addedDate, worldCheckEntities.addedDate) &&
        Objects.equals(this.lastUpdatedDate, worldCheckEntities.lastUpdatedDate) &&
        Objects.equals(this.edqWCListType, worldCheckEntities.edqWCListType) &&
        Objects.equals(this.edqOWSWatchlistName, worldCheckEntities.edqOWSWatchlistName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordID, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin, listRecordID, listRecordSubID, registrationNumber, entityNameOriginal, entityNameDerived, nameType, nameQuality, primaryName, originalScriptName, aliasIsAcronym, vesselIndicator, vesselInfo, address, city, state, postalCode, addressCountry, registrationCountry, operatingCountries, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink, linkedProfiles, linkedRelationships, riskScore, pEPRiskScore, dataConfidenceScore, dataConfidenceComment, inactiveFlag, inactiveSinceDate, pEPClassification, category, externalSources, linkedTo, companies, furtherInformation, subCategoryDescription, updateCategory, nativeAliasLanguageName, nativeAliasLanguageCountry, cachedExternalSources, addedDate, lastUpdatedDate, edqWCListType, edqOWSWatchlistName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldCheckEntities {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordID: ").append(toIndentedString(recordID)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    listKey: ").append(toIndentedString(listKey)).append("\n");
    sb.append("    listSubKey: ").append(toIndentedString(listSubKey)).append("\n");
    sb.append("    listRecordType: ").append(toIndentedString(listRecordType)).append("\n");
    sb.append("    listRecordOrigin: ").append(toIndentedString(listRecordOrigin)).append("\n");
    sb.append("    listRecordID: ").append(toIndentedString(listRecordID)).append("\n");
    sb.append("    listRecordSubID: ").append(toIndentedString(listRecordSubID)).append("\n");
    sb.append("    registrationNumber: ").append(toIndentedString(registrationNumber)).append("\n");
    sb.append("    entityNameOriginal: ").append(toIndentedString(entityNameOriginal)).append("\n");
    sb.append("    entityNameDerived: ").append(toIndentedString(entityNameDerived)).append("\n");
    sb.append("    nameType: ").append(toIndentedString(nameType)).append("\n");
    sb.append("    nameQuality: ").append(toIndentedString(nameQuality)).append("\n");
    sb.append("    primaryName: ").append(toIndentedString(primaryName)).append("\n");
    sb.append("    originalScriptName: ").append(toIndentedString(originalScriptName)).append("\n");
    sb.append("    aliasIsAcronym: ").append(toIndentedString(aliasIsAcronym)).append("\n");
    sb.append("    vesselIndicator: ").append(toIndentedString(vesselIndicator)).append("\n");
    sb.append("    vesselInfo: ").append(toIndentedString(vesselInfo)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
    sb.append("    addressCountry: ").append(toIndentedString(addressCountry)).append("\n");
    sb.append("    registrationCountry: ").append(toIndentedString(registrationCountry)).append("\n");
    sb.append("    operatingCountries: ").append(toIndentedString(operatingCountries)).append("\n");
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
    sb.append("    furtherInformation: ").append(toIndentedString(furtherInformation)).append("\n");
    sb.append("    subCategoryDescription: ").append(toIndentedString(subCategoryDescription)).append("\n");
    sb.append("    updateCategory: ").append(toIndentedString(updateCategory)).append("\n");
    sb.append("    nativeAliasLanguageName: ").append(toIndentedString(nativeAliasLanguageName)).append("\n");
    sb.append("    nativeAliasLanguageCountry: ").append(toIndentedString(nativeAliasLanguageCountry)).append("\n");
    sb.append("    cachedExternalSources: ").append(toIndentedString(cachedExternalSources)).append("\n");
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    edqWCListType: ").append(toIndentedString(edqWCListType)).append("\n");
    sb.append("    edqOWSWatchlistName: ").append(toIndentedString(edqOWSWatchlistName)).append("\n");
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
