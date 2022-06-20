/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.protocol;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations;

@UtilityClass
public class RecommendedActionUtils {

  public String nameWithoutPrefix(Recommendations.RecommendedAction action) {
    return action.name().substring(7);
  }
}
