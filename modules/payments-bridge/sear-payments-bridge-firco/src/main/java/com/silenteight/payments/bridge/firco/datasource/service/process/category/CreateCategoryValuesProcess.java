package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateCategoryValuesProcess implements CreateCategoryValue {

  private final List<CreateCategoryValueStructured> createCategoryValueStructured;
  private final List<CreateCategoryValueUnstructured> createCategoryValueUnstructured;

  @Override
  public List<CategoryValue> createStructuredCategoryValues(AeAlert alert, List<HitData> hitsData) {
    var alertName = alert.getAlertName();

    return alert.getMatches()
        .entrySet()
        .stream()
        .map(matchItem -> handleMatches(hitsData, alertName, matchItem))
        .flatMap(Collection::stream)
        .collect(toList());
  }


  @Override
  public List<CategoryValue> createUnstructuredCategoryValues(
      String alertName, String matchName, HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return createCategoryValueUnstructured.stream()
        .map(process -> process.createCategoryValue(alertName, matchName, hitAndWatchlistPartyData))
        .collect(toList());
  }

  private List<CategoryValue> handleMatches(
      List<HitData> hitsData, String alertName, Entry<String, String> matchItem) {
    return filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> CategoryValueExtractModel.builder()
            .hitData(hitData)
            .alertName(alertName)
            .matchName(matchItem.getValue())
            .build())
        .map(this::extractCategoryValues)
        .flatMap(Collection::stream)
        .collect(toList());
  }

  private List<CategoryValue> extractCategoryValues(
      CategoryValueExtractModel categoryValueExtractModel) {
    return createCategoryValueStructured.stream()
        .peek(extractor -> {
          if (log.isDebugEnabled()) {
            log.debug("Processing category: {}", extractor.getClass().getSimpleName());
          }
        })
        .map(extractor -> extractor.createCategoryValue(categoryValueExtractModel))
        .collect(toList());
  }
}
