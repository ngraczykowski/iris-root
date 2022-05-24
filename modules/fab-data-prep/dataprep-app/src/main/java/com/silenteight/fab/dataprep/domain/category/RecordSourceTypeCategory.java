package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;

import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class RecordSourceTypeCategory extends BaseCategory {

  private static final String TYPE_T24 = "T24";
  private static final String TYPE_OTHER = "OTHER";
  private static final String TYPE_NO_DATA = "NO_DATA";

  private static final String T24_TEXT = "T24";

  @Getter
  private final CategoryDefinition categoryDefinition;
  @Getter
  private final String categoryName;

  @Override
  String getValue(BuildCategoryCommand buildCategoryCommand) {
    return getValue(buildCategoryCommand.getParsedMessageData().getSource());
  }

  private static String getValue(String source) {
    if (StringUtils.isBlank(source)) {
      return TYPE_NO_DATA;
    }
    return source.startsWith(T24_TEXT) ? TYPE_T24 : TYPE_OTHER;
  }
}
