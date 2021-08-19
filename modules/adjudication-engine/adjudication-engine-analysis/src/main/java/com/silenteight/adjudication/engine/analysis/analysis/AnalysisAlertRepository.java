package com.silenteight.adjudication.engine.analysis.analysis;

import org.springframework.data.repository.Repository;

interface AnalysisAlertRepository extends Repository<AnalysisAlertEntity, AnalysisAlertKey> {

  AnalysisAlertEntity save(AnalysisAlertEntity entity);
}
