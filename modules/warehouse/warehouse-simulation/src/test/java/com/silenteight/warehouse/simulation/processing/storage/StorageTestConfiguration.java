package com.silenteight.warehouse.simulation.processing.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.simulation.processing.SimulationProcessingModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;

@ComponentScan(basePackageClasses = {
    SimulationProcessingModule.class
})
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class StorageTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }
}
