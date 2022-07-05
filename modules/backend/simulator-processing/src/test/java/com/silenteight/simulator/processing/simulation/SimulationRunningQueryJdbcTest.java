/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.simulation;

import com.silenteight.simulator.processing.alert.index.domain.State;
import com.silenteight.simulator.processing.simulation.SimulationRunningQueryJdbc.AnalysisStateAlertCountRow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationRunningQueryJdbcTest {

  @Mock JdbcTemplate jdbcTemplate;

  @Test
  void test() {
    // given
    SimulationRunningQuery simulationJdbcProcessingQuery =
        new SimulationRunningQueryJdbc(jdbcTemplate);
    when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
        .thenReturn(
            List.of(
                new AnalysisStateAlertCountRow("analysis/1", State.SENT, 1),
                new AnalysisStateAlertCountRow("analysis/1", State.SENT, 1),
                new AnalysisStateAlertCountRow("analysis/1", State.ACKED, 1),
                new AnalysisStateAlertCountRow("analysis/2", State.SENT, 10)));

    // when
    Map<String, Map<State, Long>> stringSimulationStatusAlertIndexedCountMap =
        simulationJdbcProcessingQuery.indexedAlertStatusesInAnalysis();

    // then
    assertThat(stringSimulationStatusAlertIndexedCountMap)
        .isNotNull()
        .hasSize(2)
        .containsEntry("analysis/1", Map.of(State.ACKED, 1L, State.SENT, 2L))
        .containsEntry("analysis/2", Map.of(State.SENT, 10L));
  }
}
