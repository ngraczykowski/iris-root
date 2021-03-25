package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;

@RequiredArgsConstructor
public class DatasetMetadataService {

  @NonNull
  private final DatasetEntityRepository datasetEntityRepository;

  public void createMetadata(CreateDatasetRequest request, Dataset dataset, String username) {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(request.getDatasetId())
        .resourceName(dataset.getName())
        .createdBy(username)
        .name(request.getName())
        .description(request.getDescription())
        .initialAlertCount(dataset.getAlertCount())
        .generationDateFrom(request.getRangeFrom())
        .generationDateTo(request.getRangeTo())
        .build();

    datasetEntityRepository.save(datasetEntity);
  }
}
