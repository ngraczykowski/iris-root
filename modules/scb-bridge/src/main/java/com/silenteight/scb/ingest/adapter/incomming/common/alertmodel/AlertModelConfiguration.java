package com.silenteight.scb.ingest.adapter.incomming.common.alertmodel;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails;
import com.silenteight.proto.serp.scb.v1.ScbWatchlistPartyDetails;
import com.silenteight.sep.base.common.messaging.MessageSenderFactory;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.EXCHANGE_ALERT_METADATA;
import static java.util.Arrays.asList;

@RequiredArgsConstructor
@Configuration
class AlertModelConfiguration {

  private final MessageSenderFactory messageSenderFactory;

  @Bean
  AlertModelService alertModelService() {
    AlertModelFactory alertModelFactory = new AlertModelFactory(asList(
        ScbAlertDetails.class,
        ScbAlertedPartyDetails.class,
        ScbWatchlistPartyDetails.class));

    return alertModelService(alertModelFactory);
  }

  @NotNull
  AlertModelService alertModelService(AlertModelFactory alertModelFactory) {
    return new AlertModelService(
        messageSenderFactory.get(EXCHANGE_ALERT_METADATA),
        alertModelFactory);
  }
}
