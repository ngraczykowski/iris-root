package com.silenteight.simulator.management.domain;

import com.silenteight.simulator.management.domain.exception.SimulationNotInProperStateException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.OffsetDateTime;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;
import static com.silenteight.simulator.management.domain.SimulationState.*;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
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
  @EnumSource(
      value = SimulationState.class,
      names = { "ARCHIVED", "PENDING", "RUNNING", "NEW", "CANCELED" })
  void shouldThrowExceptionWhenNotInStateForArchive(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(
        simulation::archive)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: [DONE]."));
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
  @EnumSource(
      value = SimulationState.class,
      names = { "ARCHIVED", "DONE", "RUNNING", "NEW", "CANCELED" })
  void shouldThrowExceptionWhenNotInStateForRun(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(
        simulation::run)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: [PENDING]."));
  }

  @Test
  void shouldFinishSimulation() {
    // given
    SimulationEntity simulation = createSimulationEntity(RUNNING);
    OffsetDateTime mockTime = ARBITRARY_INSTANCE.offsetDateTime();

    // when
    simulation.finish(mockTime);

    // then
    assertThat(simulation.getState()).isEqualTo(DONE);
    assertThat(simulation.getFinishedAt()).isEqualTo(mockTime);
  }

  @ParameterizedTest
  @EnumSource(
      value = SimulationState.class,
      names = { "PENDING", "DONE", "ARCHIVED", "NEW", "CANCELED" })
  void shouldThrowExceptionWhenNotInStateForFinish(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);
    OffsetDateTime currentTime = now();

    // then
    assertThatThrownBy(
        () -> simulation.finish(currentTime))
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage(format("Simulation should be in state: [RUNNING]."));
  }

  @Test
  void shouldCancelSimulationWhenPending() {
    // given
    SimulationEntity simulation = createSimulationEntity(PENDING);

    // when
    simulation.cancel();

    // then
    assertThat(simulation.getState()).isEqualTo(CANCELED);
  }

  @Test
  void shouldCancelSimulationWhenRunning() {
    // given
    SimulationEntity simulation = createSimulationEntity(RUNNING);

    // when
    simulation.cancel();

    // then
    assertThat(simulation.getState()).isEqualTo(CANCELED);
  }

  @ParameterizedTest
  @EnumSource(
      value = SimulationState.class,
      names = { "DONE", "ARCHIVED", "NEW", "CANCELED" })
  void shouldThrowExceptionWhenNotInStateForCancel(SimulationState state) {
    // given
    SimulationEntity simulation = createSimulationEntity(state);

    // then
    assertThatThrownBy(simulation::cancel)
        .isInstanceOf(SimulationNotInProperStateException.class)
        .hasMessage("Simulation should be in state: [PENDING, RUNNING].");
  }

  @Test
  void shouldCreateSimulationEntityWithInitialState() {
    // given
    SimulationEntity simulation = SimulationEntity.builder().datasetNames(of()).build();

    // then
    assertThat(simulation.getState()).isEqualTo(NEW);
    assertThat(simulation.getFinishedAt()).isNull();
  }

  private SimulationEntity createSimulationEntity(SimulationState state) {
    return SimulationEntity.builder()
        .datasetNames(of())
        .state(state)
        .build();
  }
}
