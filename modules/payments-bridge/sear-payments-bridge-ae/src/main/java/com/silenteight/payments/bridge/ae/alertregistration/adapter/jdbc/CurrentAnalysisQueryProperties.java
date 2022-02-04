package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pb.jdbc.analysis-query")
class CurrentAnalysisQueryProperties {

  private Duration newAnalysisInterval = Duration.ofMinutes(15);
}
