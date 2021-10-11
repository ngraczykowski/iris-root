package com.silenteight.adjudication.engine.dataset.dataset;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface DatasetAlertRepository extends Repository<DatasetAlertEntity, DatasetAlertKey> {

  DatasetAlertEntity save(DatasetAlertEntity entity);

  long countByIdDatasetId(long datasetId);

  Page<DatasetAlertEntity> findAllByIdDatasetId(Pageable pageable, long datasetId);

  @Modifying
  @Query(value = "INSERT INTO ae_dataset_alert\n"
      + "    SELECT ?1, aea.alert_id\n"
      + "    FROM ae_alert aea\n"
      + "    LEFT JOIN ae_alert_labels aal ON aea.alert_id = aal.alert_id\n"
      + "    WHERE aea.alerted_at >= ?3\n"
      + "    AND aea.alerted_at <= ?4\n"
      + "    AND (?2 IS NULL OR (aal.name || aal.value IN (?2)))\n"
      + "ON CONFLICT DO NOTHING",
      nativeQuery = true)
  void createFilteredDataset(
      long datasetId,
      List<String> labelsValues,
      OffsetDateTime startDate,
      OffsetDateTime endDate);
}
