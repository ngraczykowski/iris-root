package com.silenteight.bridge.core.registration.adapter.outgoing;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudMatchRepository extends CrudRepository<MatchEntity, Long> {

  @Modifying
  @Query("""
      UPDATE matches
      SET status = :status, updated_at = NOW()
      WHERE match_id IN(:matchIds) AND alert_id IN(
        SELECT id
        FROM alerts
        WHERE batch_id = :batchId AND alert_id IN(:externalAlertIds)
      )
      """)
  void updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
      MatchEntity.Status status, String batchId, List<String> matchIds,
      List<String> externalAlertIds);
}
