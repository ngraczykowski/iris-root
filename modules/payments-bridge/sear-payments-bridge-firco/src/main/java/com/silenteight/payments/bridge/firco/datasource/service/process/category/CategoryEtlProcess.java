package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class CategoryEtlProcess implements EtlProcess {

  private final List<CategoryValueProcess> categoryValueExtractors;
  private final CreateCategoryValuesClient createCategoryValuesClient;

  @Override
  public void extractAndLoad(AeAlert data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();

    data.getMatches()
        .entrySet()
        .forEach(
            matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(
      List<HitData> hitsData, Entry<String, String> matchItem) {
    var categoryValues = filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> extractCategoryValues(matchItem.getValue(), hitData))
        .flatMap(Collection::stream)
        .collect(toList());

    var request = createRequest(categoryValues);
    createCategoryValuesClient.createCategoriesValues(request);
  }

  @Nonnull
  private List<CategoryValue> extractCategoryValues(
      String matchName, HitData hitData) {
    return categoryValueExtractors.stream()
        .map(extractor -> {

          if (log.isDebugEnabled()) {
            log.debug("Processing category: {}", extractor.getClass().getSimpleName());
          }

          return extractor.extract(hitData, matchName);
        })
        .collect(toList());
  }

  private static BatchCreateCategoryValuesRequest createRequest(
      List<CategoryValue> categoryValues) {
    return BatchCreateCategoryValuesRequest
        .newBuilder()
        .addAllRequests(
            categoryValues
                .stream()
                .map(cv -> CreateCategoryValuesRequest
                    .newBuilder()
                    .setCategory(cv.getName())
                    .addCategoryValues(cv)
                    .build())
                .collect(
                    toList()))
        .build();
  }
}
