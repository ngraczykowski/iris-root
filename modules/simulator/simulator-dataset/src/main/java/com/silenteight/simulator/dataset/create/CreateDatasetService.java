package com.silenteight.simulator.dataset.create;

import com.silenteight.adjudication.api.v1.Dataset;

public interface CreateDatasetService {

  Dataset createDataset(CreateDatasetRequest request);
}
