package com.silenteight.simulator.management.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface SimulationRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAllByStateIn(Collection<SimulationState> states);

  Collection<SimulationEntity> findAllByModelNameIn(Collection<String> modelNames);

  Optional<SimulationEntity> findByAnalysisName(String analysisName);

  Optional<SimulationEntity> findSimulationEntityBySimulationId(UUID simulationId);

  @Query(value = "SELECT DISTINCT s.analysis_name"
      + " FROM simulator_simulation s"
      + " JOIN simulator_simulation_dataset_name sdn "
      + "   ON s.simulation_id = sdn.simulation_id"
      + " WHERE sdn.dataset_name IN :datasetNames", nativeQuery = true)
  Collection<String> findAllAnalysisNamesByDatasetNames(Collection<String> datasetNames);

  @Query(value = "SELECT s.analysis_name"
      + " FROM simulator_simulation s"
      + " WHERE s.simulation_id = :simulationId", nativeQuery = true)
  String findAnalysisNameBySimulationId(UUID simulationId);
}
