package com.silenteight.hsbc.bridge.json.external.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class CtrpScreeningEntity {

  @JsonProperty("CTRPPRHBListEntities.Record Id")
  private String recordId;
  @JsonProperty("CTRPPRHBListEntities.Input Stream")
  private String inputStream;
  @JsonProperty("CTRPPRHBListEntities.Country Code")
  private String countryCode;
  @JsonProperty("CTRPPRHBListEntities.Type of Match")
  private String typeOfMatch;
  @JsonProperty("CTRPPRHBListEntities.Country Name")
  private String countryName;
  @JsonProperty("CTRPPRHBListEntities.Match Type")
  private String matchType;
  @JsonProperty("CTRPPRHBListEntities.OWS Watchlist Name")
  private String owsWatchlistName;
  @JsonProperty("CTRPPRHBListEntities.PRB List Type")
  private String prbListType;
  @JsonProperty("CTRPPRHBListEntities.CTRP Value")
  private String ctrpValue;
  @JsonProperty("CTRPPRHBListEntities.CTRP Level")
  private String ctrpLevel;
  @JsonProperty("CTRPPRHBListEntities.CTRP Commonality")
  private String ctrpCommonality;
  @JsonProperty("CTRPPRHBListEntities.CTRP Source")
  private String ctrpSource;
  @JsonProperty("CTRPPRHBListEntities.CTRP Name Type")
  private String ctrpNameType;
  @JsonProperty("CTRPPRHBListEntities.CTRP Rule Narrative")
  private String ctrpRuleNarrative;
  @JsonProperty("CTRPPRHBListEntities.AlertKeyFragment")
  private String alertKeyFragment;
}
