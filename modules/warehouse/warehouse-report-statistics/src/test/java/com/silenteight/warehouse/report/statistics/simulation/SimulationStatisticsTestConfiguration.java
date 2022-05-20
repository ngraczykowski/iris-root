package com.silenteight.warehouse.report.statistics.simulation;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.production.persistence.ProductionPersistenceModule;
import com.silenteight.warehouse.report.statistics.ReportStatisticsModule;
import com.silenteight.warehouse.simulation.processing.SimulationProcessingModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;

@ComponentScan(basePackageClasses = {
    ReportStatisticsModule.class,
    ProductionPersistenceModule.class,
    SimulationProcessingModule.class
})
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@EnableAutoConfiguration
class SimulationStatisticsTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return ARBITRARY_INSTANCE;
  }
}
