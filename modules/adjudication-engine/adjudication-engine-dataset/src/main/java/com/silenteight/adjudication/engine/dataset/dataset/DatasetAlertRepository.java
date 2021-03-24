package com.silenteight.adjudication.engine.dataset.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

interface DatasetAlertRepository extends Repository<DatasetAlertEntity, DatasetAlertKey> {

  DatasetAlertEntity save(DatasetAlertEntity entity);

  long countByIdDatasetId(long datasetId);

  Page<DatasetAlertEntity> findAllByIdDatasetId(Pageable pageable, long datasetId);
}
