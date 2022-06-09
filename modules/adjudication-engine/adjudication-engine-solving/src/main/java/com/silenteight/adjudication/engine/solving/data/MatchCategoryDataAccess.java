/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

public interface MatchCategoryDataAccess {

  void store(MatchCategory matchCategory);
}
