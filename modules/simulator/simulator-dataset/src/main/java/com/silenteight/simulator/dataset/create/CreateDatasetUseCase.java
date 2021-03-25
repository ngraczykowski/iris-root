package com.silenteight.simulator.dataset.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

@RequiredArgsConstructor
public class CreateDatasetUseCase {

  @NonNull
  private final CreateDatasetService createDatasetService;
  @NonNull
  private final DatasetMetadataService datasetMetadataService;

  public void activate(CreateDatasetRequest request, String username) {
    Dataset dataset = createDatasetService.createDataset(request);
    datasetMetadataService.createMetadata(request, dataset, username);
  }
}
