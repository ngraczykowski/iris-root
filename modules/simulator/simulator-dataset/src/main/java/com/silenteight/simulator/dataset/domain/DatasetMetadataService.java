package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.create.CreateDatasetRequest;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.simulator.dataset.domain.DatasetState.ACTIVE;
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
        .state(ACTIVE)
        .generationDateFrom(request.getRangeFrom())
        .generationDateTo(request.getRangeTo())
        .labels(JsonConversionHelper.INSTANCE.serializeToString(request.getLabels()))
        .build();
    repository.save(datasetEntity);
    log.debug("Created Metadata DatasetEntity={}", datasetEntity);
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

  @Transactional
  public void archive(@NonNull UUID datasetId) {
    DatasetEntity datasetEntity = getByDatasetId(datasetId);
    datasetEntity.archive();
    log.debug("Saved as 'ARCHIVED' DatasetEntity={}", datasetEntity);
  }

  @Transactional
  public void expire(@NonNull Collection<String> externalResourceNames) {
    repository
        .findAllByExternalResourceNameIn(externalResourceNames)
        .forEach(DatasetMetadataService::expire);
  }

  private static void expire(@NonNull DatasetEntity datasetEntity) {
    datasetEntity.expire();
    log.debug("Saved as 'EXPIRED' DatasetEntity={}", datasetEntity);
  }

  private DatasetEntity getByDatasetId(@NonNull UUID datasetId) {
    return repository
        .findByDatasetId(datasetId)
        .orElseThrow(() -> new DatasetNotFoundException(datasetId));
  }
}
