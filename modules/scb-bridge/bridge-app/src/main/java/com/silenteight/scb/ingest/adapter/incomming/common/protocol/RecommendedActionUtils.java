package com.silenteight.scb.ingest.adapter.incomming.common.protocol;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

@UtilityClass
public class RecommendedActionUtils {

  public String nameWithoutPrefix(Recommendations.RecommendedAction action) {
    return action.name().substring(7);
  }
}