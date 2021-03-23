package com.silenteight.adjudication.engine.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryDatasetAlertRepository implements DatasetAlertRepository {

  private final List<DatasetAlertEntity> store = new ArrayList<>();

  @Override
  public synchronized DatasetAlertEntity save(DatasetAlertEntity entity) {
    store.add(entity);
    return entity;
  }

  @Override
  public synchronized long countByIdDatasetId(long datasetId) {
    return store.stream()
        .filter(datasetAlertEntity -> datasetAlertEntity
            .getId()
            .getDatasetId()
            .equals(datasetId))
        .count();
  }

  @Override
  public synchronized Page<DatasetAlertEntity> findAllByIdDatasetId(
      Pageable pageable, long datasetId) {
    var pageNumber = pageable.getPageNumber();
    var pageSize = pageable.getPageSize();
    var filteredDataset = store
        .stream()
        .filter(datasetAlertEntity -> datasetAlertEntity.getId().getDatasetId()
            == datasetId).collect(Collectors.toList());
    var list = filteredDataset.stream().skip(pageNumber * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
    var total = filteredDataset.stream().count();
    return new PageImpl<>(list, pageable, total);
  }
}
