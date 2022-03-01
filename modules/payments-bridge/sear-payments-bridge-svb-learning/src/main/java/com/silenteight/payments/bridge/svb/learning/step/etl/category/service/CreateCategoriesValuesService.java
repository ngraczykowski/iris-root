package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.category.port.CreateCategoriesUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateCategoriesValuesService implements CreateCategoriesUseCase {

  private final CreateCategoryValuesClient createCategoryValuesClient;
  private final List<CategoryValueExtractor> extractors;
  private final List<UnstructuredCategoryValueExtractor> unstructuredExtractors;

  @Override
  public List<CreateCategoryValuesRequest> createCategoryValues(
      List<EtlHit> etlHits, RegisterAlertResponse registeredAlert) {
    var categoryValuesRequests = etlHits
        .stream()
        .map(hit -> createCategoryValuesRequests(hit, registeredAlert))
        .flatMap(List::stream)
        .collect(toList());

    sendToDatasource(categoryValuesRequests);
    return categoryValuesRequests;
  }

  @Override
  public List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      List<HitComposite> hitComposites,
      RegisterAlertResponse registerAlert) {
    var categoryValuesRequests = hitComposites
        .stream()
        .map(hit -> createUnstructuredCategoryValuesRequests(hit, registerAlert))
        .flatMap(List::stream)
        .collect(toList());

    sendToDatasource(categoryValuesRequests);
    return categoryValuesRequests;
  }

  private List<CreateCategoryValuesRequest> createCategoryValuesRequests(
      EtlHit hit, RegisterAlertResponse registeredAlert) {
    var categoryValues = createCategoryValue(hit, registeredAlert);
    return categoryValues
        .stream()
        .map(cv -> CreateCategoryValuesRequest
            .newBuilder()
            .setCategory(cv.getName())
            .addCategoryValues(cv)
            .build())
        .collect(toList());
  }

  @Nonnull
  private List<CategoryValue> createCategoryValue(
      EtlHit hit, RegisterAlertResponse registeredAlert) {
    return extractors.stream().map(fe -> fe.extract(hit, registeredAlert)).collect(toList());
  }

  private List<CreateCategoryValuesRequest> createUnstructuredCategoryValuesRequests(
      HitComposite hit, RegisterAlertResponse registeredAlert) {
    var categoryValues = createUnstructuredCategoryValue(hit, registeredAlert);
    return categoryValues
        .stream()
        .map(cv -> CreateCategoryValuesRequest
            .newBuilder()
            .setCategory(cv.getName())
            .addCategoryValues(cv)
            .build())
        .collect(toList());
  }

  @Nonnull
  private List<CategoryValue> createUnstructuredCategoryValue(
      HitComposite hit, RegisterAlertResponse registeredAlert) {
    return unstructuredExtractors
        .stream()
        .map(fe -> fe.extract(hit, registeredAlert))
        .collect(toList());
  }

  private void sendToDatasource(List<CreateCategoryValuesRequest> categoryValuesRequests) {
    var request =
        BatchCreateCategoryValuesRequest
            .newBuilder()
            .addAllRequests(categoryValuesRequests)
            .build();
    createCategoryValuesClient.createCategoriesValues(request);

  }
}
