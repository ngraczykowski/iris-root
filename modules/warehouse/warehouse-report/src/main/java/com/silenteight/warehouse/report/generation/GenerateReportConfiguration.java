package com.silenteight.warehouse.report.generation;

import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GenerateReportConfiguration {

  @Bean
  GenerateReportService generateReportService(
      SqlExecutor sqlExecutor,
      ReportStorage reportStorage) {

    return new GenerateReportService(sqlExecutor, reportStorage);
  }
}
