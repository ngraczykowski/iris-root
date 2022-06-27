package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.common.time.TimeSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static com.silenteight.simulator.management.SimulationFixtures.SIMULATION_DTO;
import static com.silenteight.simulator.management.SimulationFixtures.SIMULATION_DTO_3;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@TestPropertySource("classpath:/data-test.properties")
@SpringBootTest(classes = SimulationTimeoutConfiguration.class)
class UpdateTimeoutValidatorTest {

  @Autowired
  UpdateTimeoutValidator underTest;

  @MockBean
  SimulationTimeoutService simulationTimeoutService;

  @MockBean
  SimulationTimeoutJobScheduler simulationTimeoutJobScheduler;

  @MockBean
  AnalysisTimeoutValidator analysisTimeoutValidator;

  @MockBean
  IndexingTimeoutValidator indexingTimeoutValidator;

  @MockBean
  TimeSource timeSource;

  @MockBean
  SimulationLastCheckTimes simulationLastCheckTimes;

  @Test
  void shouldReturnTrueWhenLastUpdateWasMoreThanHourAgo() {
    given(timeSource.offsetDateTime()).willReturn(SIMULATION_DTO.getUpdatedAt().plusMinutes(61));

    //when
    boolean valid = underTest.valid(SIMULATION_DTO);

    //then
    assertThat(valid).isTrue();
  }

  @Test
  void shouldReturnFalseWhenLastUpdateWasLessThanHourAgo() {
    given(timeSource.offsetDateTime()).willReturn(SIMULATION_DTO_3.getUpdatedAt().plusMinutes(59));
    //when
    boolean valid = underTest.valid(SIMULATION_DTO_3);

    //then
    assertThat(valid).isFalse();
  }
}
