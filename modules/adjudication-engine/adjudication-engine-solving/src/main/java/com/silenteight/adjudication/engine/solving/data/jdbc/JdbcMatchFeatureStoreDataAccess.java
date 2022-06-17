/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureValue;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcMatchFeatureStoreDataAccess implements MatchFeatureStoreDataAccess {

  private final MatchFeatureJdbcRepository matchFeatureJdbcRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void store(MatchFeatureValue matchFeature) {
    matchFeatureJdbcRepository.save(matchFeature);
  }
}
