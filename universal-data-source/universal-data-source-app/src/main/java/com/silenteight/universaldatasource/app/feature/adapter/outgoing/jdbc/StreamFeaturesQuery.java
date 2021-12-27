package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Component
class StreamFeaturesQuery {

  private static final String PEP_AGENT_INPUT_TYPE = "Feature";
  private static final int DEFAULT_BATCH_SIZE = 1024;

  @Language("PostgreSQL")
  private static final String SQL = "SELECT match_name,\n"
      + "       agent_input_type,\n"
      + "       JSON_OBJECT_AGG(feature_name, agent_input)\n"
      + "FROM uds_feature_input\n"
      + "         join uds_feature_mapper on mapped_feature_name = feature\n"
      + "WHERE agent_input_type =  :agentInputType\n"
      + "  AND match_name IN (:matchNames)\n"
      + "  AND feature_name IN (:featureNames)\n"
      + "GROUP BY 1, 2";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  int execute(BatchFeatureRequest batchFeatureRequest, Consumer<MatchFeatureOutput> consumer) {

    // TODO(jgajewski): SQL Injection
    var agentInputType = batchFeatureRequest.getAgentInputType();
    var parameters = new MapSqlParameterSource("agentInputType", agentInputType);
    parameters.addValue("matchNames", batchFeatureRequest.getMatches());
    parameters.addValue("featureNames", batchFeatureRequest.getFeatures());

    var features = jdbcTemplate.query(SQL, parameters,
        new FeatureExtractor(consumer, agentInputType, getChunkSize(agentInputType)));

    return features != null ? features : 0;
  }

  private static int getChunkSize(String agentInputType) {
    return PEP_AGENT_INPUT_TYPE.equals(agentInputType) ? 1 : DEFAULT_BATCH_SIZE;
  }
}
