package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;

@Value
@Builder
public class NameAgentRequest {

  String feature;

  List<String> watchlistNames;

  List<String> alertedPartyNames;

  WatchlistType watchlistType;

  List<String> matchingTexts;

}
