package com.silenteight.adjudication.engine.analysis.analysis;

import java.util.List;

public interface AnalysisDataAccess {

  List<String> findAgentConfigsByAnalysisId(long analysisId);
}
