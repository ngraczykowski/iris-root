/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;

public interface CategoryValuesClient {

  BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchesCategoryValuesRequest request);
}
