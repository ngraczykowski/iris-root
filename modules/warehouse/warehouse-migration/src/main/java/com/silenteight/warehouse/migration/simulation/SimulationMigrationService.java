package com.silenteight.warehouse.migration.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
class SimulationMigrationService {

  @NonNull
  private final SimulationAlertInsertService simulationAlertInsertService;

  @NonNull
  private final SimulationDbPartitionFactory simulationDbPartitionFactory;

  @Transactional
  public void migrateAnalysis(String analysisName) {
    simulationDbPartitionFactory.createDbPartition(analysisName);
    simulationAlertInsertService.migrateAlertToMatch(analysisName);
    simulationAlertInsertService.setMigrationFlag(analysisName, true);
  }

  @Transactional
  public int markFailed(String analysisName) {
    return simulationAlertInsertService.setMigrationFlag(analysisName, false);
  }
}
