package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;

import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class HitTypeCategory extends BaseCategory {

  private static final String TYPE_SANCTION = "SAN";
  private static final String TYPE_OTHER = "OTHER";
  private static final String TYPE_NO_DATA = "NO_DATA";

  private static final String SANCTION_TEXT = "SAN!";

  @Getter
  private final CategoryDefinition categoryDefinition;
  @Getter
  private final String categoryName;

  @Override
  String getValue(BuildCategoryCommand buildCategoryCommand) {
    return getValue(buildCategoryCommand.getSystemId());
  }

  private static String getValue(String systemId) {
    if (StringUtils.isBlank(systemId)) {
      return TYPE_NO_DATA;
    }
    return systemId.contains(SANCTION_TEXT) ? TYPE_SANCTION : TYPE_OTHER;
  }
}
