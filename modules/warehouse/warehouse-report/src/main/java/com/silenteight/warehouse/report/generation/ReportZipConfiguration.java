package com.silenteight.warehouse.report.generation;

import com.silenteight.warehouse.common.time.OffsetDateTimeFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(ReportZipProperties.class)
public class ReportZipConfiguration {

  @Bean
  ReportTempFileCreator reportZipService(
      ReportZipProperties reportZipProperties,
      @Qualifier("reportNameResolvers") Map<String, ReportFileName> reportNameResolvers) {
    return new ReportTempFileCreator(
        reportZipProperties,
        reportNameResolvers,
        OffsetDateTimeFormatter.INSTANCE
    );
  }
}
