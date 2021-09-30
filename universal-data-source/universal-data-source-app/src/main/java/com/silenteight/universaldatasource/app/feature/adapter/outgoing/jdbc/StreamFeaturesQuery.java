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

  @Language("PostgreSQL")
  private static final String SQL = "SELECT match_name, agent_input_type,\n"
      + "JSON_OBJECT_AGG(feature, agent_input)\n"
      + "FROM uds_feature_input\n"
      + "WHERE agent_input_type = :agentInputType \n"
      + "AND match_name IN (:matchNames) AND feature IN (:featureNames)\n"
      + "GROUP BY 1, 2";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  int execute(BatchFeatureRequest batchFeatureRequest, Consumer<MatchFeatureOutput> consumer) {

    // TODO(jgajewski): SQL Injection
    var parameters =
        new MapSqlParameterSource("agentInputType", batchFeatureRequest.getAgentInputType());
    parameters.addValue("matchNames", batchFeatureRequest.getMatches());
    parameters.addValue("featureNames", batchFeatureRequest.getFeatures());

    var features = jdbcTemplate.query(SQL, parameters,
        new FeatureExtractor(consumer, batchFeatureRequest.getAgentInputType()));
    return features != null ? features : 0;
  }
}
