package com.silenteight.payments.bridge.datasource.agent.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;

@Value
@Builder
public class FeatureInputUnstructured {

  String matchName;

  String alertName;

  ContextualAgentData contextualAgentData;

  NameMatchedTextAgent nameMatchedTextAgentData;

  @Value
  @Builder
  public static class NameMatchedTextAgent {

    String watchlistName;

    List<String> alertedPartyName;

    WatchlistType watchlistType;

    List<String> matchingTexts;

  }

  @Value
  @Builder
  public static class ContextualAgentData {

    String ofacId;

    String watchlistType;

    String matchingField;

    String matchText;

  }
}
