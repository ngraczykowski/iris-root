/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

class DatabaseCbsHitDetailsReader implements CbsHitDetailsReader {

  private final JdbcCbsHitDetailsHelperQuery query;

  DatabaseCbsHitDetailsReader(JdbcTemplate jdbcTemplate) {
    query = new JdbcCbsHitDetailsHelperQuery(jdbcTemplate);
  }

  @Transactional(transactionManager = "externalTransactionManager", readOnly = true)
  @Override
  public List<CbsHitDetails> read(String dbRelationName, Collection<AlertId> alertIds) {
    if (alertIds.isEmpty()) {
      return List.of();
    }

    var systemIds = alertIds.stream()
        .map(AlertId::getSystemId)
        .toList();
    var foundCbsHitDetails = query.execute(dbRelationName, systemIds);

    return foundCbsHitDetails.stream()
        .filter(r -> alertIds.contains(toAlertId(r)))
        .toList();
  }

  private AlertId toAlertId(CbsHitDetails r) {
    return new AlertId(r.getSystemId(), r.getBatchId());
  }
}
