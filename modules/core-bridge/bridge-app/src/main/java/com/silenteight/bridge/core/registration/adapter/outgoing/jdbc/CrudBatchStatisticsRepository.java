package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudBatchStatisticsRepository extends CrudRepository<AlertEntity, Long> {

  @Query("""
      SELECT status, COUNT(status)
      FROM core_bridge_alerts
      WHERE batch_id = :batchId
      GROUP BY status
      """)
  List<BatchStatisticProjection> getAlertsStatusStatistics(String batchId);

  @Query("""
      SELECT status, COUNT(status)
      FROM core_bridge_alerts
      WHERE batch_id = :batchId AND name IN(:alertsNames)
      GROUP BY status
      """)
  List<BatchStatisticProjection> getAlertsStatusStatistics(
      String batchId, List<String> alertsNames);
}
