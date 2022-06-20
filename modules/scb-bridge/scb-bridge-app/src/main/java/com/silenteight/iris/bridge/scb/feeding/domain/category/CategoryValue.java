/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.category;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.category.v2.CreateCategoryValuesIn;

public interface CategoryValue {

  CreateCategoryValuesIn createCategoryValuesIn(Alert alert, Match match);
}
