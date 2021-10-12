package com.silenteight.warehouse.indexer.alert.indexing;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.auth.token.TokenModule;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.query.single.SingleAlertQueryConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;

@ComponentScan(basePackageClasses = {
    AlertConfiguration.class,
    SingleAlertQueryConfiguration.class,
    TestElasticSearchModule.class,
    TokenModule.class
})
@RequiredArgsConstructor
class AlertTestConfiguration {

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }
}
