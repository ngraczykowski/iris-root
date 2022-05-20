package com.silenteight.warehouse.migration.simulation;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.processing.SimulationProcessingModule;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.PartitioningModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;

@ComponentScan(basePackageClasses = {
    PartitioningModule.class,
    SimulationProcessingModule.class,
    SimulationMigrationModule.class
})
@EnableAutoConfiguration
class SimulationMigrationTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return ARBITRARY_INSTANCE;
  }
}
