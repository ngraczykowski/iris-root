package com.silenteight.warehouse.report.remove;

import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import javax.validation.Valid;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ReportsRemovalProperties.class)
class ReportsRemovalConfiguration {

  @Bean
  ReportsRemovalService reportsRemovalService(
      TimeSource timeSource,
      List<ReportsRemoval> reportsRemovalList,
      @Valid ReportsRemovalProperties reportsRemovalProperties) {

    return new ReportsRemovalService(
        timeSource, reportsRemovalProperties.getDurationTime(),
        reportsRemovalList);
  }

  @Bean
  ReportsRemovalJobScheduler reportsRemovalJobScheduler(
      ReportsRemovalService reportsRemovalService) {

    return new ReportsRemovalJobScheduler(reportsRemovalService);
  }
}
