package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;


@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class CountryCtrpScreeningIndividuals {

  private int caseId;
  private BigDecimal recordId = null;
  private String inputStream = null;
  private String countryCode = null;
  private String typeOfMatch = null;
  private String countryName = null;
  private String matchType = null;
  private String owsWatchlistName = null;
  private String prbListType = null;
  private String ctrpValue = null;
  private Integer ctrpLevel = null;
  private Integer ctrpCommonality = null;
  private Integer ctrpSource = null;
  private String ctrpNameType = null;
  private String ctrpRuleNarrative = null;
  private String alertKeyFragment = null;

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
   * Refers to a Customer name record within an Alert where there are multiple Customer name records
   * within the same Alert. This is the working record ID.
   **/

  @Schema(description = "Refers to a Customer name record within an Alert where there are multiple Customer name records within the same Alert. This is the working record ID.")
  @JsonProperty("recordId")
  public BigDecimal getRecordId() {
    return recordId;
  }

  public void setRecordId(BigDecimal recordId) {
    this.recordId = recordId;
  }

  /**
   * Describe where the alert was generated
   **/

  @Schema(description = "Describe where the alert was generated")
  @JsonProperty("inputStream")
  public String getInputStream() {
    return inputStream;
  }

  public void setInputStream(String inputStream) {
    this.inputStream = inputStream;
  }

  /**
   * The field provides the country code which raised the alert. This will always be a two character
   * code
   **/

  @Schema(description = "The field provides the country code which raised the alert. This will always be a two character code")
  @JsonProperty("countryCode")
  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  /**
   * Describes the type of country match, this will currently be&amp;#58; Country Match or CTRP
   * Match
   **/

  @Schema(description = "Describes the type of country match, this will currently be&#58; Country Match or CTRP Match")
  @JsonProperty("typeOfMatch")
  public String getTypeOfMatch() {
    return typeOfMatch;
  }

  public void setTypeOfMatch(String typeOfMatch) {
    this.typeOfMatch = typeOfMatch;
  }

  /**
   * The field provides the country name which raised the alert.
   **/

  @Schema(description = "The field provides the country name which raised the alert.")
  @JsonProperty("countryName")
  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  /**
   * This list one of three match types&amp;#58; Nationality or Residency
   **/

  @Schema(description = "This list one of three match types&#58; Nationality or Residency")
  @JsonProperty("matchType")
  public String getMatchType() {
    return matchType;
  }

  public void setMatchType(String matchType) {
    this.matchType = matchType;
  }

  /**
   * This is the specific type of match that has ben identified, it is a sub-type of PRB List Type.
   * Currently for Individuals this will always be ISSC.
   **/

  @Schema(description = "This is the specific type of match that has ben identified, it is a sub-type of PRB List Type. Currently for Individuals this will always be ISSC.")
  @JsonProperty("owsWatchlistName")
  public String getOwsWatchlistName() {
    return owsWatchlistName;
  }

  public void setOwsWatchlistName(String owsWatchlistName) {
    this.owsWatchlistName = owsWatchlistName;
  }

  /**
   * This is the type of match that has been found it is parent group of OWS Watchlist Name,
   * currently it will always be SSC
   **/

  @Schema(description = "This is the type of match that has been found it is parent group of OWS Watchlist Name, currently it will always be SSC")
  @JsonProperty("prbListType")
  public String getPrbListType() {
    return prbListType;
  }

  public void setPrbListType(String prbListType) {
    this.prbListType = prbListType;
  }

  /**
   * List the specific CTRP value that triggered the alert. For example if this is Havana, Country
   * Name will be CUBA
   **/

  @Schema(description = "List the specific CTRP value that triggered the alert. For example if this is Havana, Country Name will be CUBA")
  @JsonProperty("ctrpValue")
  public String getCtrpValue() {
    return ctrpValue;
  }

  public void setCtrpValue(String ctrpValue) {
    this.ctrpValue = ctrpValue;
  }

  /**
   * Type of CTRP Record e.g. 1 &#x3D;Country, 2&#x3D;Postcode
   **/

  @Schema(description = "Type of CTRP Record e.g. 1 =Country, 2=Postcode")
  @JsonProperty("ctrpLevel")
  public Integer getCtrpLevel() {
    return ctrpLevel;
  }

  public void setCtrpLevel(Integer ctrpLevel) {
    this.ctrpLevel = ctrpLevel;
  }

  /**
   * The number of countries in the world that feature a place of this name
   **/

  @Schema(description = "The number of countries in the world that feature a place of this name")
  @JsonProperty("ctrpCommonality")
  public Integer getCtrpCommonality() {
    return ctrpCommonality;
  }

  public void setCtrpCommonality(Integer ctrpCommonality) {
    this.ctrpCommonality = ctrpCommonality;
  }

  /**
   * This provides a description of the CTRP Source element.
   **/

  @Schema(description = "This provides a description of the CTRP Source element.")
  @JsonProperty("ctrpSource")
  public Integer getCtrpSource() {
    return ctrpSource;
  }

  public void setCtrpSource(Integer ctrpSource) {
    this.ctrpSource = ctrpSource;
  }

  /**
   * describes the CTRP type that matched
   **/

  @Schema(description = "describes the CTRP type that matched")
  @JsonProperty("ctrpNameType")
  public String getCtrpNameType() {
    return ctrpNameType;
  }

  public void setCtrpNameType(String ctrpNameType) {
    this.ctrpNameType = ctrpNameType;
  }

  /**
   * Provides a description of what the rule was raised against
   **/

  @Schema(description = "Provides a description of what the rule was raised against")
  @JsonProperty("ctrpRuleNarrative")
  public String getCtrpRuleNarrative() {
    return ctrpRuleNarrative;
  }

  public void setCtrpRuleNarrative(String ctrpRuleNarrative) {
    this.ctrpRuleNarrative = ctrpRuleNarrative;
  }

  /**
   * Provides the full code for the match type that will be used to create the Alert Key.
   **/

  @Schema(description = "Provides the full code for the match type that will be used to create the Alert Key.")
  @JsonProperty("alertKeyFragment")
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
    CountryCtrpScreeningIndividuals countryCtrpScreeningIndividuals =
        (CountryCtrpScreeningIndividuals) o;
    return Objects.equals(caseId, countryCtrpScreeningIndividuals.caseId) &&
        Objects.equals(recordId, countryCtrpScreeningIndividuals.recordId) &&
        Objects.equals(inputStream, countryCtrpScreeningIndividuals.inputStream) &&
        Objects.equals(countryCode, countryCtrpScreeningIndividuals.countryCode) &&
        Objects.equals(typeOfMatch, countryCtrpScreeningIndividuals.typeOfMatch) &&
        Objects.equals(countryName, countryCtrpScreeningIndividuals.countryName) &&
        Objects.equals(matchType, countryCtrpScreeningIndividuals.matchType) &&
        Objects.equals(owsWatchlistName, countryCtrpScreeningIndividuals.owsWatchlistName) &&
        Objects.equals(prbListType, countryCtrpScreeningIndividuals.prbListType) &&
        Objects.equals(ctrpValue, countryCtrpScreeningIndividuals.ctrpValue) &&
        Objects.equals(ctrpLevel, countryCtrpScreeningIndividuals.ctrpLevel) &&
        Objects.equals(ctrpCommonality, countryCtrpScreeningIndividuals.ctrpCommonality) &&
        Objects.equals(ctrpSource, countryCtrpScreeningIndividuals.ctrpSource) &&
        Objects.equals(ctrpNameType, countryCtrpScreeningIndividuals.ctrpNameType) &&
        Objects.equals(ctrpRuleNarrative, countryCtrpScreeningIndividuals.ctrpRuleNarrative) &&
        Objects.equals(alertKeyFragment, countryCtrpScreeningIndividuals.alertKeyFragment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        caseId, recordId, inputStream, countryCode, typeOfMatch, countryName, matchType,
        owsWatchlistName, prbListType, ctrpValue, ctrpLevel, ctrpCommonality, ctrpSource,
        ctrpNameType, ctrpRuleNarrative, alertKeyFragment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CountryCtrpScreeningIndividuals {\n");

    sb.append("    caseId: ").append(toIndentedString(caseId)).append("\n");
    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    inputStream: ").append(toIndentedString(inputStream)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    typeOfMatch: ").append(toIndentedString(typeOfMatch)).append("\n");
    sb.append("    countryName: ").append(toIndentedString(countryName)).append("\n");
    sb.append("    matchType: ").append(toIndentedString(matchType)).append("\n");
    sb.append("    owsWatchlistName: ").append(toIndentedString(owsWatchlistName)).append("\n");
    sb.append("    prbListType: ").append(toIndentedString(prbListType)).append("\n");
    sb.append("    ctrpValue: ").append(toIndentedString(ctrpValue)).append("\n");
    sb.append("    ctrpLevel: ").append(toIndentedString(ctrpLevel)).append("\n");
    sb.append("    ctrpCommonality: ").append(toIndentedString(ctrpCommonality)).append("\n");
    sb.append("    ctrpSource: ").append(toIndentedString(ctrpSource)).append("\n");
    sb.append("    ctrpNameType: ").append(toIndentedString(ctrpNameType)).append("\n");
    sb.append("    ctrpRuleNarrative: ").append(toIndentedString(ctrpRuleNarrative)).append("\n");
    sb.append("    alertKeyFragment: ").append(toIndentedString(alertKeyFragment)).append("\n");
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
