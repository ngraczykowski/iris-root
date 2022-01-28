package com.silenteight.bridge.core.registration.adapter.outgoing;


import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  @Modifying
  @Query("""
      UPDATE alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId AND name IN(:alertNames)""")
  void updateAlertsStatusByBatchIdAndNamesIn(
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
            a.alert_id AS alert_alert_id,
            a.name AS alert_name, 
            a.status AS alert_status, 
            a.metadata AS alert_metadata, 
            a.error_description AS alert_error_description,
            m.match_id AS match_match_id,
            m.name AS match_name
          FROM alerts a
          JOIN matches m ON m.alert_id = a.id
          WHERE a.batch_id = :batchId""")
  List<AlertWithMatchNamesProjection> findAllWithMatchesByBatchId(String batchId);

  @Query(
      rowMapperClass = AlertWithMatchNamesProjectionRowMapper.class,
      value = """
          SELECT 
            a.alert_id AS alert_alert_id,
            a.name AS alert_name, 
            a.status AS alert_status, 
            a.metadata AS alert_metadata, 
            a.error_description AS alert_error_description,
            m.match_id AS match_match_id,
            m.name AS match_name
          FROM alerts a
          LEFT JOIN matches m ON m.alert_id = a.id
          WHERE a.batch_id = :batchId AND a.alert_id IN(:alertIds)""")
  List<AlertWithMatchNamesProjection> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<AlertEntity> findAllByBatchId(String batchId);

  List<AlertEntity> findAllByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  @Query("""
      SELECT status, COUNT(*) 
      FROM alerts
      WHERE batch_id = :batchId
      GROUP BY status""")
  List<AlertStatusStatisticsProjection> countAlertsByStatusForBatchId(String batchId);

  @Modifying
  @Query("""
      UPDATE alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId AND alert_id IN(:alertIds)""")
  void updateAlertsStatusByBatchIdAndIdsIn(String batchId, String status, List<String> alertIds);
}
