/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.AlertParserUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;

@RequiredArgsConstructor
class EcmDecisionRowMapper {

  private static final String ACTION_DATE_COLUMN = "ACTION_DATE";
  private static final String ANALYST_DECISION_COLUMN = "ANALYST_DECISION";
  private static final String ANALYST_COMMENTS_COLUMN = "ANALYST_COMMENTS";
  private static final String OPERATOR = "ECM";

  private final DateConverter dateConverter;
  private final EcmAnalystDecisionMapper ecmAnalystDecisionMapper;

  Decision mapRow(ResultSet resultSet) throws SQLException {
    Instant decisionDate =
        dateConverter.convert(resultSet.getString(ACTION_DATE_COLUMN)).orElse(null);
    final Decision.AnalystSolution analystDecision =
        ecmAnalystDecisionMapper.mapEcmDecision(resultSet.getString(ANALYST_DECISION_COLUMN));

    Decision.DecisionBuilder builder = Decision
        .builder()
        .createdAt(decisionDate)
        .id(makeDecisionId(OPERATOR, analystDecision.getValue(), decisionDate))
        .solution(analystDecision);

    AlertParserUtils.mapString(resultSet.getString(ANALYST_COMMENTS_COLUMN), builder::comment);
    AlertParserUtils.mapString(OPERATOR, builder::authorId);

    return builder.build();
  }

  private static ObjectId makeDecisionId(
      @Nullable String operator,
      int type,
      @Nullable Instant date) {
    return ObjectId.builder()
        .id(UUID.randomUUID())
        .sourceId(nullToEmpty(operator) + "@" + type)
        .discriminator(String.valueOf(date))
        .build();
  }
}
