package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.indexer.query.streaming.DataProvider;
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
      ReportStorage reportStorage) {

    return new ReportGenerationService(provider, temporaryFileStorage, reportStorage);
  }
}
