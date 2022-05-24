package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;

@RequiredArgsConstructor
public class IsHitOnWlNameCategory extends BaseCategory {

  private static final String TYPE_NO = "NO";
  private static final String TYPE_NO_DATA = "NO_DATA";

  private static final String WATCH_LIST_NAME_PATH = "$.HittedEntity.Names[?(@.Name==\"*\")].Name";

  @Getter
  private final CategoryDefinition categoryDefinition;
  @Getter
  private final String categoryName;
  private final ParseContext parseContext;

  @Override
  String getValue(BuildCategoryCommand buildCategoryCommand) {
    return getValue(buildCategoryCommand.getMatch().getPayloads());
  }

  private String getValue(List<JsonNode> jsonNodes) {
    return jsonNodes.stream().anyMatch(this::isHitOnWlName) ? TYPE_NO : TYPE_NO_DATA;
  }

  private boolean isHitOnWlName(JsonNode jsonNode) {
    return !parseContext.parse(jsonNode).read(WATCH_LIST_NAME_PATH, LIST_OF_STRINGS).isEmpty();
  }
}
