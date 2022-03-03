package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.proto.serp.scb.v1.ScbDecisionDetails;
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
import static com.silenteight.sep.base.common.protocol.AnyUtils.pack;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
class DecisionRowMapper {

  private final DateConverter dateConverter;
  private final GnsSolutionMapper gnsSolutionMapper;

  Decision mapRow(ResultSet resultSet) throws SQLException {
    var operator = resultSet.getString("operator");
    var decisionDate = dateConverter.convert(resultSet.getString("decision_date")).orElse(null);
    var type = resultSet.getInt("type");
    var stateName = resultSet.getString("state_name");

    Builder builder = Decision
        .newBuilder()
        .setCreatedAt(toTimestampOrDefault(decisionDate, Timestamp.getDefaultInstance()))
        .setId(makeDecisionId(operator, type, decisionDate))
        .setSolution(mapDecisionType(type));

    mapString(resultSet.getString("comments"), builder::setComment);
    mapString(operator, builder::setAuthorId);

    if (isNotEmpty(stateName))
      builder.setDetails(pack(ScbDecisionDetails.newBuilder().setStateName(stateName).build()));

    return builder.build();
  }

  private static ObjectId makeDecisionId(
      @Nullable String operator,
      int type,
      @Nullable Instant date) {

    return ObjectIds.fromUuidAndSource(
        UUID.randomUUID(), nullToEmpty(operator) + "@" + type, String.valueOf(date));
  }

  private AnalystSolution mapDecisionType(int type) {
    return gnsSolutionMapper.mapSolution(type);
  }
}
