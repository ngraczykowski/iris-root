package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest.ContextualLearningAgentRequestBuilder;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.ContextualAgentData;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.NameMatchedTextAgent;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured.CategoryValueUnstructuredBuilder;

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

  public ContextualLearningAgentRequestBuilder contextualLearningAgentRequestBuilder() {
    return ContextualLearningAgentRequest.builder()
        .ofacId(getOfacId())
        .watchlistType(watchlistType.toString())
        .matchingField(getMatchingField())
        .matchText(matchingText);
  }

  public ContextualAgentData getContextualAgentData() {
    return ContextualAgentData.builder()
        .ofacId(getOfacId())
        .watchlistType(getWatchlistType().toString())
        .matchingField(getMatchingField())
        .matchText(matchingText)
        .build();
  }

  public NameMatchedTextAgent getNameMatchedTextAgent() {
    var alertedPartyName = allMatchingTexts.isEmpty() ? "" : allMatchingTexts.get(0);

    return NameMatchedTextAgent.builder()
        .watchlistName(name)
        .alertedPartyName(List.of(alertedPartyName))
        .watchlistType(watchlistType)
        .matchingTexts(allMatchingTexts)
        .build();
  }

  private String getOfacId() {
    return id.toUpperCase().trim();
  }

  private String getMatchingField() {
    return allMatchingFieldValues.stream().findFirst().orElse("");
  }

  public CategoryValueUnstructuredBuilder toCategoryValueUnstructuredModelBuilder() {
    return CategoryValueUnstructured.builder()
        .tag(tag)
        .solutionType(solutionType)
        .watchlistType(watchlistType)
        .allMatchingFieldValues(String.join(" ", allMatchingFieldValues));
  }
}
