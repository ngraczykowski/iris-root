package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
class SelectAgentExchangeMatchFeatureIdsByAlertIdsQuery {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  List<Long> execute(List<Long> alertIds) {
    var paramMap = Map.of("ids", alertIds);

    return jdbcTemplate.queryForList(
        "SELECT distinct aaemf.agent_exchange_match_feature_id\n"
            + "FROM ae_agent_exchange_match_feature aaemf\n"
            + "WHERE aaemf.match_id IN\n"
            + "(SELECT distinct am.match_id\n"
            + "FROM ae_match am\n"
            + "WHERE am.alert_id IN (:ids))",
        paramMap, Long.class);
  }
}
