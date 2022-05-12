package com.silenteight.warehouse.report.download;

import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
class DownloadConfiguration {

  @Bean
  DownloadService downloadService(
      ReportPersistenceService reportPersistenceService,
      ReportStorage reportStorage,
      @Qualifier("reportNameResolvers") Map<String, ReportFileName> reportNameResolvers) {

    return new DownloadService(
        reportPersistenceService,
        reportStorage,
        reportNameResolvers,
        IsoOffsetDateFormatter.INSTANCE
    );
  }
}
