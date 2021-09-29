package com.silenteight.warehouse.indexer.indextracking;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.environment.EnvironmentModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.warehouse.indexer.indextracking.IndexTrackingFixtures.CURRENT_DATETIME;

@ComponentScan(basePackageClasses = {
    IndexTrackingModule.class,
    EnvironmentModule.class
})
class IndexTrackingTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(CURRENT_DATETIME.toInstant());
  }
}
