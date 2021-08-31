package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface AnalysisAlertRepository extends Repository<AnalysisAlertEntity, AnalysisAlertKey> {

  Collection<AnalysisAlertEntity> saveAll(Iterable<AnalysisAlertEntity> entities);
}
