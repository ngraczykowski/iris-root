package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

import java.util.UUID;

@RequiredArgsConstructor
public class CustomerTypeCategory implements FabCategory {

  @Getter
  private final CategoryDefinition categoryDefinition;
  @Getter
  private final String categoryName;

  @Override
  public CategoryValueIn buildCategory(
      BuildCategoryCommand buildCategoryCommand) {
    return CategoryValueIn.builder()
        .name(generateValueName())
        .match(buildCategoryCommand.getMatchName())
        .singleValue(getValue(buildCategoryCommand.getParsedMessageData()))
        .build();
  }

  private String generateValueName() {
    return getCategoryName() + "/values/" + UUID.randomUUID();
  }

  private static String getValue(ParsedMessageData parsedMessageData) {
    return parsedMessageData.getCustomerType();
  }
}
