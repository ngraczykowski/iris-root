package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CtrpScreeningIndividual {

  @JsonProperty("CTRPPRHBListIndividuals.Record Id")
  private String recordId;
  @JsonProperty("CTRPPRHBListIndividuals.Input Stream")
  private String inputStream;
  @JsonProperty("CTRPPRHBListIndividuals.Country Code")
  private String countryCode;
  @JsonProperty("CTRPPRHBListIndividuals.Type of Match")
  private String typeOfMatch;
  @JsonProperty("CTRPPRHBListIndividuals.Country Name")
  private String countryName;
  @JsonProperty("CTRPPRHBListIndividuals.Match Type")
  private String matchType;
  @JsonProperty("CTRPPRHBListIndividuals.OWS Watchlist Name")
  private String owsWatchlistName;
  @JsonProperty("CTRPPRHBListIndividuals.PRB List Type")
  private String prbListType;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Value")
  private String ctrpValue;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Level")
  private Integer ctrpLevel;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Commonality")
  private Integer ctrpCommonality;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Source")
  private Integer ctrpSource;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Name Type")
  private String ctrpNameType;
  @JsonProperty("CTRPPRHBListIndividuals.CTRP Rule Narrative")
  private String ctrpRuleNarrative;
  @JsonProperty("CTRPPRHBListIndividuals.AlertKeyFragment")
  private String alertKeyFragment;
}
