/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetailsRowMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.validation.OracleRelationName;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.QueryStatementHelper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import com.google.common.collect.Lists;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.validation.Valid;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class JdbcCbsHitDetailsHelperQuery {

  @Language("Oracle")
  private static final String CBS_HITS_DETAILS_QUERY =
      "SELECT SYSTEM_ID, BATCH_ID, SEQ_NO, HIT_NEO_FLAG"
          + " FROM :dbRelationName"
          + " WHERE SYSTEM_ID IN (%s)";
  private static final int HITS_PER_RECORD = 1_000;
  private final CbsHitDetailsRowMapper mapper = new CbsHitDetailsRowMapper();
  private final JdbcTemplate jdbcTemplate;

  List<CbsHitDetails> execute(
      @Valid @OracleRelationName String dbRelationName, Collection<String> systemIds) {

    if (systemIds.isEmpty())
      return of();

    return Lists.partition(new ArrayList<>(systemIds), HITS_PER_RECORD).stream()
        .map(ids -> getCbsHitDetails(dbRelationName, ids))
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(toList());
  }

  @Nonnull
  private List<CbsHitDetails> getCbsHitDetails(
      String dbRelationName, Collection<String> systemIds) {
    String query =
        QueryStatementHelper.prepareQuery(CBS_HITS_DETAILS_QUERY, dbRelationName, systemIds);
    jdbcTemplate.setFetchSize(HITS_PER_RECORD);
    return jdbcTemplate.query(query, getArgumentSetter(systemIds), mapper);
  }

  @Nonnull
  private static ArgumentPreparedStatementSetter getArgumentSetter(Collection<String> systemIds) {
    return new ArgumentPreparedStatementSetter(systemIds.toArray());
  }
}
