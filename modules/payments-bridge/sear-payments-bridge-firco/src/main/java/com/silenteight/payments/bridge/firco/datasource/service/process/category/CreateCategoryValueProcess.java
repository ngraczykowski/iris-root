package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.exception.MatchNotFoundException;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.datasource.category.CreateCategoryValuesProcess;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateCategoryValueProcess implements CreateCategoryValue {

  private final CreateCategoryValuesProcess createCategoryValues;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;

  @Override
  public void createStructuredCategoryValues(
      AeAlert alert, List<HitData> hitsData) {
    var alertName = alert.getAlertName();

    var categoryValueModels = alert.getMatches()
        .entrySet()
        .stream()
        .map(matchItem -> handleMatches(hitsData, alertName, matchItem))
        .flatMap(List::stream)
        .collect(toList());

    createCategoryValues.createStructuredCategoryValues(categoryValueModels);
  }

  @Override
  public void createUnstructuredCategoryValues(
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitMap) {

    var categoryValueModels = hitMap.entrySet().stream()
        .map(hit -> createCategoryValueUnstructuredModel(alert, hit.getKey(), hit.getValue()))
        .collect(toList());

    createCategoryValues.createUnstructuredCategoryValues(categoryValueModels);
  }

  private static CategoryValueUnstructured createCategoryValueUnstructuredModel(
      AeAlert alert, String matchId, HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return hitAndWatchlistPartyData.toCategoryValueUnstructuredModelBuilder()
        .alertName(alert.getAlertName())
        .matchName(getMatchNameFromMathId(alert.getMatches(), matchId))
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

  private List<CategoryValueStructured> handleMatches(
      List<HitData> hitsData, String alertName, Entry<String, String> matchItem) {
    return filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> extractCategoryValues(matchItem.getValue(), alertName, hitData))
        .collect(toList());
  }

  private CategoryValueStructured extractCategoryValues(
      String matchName, String alertName, HitData hitData) {

    var alertedPartyEntities = createAlertedPartyEntities(
        hitData.getAlertedPartyData(),
        hitData.getHitAndWlPartyData().getAllMatchingTexts());

    return hitData.toCategoryValueStructuredModelBuilder(alertedPartyEntities)
        .matchName(matchName)
        .alertName(alertName)
        .build();
  }

  private Map<AlertedPartyKey, String> createAlertedPartyEntities(
      AlertedPartyData alertedPartyData, List<String> matchingTexts) {
    return createAlertedPartyEntitiesUseCase.create(CreateAlertedPartyEntitiesRequest
        .builder()
        .alertedPartyData(alertedPartyData)
        .allMatchingText(matchingTexts)
        .build());
  }
}
