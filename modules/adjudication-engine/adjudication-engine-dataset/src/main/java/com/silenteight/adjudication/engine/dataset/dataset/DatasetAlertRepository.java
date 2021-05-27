package com.silenteight.adjudication.engine.dataset.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

interface DatasetAlertRepository extends Repository<DatasetAlertEntity, DatasetAlertKey> {

  DatasetAlertEntity save(DatasetAlertEntity entity);

  long countByIdDatasetId(long datasetId);

  Page<DatasetAlertEntity> findAllByIdDatasetId(Pageable pageable, long datasetId);

  @Modifying
  @Query(value = "INSERT INTO ae_dataset_alert\n"
      + "    SELECT :datasetId, aea.alert_id\n"
      + "    FROM ae_alert aea\n"
      + "    WHERE aea.alerted_at >= :startDate\n"
      + "    AND aea.alerted_at <= :endDate\n"
      + "ON CONFLICT DO NOTHING",
      nativeQuery = true)
  void createFilteredDataset(
      @Param("datasetId") long datasetId,
      @Param("startDate") OffsetDateTime startDate,
      @Param("endDate") OffsetDateTime endDate);
}
