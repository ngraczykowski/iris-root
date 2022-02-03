package com.silenteight.warehouse.report.reporting;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.query.streaming.DataProvider;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.storage.ReportStorage;
import com.silenteight.warehouse.report.storage.temporary.TemporaryFileStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReportingConfiguration {

  @Bean
  ReportGenerationService reportGenerationService(
      DataProvider provider,
      TemporaryFileStorage temporaryFileStorage,
      ReportStorage reportStorage,
      SqlExecutor sqlExecutor,
      UserAwareTokenProvider userAwareTokenProvider,
      CountryPermissionService countryPermissionService) {

    return new ReportGenerationService(provider, temporaryFileStorage,
        reportStorage, sqlExecutor, userAwareTokenProvider, countryPermissionService);
  }
}
