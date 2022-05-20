package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest.ContextualLearningAgentRequestBuilder;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.ContextualAgentData;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.NameMatchedTextAgent;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Value
@Builder
public class HitComposite {

  long hitId;

  String fkcoVMatchedTag;

  String fkcoISequence;

  String fkcoVListFmmId;

  String fkcoVListCity;

  String fkcoVListState;

  String fkcoVListCountry;

  String fkcoVMatchedTagContent;

  String fkcoVCityMatchedText;

  String fkcoVStateMatchedText;

  String fkcoVCountryMatchedText;

  String fkcoVAddressMatchedText;

  String fkcoVListMatchedName;

  String fkcoVHitType;

  String fkcoVListType;

  String fkcoVListName;

  String fkcoVNameMatchedText;

  public String getMatchId() {
    return fkcoVListFmmId + "(" + fkcoVMatchedTag + ", #" + fkcoISequence
        + ")";
  }

  public SpecificTermsRequest toSpecificTermsRequest() {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(fkcoVMatchedTagContent)
        .build();
  }

  public String getWatchlistLocation() {
    return String.join(", ", List.of(fkcoVListCountry, fkcoVListState, fkcoVListCity));
  }

  public List<String> getMatchingTexts() {
    var matchingText = newHashSet(
        fkcoVCityMatchedText,
        fkcoVAddressMatchedText,
        fkcoVStateMatchedText,
        fkcoVCountryMatchedText,
        fkcoVNameMatchedText);
    return filterEmptyFields(matchingText);
  }

  private static Set<String> newHashSet(String... objs) {
    Set<String> set = new HashSet<>();
    Collections.addAll(set, objs);
    return set;
  }

  private static List<String> filterEmptyFields(Set<String> matchingText) {
    return matchingText
        .stream()
        .filter(s -> !s.equalsIgnoreCase("N/A"))
        .collect(toList());
  }

  public List<String> getSearchCodes() {
    var matchedNames = new ArrayList<>(Arrays.asList(fkcoVListMatchedName.split(",")));
    var codes = ObjectUtils.defaultIfNull(matchedNames, new ArrayList<String>());

    if (fkcoVHitType.contains("EMBARGO")) {
      codes.addAll(List.of(fkcoVListCountry.split(",")));
    }

    return codes;
  }

  public List<String> getWatchlistNames() {
    return List.of(getFkcoVListName().split(","));
  }

  public WatchlistType getWatchlistType() {
    return WatchlistType.ofCode(fkcoVListType);
  }

  public List<String> getMatchedNames() {
    return new ArrayList<>(Arrays.asList(fkcoVListMatchedName.split(",")));
  }

  public EtlHit toEtlHit(
      AlertedPartyData alertedPartyData, Map<AlertedPartyKey, String> alertedPartyEntities) {
    return EtlHit
        .builder()
        .hitComposite(this)
        .alertedPartyData(alertedPartyData)
        .alertedPartyEntities(alertedPartyEntities)
        .build();
  }

  Optional<String> getFirstWatchlistName() {
    return getWatchlistNames().stream().findFirst();
  }

  public SolutionType getSolutionType() {
    var hitType = getFkcoVHitType();
    var solutionType = hitType.replace(" ", "_");

    if (!EnumUtils.isValidEnum(SolutionType.class, solutionType)) {
      log.warn("Solution type: {} is not valid", solutionType);
      return SolutionType.UNKNOWN;
    }
    return SolutionType.valueOf(solutionType);
  }

  public ContextualLearningAgentRequestBuilder createContextualAgentRequestBuilder() {
    return ContextualLearningAgentRequest.builder()
        .ofacId(fkcoVListFmmId)
        .watchlistType(fkcoVListType)
        .matchingField(fkcoVMatchedTagContent)
        .matchText(fkcoVNameMatchedText);
  }

  public ContextualAgentData getContextualAgentData() {
    return ContextualAgentData.builder()
        .ofacId(fkcoVListFmmId)
        .watchlistType(fkcoVListType)
        .matchingField(fkcoVMatchedTagContent)
        .matchText(fkcoVNameMatchedText)
        .build();
  }

  public NameMatchedTextAgent getNameMatchedTextAgent() {
    return NameMatchedTextAgent.builder()
        .watchlistName(fkcoVListMatchedName)
        .alertedPartyName(List.of(fkcoVNameMatchedText))
        .watchlistType(getWatchlistType())
        .matchingTexts(List.of(fkcoVNameMatchedText))
        .build();
  }

}
