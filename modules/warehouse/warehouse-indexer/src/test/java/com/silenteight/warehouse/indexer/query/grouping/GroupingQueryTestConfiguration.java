package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.indexer.query.QueryAlertModule;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    QueryAlertModule.class
})
@JdbcTest
class GroupingQueryTestConfiguration {

  @Bean
  UserAwareTokenProvider userAwareTokenProvider() {
    return mock(UserAwareTokenProvider.class);
  }
}
