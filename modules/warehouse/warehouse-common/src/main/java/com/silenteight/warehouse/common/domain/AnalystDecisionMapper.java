package com.silenteight.warehouse.common.domain;

import lombok.Getter;
import lombok.NonNull;

import com.silenteight.warehouse.common.EnumPropertiesUtil;
import com.silenteight.warehouse.common.properties.AnalystDecisionProperties;

import java.util.Map;

public final class AnalystDecisionMapper {

  @NonNull
  @Getter
  private final String analystDecisionFieldName;

  @NonNull
  private final Map<String, AnalystDecision> analystDecisionMap;

  public AnalystDecisionMapper(AnalystDecisionProperties properties) {
    analystDecisionFieldName = properties.getFieldName();
    analystDecisionMap =
        EnumPropertiesUtil.mapPropertiesToEnum(AnalystDecision.class, properties.getValues());

  }

  public AnalystDecision getAnalystDecisionByValue(Map<String, String> payload) {
    var recommendationFromPayload =
        payload.get(analystDecisionFieldName);
    return analystDecisionMap.getOrDefault(
        recommendationFromPayload, AnalystDecision.UNSPECIFIED);
  }
}
