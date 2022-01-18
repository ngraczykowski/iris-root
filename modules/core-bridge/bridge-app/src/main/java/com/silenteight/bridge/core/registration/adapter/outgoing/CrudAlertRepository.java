package com.silenteight.bridge.core.registration.adapter.outgoing;


import com.silenteight.bridge.core.registration.adapter.outgoing.AlertEntity.Status;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  @Modifying
  @Query("""
      WITH a AS (
          UPDATE alerts
              SET status = :status, updated_at = NOW()
              WHERE batch_id = :batchId AND name IN(:alertNames)
              RETURNING id
      )
      UPDATE matches
          SET status = :status
          WHERE alert_id = (SELECT id FROM a)""")
  void updateAlertsWithMatchesStatusByBatchIdAndNamesIn(
      String batchId, String status, List<String> alertNames);

  @Query("""
      SELECT COUNT(status) FROM alerts 
      WHERE batch_id = :batchId AND NOT status = 'RECOMMENDED' AND NOT status = 'ERROR'""")
  long countAllAlertsByBatchIdAndNotRecommendedAndNotErrorStatuses(String batchId);

  List<AlertIdProjection> findByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<AlertEntity> findAllByBatchId(String batchId);

  List<AlertEntity> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  @Modifying
  @Query("""
      UPDATE alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId AND alert_id IN(:alertIds)""")
  void updateStatusByBatchIdAndAlertIdIn(Status status, String batchId, List<String> alertIds);

  @Query("""
      SELECT status, COUNT(*) 
      FROM alerts
      WHERE batch_id = :batchId
      GROUP BY status""")
  List<AlertStatusStatisticsProjection> countAlertsByStatusForBatchId(String batchId);
}
