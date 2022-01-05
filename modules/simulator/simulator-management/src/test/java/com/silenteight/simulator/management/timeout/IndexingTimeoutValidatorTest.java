package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.simulator.management.domain.SimulationTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static com.silenteight.simulator.management.SimulationFixtures.SIMULATION_DTO;
import static com.silenteight.simulator.management.SimulationFixtures.SIMULATION_DTO_2;
import static com.silenteight.simulator.management.SimulationFixtures.SIMULATION_DTO_3;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { SimulationTestConfiguration.class })
class IndexingTimeoutValidatorTest extends BaseDataJpaTest {

  @Autowired
  IndexingTimeoutValidator underTest;

  @Test
  void shouldReturnTrueWhenThereIsMessageOnIndexing() {
    //when
    boolean valid = underTest.valid(SIMULATION_DTO);

    //then
    assertThat(valid).isTrue();
  }

  @Test
  void shouldReturnTrueWhenLastMessageOnIndexingWasMoreThenHourAgo() {
    //when
    boolean valid = underTest.valid(SIMULATION_DTO_2);

    //then
    assertThat(valid).isTrue();
  }

  @Test
  void shouldReturnFalseWhenLastMessageOnIndexingWasLessThenHourAgo() {
    //when
    boolean valid = underTest.valid(SIMULATION_DTO_3);

    //then
    assertThat(valid).isFalse();
  }
}
