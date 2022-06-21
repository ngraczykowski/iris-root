package com.silenteight.warehouse.management.group;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.RoleAccessorConfiguration;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.warehouse.alert.rest.RestAlertModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.indexer.alert.AlertDtoRowMapper;
import com.silenteight.warehouse.indexer.alert.AlertPostgresRepository;
import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.management.ManagementModule;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.Instant.parse;

@ComponentScan(basePackageClasses = {
    ManagementModule.class,
    RestAlertModule.class,
    DomainModule.class
})
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@Import(RoleAccessorConfiguration.class)
@RequiredArgsConstructor
@Slf4j
class CountryGroupTestConfiguration {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Bean
  TimeSource timeSource() {
    return new MockTimeSource(parse(PROCESSING_TIMESTAMP));
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public AlertRepository alertRepository() {
    return new AlertPostgresRepository(jdbcTemplate, new AlertDtoRowMapper());
  }
}
