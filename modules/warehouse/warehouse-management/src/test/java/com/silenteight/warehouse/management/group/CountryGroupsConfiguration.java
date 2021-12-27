package com.silenteight.warehouse.management.group;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.alert.rest.RestAlertModule;
import com.silenteight.warehouse.common.elastic.ElasticsearchRestClientModule;
import com.silenteight.warehouse.common.environment.EnvironmentModule;
import com.silenteight.warehouse.common.opendistro.OpendistroModule;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.common.testing.elasticsearch.TestElasticSearchModule;
import com.silenteight.warehouse.indexer.query.QueryAlertModule;
import com.silenteight.warehouse.management.ManagementModule;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;
import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    QueryAlertModule.class,
    OpendistroModule.class,
    ElasticsearchRestClientModule.class,
    EnvironmentModule.class,
    ManagementModule.class,
    TestElasticSearchModule.class,
    UserAwareTokenProvider.class,
    RestAlertModule.class
})
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@RequiredArgsConstructor
@Slf4j
class CountryGroupsConfiguration {

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }

  // Current implementation breaks the test-specific setup:
  // In the test setup we are using basic auth, and the role is attached directly to the user
  // In the production setup we are using JWT auth, and the role needs to be linked to client's role
  @Primary
  @Bean
  RolesMappingService rolesMappingServiceMock() {
    return mock(RolesMappingService.class);
  }
}
