package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;
import com.silenteight.payments.bridge.svb.learning.categories.port.outgoing.CreateCategoryValuesClient;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
class CategoryEtlProcess implements EtlProcess {

  private final List<CategoryValueProcess> categoryValueExtractors;
  private final CreateCategoryValuesClient createCategoryValuesClient;

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    data.getMatches()
        .entrySet()
        .forEach(
            matchItem -> handleMatches(data, hitsData, matchItem, data.getAlertRegisteredName()));
  }

  private void handleMatches(
      AlertRegisteredEvent data,
      List<HitData> hitsData, Entry<String, String> matchItem, String alertRegisteredName) {
    var categoryValues = filterHitsData(hitsData, matchItem).stream()
        .filter(Objects::nonNull)
        .map(hitData -> categoryValueExtractors
            .stream()
            .map(ce -> ce.extract(data, hitData, matchItem.getValue()))
            .collect(Collectors.toList())
        ).flatMap(Collection::stream).collect(Collectors.toList());

    BatchCreateCategoryValuesRequest request = createRequest(categoryValues);
    createCategoryValuesClient.createCategoriesValues(request);
  }

  @Nonnull
  private BatchCreateCategoryValuesRequest createRequest(List<CategoryValue> categoryValues) {
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
                    Collectors.toList()))
        .build();
  }

  @Nonnull
  private List<HitData> filterHitsData(List<HitData> hitsData, Entry<String, String> matchItem) {
    return hitsData.stream()
        .filter(hitData -> matchItem.getKey().equals(
            Optional.of(hitData)
                .map(HitData::getHitAndWlPartyData)
                .map(HitAndWatchlistPartyData::getId)
                .orElse(null)))
        .collect(Collectors.toList());
  }

  @Override
  public boolean supports(AlertRegisteredEvent data) {
    // check whether etl-process should handle the received data
    return true;
  }
}
