package com.silenteight.bridge.core.registration.adapter.outgoing;


import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  @Modifying
  @Query("""
      UPDATE core_bridge_alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId AND name IN(:alertNames)""")
  void updateAlertsStatusByBatchIdAndNamesIn(
      String batchId, String status, List<String> alertNames);

  @Query("""
      SELECT COUNT(status) 
      FROM core_bridge_alerts 
      WHERE batch_id = :batchId AND status IN(:statuses)""")
  long countAllAlertsByBatchIdAndStatusIn(String batchId, Set<String> statuses);

  @Query("""
      SELECT COUNT(status)
      FROM core_bridge_alerts
      WHERE batch_id = :batchId """)
  long countAllAlertsByBatchId(String batchId);


  @Query("""
        SELECT name 
        FROM core_bridge_alerts
        WHERE batch_id = :batchId AND (status = 'REGISTERED' OR status = 'PROCESSING')
      """)
  List<AlertNameProjection> findNamesByBatchIdAndStatusIsRegisteredOrProcessing(String batchId);

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
          FROM core_bridge_alerts a
          LEFT JOIN core_bridge_matches m ON m.alert_id = a.id
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
          FROM core_bridge_alerts a
          LEFT JOIN core_bridge_matches m ON m.alert_id = a.id
          WHERE a.batch_id = :batchId AND a.alert_id IN(:alertIds)""")
  List<AlertWithMatchNamesProjection> findAllWithMatchesByBatchIdAndAlertIdsIn(
      String batchId, List<String> alertIds);

  List<AlertEntity> findAllByBatchId(String batchId);

  List<AlertEntity> findAllByBatchIdAndNameIn(String batchId, List<String> alertNames);

  @Modifying
  @Query("""
      UPDATE core_bridge_alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId AND name IN(:alertNames)""")
  void updateAlertsStatusByBatchIdAndIdsIn(String batchId, String status, List<String> alertNames);

  @Modifying
  @Query("""
      UPDATE core_bridge_alerts
      SET status = :status, error_description = :errorDescription, updated_at = NOW()
      WHERE batch_id = :batchId AND name IN(:alertNames)""")
  void updateAlertsStatusWithErrorDescriptionByBatchIdAndAlertNames(
      String batchId, Set<String> alertNames, String status, String errorDescription);

  @Modifying
  @Query("""
      UPDATE core_bridge_alerts
      SET status = :status, updated_at = NOW()
      WHERE batch_id = :batchId""")
  void updateAlertsStatusByBatchId(String batchId, String status);
}
