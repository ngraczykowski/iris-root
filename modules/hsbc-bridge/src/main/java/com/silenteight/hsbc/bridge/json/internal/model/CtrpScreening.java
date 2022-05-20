package com.silenteight.hsbc.bridge.json.internal.model;

import lombok.Data;

@Data
public class CtrpScreening implements com.silenteight.hsbc.datasource.datamodel.CtrpScreening {

  private String recordId;
  private String inputStream;
  private String countryCode;
  private String typeOfMatch;
  private String countryName;
  private String matchType;
  private String owsWatchlistName;
  private String prbListType;
  private String ctrpValue;
  private String ctrpLevel;
  private String ctrpCommonality;
  private String ctrpSource;
  private String ctrpNameType;
  private String ctrpRuleNarrative;
  private String alertKeyFragment;
}
