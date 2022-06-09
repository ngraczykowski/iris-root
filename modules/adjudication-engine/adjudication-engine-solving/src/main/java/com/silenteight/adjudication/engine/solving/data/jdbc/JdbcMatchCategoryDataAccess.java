/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

@RequiredArgsConstructor
public class JdbcMatchCategoryDataAccess implements MatchCategoryDataAccess {

  private final MatchCategoryJdbcRepository matchCategoryJdbcRepository;

  @Override
  public void store(MatchCategory matchCategory) {
    matchCategoryJdbcRepository.save(matchCategory);
  }
}
