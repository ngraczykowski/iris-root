package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class WorldCheckEntities {

  private Integer caseId = null;
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
  private String category = null;
  private String externalSources = null;
  private String linkedTo = null;
  private String companies = null;
  private String furtherInformation = null;
  private String subCategoryDescription = null;
  private String updateCategory = null;
  private String nativeAliasLanguageName = null;
  private String nativeAliasLanguageCountry = null;
  private String cachedExternalSources = null;
  private String addedDate = null;
  private String lastUpdatedDate = null;
  private String edqWcListType = null;
  private String edqOwsWatchlistName = null;

  /**
   * Unique Identifier assigned to the Case or Alert within Case Management
   **/

  @Schema(description = "Unique Identifier assigned to the Case or Alert within Case Management")
  @JsonProperty("caseId")
  public Integer getCaseId() {
    return caseId;
  }

  public void setCaseId(Integer caseId) {
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
  @JsonProperty("edqWcListType")
  public String getEdqWcListType() {
    return edqWcListType;
  }

  public void setEdqWcListType(String edqWcListType) {
    this.edqWcListType = edqWcListType;
  }

  /**
   * The world check watchlist name is transposed into a HSBC equivalent.
   **/

  @Schema(description = "The world check watchlist name is transposed into a HSBC equivalent.")
  @JsonProperty("edqOwsWatchlistName")
  public String getEdqOwsWatchlistName() {
    return edqOwsWatchlistName;
  }

  public void setEdqOwsWatchlistName(String edqOwsWatchlistName) {
    this.edqOwsWatchlistName = edqOwsWatchlistName;
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
    return Objects.equals(caseId, worldCheckEntities.caseId) &&
        Objects.equals(recordId, worldCheckEntities.recordId) &&
        Objects.equals(inputStream, worldCheckEntities.inputStream) &&
        Objects.equals(listKey, worldCheckEntities.listKey) &&
        Objects.equals(listSubKey, worldCheckEntities.listSubKey) &&
        Objects.equals(listRecordType, worldCheckEntities.listRecordType) &&
        Objects.equals(listRecordOrigin, worldCheckEntities.listRecordOrigin) &&
        Objects.equals(listRecordId, worldCheckEntities.listRecordId) &&
        Objects.equals(listRecordSubId, worldCheckEntities.listRecordSubId) &&
        Objects.equals(registrationNumber, worldCheckEntities.registrationNumber) &&
        Objects.equals(entityNameOriginal, worldCheckEntities.entityNameOriginal) &&
        Objects.equals(entityNameDerived, worldCheckEntities.entityNameDerived) &&
        Objects.equals(nameType, worldCheckEntities.nameType) &&
        Objects.equals(nameQuality, worldCheckEntities.nameQuality) &&
        Objects.equals(primaryName, worldCheckEntities.primaryName) &&
        Objects.equals(originalScriptName, worldCheckEntities.originalScriptName) &&
        Objects.equals(aliasIsAcronym, worldCheckEntities.aliasIsAcronym) &&
        Objects.equals(vesselIndicator, worldCheckEntities.vesselIndicator) &&
        Objects.equals(vesselInfo, worldCheckEntities.vesselInfo) &&
        Objects.equals(address, worldCheckEntities.address) &&
        Objects.equals(city, worldCheckEntities.city) &&
        Objects.equals(state, worldCheckEntities.state) &&
        Objects.equals(postalCode, worldCheckEntities.postalCode) &&
        Objects.equals(addressCountry, worldCheckEntities.addressCountry) &&
        Objects.equals(registrationCountry, worldCheckEntities.registrationCountry) &&
        Objects.equals(operatingCountries, worldCheckEntities.operatingCountries) &&
        Objects.equals(countryCodesAll, worldCheckEntities.countryCodesAll) &&
        Objects.equals(countriesAll, worldCheckEntities.countriesAll) &&
        Objects.equals(profileHyperlink, worldCheckEntities.profileHyperlink) &&
        Objects.equals(searchHyperlink, worldCheckEntities.searchHyperlink) &&
        Objects.equals(linkedProfiles, worldCheckEntities.linkedProfiles) &&
        Objects.equals(linkedRelationships, worldCheckEntities.linkedRelationships) &&
        Objects.equals(riskScore, worldCheckEntities.riskScore) &&
        Objects.equals(pepRiskScore, worldCheckEntities.pepRiskScore) &&
        Objects.equals(dataConfidenceScore, worldCheckEntities.dataConfidenceScore) &&
        Objects.equals(dataConfidenceComment, worldCheckEntities.dataConfidenceComment) &&
        Objects.equals(inactiveFlag, worldCheckEntities.inactiveFlag) &&
        Objects.equals(inactiveSinceDate, worldCheckEntities.inactiveSinceDate) &&
        Objects.equals(pepClassification, worldCheckEntities.pepClassification) &&
        Objects.equals(category, worldCheckEntities.category) &&
        Objects.equals(externalSources, worldCheckEntities.externalSources) &&
        Objects.equals(linkedTo, worldCheckEntities.linkedTo) &&
        Objects.equals(companies, worldCheckEntities.companies) &&
        Objects.equals(furtherInformation, worldCheckEntities.furtherInformation) &&
        Objects.equals(subCategoryDescription, worldCheckEntities.subCategoryDescription) &&
        Objects.equals(updateCategory, worldCheckEntities.updateCategory) &&
        Objects.equals(nativeAliasLanguageName, worldCheckEntities.nativeAliasLanguageName) &&
        Objects.equals(nativeAliasLanguageCountry, worldCheckEntities.nativeAliasLanguageCountry) &&
        Objects.equals(cachedExternalSources, worldCheckEntities.cachedExternalSources) &&
        Objects.equals(addedDate, worldCheckEntities.addedDate) &&
        Objects.equals(lastUpdatedDate, worldCheckEntities.lastUpdatedDate) &&
        Objects.equals(edqWcListType, worldCheckEntities.edqWcListType) &&
        Objects.equals(edqOwsWatchlistName, worldCheckEntities.edqOwsWatchlistName);
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
        dataConfidenceComment, inactiveFlag, inactiveSinceDate, pepClassification, category,
        externalSources, linkedTo, companies, furtherInformation, subCategoryDescription,
        updateCategory, nativeAliasLanguageName, nativeAliasLanguageCountry, cachedExternalSources,
        addedDate, lastUpdatedDate, edqWcListType, edqOwsWatchlistName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldCheckEntities {\n");

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
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    externalSources: ").append(toIndentedString(externalSources)).append("\n");
    sb.append("    linkedTo: ").append(toIndentedString(linkedTo)).append("\n");
    sb.append("    companies: ").append(toIndentedString(companies)).append("\n");
    sb.append("    furtherInformation: ").append(toIndentedString(furtherInformation)).append("\n");
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
    sb.append("    edqWcListType: ").append(toIndentedString(edqWcListType)).append("\n");
    sb
        .append("    edqOwsWatchlistName: ")
        .append(toIndentedString(edqOwsWatchlistName))
        .append("\n");
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
