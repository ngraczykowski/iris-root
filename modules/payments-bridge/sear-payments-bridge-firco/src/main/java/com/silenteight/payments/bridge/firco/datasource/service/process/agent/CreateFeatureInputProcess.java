package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.exception.MatchNotFoundException;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.datasource.agent.CreateFeatureInputsProcess;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateFeatureInputProcess implements CreateFeatureInput {

  private final CreateFeatureInputsProcess createFeatureInputsProcess;

  @Override
  public void createStructuredFeatureInputs(AeAlert alert, List<HitData> hitsData) {

    var featureInputsStructured = handleMatches(alert, hitsData);
    createFeatureInputsProcess.createStructuredFeatureInputs(featureInputsStructured);
  }

  private static List<FeatureInputStructured> handleMatches(AeAlert alert, List<HitData> hitsData) {
    var alertName = alert.getAlertName();
    return alert.getMatches().entrySet().stream()
        .map(matchItem -> createAgentInputs(hitsData, alertName, matchItem))
        .flatMap(List::stream)
        .collect(toList());
  }

  private static List<FeatureInputStructured> createAgentInputs(
      List<HitData> hitsData, String alertName, Entry<String, String> matchItem) {
    return filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> createAgentInput(alertName, matchItem.getValue(), hitData))
        .collect(toList());
  }

  private static FeatureInputStructured createAgentInput(
      String alertName, String matchName, HitData hitData) {
    return new HitDataToFeatureInputMapper(
        hitData.getMatchId(), hitData.getAlertedPartyData(), hitData.getHitAndWlPartyData())
        .mapToFeatureInputStructured(alertName, matchName);
  }

  @Override
  public void createUnstructuredFeatureInputs(
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

    var featureInputsUnstructured = hitAndWatchlistPartyData.entrySet()
        .stream()
        .map(entry -> createUnstructuredInput(alert, entry))
        .collect(toList());

    createFeatureInputsProcess.createUnstructuredFeatureInputs(featureInputsUnstructured);
  }

  private static FeatureInputUnstructured createUnstructuredInput(
      AeAlert alert, Entry<String, HitAndWatchlistPartyData> entry) {

    var contextualAgentData = entry.getValue().getContextualAgentData();
    var nameMatchedTextAgentData = entry.getValue().getNameMatchedTextAgent();
    return FeatureInputUnstructured.builder()
        .alertName(alert.getAlertName())
        .matchName(getMatchNameFromMathId(alert.getMatches(), entry.getKey()))
        .nameMatchedTextAgentData(nameMatchedTextAgentData)
        .contextualAgentData(contextualAgentData)
        .build();
  }

  private static String getMatchNameFromMathId(Map<String, String> matches, String matchId) {
    return matches.entrySet().stream()
        .filter(entry -> entry.getKey().equals(matchId))
        .map(Entry::getValue)
        .findFirst()
        .orElseThrow(
            () -> new MatchNotFoundException("No match name found for matchId: " + matchId));
  }
}
