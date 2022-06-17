/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcMatchCategoryDataAccess implements MatchCategoryDataAccess {

  private final MatchCategoryJdbcRepository matchCategoryJdbcRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void store(MatchCategory matchCategory) {
    matchCategoryJdbcRepository.save(matchCategory);
  }
}
