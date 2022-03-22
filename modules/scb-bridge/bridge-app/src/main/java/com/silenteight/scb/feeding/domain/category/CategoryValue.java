package com.silenteight.scb.feeding.domain.category;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

public interface CategoryValue {

  CreateCategoryValuesIn createCategoryValuesIn(Alert alert, Match match);
}
