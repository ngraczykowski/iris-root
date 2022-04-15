package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DatasetQuery {

  @NonNull
  private final DatasetEntityRepository repository;

  public List<DatasetDto> list(@NonNull Set<DatasetState> states) {
    log.trace("Getting all DatasetDto with states={}", states);

    return findAll(states)
        .stream()
        .map(DatasetEntity::toDto)
        .collect(toList());
  }

  private Collection<DatasetEntity> findAll(Set<DatasetState> states) {
    if (states.isEmpty())
      return repository.findAll();
    else
      return repository.findAllByStateIn(states);
  }

  public DatasetDto get(@NonNull UUID datasetId) {
    log.trace("Getting DatasetDto by datasetId={}", datasetId);

    return repository
        .findByDatasetId(datasetId)
        .map(DatasetEntity::toDto)
        .orElseThrow(() -> new DatasetNotFoundException(datasetId));
  }

  public String getExternalResourceName(@NonNull UUID datasetId) {
    log.trace("Getting ExternalResourceName by datasetId={}", datasetId);

    return repository
        .findByDatasetId(datasetId)
        .map(DatasetEntity::getExternalResourceName)
        .orElseThrow(() -> new DatasetNotFoundException(datasetId));
  }

  public Collection<String> getDatasetNames(@NonNull Collection<String> externalResourceNames) {
    return repository
        .findAllDatasetIdsByExternalResourceNameIn(externalResourceNames)
        .stream()
        .map(DatasetResource::toResourceName)
        .collect(toList());
  }
}
