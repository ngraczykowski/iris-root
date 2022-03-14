package com.silenteight.universaldatasource.app.category.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "uds.category")
class ListAvailableCategoriesProperties {

  private List<String> available = DEFAULT_AVAILABLE_CATEGORIES;

  private static final List<String> DEFAULT_AVAILABLE_CATEGORIES = List.of(
      "categories/crossmatch",
      "categories/specificTerms",
      "categories/specificTerms2",
      "categories/historicalRiskAssessment",
      "categories/watchlistType",
      "categories/matchType",
      "categories/companyNameSurrounding",
      "categories/messageStructure"
  );
}
