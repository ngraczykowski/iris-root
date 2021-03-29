package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class PrivateListEntities {

  private int caseId;
  private BigDecimal recordId = null;
  private String inputStream = null;
  private String listKey = null;
  private String listSubKey = null;
  private String listRecordType = null;
  private String listRecordOrigin = null;
  private String listRecordId = null;
  private String listRecordSubId = null;
  private String registrationNumber = null;
  private String entityNameOriginal = null;
  private String entityNameDerived = null;
  private String nameType = null;
  private String nameQuality = null;
  private String primaryName = null;
  private String originalScriptName = null;
  private String aliasIsAcronym = null;
  private String vesselIndicator = null;
  private String vesselInfo = null;
  private String address = null;
  private String city = null;
  private String state = null;
  private String postalCode = null;
  private String addressCountry = null;
  private String registrationCountry = null;
  private String operatingCountries = null;
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
  private String addedDate = null;
  private String lastUpdatedDate = null;
  private String addressType = null;
  private String currentAddress = null;
  private String telephoneNumber = null;
  private String edqBusinessTelephoneNumber = null;
  private String telephoneNumberBusiness = null;
  private String edqCellNumberPersonal = null;
  private String edqTaxNumber = null;
  private String edqEmailAddress = null;
  private String scionReferenceNumber = null;
  private String role = null;
  private String listCategory = null;
  private String furtherInfo = null;
  private String edqBusinessType = null;
  private String dateOfIncorporation = null;
  private String edqDataOfIncorporationString = null;
  private String externalCustomerId = null;
  private String listCustomerType = null;
  private String edqListName = null;
  private String edqWatchlistLstType = null;
  private String primaryName1 = null;
  private String entityName = null;
  private String entityName1 = null;
  private String entityName2 = null;

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
   * Denotes the type of data, for example&amp;#58; Private List Data
   **/

  @Schema(description = "Denotes the type of data, for example&#58; Private List Data")
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
   * Entity Registration Number
   **/

  @Schema(description = "Entity Registration Number")
  @JsonProperty("registrationNumber")
  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  /**
   * Entity Name
   **/

  @Schema(description = "Entity Name")
  @JsonProperty("entityNameOriginal")
  public String getEntityNameOriginal() {
    return entityNameOriginal;
  }

  public void setEntityNameOriginal(String entityNameOriginal) {
    this.entityNameOriginal = entityNameOriginal;
  }

  /**
   * Records the Entity Name where it has been derived by OWS
   **/

  @Schema(description = "Records the Entity Name where it has been derived by OWS")
  @JsonProperty("entityNameDerived")
  public String getEntityNameDerived() {
    return entityNameDerived;
  }

  public void setEntityNameDerived(String entityNameDerived) {
    this.entityNameDerived = entityNameDerived;
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
   * Indicates is the name is an alias or primary name
   **/

  @Schema(description = "Indicates is the name is an alias or primary name")
  @JsonProperty("aliasIsAcronym")
  public String getAliasIsAcronym() {
    return aliasIsAcronym;
  }

  public void setAliasIsAcronym(String aliasIsAcronym) {
    this.aliasIsAcronym = aliasIsAcronym;
  }

  /**
   * Marks if the Watchlist Entry is a Vessel
   **/

  @Schema(description = "Marks if the Watchlist Entry is a Vessel")
  @JsonProperty("vesselIndicator")
  public String getVesselIndicator() {
    return vesselIndicator;
  }

  public void setVesselIndicator(String vesselIndicator) {
    this.vesselIndicator = vesselIndicator;
  }

  /**
   * Information relating to the Vessel
   **/

  @Schema(description = "Information relating to the Vessel")
  @JsonProperty("vesselInfo")
  public String getVesselInfo() {
    return vesselInfo;
  }

  public void setVesselInfo(String vesselInfo) {
    this.vesselInfo = vesselInfo;
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
   * Watchlist Entry&#x27;s registration country (if known)
   **/

  @Schema(description = "Watchlist Entry's registration country (if known)")
  @JsonProperty("registrationCountry")
  public String getRegistrationCountry() {
    return registrationCountry;
  }

  public void setRegistrationCountry(String registrationCountry) {
    this.registrationCountry = registrationCountry;
  }

  /**
   * Watchlist Entry operating country (if known)
   **/

  @Schema(description = "Watchlist Entry operating country (if known)")
  @JsonProperty("operatingCountries")
  public String getOperatingCountries() {
    return operatingCountries;
  }

  public void setOperatingCountries(String operatingCountries) {
    this.operatingCountries = operatingCountries;
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
  @JsonProperty("pepClassification")
  public String getPepClassification() {
    return pepClassification;
  }

  public void setPepClassification(String pepClassification) {
    this.pepClassification = pepClassification;
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
  @JsonProperty("addressType")
  public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  /**
   * Address
   **/

  @Schema(description = "Address")
  @JsonProperty("currentAddress")
  public String getCurrentAddress() {
    return currentAddress;
  }

  public void setCurrentAddress(String currentAddress) {
    this.currentAddress = currentAddress;
  }

  /**
   * Telephone number
   **/

  @Schema(description = "Telephone number")
  @JsonProperty("telephoneNumber")
  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  /**
   * Business Telephone Number that has been normalised
   **/

  @Schema(description = "Business Telephone Number that has been normalised")
  @JsonProperty("edqBusinessTelephoneNumber")
  public String getEdqBusinessTelephoneNumber() {
    return edqBusinessTelephoneNumber;
  }

  public void setEdqBusinessTelephoneNumber(String edqBusinessTelephoneNumber) {
    this.edqBusinessTelephoneNumber = edqBusinessTelephoneNumber;
  }

  /**
   * Business Telephone Number
   **/

  @Schema(description = "Business Telephone Number")
  @JsonProperty("telephoneNumberBusiness")
  public String getTelephoneNumberBusiness() {
    return telephoneNumberBusiness;
  }

  public void setTelephoneNumberBusiness(String telephoneNumberBusiness) {
    this.telephoneNumberBusiness = telephoneNumberBusiness;
  }

  /**
   * Mobile phone number (normalised)
   **/

  @Schema(description = "Mobile phone number (normalised)")
  @JsonProperty("edqCellNumberPersonal")
  public String getEdqCellNumberPersonal() {
    return edqCellNumberPersonal;
  }

  public void setEdqCellNumberPersonal(String edqCellNumberPersonal) {
    this.edqCellNumberPersonal = edqCellNumberPersonal;
  }

  /**
   * Tax Number (Normalised)
   **/

  @Schema(description = "Tax Number (Normalised)")
  @JsonProperty("edqTaxNumber")
  public String getEdqTaxNumber() {
    return edqTaxNumber;
  }

  public void setEdqTaxNumber(String edqTaxNumber) {
    this.edqTaxNumber = edqTaxNumber;
  }

  /**
   * Email address
   **/

  @Schema(description = "Email address")
  @JsonProperty("edqEmailAddress")
  public String getEdqEmailAddress() {
    return edqEmailAddress;
  }

  public void setEdqEmailAddress(String edqEmailAddress) {
    this.edqEmailAddress = edqEmailAddress;
  }

  /**
   * SCION Reference Number
   **/

  @Schema(description = "SCION Reference Number")
  @JsonProperty("scionReferenceNumber")
  public String getScionReferenceNumber() {
    return scionReferenceNumber;
  }

  public void setScionReferenceNumber(String scionReferenceNumber) {
    this.scionReferenceNumber = scionReferenceNumber;
  }

  /**
   * The role the person has within a business
   **/

  @Schema(description = "The role the person has within a business")
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Indicates the list record&#x27;s category.
   **/

  @Schema(description = "Indicates the list record's category.")
  @JsonProperty("listCategory")
  public String getListCategory() {
    return listCategory;
  }

  public void setListCategory(String listCategory) {
    this.listCategory = listCategory;
  }

  /**
   * Any further information as deemed useful
   **/

  @Schema(description = "Any further information as deemed useful")
  @JsonProperty("furtherInfo")
  public String getFurtherInfo() {
    return furtherInfo;
  }

  public void setFurtherInfo(String furtherInfo) {
    this.furtherInfo = furtherInfo;
  }

  /**
   * Indicates the business sector
   **/

  @Schema(description = "Indicates the business sector")
  @JsonProperty("edqBusinessType")
  public String getEdqBusinessType() {
    return edqBusinessType;
  }

  public void setEdqBusinessType(String edqBusinessType) {
    this.edqBusinessType = edqBusinessType;
  }

  /**
   * Date of incorporation from a date format attribute
   **/

  @Schema(description = "Date of incorporation from a date format attribute")
  @JsonProperty("dateOfIncorporation")
  public String getDateOfIncorporation() {
    return dateOfIncorporation;
  }

  public void setDateOfIncorporation(String dateOfIncorporation) {
    this.dateOfIncorporation = dateOfIncorporation;
  }

  /**
   * Date of Incorporation from a string format attribute
   **/

  @Schema(description = "Date of Incorporation from a string format attribute")
  @JsonProperty("edqDataOfIncorporationString")
  public String getEdqDataOfIncorporationString() {
    return edqDataOfIncorporationString;
  }

  public void setEdqDataOfIncorporationString(String edqDataOfIncorporationString) {
    this.edqDataOfIncorporationString = edqDataOfIncorporationString;
  }

  /**
   * Customer ID the record relates to
   **/

  @Schema(description = "Customer ID the record relates to")
  @JsonProperty("externalCustomerId")
  public String getExternalCustomerId() {
    return externalCustomerId;
  }

  public void setExternalCustomerId(String externalCustomerId) {
    this.externalCustomerId = externalCustomerId;
  }

  /**
   * Indicates if the record is a Person or Business, P or B respectively.
   **/

  @Schema(description = "Indicates if the record is a Person or Business, P or B respectively.")
  @JsonProperty("listCustomerType")
  public String getListCustomerType() {
    return listCustomerType;
  }

  public void setListCustomerType(String listCustomerType) {
    this.listCustomerType = listCustomerType;
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
  @JsonProperty("edqWatchlistLstType")
  public String getEdqWatchlistLstType() {
    return edqWatchlistLstType;
  }

  public void setEdqWatchlistLstType(String edqWatchlistLstType) {
    this.edqWatchlistLstType = edqWatchlistLstType;
  }

  /**
   * Customers primary name
   **/

  @Schema(description = "Customers primary name")
  @JsonProperty("primaryName1")
  public String getPrimaryName1() {
    return primaryName1;
  }

  public void setPrimaryName1(String primaryName1) {
    this.primaryName1 = primaryName1;
  }

  /**
   * Entity name, place holder 1
   **/

  @Schema(description = "Entity name, place holder 1")
  @JsonProperty("entityName")
  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Entity name place holder 2
   **/

  @Schema(description = "Entity name place holder 2")
  @JsonProperty("entityName1")
  public String getEntityName1() {
    return entityName1;
  }

  public void setEntityName1(String entityName1) {
    this.entityName1 = entityName1;
  }

  /**
   * Entity name place holder 3
   **/

  @Schema(description = "Entity name place holder 3")
  @JsonProperty("entityName2")
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
    return Objects.equals(caseId, privateListEntities.caseId) &&
        Objects.equals(recordId, privateListEntities.recordId) &&
        Objects.equals(inputStream, privateListEntities.inputStream) &&
        Objects.equals(listKey, privateListEntities.listKey) &&
        Objects.equals(listSubKey, privateListEntities.listSubKey) &&
        Objects.equals(listRecordType, privateListEntities.listRecordType) &&
        Objects.equals(listRecordOrigin, privateListEntities.listRecordOrigin) &&
        Objects.equals(listRecordId, privateListEntities.listRecordId) &&
        Objects.equals(listRecordSubId, privateListEntities.listRecordSubId) &&
        Objects.equals(registrationNumber, privateListEntities.registrationNumber) &&
        Objects.equals(entityNameOriginal, privateListEntities.entityNameOriginal) &&
        Objects.equals(entityNameDerived, privateListEntities.entityNameDerived) &&
        Objects.equals(nameType, privateListEntities.nameType) &&
        Objects.equals(nameQuality, privateListEntities.nameQuality) &&
        Objects.equals(primaryName, privateListEntities.primaryName) &&
        Objects.equals(originalScriptName, privateListEntities.originalScriptName) &&
        Objects.equals(aliasIsAcronym, privateListEntities.aliasIsAcronym) &&
        Objects.equals(vesselIndicator, privateListEntities.vesselIndicator) &&
        Objects.equals(vesselInfo, privateListEntities.vesselInfo) &&
        Objects.equals(address, privateListEntities.address) &&
        Objects.equals(city, privateListEntities.city) &&
        Objects.equals(state, privateListEntities.state) &&
        Objects.equals(postalCode, privateListEntities.postalCode) &&
        Objects.equals(addressCountry, privateListEntities.addressCountry) &&
        Objects.equals(registrationCountry, privateListEntities.registrationCountry) &&
        Objects.equals(operatingCountries, privateListEntities.operatingCountries) &&
        Objects.equals(countryCodesAll, privateListEntities.countryCodesAll) &&
        Objects.equals(countriesAll, privateListEntities.countriesAll) &&
        Objects.equals(profileHyperlink, privateListEntities.profileHyperlink) &&
        Objects.equals(searchHyperlink, privateListEntities.searchHyperlink) &&
        Objects.equals(linkedProfiles, privateListEntities.linkedProfiles) &&
        Objects.equals(linkedRelationships, privateListEntities.linkedRelationships) &&
        Objects.equals(riskScore, privateListEntities.riskScore) &&
        Objects.equals(pepRiskScore, privateListEntities.pepRiskScore) &&
        Objects.equals(dataConfidenceScore, privateListEntities.dataConfidenceScore) &&
        Objects.equals(dataConfidenceComment, privateListEntities.dataConfidenceComment) &&
        Objects.equals(inactiveFlag, privateListEntities.inactiveFlag) &&
        Objects.equals(inactiveSinceDate, privateListEntities.inactiveSinceDate) &&
        Objects.equals(pepClassification, privateListEntities.pepClassification) &&
        Objects.equals(addedDate, privateListEntities.addedDate) &&
        Objects.equals(lastUpdatedDate, privateListEntities.lastUpdatedDate) &&
        Objects.equals(addressType, privateListEntities.addressType) &&
        Objects.equals(currentAddress, privateListEntities.currentAddress) &&
        Objects.equals(telephoneNumber, privateListEntities.telephoneNumber) &&
        Objects.equals(edqBusinessTelephoneNumber, privateListEntities.edqBusinessTelephoneNumber)
        &&
        Objects.equals(telephoneNumberBusiness, privateListEntities.telephoneNumberBusiness) &&
        Objects.equals(edqCellNumberPersonal, privateListEntities.edqCellNumberPersonal) &&
        Objects.equals(edqTaxNumber, privateListEntities.edqTaxNumber) &&
        Objects.equals(edqEmailAddress, privateListEntities.edqEmailAddress) &&
        Objects.equals(scionReferenceNumber, privateListEntities.scionReferenceNumber) &&
        Objects.equals(role, privateListEntities.role) &&
        Objects.equals(listCategory, privateListEntities.listCategory) &&
        Objects.equals(furtherInfo, privateListEntities.furtherInfo) &&
        Objects.equals(edqBusinessType, privateListEntities.edqBusinessType) &&
        Objects.equals(dateOfIncorporation, privateListEntities.dateOfIncorporation) &&
        Objects.equals(
            edqDataOfIncorporationString, privateListEntities.edqDataOfIncorporationString) &&
        Objects.equals(externalCustomerId, privateListEntities.externalCustomerId) &&
        Objects.equals(listCustomerType, privateListEntities.listCustomerType) &&
        Objects.equals(edqListName, privateListEntities.edqListName) &&
        Objects.equals(edqWatchlistLstType, privateListEntities.edqWatchlistLstType) &&
        Objects.equals(primaryName1, privateListEntities.primaryName1) &&
        Objects.equals(entityName, privateListEntities.entityName) &&
        Objects.equals(entityName1, privateListEntities.entityName1) &&
        Objects.equals(entityName2, privateListEntities.entityName2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, listKey, listSubKey, listRecordType, listRecordOrigin,
        listRecordId, listRecordSubId, registrationNumber, entityNameOriginal, entityNameDerived,
        nameType, nameQuality, primaryName, originalScriptName, aliasIsAcronym, vesselIndicator,
        vesselInfo, address, city, state, postalCode, addressCountry, registrationCountry,
        operatingCountries, countryCodesAll, countriesAll, profileHyperlink, searchHyperlink,
        linkedProfiles, linkedRelationships, riskScore, pepRiskScore, dataConfidenceScore,
        dataConfidenceComment, inactiveFlag, inactiveSinceDate, pepClassification, addedDate,
        lastUpdatedDate, addressType, currentAddress, telephoneNumber, edqBusinessTelephoneNumber,
        telephoneNumberBusiness, edqCellNumberPersonal, edqTaxNumber, edqEmailAddress,
        scionReferenceNumber, role, listCategory, furtherInfo, edqBusinessType, dateOfIncorporation,
        edqDataOfIncorporationString, externalCustomerId, listCustomerType, edqListName,
        edqWatchlistLstType, primaryName1, entityName, entityName1, entityName2);
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
    sb
        .append("    registrationCountry: ")
        .append(toIndentedString(registrationCountry))
        .append("\n");
    sb.append("    operatingCountries: ").append(toIndentedString(operatingCountries)).append("\n");
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
    sb.append("    addedDate: ").append(toIndentedString(addedDate)).append("\n");
    sb.append("    lastUpdatedDate: ").append(toIndentedString(lastUpdatedDate)).append("\n");
    sb.append("    addressType: ").append(toIndentedString(addressType)).append("\n");
    sb.append("    currentAddress: ").append(toIndentedString(currentAddress)).append("\n");
    sb.append("    telephoneNumber: ").append(toIndentedString(telephoneNumber)).append("\n");
    sb
        .append("    edqBusinessTelephoneNumber: ")
        .append(toIndentedString(edqBusinessTelephoneNumber))
        .append("\n");
    sb
        .append("    telephoneNumberBusiness: ")
        .append(toIndentedString(telephoneNumberBusiness))
        .append("\n");
    sb
        .append("    edqCellNumberPersonal: ")
        .append(toIndentedString(edqCellNumberPersonal))
        .append("\n");
    sb.append("    edqTaxNumber: ").append(toIndentedString(edqTaxNumber)).append("\n");
    sb.append("    edqEmailAddress: ").append(toIndentedString(edqEmailAddress)).append("\n");
    sb
        .append("    scionReferenceNumber: ")
        .append(toIndentedString(scionReferenceNumber))
        .append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    listCategory: ").append(toIndentedString(listCategory)).append("\n");
    sb.append("    furtherInfo: ").append(toIndentedString(furtherInfo)).append("\n");
    sb.append("    edqBusinessType: ").append(toIndentedString(edqBusinessType)).append("\n");
    sb
        .append("    dateOfIncorporation: ")
        .append(toIndentedString(dateOfIncorporation))
        .append("\n");
    sb
        .append("    edqDataOfIncorporationString: ")
        .append(toIndentedString(edqDataOfIncorporationString))
        .append("\n");
    sb.append("    externalCustomerId: ").append(toIndentedString(externalCustomerId)).append("\n");
    sb.append("    listCustomerType: ").append(toIndentedString(listCustomerType)).append("\n");
    sb.append("    edqListName: ").append(toIndentedString(edqListName)).append("\n");
    sb
        .append("    edqWatchlistLstType: ")
        .append(toIndentedString(edqWatchlistLstType))
        .append("\n");
    sb.append("    primaryName1: ").append(toIndentedString(primaryName1)).append("\n");
    sb.append("    entityName: ").append(toIndentedString(entityName)).append("\n");
    sb.append("    entityName1: ").append(toIndentedString(entityName1)).append("\n");
    sb.append("    entityName2: ").append(toIndentedString(entityName2)).append("\n");
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
