package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.api.v1.Dataset;

public interface DataSetRepository {

  void save(Dataset dataset);
}
