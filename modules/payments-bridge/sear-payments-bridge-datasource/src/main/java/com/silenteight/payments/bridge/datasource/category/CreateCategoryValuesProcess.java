package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCategoryValuesProcess {

  private final List<CategoryValueStructuredFactory> categoryValueStructuredFactories;
  private final List<CategoryValueUnstructuredFactory> categoryValueUnstructuredFactories;
  private final CategoryValueRepository categoryValueRepository;

  public void createStructuredCategoryValues(
      List<CategoryValueStructured> categoryValuesStructured,
      FeatureInputSpecification featureInputSpecification) {

    var categoryValues = categoryValuesStructured.stream()
        .map((CategoryValueStructured categoryValueStructured) -> createStructuredCategoryValue(
            categoryValueStructured, featureInputSpecification))
        .flatMap(List::stream)
        .collect(toList());

    categoryValueRepository.save(categoryValues);
  }

  public void createStructuredCategoryValues(
      List<CategoryValueStructured> categoryValuesStructured) {
    this.createStructuredCategoryValues(
        categoryValuesStructured, DefaultFeatureInputSpecification.INSTANCE);
  }

  public void createUnstructuredCategoryValues(
      List<CategoryValueUnstructured> categoryValueUnstructured,
      FeatureInputSpecification featureInputSpecification) {

    var categoryValues = categoryValueUnstructured.stream()
        .map(categoryValueUnstructured1 -> createUnstructuredCategoryValue(
            categoryValueUnstructured1, featureInputSpecification))
        .flatMap(List::stream)
        .collect(toList());

    categoryValueRepository.save(categoryValues);
  }

  public void createUnstructuredCategoryValues(
      List<CategoryValueUnstructured> categoryValueUnstructured) {
    this.createUnstructuredCategoryValues(
        categoryValueUnstructured, DefaultFeatureInputSpecification.INSTANCE);
  }

  private List<CategoryValue> createStructuredCategoryValue(
      CategoryValueStructured categoryValueStructured,
      final FeatureInputSpecification featureInputSpecification) {
    return this.categoryValueStructuredFactories.stream()
        .flatMap(ca -> ca
            .createCategoryValue(categoryValueStructured, featureInputSpecification)
            .stream())
        .collect(toList());
  }

  private List<CategoryValue> createUnstructuredCategoryValue(
      CategoryValueUnstructured categoryValueUnstructured,
      final FeatureInputSpecification featureInputSpecification) {
    return this.categoryValueUnstructuredFactories.stream()
        .flatMap(ca -> ca
            .createCategoryValue(categoryValueUnstructured, featureInputSpecification)
            .stream())
        .collect(toList());
  }


}
