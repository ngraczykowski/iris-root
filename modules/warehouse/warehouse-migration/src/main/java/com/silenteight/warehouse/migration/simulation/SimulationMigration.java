package com.silenteight.warehouse.migration.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertQueryService;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
class SimulationMigration {

  @NonNull
  private final SimulationMigrationService simulationMigrationService;

  @NonNull
  private final SimulationAlertQueryService simulationAlertQueryService;

  @EventListener(ApplicationStartedEvent.class)
  public void migrate() {
    var analysisNamesForMigration = simulationAlertQueryService.getAnalysisNamesForMigration();
    if (analysisNamesForMigration.isEmpty()) {
      log.info("There are no alert-level simulations that qualify for migration to match-level.");
      return;
    }

    log.debug("Migration of alert-level simulation data to match-level started, "
        + "simulationsForMigrationCount={}", analysisNamesForMigration.size());

    int migratedAnalysisCount = 0;
    int failedAnalysisCount = 0;
    for (String analysisName : analysisNamesForMigration) {
      try {
        log.debug("Migration of alert-level simulation data to match-level in progress, "
            + "analysisName={}", analysisName);
        simulationMigrationService.migrateAnalysis(analysisName);
        migratedAnalysisCount++;
      } catch (RuntimeException e) {
        log.error("Migration of an analysis failed, will attempt to mark migrated as false "
            + "and continue, failedAnalysisName=" + analysisName, e.getCause());
        simulationMigrationService.markFailed(analysisName);
        failedAnalysisCount++;
      }
    }

    log.info("Migration of alert-level simulation data to match-level completed, "
            + "migratedAnalysisCount={}, failedAnalysisCount={}",
        migratedAnalysisCount, failedAnalysisCount);
  }
}
