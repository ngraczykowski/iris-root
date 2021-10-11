package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;


import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureInput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InsertFeatureQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO uds_feature_input(match_name, feature, agent_input_type, agent_input)\n"
          + " VALUES (:match_name, :feature, :agent_input_type, :agent_input)\n"
          + " ON CONFLICT DO NOTHING\n"
          + "RETURNING agent_input_id, match_name, feature, agent_input_type, agent_input";

  private static final String MATCH_NAME = "match_name";
  private static final String FEATURE = "feature";
  private static final String AGENT_INPUT_TYPE = "agent_input_type";
  private static final String AGENT_INPUT = "agent_input";

  private final BatchSqlUpdate batchSqlUpdate;

  InsertFeatureQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(MATCH_NAME, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(FEATURE, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(AGENT_INPUT_TYPE, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(AGENT_INPUT, Types.OTHER));
    batchSqlUpdate.setReturnGeneratedKeys(true);

    batchSqlUpdate.compile();
  }

  List<CreatedAgentInput> execute(Collection<MatchFeatureInput> matchFeatureInputs) {
    List<Map<String, Object>> keyList = new ArrayList<>();
    var keyHolder = new GeneratedKeyHolder();
    for (MatchFeatureInput matchFeatureInput : matchFeatureInputs) {
      update(matchFeatureInput, keyHolder);
      keyList.addAll(keyHolder.getKeyList());
    }

    batchSqlUpdate.flush();

    return getCreatedAgentInputs(keyList);
  }

  private void update(MatchFeatureInput matchFeatureInput, KeyHolder keyHolder) {
    var paramMap =
        Map.of(MATCH_NAME, matchFeatureInput.getMatch(),
            FEATURE, matchFeatureInput.getFeature(),
            AGENT_INPUT_TYPE, matchFeatureInput.getAgentInputType(),
            AGENT_INPUT, matchFeatureInput.getAgentInput());

    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }

  private static List<CreatedAgentInput> getCreatedAgentInputs(List<Map<String, Object>> keyList) {
    return keyList
        .stream()
        .map(it -> CreatedAgentInput
            .newBuilder()
            .setName("agent-inputs/" + it.get("agent_input_id").toString())
            .setMatch(it.get(MATCH_NAME).toString())
            .build()).collect(Collectors.toList());
  }
}
