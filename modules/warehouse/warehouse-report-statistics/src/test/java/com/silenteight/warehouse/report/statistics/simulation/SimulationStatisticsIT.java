package com.silenteight.warehouse.report.statistics.simulation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EffectivenessDto;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EfficiencyDto;
import com.silenteight.warehouse.report.statistics.simulation.query.StatisticsQuery;
import com.silenteight.warehouse.simulation.processing.SimulationAlertV1UseCase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.warehouse.report.statistics.simulation.SimulationStatisticsTestFixtures.*;
import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest(classes = SimulationStatisticsTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class SimulationStatisticsIT {

  @Autowired
  private StatisticsQuery statisticsQuery;

  @Autowired
  private PersistenceService persistenceService;

  @Autowired
  private SimulationAlertV1UseCase simulationAlertV1UseCase;

  @Test
  void shouldCountStatistics() {
    // given
    persistenceService.insert(ALERT_1);
    persistenceService.insert(ALERT_2);
    persistenceService.insert(ALERT_3);
    simulationAlertV1UseCase.handle(SIM_INDEX_REQUEST_1);

    // when
    SimulationStatisticsDto statistics = statisticsQuery.getStatistics(ANALYSIS_ID_1);

    // then
    EfficiencyDto efficiency = statistics.getEfficiency();
    EffectivenessDto effectiveness = statistics.getEffectiveness();

    assertThat(efficiency).isEqualTo(COUNT_STATISTICS_EFFICIENCY_DTO);
    assertThat(effectiveness).isEqualTo(COUNT_STATISTICS_EFFECTIVENESS_DTO);
  }

  @Test
  void shouldReturnEmptyStatistics() {
    // when
    SimulationStatisticsDto statistics = statisticsQuery.getStatistics(ANALYSIS_ID_1);

    // then
    EfficiencyDto efficiency = statistics.getEfficiency();
    EffectivenessDto effectiveness = statistics.getEffectiveness();

    assertThat(efficiency).isEqualTo(EMPTY_EFFICIENCY_DTO);
    assertThat(effectiveness).isEqualTo(EMPTY_EFFECTIVENESS_DTO);
  }
}
