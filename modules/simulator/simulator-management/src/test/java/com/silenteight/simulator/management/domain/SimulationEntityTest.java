package com.silenteight.simulator.management.domain;

import com.silenteight.simulator.management.domain.exception.SimulationNotInProperStateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.silenteight.simulator.management.domain.SimulationState.*;
import static java.lang.String.format;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;

class SimulationEntityTest {

  @Test
  void shouldArchiveSimulation() {
    // given
    SimulationEntity simulation = createSimulationEntity(DONE);

    // when
    simulation.archive();

    // then
    assertThat(simulation.getState()).isEqualTo(ARCHIVED);
  }

  @ParameterizedTest
  @EnumSource(value = SimulationState.class, names = { "ARCHIVED", "PENDING", "RUNNING", "NEW" })
  void shouldThrowExceptionWhenNotInStateForArchive(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(
        simulation::archive)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: %s.", DONE));
  }

  @Test
  void shouldRunSimulation() {
    // given
    SimulationEntity simulation = createSimulationEntity(PENDING);

    // when
    simulation.run();

    // then
    assertThat(simulation.getState()).isEqualTo(RUNNING);
  }

  @ParameterizedTest
  @EnumSource(value = SimulationState.class, names = { "ARCHIVED", "DONE", "RUNNING", "NEW" })
  void shouldThrowExceptionWhenNotInStateForRun(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(
        simulation::run)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: %s.", PENDING));
  }

  @Test
  void shouldFinishSimulation() {
    // given
    SimulationEntity simulation = createSimulationEntity(RUNNING);

    // when
    simulation.finish();

    // then
    assertThat(simulation.getState()).isEqualTo(DONE);
  }

  @ParameterizedTest
  @EnumSource(value = SimulationState.class, names = { "PENDING", "DONE", "ARCHIVED", "NEW" })
  void shouldThrowExceptionWhenNotInStateForFinish(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(
        simulation::finish)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: %s.", RUNNING));
  }

  @Test
  void shouldCreateSimulationEntityWithInitialState() {
    // given
    SimulationEntity simulation = SimulationEntity.builder().datasetNames(of()).build();

    // then
    assertThat(simulation.getState()).isEqualTo(NEW);
  }

  private SimulationEntity createSimulationEntity(SimulationState state) {
    return SimulationEntity.builder()
        .datasetNames(of())
        .state(state)
        .build();
  }
}