package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class DeleteAgentExchangeMatchFeatureByIdsQuery {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void execute(List<Long> ids) {
    var parameters = new MapSqlParameterSource(Map.of("ids", ids));
    jdbcTemplate.update(
        "DELETE\n"
            + "FROM ae_agent_exchange_match_feature aaemf\n"
            + "WHERE aaemf.match_id IN\n"
            + "(SELECT distinct am.match_id\n"
            + "FROM ae_match am\n"
            + "WHERE am.alert_id IN (:ids))",
        parameters);
  }
}
