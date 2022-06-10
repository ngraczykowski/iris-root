/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureValue;

@RequiredArgsConstructor
public class JdbcMatchFeatureStoreDataAccess implements MatchFeatureStoreDataAccess {

  private final MatchFeatureJdbcRepository matchFeatureJdbcRepository;

  @Override
  public void store(MatchFeatureValue matchFeature) {
    matchFeatureJdbcRepository.save(matchFeature);
  }
}
