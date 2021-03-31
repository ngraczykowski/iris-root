package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.simulator.dataset.dto.DatasetDto;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DatasetQuery {

  @NonNull
  private final DatasetEntityRepository repository;

  public List<DatasetDto> list(DatasetState state) {
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
}
