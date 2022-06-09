/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchCategoryPublisherPort;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

import java.util.ArrayList;
import java.util.List;

class MockMatchCategoryPublisherPort implements MatchCategoryPublisherPort {

  private final List<MatchCategory> matchCategories;

  MockMatchCategoryPublisherPort() {
    this.matchCategories = new ArrayList<>();
  }

  @Override
  public void resolve(MatchCategory matchCategory) {
    matchCategories.add(matchCategory);
  }

  int getResolvedCount() {
    return this.matchCategories.size();
  }
}
