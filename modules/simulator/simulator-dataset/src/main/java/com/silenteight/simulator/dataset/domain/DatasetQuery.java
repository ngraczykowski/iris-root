package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;
import com.silenteight.simulator.dataset.dto.DatasetDto;
import com.silenteight.simulator.dataset.dto.RangeQueryDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.simulator.dataset.common.DatasetResource.toResourceName;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DatasetQuery {

  @NonNull
  private final DatasetEntityRepository repository;

  public List<DatasetDto> list(DatasetState state) {
    return findAll(state)
        .stream()
        .map(DatasetQuery::toDto)
        .collect(toList());
  }

  private Collection<DatasetEntity> findAll(DatasetState state) {
    if (state != null)
      return repository.findAllByState(state);
    else
      return repository.findAll();
  }

  public DatasetDto get(@NonNull UUID datasetId) {
    return repository
        .findByDatasetId(datasetId)
        .map(DatasetQuery::toDto)
        .orElseThrow(() -> new DatasetNotFoundException(datasetId));
  }

  private static DatasetDto toDto(DatasetEntity datasetEntity) {
    return DatasetDto
        .builder()
        .id(datasetEntity.getDatasetId())
        .name(toResourceName(datasetEntity.getDatasetId()))
        .datasetName(datasetEntity.getName())
        .description(datasetEntity.getDescription())
        .state(datasetEntity.getState())
        .alertsCount(datasetEntity.getInitialAlertCount())
        .query(toQuery(datasetEntity))
        .createdAt(datasetEntity.getCreatedAt())
        .createdBy(datasetEntity.getCreatedBy())
        .build();
  }

  private static AlertSelectionCriteriaDto toQuery(DatasetEntity datasetEntity) {
    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(toRange(datasetEntity))
        .build();
  }

  private static RangeQueryDto toRange(DatasetEntity datasetEntity) {
    return RangeQueryDto.builder()
        .from(datasetEntity.getGenerationDateFrom())
        .to(datasetEntity.getGenerationDateTo())
        .build();
  }
}
