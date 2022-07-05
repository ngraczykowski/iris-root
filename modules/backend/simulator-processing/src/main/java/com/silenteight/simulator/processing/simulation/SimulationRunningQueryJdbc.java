/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.simulator.processing.alert.index.domain.State;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class SimulationRunningQueryJdbc implements SimulationRunningQuery {

  private static final String QUERY_ACTIVE_ANALYSIS = """
      select ss.analysis_name, sia.state, sum(sia.alert_count)
           from simulator_simulation ss
                    join simulator_indexed_alert sia on ss.analysis_name = sia.analysis_name
           where ss.state = 'RUNNING'
           group by ss.analysis_name, sia.state
      """;
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Map<String, Map<State, Long>> indexedAlertStatusesInAnalysis() {
    return jdbcTemplate.query(QUERY_ACTIVE_ANALYSIS, mapper())
        .stream()
        .collect(Collectors.toMap(
            AnalysisStateAlertCountRow::analysisName,
            SimulationRunningQueryJdbc::getStateLongEnumMap,
            (s1, s2) -> Stream.of(s1, s2)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, Long::sum))));
  }

  @NotNull
  private static EnumMap<State, Long> getStateLongEnumMap(AnalysisStateAlertCountRow row) {
    var map = new EnumMap<State, Long>(State.class);
    map.put(row.state(), row.count());
    return map;
  }

  @NotNull
  private static RowMapper<AnalysisStateAlertCountRow> mapper() {
    return (rs, rowNum) -> new AnalysisStateAlertCountRow(
        rs.getString(1), State.from(rs.getString(2)), rs.getLong(3));
  }

  record AnalysisStateAlertCountRow(@NonNull String analysisName, @NonNull State state, long count){

  }

}
