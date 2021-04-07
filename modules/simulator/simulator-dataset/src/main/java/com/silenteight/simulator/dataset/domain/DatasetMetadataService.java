package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;

@RequiredArgsConstructor
public class DatasetMetadataService {

  @NonNull
  private final DatasetEntityRepository repository;

  public void createMetadata(CreateDatasetRequest request, Dataset dataset) {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(request.getId())
        .name(request.getName())
        .description(request.getDescription())
        .externalResourceName(dataset.getName())
        .createdBy(request.getCreatedBy())
        .initialAlertCount(dataset.getAlertCount())
        .state(CURRENT)
        .generationDateFrom(request.getRangeFrom())
        .generationDateTo(request.getRangeTo())
        .build();

    repository.save(datasetEntity);
  }
}
