package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

class DatabaseCbsHitDetailsReader implements CbsHitDetailsReader {

  private final JdbcCbsHitDetailsHelperQuery query;

  DatabaseCbsHitDetailsReader(JdbcTemplate jdbcTemplate) {
    query = new JdbcCbsHitDetailsHelperQuery(jdbcTemplate);
  }

  @Transactional(
      transactionManager = "externalTransactionManager",
      isolation = SERIALIZABLE,
      readOnly = true)
  @Override
  public List<CbsHitDetails> read(String dbRelationName, Collection<AlertId> alertIds) {
    if (alertIds.isEmpty()) {
      return List.of();
    }

    var systemIds = alertIds.stream()
        .map(AlertId::getSystemId)
        .collect(Collectors.toList());
    var foundCbsHitDetails = query.execute(dbRelationName, systemIds);

    return foundCbsHitDetails.stream()
        .filter(r -> alertIds.contains(toAlertId(r)))
        .collect(Collectors.toList());
  }

  private AlertId toAlertId(CbsHitDetails r) {
    return AlertId.builder().batchId(r.getBatchId()).systemId(r.getSystemId()).build();
  }
}
