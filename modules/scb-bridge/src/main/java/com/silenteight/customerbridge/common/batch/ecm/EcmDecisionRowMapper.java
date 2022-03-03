package com.silenteight.customerbridge.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.batch.DateConverter;
import com.silenteight.proto.serp.v1.alert.AnalystSolution;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.proto.serp.v1.alert.Decision.Builder;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.ObjectIds;

import com.google.protobuf.Timestamp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.customerbridge.common.util.AlertParserUtils.mapString;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestampOrDefault;

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
    final AnalystSolution analystDecision =
        ecmAnalystDecisionMapper.mapEcmDecision(resultSet.getString(ANALYST_DECISION_COLUMN));

    Builder builder = Decision
        .newBuilder()
        .setCreatedAt(toTimestampOrDefault(decisionDate, Timestamp.getDefaultInstance()))
        .setId(makeDecisionId(OPERATOR, analystDecision.getNumber(), decisionDate))
        .setSolution(analystDecision);

    mapString(resultSet.getString(ANALYST_COMMENTS_COLUMN), builder::setComment);
    mapString(OPERATOR, builder::setAuthorId);

    return builder.build();
  }

  private static ObjectId makeDecisionId(
      @Nullable String operator,
      int type,
      @Nullable Instant date) {

    return ObjectIds.fromUuidAndSource(
        UUID.randomUUID(), nullToEmpty(operator) + "@" + type, String.valueOf(date));
  }
}
