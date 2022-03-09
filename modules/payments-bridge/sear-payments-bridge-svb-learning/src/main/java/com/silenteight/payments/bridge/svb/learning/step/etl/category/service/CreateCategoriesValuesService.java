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
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.service.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.service.FeatureInputSpecification;

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
      final List<EtlHit> etlHits, final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification) {
    var categoryValuesRequests = etlHits
        .stream()
        .map(hit -> createCategoryValuesRequests(hit, registeredAlert, featureInputSpecification))
        .flatMap(List::stream)
        .collect(toList());

    sendToDatasource(categoryValuesRequests);
    return categoryValuesRequests;
  }

  @Override
  public List<CreateCategoryValuesRequest> createCategoryValues(
      List<EtlHit> etlHits, RegisterAlertResponse registeredAlert) {
    return this.createCategoryValues(
        etlHits, registeredAlert, DefaultFeatureInputSpecification.INSTANCE);
  }


  @Override
  public List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      List<HitComposite> hitComposites,
      RegisterAlertResponse registerAlert
  ) {
    return this.createUnstructuredCategoryValues(hitComposites, registerAlert,
        DefaultFeatureInputSpecification.INSTANCE);
  }

  @Override
  public List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      final List<HitComposite> hitComposites,
      final RegisterAlertResponse registerAlert,
      final FeatureInputSpecification featureInputSpecification) {
    var categoryValuesRequests = hitComposites
        .stream()
        .map(hit -> createUnstructuredCategoryValuesRequests(hit, registerAlert,
            featureInputSpecification))
        .flatMap(List::stream)
        .collect(toList());

    sendToDatasource(categoryValuesRequests);
    return categoryValuesRequests;
  }

  private List<CreateCategoryValuesRequest> createCategoryValuesRequests(
      final EtlHit hit, final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification) {
    var categoryValues = createCategoryValue(hit, registeredAlert);
    return categoryValues
        .stream()
        .filter(featureInputSpecification::isSatisfy)
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
      HitComposite hit, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {
    var categoryValues = createUnstructuredCategoryValue(hit, registeredAlert);
    return categoryValues
        .stream()
        .filter(featureInputSpecification::isSatisfy)
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
