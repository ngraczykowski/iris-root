package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;

import java.util.List;

@Value
@Builder
public class HitAndWatchlistPartyData {

  SolutionType solutionType;
  WatchlistType watchlistType;
  String tag;
  String id;
  String name;
  String entityText;
  String matchingText;
  List<String> allMatchingTexts;
  String fieldValue;
  List<String> allMatchingFieldValues;
  List<String> postalAddresses;
  List<String> cities;
  List<String> states;
  List<String> countries;
  boolean mainAddress;
  String origin;
  String designation;
  String direction;
  List<String> searchCodes;
  List<String> passports;
  List<String> natIds;
  List<String> bics;

  public ContextualLearningAgentRequest contextualLearningAgentRequest() {
    return ContextualLearningAgentRequest.builder()
        .ofacId(id.toUpperCase().trim())
        .watchlistType(watchlistType.toString())
        .matchingField(getMatchingField())
        .matchText(matchingText)
        .build();
  }

  private String getMatchingField() {
    return allMatchingFieldValues.stream().findFirst().orElse("");
  }
}
