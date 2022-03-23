package com.silenteight.payments.bridge.app.reports;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DailyReportsProperties.class)
class RemoveDailyReportsConfiguration {

  private final DailyReportsProperties dailyReportsProperties;
  private final CsvFileResourceProvider csvFileResourceProvider;

  @Bean
  @Profile("mockaws")
  ProcessRemoveDailyReportsUseCase mockedProcessRemoveDailyReportsUseCase() {
    return new MockedProcessRemoveDailyReportsService();
  }


  @Bean
  @Profile("!mockaws")
  ProcessRemoveDailyReportsUseCase processRemoveDailyReportsUseCase() {
    return new ProcessRemoveDailyReportsService(dailyReportsProperties, csvFileResourceProvider);
  }

}
