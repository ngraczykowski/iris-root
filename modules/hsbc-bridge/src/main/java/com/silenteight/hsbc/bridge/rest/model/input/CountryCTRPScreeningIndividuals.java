package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * CountryCTRPScreeningIndividuals
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class CountryCTRPScreeningIndividuals   {
  @JsonProperty("caseId")
  private Integer caseId = null;

  @JsonProperty("recordId")
  private BigDecimal recordId = null;

  @JsonProperty("inputStream")
  private String inputStream = null;

  @JsonProperty("countryCode")
  private String countryCode = null;

  @JsonProperty("typeOfMatch")
  private String typeOfMatch = null;

  @JsonProperty("countryName")
  private String countryName = null;

  @JsonProperty("matchType")
  private String matchType = null;

  @JsonProperty("oWSWatchlistName")
  private String oWSWatchlistName = null;

  @JsonProperty("pRBListType")
  private String pRBListType = null;

  @JsonProperty("cTRPValue")
  private String cTRPValue = null;

  @JsonProperty("cTRPLevel")
  private Integer cTRPLevel = null;

  @JsonProperty("cTRPCommonality")
  private Integer cTRPCommonality = null;

  @JsonProperty("cTRPSource")
  private Integer cTRPSource = null;

  @JsonProperty("cTRPNameType")
  private String cTRPNameType = null;

  @JsonProperty("cTRPRuleNarrative")
  private String cTRPRuleNarrative = null;

  @JsonProperty("alertKeyFragment")
  private String alertKeyFragment = null;

  public CountryCTRPScreeningIndividuals caseId(Integer caseId) {
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

  public CountryCTRPScreeningIndividuals recordId(BigDecimal recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.
   * @return recordId
   **/
  @Schema(description = "Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.")
  
    @Valid
    public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  public CountryCTRPScreeningIndividuals inputStream(String inputStream) {
    this.inputStream = inputStream;
    return this;
  }

  /**
   * Describe where the alert was generated
   * @return inputStream
   **/
  @Schema(description = "Describe where the alert was generated")
  
    public String getInputStream() {
    return inputStream;
  }

  public void setInputStream(String inputStream) {
    this.inputStream = inputStream;
  }

  public CountryCTRPScreeningIndividuals countryCode(String countryCode) {
    this.countryCode = countryCode;
    return this;
  }

  /**
   * The field provides the country code which raised the alert. This will always be a two character code
   * @return countryCode
   **/
  @Schema(description = "The field provides the country code which raised the alert. This will always be a two character code")
  
    public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public CountryCTRPScreeningIndividuals typeOfMatch(String typeOfMatch) {
    this.typeOfMatch = typeOfMatch;
    return this;
  }

  /**
   * Describes the type of country match, this will currently be&#58; Country Match or CTRP Match
   * @return typeOfMatch
   **/
  @Schema(description = "Describes the type of country match, this will currently be&#58; Country Match or CTRP Match")
  
    public String getTypeOfMatch() {
    return typeOfMatch;
  }

  public void setTypeOfMatch(String typeOfMatch) {
    this.typeOfMatch = typeOfMatch;
  }

  public CountryCTRPScreeningIndividuals countryName(String countryName) {
    this.countryName = countryName;
    return this;
  }

  /**
   * The field provides the country name which raised the alert.
   * @return countryName
   **/
  @Schema(description = "The field provides the country name which raised the alert.")
  
    public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public CountryCTRPScreeningIndividuals matchType(String matchType) {
    this.matchType = matchType;
    return this;
  }

  /**
   * This list one of three match types&#58; Nationality or Residency
   * @return matchType
   **/
  @Schema(description = "This list one of three match types&#58; Nationality or Residency")
  
    public String getMatchType() {
    return matchType;
  }

  public void setMatchType(String matchType) {
    this.matchType = matchType;
  }

  public CountryCTRPScreeningIndividuals oWSWatchlistName(String oWSWatchlistName) {
    this.oWSWatchlistName = oWSWatchlistName;
    return this;
  }

  /**
   * This is the specific type of match that has ben identified, it is a sub-type of PRB List Type. Currently for Individuals this will always be ISSC.
   * @return oWSWatchlistName
   **/
  @Schema(description = "This is the specific type of match that has ben identified, it is a sub-type of PRB List Type. Currently for Individuals this will always be ISSC.")
  
    public String getOWSWatchlistName() {
    return oWSWatchlistName;
  }

  public void setOWSWatchlistName(String oWSWatchlistName) {
    this.oWSWatchlistName = oWSWatchlistName;
  }

  public CountryCTRPScreeningIndividuals pRBListType(String pRBListType) {
    this.pRBListType = pRBListType;
    return this;
  }

  /**
   * This is the type of match that has been found it is parent group of OWS Watchlist Name, currently it will always be SSC
   * @return pRBListType
   **/
  @Schema(description = "This is the type of match that has been found it is parent group of OWS Watchlist Name, currently it will always be SSC")
  
    public String getPRBListType() {
    return pRBListType;
  }

  public void setPRBListType(String pRBListType) {
    this.pRBListType = pRBListType;
  }

  public CountryCTRPScreeningIndividuals cTRPValue(String cTRPValue) {
    this.cTRPValue = cTRPValue;
    return this;
  }

  /**
   * List the specific CTRP value that triggered the alert. For example if this is Havana, Country Name will be CUBA
   * @return cTRPValue
   **/
  @Schema(description = "List the specific CTRP value that triggered the alert. For example if this is Havana, Country Name will be CUBA")
  
    public String getCTRPValue() {
    return cTRPValue;
  }

  public void setCTRPValue(String cTRPValue) {
    this.cTRPValue = cTRPValue;
  }

  public CountryCTRPScreeningIndividuals cTRPLevel(Integer cTRPLevel) {
    this.cTRPLevel = cTRPLevel;
    return this;
  }

  /**
   * Type of CTRP Record e.g. 1 =Country, 2=Postcode
   * @return cTRPLevel
   **/
  @Schema(description = "Type of CTRP Record e.g. 1 =Country, 2=Postcode")
  
    public Integer getCTRPLevel() {
    return cTRPLevel;
  }

  public void setCTRPLevel(Integer cTRPLevel) {
    this.cTRPLevel = cTRPLevel;
  }

  public CountryCTRPScreeningIndividuals cTRPCommonality(Integer cTRPCommonality) {
    this.cTRPCommonality = cTRPCommonality;
    return this;
  }

  /**
   * The number of countries in the world that feature a place of this name
   * @return cTRPCommonality
   **/
  @Schema(description = "The number of countries in the world that feature a place of this name")
  
    public Integer getCTRPCommonality() {
    return cTRPCommonality;
  }

  public void setCTRPCommonality(Integer cTRPCommonality) {
    this.cTRPCommonality = cTRPCommonality;
  }

  public CountryCTRPScreeningIndividuals cTRPSource(Integer cTRPSource) {
    this.cTRPSource = cTRPSource;
    return this;
  }

  /**
   * This provides a description of the CTRP Source element.
   * @return cTRPSource
   **/
  @Schema(description = "This provides a description of the CTRP Source element.")
  
    public Integer getCTRPSource() {
    return cTRPSource;
  }

  public void setCTRPSource(Integer cTRPSource) {
    this.cTRPSource = cTRPSource;
  }

  public CountryCTRPScreeningIndividuals cTRPNameType(String cTRPNameType) {
    this.cTRPNameType = cTRPNameType;
    return this;
  }

  /**
   * describes the CTRP type that matched
   * @return cTRPNameType
   **/
  @Schema(description = "describes the CTRP type that matched")
  
    public String getCTRPNameType() {
    return cTRPNameType;
  }

  public void setCTRPNameType(String cTRPNameType) {
    this.cTRPNameType = cTRPNameType;
  }

  public CountryCTRPScreeningIndividuals cTRPRuleNarrative(String cTRPRuleNarrative) {
    this.cTRPRuleNarrative = cTRPRuleNarrative;
    return this;
  }

  /**
   * Provides a description of what the rule was raised against
   * @return cTRPRuleNarrative
   **/
  @Schema(description = "Provides a description of what the rule was raised against")
  
    public String getCTRPRuleNarrative() {
    return cTRPRuleNarrative;
  }

  public void setCTRPRuleNarrative(String cTRPRuleNarrative) {
    this.cTRPRuleNarrative = cTRPRuleNarrative;
  }

  public CountryCTRPScreeningIndividuals alertKeyFragment(String alertKeyFragment) {
    this.alertKeyFragment = alertKeyFragment;
    return this;
  }

  /**
   * Provides the full code for the match type that will be used to create the Alert Key.
   * @return alertKeyFragment
   **/
  @Schema(description = "Provides the full code for the match type that will be used to create the Alert Key.")
  
    public String getAlertKeyFragment() {
    return alertKeyFragment;
  }

  public void setAlertKeyFragment(String alertKeyFragment) {
    this.alertKeyFragment = alertKeyFragment;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CountryCTRPScreeningIndividuals countryCTRPScreeningIndividuals = (CountryCTRPScreeningIndividuals) o;
    return Objects.equals(this.caseId, countryCTRPScreeningIndividuals.caseId) &&
        Objects.equals(this.recordId, countryCTRPScreeningIndividuals.recordId) &&
        Objects.equals(this.inputStream, countryCTRPScreeningIndividuals.inputStream) &&
        Objects.equals(this.countryCode, countryCTRPScreeningIndividuals.countryCode) &&
        Objects.equals(this.typeOfMatch, countryCTRPScreeningIndividuals.typeOfMatch) &&
        Objects.equals(this.countryName, countryCTRPScreeningIndividuals.countryName) &&
        Objects.equals(this.matchType, countryCTRPScreeningIndividuals.matchType) &&
        Objects.equals(this.oWSWatchlistName, countryCTRPScreeningIndividuals.oWSWatchlistName) &&
        Objects.equals(this.pRBListType, countryCTRPScreeningIndividuals.pRBListType) &&
        Objects.equals(this.cTRPValue, countryCTRPScreeningIndividuals.cTRPValue) &&
        Objects.equals(this.cTRPLevel, countryCTRPScreeningIndividuals.cTRPLevel) &&
        Objects.equals(this.cTRPCommonality, countryCTRPScreeningIndividuals.cTRPCommonality) &&
        Objects.equals(this.cTRPSource, countryCTRPScreeningIndividuals.cTRPSource) &&
        Objects.equals(this.cTRPNameType, countryCTRPScreeningIndividuals.cTRPNameType) &&
        Objects.equals(this.cTRPRuleNarrative, countryCTRPScreeningIndividuals.cTRPRuleNarrative) &&
        Objects.equals(this.alertKeyFragment, countryCTRPScreeningIndividuals.alertKeyFragment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseId, recordId, inputStream, countryCode, typeOfMatch, countryName, matchType, oWSWatchlistName, pRBListType, cTRPValue, cTRPLevel, cTRPCommonality, cTRPSource, cTRPNameType, cTRPRuleNarrative, alertKeyFragment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CountryCTRPScreeningIndividuals {\n");
    
    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    typeOfMatch: ").append(toIndentedString(typeOfMatch)).append("\n");
    sb.append("    countryName: ").append(toIndentedString(countryName)).append("\n");
    sb.append("    matchType: ").append(toIndentedString(matchType)).append("\n");
    sb.append("    oWSWatchlistName: ").append(toIndentedString(oWSWatchlistName)).append("\n");
    sb.append("    pRBListType: ").append(toIndentedString(pRBListType)).append("\n");
    sb.append("    cTRPValue: ").append(toIndentedString(cTRPValue)).append("\n");
    sb.append("    cTRPLevel: ").append(toIndentedString(cTRPLevel)).append("\n");
    sb.append("    cTRPCommonality: ").append(toIndentedString(cTRPCommonality)).append("\n");
    sb.append("    cTRPSource: ").append(toIndentedString(cTRPSource)).append("\n");
    sb.append("    cTRPNameType: ").append(toIndentedString(cTRPNameType)).append("\n");
    sb.append("    cTRPRuleNarrative: ").append(toIndentedString(cTRPRuleNarrative)).append("\n");
    sb.append("    alertKeyFragment: ").append(toIndentedString(alertKeyFragment)).append("\n");
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
