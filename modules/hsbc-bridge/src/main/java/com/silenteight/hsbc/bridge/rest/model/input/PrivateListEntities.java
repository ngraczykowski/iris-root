package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * PrivateListEntities
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class PrivateListEntities   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("recordId")
  private BigDecimal recordId = null;

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

  @JsonProperty("listRecordId")
  private String listRecordId = null;

  @JsonProperty("listRecordSubId")
  private String listRecordSubId = null;

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

  @JsonProperty("addedDate")
  private String addedDate = null;

  @JsonProperty("lastUpdatedDate")
  private String lastUpdatedDate = null;

  @JsonProperty("addressType")
  private String addressType = null;

  @JsonProperty("currentAddress")
  private String currentAddress = null;

  @JsonProperty("telephoneNumber")
  private String telephoneNumber = null;

  @JsonProperty("edqBusinessTelephoneNumber")
  private String edqBusinessTelephoneNumber = null;

  @JsonProperty("telephoneNumberBusiness")
  private String telephoneNumberBusiness = null;

  @JsonProperty("edqCellNumberPersonal")
  private String edqCellNumberPersonal = null;

  @JsonProperty("edqTaxNumber")
  private String edqTaxNumber = null;

  @JsonProperty("edqEmailAddress")
  private String edqEmailAddress = null;

  @JsonProperty("sCIONReferenceNumber")
  private String sCIONReferenceNumber = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("listCategory")
  private String listCategory = null;

  @JsonProperty("furtherInfo")
  private String furtherInfo = null;

  @JsonProperty("edqBusinessType")
  private String edqBusinessType = null;

  @JsonProperty("dateOfIncorporation")
  private String dateOfIncorporation = null;

  @JsonProperty("edqDataOfIncorporationSTRING")
  private String edqDataOfIncorporationSTRING = null;

  @JsonProperty("externalCustomerID")
  private String externalCustomerID = null;

  @JsonProperty("listCustomerType")
  private String listCustomerType = null;

  @JsonProperty("edqListName")
  private String edqListName = null;

  @JsonProperty("edqWatchlistLstType")
  private String edqWatchlistLstType = null;

  @JsonProperty("primaryName1")
  private String primaryName1 = null;

  @JsonProperty("entityName")
  private String entityName = null;

  @JsonProperty("entityName1")
  private String entityName1 = null;

  @JsonProperty("entityName2")
  private String entityName2 = null;

  public PrivateListEntities caseId(Integer caseId) {
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

  public PrivateListEntities recordId(BigDecimal recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * Refers to a record within an Alert where there are multiple Customer name records within the same Alert
   * @return recordId
   **/
  @Schema(description = "Refers to a record within an Alert where there are multiple Customer name records within the same Alert")
  
    @Valid
    public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  public PrivateListEntities inputStream(String inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  /**
   * Denotes the type of data, for example&#58; Private List Data
   * @return inputStream
   **/
  @Schema(description = "Denotes the type of data, for example&#58; Private List Data")
  
    public String getInputStream() {
    return inputStream;
  }

  public void setInputStream(String inputStream) {
    this.inputStream = inputStream;
  }

  public PrivateListEntities listKey(String listKey) {
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

  public PrivateListEntities listSubKey(String listSubKey) {
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

  public PrivateListEntities listRecordType(String listRecordType) {
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

  public PrivateListEntities listRecordOrigin(String listRecordOrigin) {
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

  public PrivateListEntities listRecordId(String listRecordId) {
    this.listRecordId = listRecordId;
    return this;
  }

  /**
   * Unique Identifier assigned to the Private List record
   * @return listRecordId
   **/
  @Schema(description = "Unique Identifier assigned to the Private List record")
  
    public String getListRecordId() {
    return listRecordId;
  }

  public void setListRecordId(String listRecordId) {
    this.listRecordId = listRecordId;
  }

  public PrivateListEntities listRecordSubId(String listRecordSubId) {
    this.listRecordSubId = listRecordSubId;
    return this;
  }

  /**
   * ID for the sub records within a Private List record
   * @return listRecordSubId
   **/
  @Schema(description = "ID for the sub records within a Private List record")
  
    public String getListRecordSubId() {
    return listRecordSubId;
  }

  public void setListRecordSubId(String listRecordSubId) {
    this.listRecordSubId = listRecordSubId;
  }

  public PrivateListEntities registrationNumber(String registrationNumber) {
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

  public PrivateListEntities entityNameOriginal(String entityNameOriginal) {
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

  public PrivateListEntities entityNameDerived(String entityNameDerived) {
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

  public PrivateListEntities nameType(String nameType) {
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

  public PrivateListEntities nameQuality(String nameQuality) {
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

  public PrivateListEntities primaryName(String primaryName) {
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

  public PrivateListEntities originalScriptName(String originalScriptName) {
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

  public PrivateListEntities aliasIsAcronym(String aliasIsAcronym) {
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

  public PrivateListEntities vesselIndicator(String vesselIndicator) {
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

  public PrivateListEntities vesselInfo(String vesselInfo) {
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

  public PrivateListEntities address(String address) {
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

  public PrivateListEntities city(String city) {
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

  public PrivateListEntities state(String state) {
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

  public PrivateListEntities postalCode(String postalCode) {
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

  public PrivateListEntities addressCountry(String addressCountry) {
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

  public PrivateListEntities registrationCountry(String registrationCountry) {
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

  public PrivateListEntities operatingCountries(String operatingCountries) {
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

  public PrivateListEntities countryCodesAll(String countryCodesAll) {
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

  public PrivateListEntities countriesAll(String countriesAll) {
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

  public PrivateListEntities profileHyperlink(String profileHyperlink) {
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

  public PrivateListEntities searchHyperlink(String searchHyperlink) {
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

  public PrivateListEntities linkedProfiles(String linkedProfiles) {
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

  public PrivateListEntities linkedRelationships(String linkedRelationships) {
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

  public PrivateListEntities riskScore(BigDecimal riskScore) {
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

  public PrivateListEntities pEPRiskScore(BigDecimal pEPRiskScore) {
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

  public PrivateListEntities dataConfidenceScore(BigDecimal dataConfidenceScore) {
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

  public PrivateListEntities dataConfidenceComment(String dataConfidenceComment) {
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

  public PrivateListEntities inactiveFlag(String inactiveFlag) {
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

  public PrivateListEntities inactiveSinceDate(String inactiveSinceDate) {
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

  public PrivateListEntities pEPClassification(String pEPClassification) {
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

  public PrivateListEntities addedDate(String addedDate) {
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

  public PrivateListEntities lastUpdatedDate(String lastUpdatedDate) {
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

  public PrivateListEntities addressType(String addressType) {
    this.addressType = addressType;
    return this;
  }

  /**
   * Address label as provided by the private list
   * @return addressType
   **/
  @Schema(description = "Address label as provided by the private list")
  
    public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public PrivateListEntities currentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
    return this;
  }

  /**
   * Address
   * @return currentAddress
   **/
  @Schema(description = "Address")
  
    public String getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
  }

  public PrivateListEntities telephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
    return this;
  }

  /**
   * Telephone number
   * @return telephoneNumber
   **/
  @Schema(description = "Telephone number")
  
    public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public PrivateListEntities edqBusinessTelephoneNumber(String edqBusinessTelephoneNumber) {
    this.edqBusinessTelephoneNumber = edqBusinessTelephoneNumber;
    return this;
  }

  /**
   * Business Telephone Number that has been normalised
   * @return edqBusinessTelephoneNumber
   **/
  @Schema(description = "Business Telephone Number that has been normalised")
  
    public String getEdqBusinessTelephoneNumber() {
    return edqBusinessTelephoneNumber;
  }

  public void setEdqBusinessTelephoneNumber(String edqBusinessTelephoneNumber) {
    this.edqBusinessTelephoneNumber = edqBusinessTelephoneNumber;
  }

  public PrivateListEntities telephoneNumberBusiness(String telephoneNumberBusiness) {
    this.telephoneNumberBusiness = telephoneNumberBusiness;
    return this;
  }

  /**
   * Business Telephone Number
   * @return telephoneNumberBusiness
   **/
  @Schema(description = "Business Telephone Number")
  
    public String getTelephoneNumberBusiness() {
    return telephoneNumberBusiness;
  }

  public void setTelephoneNumberBusiness(String telephoneNumberBusiness) {
    this.telephoneNumberBusiness = telephoneNumberBusiness;
  }

  public PrivateListEntities edqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
    return this;
  }

  /**
   * Mobile phone number (normalised)
   * @return edqCellNumberPersonal
   **/
  @Schema(description = "Mobile phone number (normalised)")
  
    public String getEdqCellNumberPersonal() {
    return edqCellNumberPersonal;
  }

  public void setEdqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
  }

  public PrivateListEntities edqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
    return this;
  }

  /**
   * Tax Number (Normalised)
   * @return edqTaxNumber
   **/
  @Schema(description = "Tax Number (Normalised)")
  
    public String getEdqTaxNumber() {
    return edqTaxNumber;
  }

  public void setEdqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
  }

  public PrivateListEntities edqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
    return this;
  }

  /**
   * Email address
   * @return edqEmailAddress
   **/
  @Schema(description = "Email address")
  
    public String getEdqEmailAddress() {
    return edqEmailAddress;
  }

  public void setEdqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
  }

  public PrivateListEntities sCIONReferenceNumber(String sCIONReferenceNumber) {
    this.sCIONReferenceNumber = sCIONReferenceNumber;
    return this;
  }

  /**
   * SCION Reference Number
   * @return sCIONReferenceNumber
   **/
  @Schema(description = "SCION Reference Number")
  
    public String getSCIONReferenceNumber() {
    return sCIONReferenceNumber;
  }

  public void setSCIONReferenceNumber(String sCIONReferenceNumber) {
    this.sCIONReferenceNumber = sCIONReferenceNumber;
  }

  public PrivateListEntities role(String role) {
    this.role = role;
    return this;
  }

  /**
   * The role the person has within a business
   * @return role
   **/
  @Schema(description = "The role the person has within a business")
  
    public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public PrivateListEntities listCategory(String listCategory) {
    this.listCategory = listCategory;
    return this;
  }

  /**
   * Indicates the list record's category.
   * @return listCategory
   **/
  @Schema(description = "Indicates the list record's category.")
  
    public String getListCategory() {
    return listCategory;
  }

  public void setListCategory(String listCategory) {
    this.listCategory = listCategory;
  }

  public PrivateListEntities furtherInfo(String furtherInfo) {
    this.furtherInfo = furtherInfo;
    return this;
  }

  /**
   * Any further information as deemed useful
   * @return furtherInfo
   **/
  @Schema(description = "Any further information as deemed useful")
  
    public String getFurtherInfo() {
    return furtherInfo;
  }

  public void setFurtherInfo(String furtherInfo) {
    this.furtherInfo = furtherInfo;
  }

  public PrivateListEntities edqBusinessType(String edqBusinessType) {
    this.edqBusinessType = edqBusinessType;
    return this;
  }

  /**
   * Indicates the business sector
   * @return edqBusinessType
   **/
  @Schema(description = "Indicates the business sector")
  
    public String getEdqBusinessType() {
    return edqBusinessType;
  }

  public void setEdqBusinessType(String edqBusinessType) {
    this.edqBusinessType = edqBusinessType;
  }

  public PrivateListEntities dateOfIncorporation(String dateOfIncorporation) {
    this.dateOfIncorporation = dateOfIncorporation;
    return this;
  }

  /**
   * Date of incorporation from a date format attribute
   * @return dateOfIncorporation
   **/
  @Schema(description = "Date of incorporation from a date format attribute")
  
    public String getDateOfIncorporation() {
    return dateOfIncorporation;
  }

  public void setDateOfIncorporation(String dateOfIncorporation) {
    this.dateOfIncorporation = dateOfIncorporation;
  }

  public PrivateListEntities edqDataOfIncorporationSTRING(String edqDataOfIncorporationSTRING) {
    this.edqDataOfIncorporationSTRING = edqDataOfIncorporationSTRING;
    return this;
  }

  /**
   * Date of Incorporation from a string format attribute
   * @return edqDataOfIncorporationSTRING
   **/
  @Schema(description = "Date of Incorporation from a string format attribute")
  
    public String getEdqDataOfIncorporationSTRING() {
    return edqDataOfIncorporationSTRING;
  }

  public void setEdqDataOfIncorporationSTRING(String edqDataOfIncorporationSTRING) {
    this.edqDataOfIncorporationSTRING = edqDataOfIncorporationSTRING;
  }

  public PrivateListEntities externalCustomerID(String externalCustomerID) {
    this.externalCustomerID = externalCustomerID;
    return this;
  }

  /**
   * Customer ID the record relates to
   * @return externalCustomerID
   **/
  @Schema(description = "Customer ID the record relates to")
  
    public String getExternalCustomerID() {
    return externalCustomerID;
  }

  public void setExternalCustomerID(String externalCustomerID) {
    this.externalCustomerID = externalCustomerID;
  }

  public PrivateListEntities listCustomerType(String listCustomerType) {
    this.listCustomerType = listCustomerType;
    return this;
  }

  /**
   * Indicates if the record is a Person or Business, P or B respectively.
   * @return listCustomerType
   **/
  @Schema(description = "Indicates if the record is a Person or Business, P or B respectively.")
  
    public String getListCustomerType() {
    return listCustomerType;
  }

  public void setListCustomerType(String listCustomerType) {
    this.listCustomerType = listCustomerType;
  }

  public PrivateListEntities edqListName(String edqListName) {
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

  public PrivateListEntities edqWatchlistLstType(String edqWatchlistLstType) {
    this.edqWatchlistLstType = edqWatchlistLstType;
    return this;
  }

  /**
   * SCION List short form  name
   * @return edqWatchlistLstType
   **/
  @Schema(description = "SCION List short form  name")
  
    public String getEdqWatchlistLstType() {
    return edqWatchlistLstType;
  }

  public void setEdqWatchlistLstType(String edqWatchlistLstType) {
    this.edqWatchlistLstType = edqWatchlistLstType;
  }

  public PrivateListEntities primaryName1(String primaryName1) {
    this.primaryName1 = primaryName1;
    return this;
  }

  /**
   * Customers primary name
   * @return primaryName1
   **/
  @Schema(description = "Customers primary name")
  
    public String getPrimaryName1() {
    return primaryName1;
  }

  public void setPrimaryName1(String primaryName1) {
    this.primaryName1 = primaryName1;
  }

  public PrivateListEntities entityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

  /**
   * Entity name, place holder 1
   * @return entityName
   **/
  @Schema(description = "Entity name, place holder 1")
  
    public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public PrivateListEntities entityName1(String entityName1) {
    this.entityName1 = entityName1;
    return this;
  }

  /**
   * Entity name place holder 2
   * @return entityName1
   **/
  @Schema(description = "Entity name place holder 2")
  
    public String getEntityName1() {
    return entityName1;
  }

  public void setEntityName1(String entityName1) {
    this.entityName1 = entityName1;
  }

  public PrivateListEntities entityName2(String entityName2) {
    this.entityName2 = entityName2;
    return this;
  }

  /**
   * Entity name place holder 3
   * @return entityName2
   **/
  @Schema(description = "Entity name place holder 3")
  
    public String getEntityName2() {
    return entityName2;
  }

  public void setEntityName2(String entityName2) {
    this.entityName2 = entityName2;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrivateListEntities privateListEntities = (PrivateListEntities) o;
    return Objects.equals(this.caseId, privateListEntities.caseId) &&
        Objects.equals(this.recordId, privateListEntities.recordId) &&
        Objects.equals(this.inputStream, privateListEntities.inputStream) &&
        Objects.equals(this.listKey, privateListEntities.listKey) &&
        Objects.equals(this.listSubKey, privateListEntities.listSubKey) &&
        Objects.equals(this.listRecordType, privateListEntities.listRecordType) &&
        Objects.equals(this.listRecordOrigin, privateListEntities.listRecordOrigin) &&
        Objects.equals(this.listRecordId, privateListEntities.listRecordId) &&
        Objects.equals(this.listRecordSubId, privateListEntities.listRecordSubId) &&
        Objects.equals(this.registrationNumber, privateListEntities.registrationNumber) &&
        Objects.equals(this.entityNameOriginal, privateListEntities.entityNameOriginal) &&
        Objects.equals(this.entityNameDerived, privateListEntities.entityNameDerived) &&
        Objects.equals(this.nameType, privateListEntities.nameType) &&
        Objects.equals(this.nameQuality, privateListEntities.nameQuality) &&
        Objects.equals(this.primaryName, privateListEntities.primaryName) &&
        Objects.equals(this.originalScriptName, privateListEntities.originalScriptName) &&
        Objects.equals(this.aliasIsAcronym, privateListEntities.aliasIsAcronym) &&
        Objects.equals(this.vesselIndicator, privateListEntities.vesselIndicator) &&
        Objects.equals(this.vesselInfo, privateListEntities.vesselInfo) &&
        Objects.equals(this.address, privateListEntities.address) &&
        Objects.equals(this.city, privateListEntities.city) &&
        Objects.equals(this.state, privateListEntities.state) &&
        Objects.equals(this.postalCode, privateListEntities.postalCode) &&
        Objects.equals(this.addressCountry, privateListEntities.addressCountry) &&
        Objects.equals(this.registrationCountry, privateListEntities.registrationCountry) &&
        Objects.equals(this.operatingCountries, privateListEntities.operatingCountries) &&
        Objects.equals(this.countryCodesAll, privateListEntities.countryCodesAll) &&
        Objects.equals(this.countriesAll, privateListEntities.countriesAll) &&
        Objects.equals(this.profileHyperlink, privateListEntities.profileHyperlink) &&
        Objects.equals(this.searchHyperlink, privateListEntities.searchHyperlink) &&
        Objects.equals(this.linkedProfiles, privateListEntities.linkedProfiles) &&
        Objects.equals(this.linkedRelationships, privateListEntities.linkedRelationships) &&
        Objects.equals(this.riskScore, privateListEntities.riskScore) &&
        Objects.equals(this.pEPRiskScore, privateListEntities.pEPRiskScore) &&
        Objects.equals(this.dataConfidenceScore, privateListEntities.dataConfidenceScore) &&
        Objects.equals(this.dataConfidenceComment, privateListEntities.dataConfidenceComment) &&
        Objects.equals(this.inactiveFlag, privateListEntities.inactiveFlag) &&
        Objects.equals(this.inactiveSinceDate, privateListEntities.inactiveSinceDate) &&
        Objects.equals(this.pEPClassification, privateListEntities.pEPClassification) &&
        Objects.equals(this.addedDate, privateListEntities.addedDate) &&
        Objects.equals(this.lastUpdatedDate, privateListEntities.lastUpdatedDate) &&
        Objects.equals(this.addressType, privateListEntities.addressType) &&
        Objects.equals(this.currentAddress, privateListEntities.currentAddress) &&
        Objects.equals(this.telephoneNumber, privateListEntities.telephoneNumber) &&
        Objects.equals(this.edqBusinessTelephoneNumber, privateListEntities.edqBusinessTelephoneNumber) &&
        Objects.equals(this.telephoneNumberBusiness, privateListEntities.telephoneNumberBusiness) &&
        Objects.equals(this.edqCellNumberPersonal, privateListEntities.edqCellNumberPersonal) &&
        Objects.equals(this.edqTaxNumber, privateListEntities.edqTaxNumber) &&
        Objects.equals(this.edqEmailAddress, privateListEntities.edqEmailAddress) &&
        Objects.equals(this.sCIONReferenceNumber, privateListEntities.sCIONReferenceNumber) &&
        Objects.equals(this.role, privateListEntities.role) &&
        Objects.equals(this.listCategory, privateListEntities.listCategory) &&
        Objects.equals(this.furtherInfo, privateListEntities.furtherInfo) &&
        Objects.equals(this.edqBusinessType, privateListEntities.edqBusinessType) &&
        Objects.equals(this.dateOfIncorporation, privateListEntities.dateOfIncorporation) &&
        Objects.equals(this.edqDataOfIncorporationSTRING, privateListEntities.edqDataOfIncorporationSTRING) &&
        Objects.equals(this.externalCustomerID, privateListEntities.externalCustomerID) &&
        Objects.equals(this.listCustomerType, privateListEntities.listCustomerType) &&
        Objects.equals(this.edqListName, privateListEntities.edqListName) &&
        Objects.equals(this.edqWatchlistLstType, privateListEntities.edqWatchlistLstType) &&
        Objects.equals(this.primaryName1, privateListEntities.primaryName1) &&
        Objects.equals(this.entityName, privateListEntities.entityName) &&
        Objects.equals(this.entityName1, privateListEntities.entityName1) &&
        Objects.equals(this.entityName2, privateListEntities.entityName2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordId, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin, listRecordId, listRecordSubId, registrationNumber, entityNameOriginal, entityNameDerived, nameType, nameQuality, primaryName, originalScriptName, aliasIsAcronym, vesselIndicator, vesselInfo, address, city, state, postalCode, addressCountry, registrationCountry, operatingCountries, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink, linkedProfiles, linkedRelationships, riskScore, pEPRiskScore, dataConfidenceScore, dataConfidenceComment, inactiveFlag, inactiveSinceDate, pEPClassification, addedDate, lastUpdatedDate, addressType, currentAddress, telephoneNumber, edqBusinessTelephoneNumber, telephoneNumberBusiness, edqCellNumberPersonal, edqTaxNumber, edqEmailAddress, sCIONReferenceNumber, role, listCategory, furtherInfo, edqBusinessType, dateOfIncorporation, edqDataOfIncorporationSTRING, externalCustomerID, listCustomerType, edqListName, edqWatchlistLstType, primaryName1, entityName, entityName1, entityName2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrivateListEntities {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    listKey: ").append(toIndentedString(listKey)).append("\n");
    sb.append("    listSubKey: ").append(toIndentedString(listSubKey)).append("\n");
    sb.append("    listRecordType: ").append(toIndentedString(listRecordType)).append("\n");
    sb.append("    listRecordOrigin: ").append(toIndentedString(listRecordOrigin)).append("\n");
    sb.append("    listRecordId: ").append(toIndentedString(listRecordId)).append("\n");
    sb.append("    listRecordSubId: ").append(toIndentedString(listRecordSubId)).append("\n");
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
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    currentAddress: ").append(toIndentedString(currentAddress)).append("\n");
    sb.append("    telephoneNumber: ").append(toIndentedString(telephoneNumber)).append("\n");
    sb.append("    edqBusinessTelephoneNumber: ").append(toIndentedString(edqBusinessTelephoneNumber)).append("\n");
    sb.append("    telephoneNumberBusiness: ").append(toIndentedString(telephoneNumberBusiness)).append("\n");
    sb.append("    edqCellNumberPersonal: ").append(toIndentedString(edqCellNumberPersonal)).append("\n");
    sb.append("    edqTaxNumber: ").append(toIndentedString(edqTaxNumber)).append("\n");
    sb.append("    edqEmailAddress: ").append(toIndentedString(edqEmailAddress)).append("\n");
    sb.append("    sCIONReferenceNumber: ").append(toIndentedString(sCIONReferenceNumber)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    listCategory: ").append(toIndentedString(listCategory)).append("\n");
    sb.append("    furtherInfo: ").append(toIndentedString(furtherInfo)).append("\n");
    sb.append("    edqBusinessType: ").append(toIndentedString(edqBusinessType)).append("\n");
    sb.append("    dateOfIncorporation: ").append(toIndentedString(dateOfIncorporation)).append("\n");
    sb.append("    edqDataOfIncorporationSTRING: ").append(toIndentedString(edqDataOfIncorporationSTRING)).append("\n");
    sb.append("    externalCustomerID: ").append(toIndentedString(externalCustomerID)).append("\n");
    sb.append("    listCustomerType: ").append(toIndentedString(listCustomerType)).append("\n");
    sb.append("    edqListName: ").append(toIndentedString(edqListName)).append("\n");
    sb.append("    edqWatchlistLstType: ").append(toIndentedString(edqWatchlistLstType)).append("\n");
    sb.append("    primaryName1: ").append(toIndentedString(primaryName1)).append("\n");
    sb.append("    entityName: ").append(toIndentedString(entityName)).append("\n");
    sb.append("    entityName1: ").append(toIndentedString(entityName1)).append("\n");
    sb.append("    entityName2: ").append(toIndentedString(entityName2)).append("\n");
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
