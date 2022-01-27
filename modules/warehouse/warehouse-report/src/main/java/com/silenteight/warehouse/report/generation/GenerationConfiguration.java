package com.silenteight.warehouse.report.generation;

import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GenerationConfiguration {

  @Bean
  GenerationService reportGenerationService2(
      ReportPersistenceService reportPersistenceService,
      ReportStorage reportStorage,
      SqlExecutor sqlExecutor) {

    return new GenerationService(
        reportPersistenceService,
        reportStorage,
        sqlExecutor);
  }
}
