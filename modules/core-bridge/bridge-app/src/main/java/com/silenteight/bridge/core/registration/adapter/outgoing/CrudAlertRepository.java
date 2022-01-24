package com.silenteight.bridge.core.registration.adapter.outgoing;


import com.silenteight.bridge.core.registration.adapter.outgoing.AlertEntity.Status;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  @Modifying
  @Query("""
      WITH updated_alerts AS (
          UPDATE alerts
              SET status = :status, updated_at = NOW()
              WHERE batch_id = :batchId AND name IN(:alertNames)
              RETURNING id
      )
      UPDATE matches
          SET status = :status
          WHERE alert_id IN (SELECT id FROM updated_alerts)""")
  void updateAlertsWithMatchesStatusByBatchIdAndNamesIn(
      String batchId, String status, List<String> alertNames);

  @Query("""
      SELECT COUNT(status) FROM alerts 
      WHERE batch_id = :batchId AND NOT status = 'RECOMMENDED' AND NOT status = 'ERROR'""")
  long countAllAlertsByBatchIdAndNotRecommendedAndNotErrorStatuses(String batchId);

  List<AlertIdNameProjection> findByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  @Query(
      rowMapperClass = AlertWithMatchNamesProjectionRowMapper.class,
      value = """
          SELECT 
            A.alert_id AS alert_alert_id,
            A.name AS alert_name, 
            A.status AS alert_status, 
            A.metadata AS alert_metadata, 
            A.error_description AS alert_error_description,
            M.match_id AS match_match_id,
            M.name AS match_name
          FROM ALERTS A
          JOIN MATCHES M ON m.alert_id = A.id
          WHERE a.batch_id = :batchId
          """)
  List<AlertWithMatchNamesProjection> findAllWithMatchesByBatchId(String batchId);

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

  @Modifying
  @Query("""
      WITH updated_alerts AS (
          UPDATE alerts
              SET status = :status, updated_at = NOW()
              WHERE batch_id = :batchId AND alert_id IN(:alertIds)
              RETURNING id
      )
      UPDATE matches
          SET status = :status
          WHERE alert_id IN (SELECT id FROM updated_alerts)""")
  void updateAlertsWithMatchesStatusByBatchIdAndIdsIn(
      String batchId, String status, List<String> alertIds);
}
