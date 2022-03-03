package com.silenteight.customerbridge.common.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfoMapper;
import com.silenteight.sep.base.common.messaging.MessageSenderFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_REPORT_DATA;

@Configuration
@RequiredArgsConstructor
// NOTE: (smrozowski) class ScbMessagingConfiguration does not come from SERP Bridge,
// and it requires additional beans to run app, so I decided to not copy this class.
//@Import(ScbMessagingConfiguration.class)
class ReportConfiguration {

  @NonNull
  private final MessageSenderFactory messageSenderFactory;

  @Bean
  @ConditionalOnProperty(
      prefix = "serp.reporting.broadcast",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  ReportDataBroadcaster reportDataBroadcaster() {
    var sender = messageSenderFactory.get(EXCHANGE_REPORT_DATA);
    var mapper = new AlertInfoMapper();
    return new ReportDataBroadcaster(sender, mapper);
  }
}
