package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Component
class StreamFeaturesQuery {

  @Language("PostgreSQL")
  private static final String SQL = "SELECT match_name, agent_input_type,\n"
      + "JSON_OBJECT_AGG(feature, agent_input)\n"
      + "FROM uds_feature_input\n"
      + "WHERE match_name IN (:matchNames) AND feature IN (:featureNames)\n"
      + "GROUP BY 1, 2";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  int execute(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<MatchFeatureOutput> consumer) {

    // TODO(jgajewski): SQL Injection
    var parameters = new MapSqlParameterSource("matchNames", matchNames);
    parameters.addValue("featureNames", featureNames);

    var features = jdbcTemplate.query(SQL, parameters, new FeatureExtractor(consumer));
    return features != null ? features : 0;
  }
}
