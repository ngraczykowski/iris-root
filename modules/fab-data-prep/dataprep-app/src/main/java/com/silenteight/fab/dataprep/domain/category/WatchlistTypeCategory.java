package com.silenteight.fab.dataprep.domain.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.ParseContext;

import java.util.List;
import java.util.Map;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;

@RequiredArgsConstructor
public class WatchlistTypeCategory extends BaseCategory {

  private static final Map<String, String> TYPES_MAP = Map.of("I", "INDIVIDUAL",
      "C", "COMPANY",
      "O", "ADDRESS",
      "V", "VESSEL");

  private static final String TYPE_OTHER = "OTHER";
  private static final String WATCH_LIST_TYPE_PATH = "$.HittedEntity.Type";

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
    String type = jsonNodes.stream()
        .map(parseContext::parse)
        .flatMap(doc -> doc.read(WATCH_LIST_TYPE_PATH, LIST_OF_STRINGS).stream())
        .findFirst()
        .orElse("");

    return TYPES_MAP.getOrDefault(type, TYPE_OTHER);
  }
}
