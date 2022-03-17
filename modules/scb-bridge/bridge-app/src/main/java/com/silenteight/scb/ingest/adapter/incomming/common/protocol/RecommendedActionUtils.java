package com.silenteight.scb.ingest.adapter.incomming.common.protocol;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.RecommendedAction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RecommendedActionUtils {

  public static String nameWithoutPrefix(RecommendedAction action) {
    return action.name().substring(7);
  }
}