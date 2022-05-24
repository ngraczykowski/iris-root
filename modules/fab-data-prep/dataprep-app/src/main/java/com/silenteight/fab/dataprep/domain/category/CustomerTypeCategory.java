package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;

@RequiredArgsConstructor
public class CustomerTypeCategory extends BaseCategory {

  private static final String TYPE_INDIVIDUAL = "I";
  private static final String TYPE_CORPORATE = "C";
  private static final String TYPE_NO_DATA = "NO_DATA";
  private static final String TYPE_DATA_SOURCE_ERROR = "DATA_SOURCE_ERROR";

  @Getter
  private final CategoryDefinition categoryDefinition;
  @Getter
  private final String categoryName;

  @Override
  String getValue(BuildCategoryCommand buildCategoryCommand) {
    return getValue(buildCategoryCommand.getParsedMessageData());
  }

  private static String getValue(ParsedMessageData parsedMessageData) {
    switch (parsedMessageData.getCustomerTypeAsEnum()) {
      case NO_DATA:
        return TYPE_NO_DATA;
      case INDIVIDUAL:
        return TYPE_INDIVIDUAL;
      case CORPORATE:
        return TYPE_CORPORATE;
      case DATA_SOURCE_ERROR:
      default:
        return TYPE_DATA_SOURCE_ERROR;
    }
  }
}
