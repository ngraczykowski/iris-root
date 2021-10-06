package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

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
            matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    var categoryValues = filterHitsData(hitsData, matchItem).stream()
        .map(hitData -> categoryValueExtractors
            .stream()
            .map(ce -> ce.extract(hitData, matchItem.getValue()))
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

  @Override
  public boolean supports(AlertRegisteredEvent data) {
    // check whether etl-process should handle the received data
    return true;
  }
}
