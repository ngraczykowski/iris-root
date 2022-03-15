package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.category.BuildCategoryCommand;
import com.silenteight.fab.dataprep.domain.category.FabCategory;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;
import com.silenteight.universaldatasource.api.library.category.v2.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
class CategoryService {

  private final CategoryServiceClient categoryServiceClient;
  private final CategoryValuesServiceClient categoryValuesServiceClient;
  private final List<FabCategory> categories;

  CategoryService(
      CategoryServiceClient categoryServiceClient,
      CategoryValuesServiceClient categoryValuesServiceClient,
      List<FabCategory> categories,
      CategoriesConfigurationProperties categoriesConfigurationProperties) {
    this.categoryServiceClient = categoryServiceClient;
    this.categoryValuesServiceClient = categoryValuesServiceClient;
    this.categories = categories;

    createCategories(categoriesConfigurationProperties.getCategories());
  }

  final void createCategories(Map<String, CategoryDefinition> categories) {
    log.info("Creating categories {}", categories);
    BatchCreateCategoriesIn batchCreateCategoriesIn = BatchCreateCategoriesIn.builder()
        .categories(getCategoryShareds(categories))
        .build();
    categoryServiceClient.createCategories(batchCreateCategoriesIn);
  }

  private static List<CategoryShared> getCategoryShareds(
      Map<String, CategoryDefinition> categories) {
    return categories.entrySet()
        .stream()
        .filter(entry -> entry.getValue().isEnabled())
        .map(entry -> CategoryShared.builder()
            .name(entry.getKey())
            .displayName(entry.getValue().getDisplayName())
            .categoryType(CategoryTypeShared.valueOf(entry.getValue().getCategoryType()))
            .allowedValues(entry.getValue().getAllowedValues())
            .multiValue(entry.getValue().isMultiValue())
            .build())
        .collect(toList());
  }

  void createCategoryInputs(FeatureInputsCommand featureInputsCommand) {
    List<CreateCategoryValuesIn> agentInputs = categories.stream()
        .map(category -> CreateCategoryValuesIn.builder()
            .category(category.getCategoryName())
            .categoryValues(buildCategories(category, featureInputsCommand))
            .build())
        .collect(toList());

    feedUds(agentInputs);
  }

  private static List<CategoryValueIn> buildCategories(
      FabCategory fabCategory, FeatureInputsCommand featureInputsCommand) {
    RegisteredAlert registeredAlert = featureInputsCommand.getRegisteredAlert();
    return registeredAlert.getMatches()
        .stream()
        .map(match -> fabCategory.buildCategory(BuildCategoryCommand.builder()
            .matchName(match.getMatchName())
            .systemId(registeredAlert.getSystemId())
            .build()))
        .collect(toList());
  }

  void feedUds(List<CreateCategoryValuesIn> requests) {
    BatchCreateCategoryValuesIn categoryValuesIn = BatchCreateCategoryValuesIn.builder()
        .requests(requests)
        .build();

    var categoryValuesOut = categoryValuesServiceClient.createCategoriesValues(categoryValuesIn);

    log.info("Created agent inputs: {}", categoryValuesOut);
  }
}
