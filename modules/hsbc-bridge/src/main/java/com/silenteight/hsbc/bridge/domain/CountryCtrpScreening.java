package com.silenteight.hsbc.bridge.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountryCtrpScreening {

  private Integer caseId;
  private BigDecimal recordId;
  private String inputStream;
  private String countryCode;
  private String typeOfMatch;
  private String countryName;
  private String matchType;
  private String owsWatchlistName;
  private String prbListType;
  private String ctrpValue;
  private Integer ctrpLevel;
  private Integer ctrpCommonality;
  private Integer ctrpSource;
  private String ctrpNameType;
  private String ctrpRuleNarrative;
  private String alertKeyFragment;
}
