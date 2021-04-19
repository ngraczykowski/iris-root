package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AnalysisQueryRepository extends Repository<AnalysisQuery, Long> {

  Optional<AnalysisQuery> findById(Long id);
}
