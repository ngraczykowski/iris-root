package com.silenteight.payments.bridge.common.integration.channel;

import com.silenteight.payments.bridge.event.AlertRegistered;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.annotation.Nullable;

@Component
public class AlertRegisteredChannel extends EnrichedChannel {

  public static final String QUALIFIER = "AlertRegisteredChannelInterceptor";

  AlertRegisteredChannel(@Nullable @Qualifier(QUALIFIER)
      List<ChannelInterceptor> channelInterceptors) {
    super(channelInterceptors, AlertRegistered.class);
  }
}
