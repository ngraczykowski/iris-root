/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.domain.port.outgoing;

import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoryValuesIn;

public interface UniversalDatasourceApiClient {

  void registerCategories(BatchCreateCategoriesIn categories);

  void registerCategoryValues(BatchCreateCategoryValuesIn categoryValuesIn);

  <T extends Feature> void registerAgentInputs(BatchCreateAgentInputsIn<T> agentInputs);
}
