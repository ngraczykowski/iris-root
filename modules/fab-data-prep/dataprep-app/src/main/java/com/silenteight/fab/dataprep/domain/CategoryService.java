package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.category.BuildCategoryCommand;
import com.silenteight.fab.dataprep.domain.category.FabCategory;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.universaldatasource.api.library.category.v2.*;

import org.springframework.stereotype.Service;

import java.util.List;

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
      List<FabCategory> categories) {
    this.categoryServiceClient = categoryServiceClient;
    this.categoryValuesServiceClient = categoryValuesServiceClient;
    this.categories = categories;

    createCategories(categories);
  }

  final void createCategories(List<FabCategory> categories) {
    log.info("Creating categories {}", categories.stream()
        .map(FabCategory::getCategoryName)
        .collect(toList()));

    BatchCreateCategoriesIn batchCreateCategoriesIn = BatchCreateCategoriesIn.builder()
        .categories(getCategoryShareds(categories))
        .build();
    categoryServiceClient.createCategories(batchCreateCategoriesIn);
  }

  private static List<CategoryShared> getCategoryShareds(
      List<FabCategory> categories) {
    return categories.stream()
        .map(entry -> CategoryShared.builder()
            .name(entry.getCategoryName())
            .displayName(entry.getCategoryDefinition().getDisplayName())
            .categoryType(
                CategoryTypeShared.valueOf(entry.getCategoryDefinition().getCategoryType()))
            .allowedValues(entry.getCategoryDefinition().getAllowedValues())
            .multiValue(entry.getCategoryDefinition().isMultiValue())
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
            .parsedMessageData(featureInputsCommand.getRegisteredAlert().getParsedMessageData())
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
