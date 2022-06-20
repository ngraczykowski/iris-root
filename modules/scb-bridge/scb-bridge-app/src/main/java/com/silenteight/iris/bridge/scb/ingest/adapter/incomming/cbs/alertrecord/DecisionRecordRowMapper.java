/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.DateConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import javax.validation.Valid;

@RequiredArgsConstructor
class DecisionRecordRowMapper {

  private final DateConverter dateConverter;
  private final GnsSolutionMapper gnsSolutionMapper;

  @Valid
  public DecisionRecord mapResultSet(ResultSet resultSet) throws SQLException {
    var type = resultSet.getInt("type");

    return DecisionRecord.builder()
        .systemId(resultSet.getString("system_id"))
        .operator(resultSet.getString("operator"))
        .decisionDate(toInstant(resultSet.getString("decision_date")))
        .stateName(resultSet.getString("state_name"))
        .type(type)
        .comments(resultSet.getString("comments"))
        .solution(gnsSolutionMapper.mapSolution(type))
        .build();
  }

  private Instant toInstant(String date) {
    return dateConverter.convert(date).orElse(null);
  }
}
