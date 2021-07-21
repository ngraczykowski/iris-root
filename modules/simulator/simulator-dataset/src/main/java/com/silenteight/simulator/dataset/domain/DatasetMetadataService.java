package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.dataset.domain.DatasetState.CURRENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DatasetMetadataService {

  @NonNull
  private final DatasetEntityRepository repository;

  public void createMetadata(CreateDatasetRequest request, Dataset dataset) {
    DatasetEntity datasetEntity = DatasetEntity.builder()
        .datasetId(request.getId())
        .name(request.getDatasetName())
        .description(request.getDescription())
        .externalResourceName(dataset.getName())
        .createdBy(request.getCreatedBy())
        .initialAlertCount(dataset.getAlertCount())
        .state(CURRENT)
        .generationDateFrom(request.getRangeFrom())
        .generationDateTo(request.getRangeTo())
        .countries(request.getCountries())
        .build();
    repository.save(datasetEntity);
    log.trace("Created Metadata DatasetEntity={}", datasetEntity);
  }

  public long countAllAlerts(Set<String> datasetNames) {
    log.debug("Summing all alerts in datasets={}", datasetNames);

    return repository.sumAlertsInDatasets(toDatasetIds(datasetNames));
  }

  private static Collection<UUID> toDatasetIds(Set<String> datasetNames) {
    return datasetNames
        .stream()
        .map(DatasetResource::fromResourceName)
        .collect(toList());
  }
}
