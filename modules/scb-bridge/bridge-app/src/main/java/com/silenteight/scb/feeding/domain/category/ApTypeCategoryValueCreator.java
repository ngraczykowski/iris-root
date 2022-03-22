package com.silenteight.scb.feeding.domain.category;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

import java.util.Collections;

public class ApTypeCategoryValueCreator implements CategoryValue {

  @Override
  public CreateCategoryValuesIn createCategoryValuesIn(Alert alert, Match match) {
    return CreateCategoryValuesIn.builder()
        .category("categories/apType")
        .categoryValues(Collections.singletonList(mapToCategoryValueIn(alert, match)))
        .build();
  }

  private CategoryValueIn mapToCategoryValueIn(Alert alert, Match match) {
    return CategoryValueIn.builder()
        .alert(alert.details().getAlertName())
        .match(match.details().getMatchName())
        .singleValue(match.matchedParty().apType())
        .build();
  }
}
