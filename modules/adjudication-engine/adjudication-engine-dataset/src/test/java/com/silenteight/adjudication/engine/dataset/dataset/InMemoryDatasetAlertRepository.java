package com.silenteight.adjudication.engine.dataset.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
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
    var list = filteredDataset.stream().skip((long) pageNumber * pageSize)
        .limit(pageSize)
        .collect(Collectors.toList());
    var total = filteredDataset.size();
    return new PageImpl<>(list, pageable, total);
  }

  @Override
  public void createFilteredDataset(
      long datasetId, OffsetDateTime startDate, OffsetDateTime endDate) {
    for (int i = 1; i <= 10; i++) {
      var entity = getRandomEntity(datasetId, i);
      store.add(entity);
    }
  }

  private DatasetAlertEntity getRandomEntity(long datasetId, long alertId) {
    return DatasetAlertEntity
        .builder()
        .id(DatasetAlertKey
            .builder()
            .datasetId(datasetId)
            .alertId(alertId)
            .build())
        .build();
  }
}
