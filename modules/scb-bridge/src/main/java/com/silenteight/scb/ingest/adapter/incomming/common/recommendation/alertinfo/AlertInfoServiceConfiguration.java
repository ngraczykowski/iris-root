package com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.messaging.ReactiveMessageSender;
import com.silenteight.sep.base.common.messaging.MessageSenderFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.EXCHANGE_REPORT_DATA;

@RequiredArgsConstructor
@Configuration
class AlertInfoServiceConfiguration {

  @NonNull
  private final ReactiveMessageSender reactiveMessageSender;
  @NonNull
  private final MessageSenderFactory messageSenderFactory;

  @Bean
  AlertInfoService alertInfoService() {
    var mapper = new AlertInfoMapper();
    var sender = messageSenderFactory.get(EXCHANGE_REPORT_DATA);
    var broadcaster = new AlertInfoBroadcaster(sender, reactiveMessageSender);
    return new AlertInfoService(mapper, broadcaster);
  }
}
