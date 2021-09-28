package com.silenteight.payments.bridge.common.integration.channel;

import com.silenteight.payments.bridge.event.AlertDelivered;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.annotation.Nullable;

@Component
public class AlertDeliveredChannel extends EnrichedChannel {

  public static final String QUALIFIER = "AlertDeliveredChannelInterceptor";

  AlertDeliveredChannel(@Nullable @Qualifier(QUALIFIER)
      List<ChannelInterceptor> channelInterceptors) {
    super(channelInterceptors, AlertDelivered.class);
  }
}
