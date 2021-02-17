package com.silenteight.simulator.management;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface SimulationEntityRepository extends Repository<SimulationEntity, Long> {

  SimulationEntity save(SimulationEntity simulationEntity);

  Collection<SimulationEntity> findAll();
}
