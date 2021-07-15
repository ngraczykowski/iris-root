package com.silenteight.simulator.management.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface SimulationEntityRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAll();

  Collection<SimulationEntity> findAllByModelName(String modelName);

  Optional<SimulationEntity> findByAnalysisName(String analysisName);

  Optional<SimulationEntity> findBySimulationId(UUID simulationId);

  @Query(value = "SELECT SUM(d.initial_alert_count)"
      + " FROM simulator_simulation s"
      + " JOIN simulator_simulation_dataset_name sdn ON s.simulation_id = sdn.simulation_id"
      + " JOIN simulator_dataset d ON sdn.dataset_name = d.external_resource_name"
      + " WHERE s.analysis_name = :analysisName", nativeQuery = true)
  Optional<Long> countAllAlerts(String analysisName);
}
