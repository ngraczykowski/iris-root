/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.data.MatchSolutionEntityExtractor;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

@Slf4j
@RequiredArgsConstructor
public class JdbcMatchSolutionStore implements MatchSolutionStore {

  private final MatchSolutionJdbcRepository matchSolutionJdbcRepository;
  private final MatchSolutionEntityExtractor matchSolutionEntityExtractor;

  @Override
  public void store(MatchSolution matchSolution) {
    try {
      if (log.isTraceEnabled()) {
        log.trace("Store match solution={}", matchSolution);
      }
      matchSolutionJdbcRepository.save(matchSolutionEntityExtractor.extract(matchSolution));
    } catch (Exception e) {
      log.error("Exception while saving match solution data", e);
    }
  }
}
