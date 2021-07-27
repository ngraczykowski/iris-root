package com.silenteight.simulator.dataset.create;

import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Dataset;

public interface CreateDatasetService {

  Dataset createDataset(@NonNull CreateDatasetRequest request);
}
