package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.dataset.domain.exception.DatasetNotFoundException;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class DatasetQuery {

  @NonNull
  private final DatasetEntityRepository repository;

  public List<DatasetDto> list(DatasetState state) {
    log.trace("Getting all DatasetDto with state={}", state);

    return findAll(state)
        .stream()
        .map(DatasetEntity::toDto)
        .collect(toList());
  }

  private Collection<DatasetEntity> findAll(DatasetState state) {
    if (state != null)
      return repository.findAllByState(state);
    else
      return repository.findAll();
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
}
