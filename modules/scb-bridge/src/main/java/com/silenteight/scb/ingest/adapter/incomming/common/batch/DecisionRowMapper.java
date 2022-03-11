package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution;
import com.silenteight.scb.ingest.adapter.incomming.common.util.AlertParserUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
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

    Decision.DecisionBuilder builder = Decision
        .builder()
        .createdAt(decisionDate)
        .id(makeDecisionId(operator, type, decisionDate))
        .solution(mapDecisionType(type));

    AlertParserUtils.mapString(resultSet.getString("comments"), builder::comment);
    AlertParserUtils.mapString(operator, builder::authorId);

    if (isNotEmpty(stateName))
      builder.stateName(stateName);

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

  private AnalystSolution mapDecisionType(int type) {
    return gnsSolutionMapper.mapSolution(type);
  }
}
