package com.silenteight.adjudication.engine.analysis.matchsolution;

import org.springframework.data.repository.Repository;

import javax.annotation.Nonnull;

interface MatchSolutionRepository extends Repository<MatchSolutionEntity, Long> {

  @Nonnull
  MatchSolutionEntity save(MatchSolutionEntity entity);

  int count();
}
