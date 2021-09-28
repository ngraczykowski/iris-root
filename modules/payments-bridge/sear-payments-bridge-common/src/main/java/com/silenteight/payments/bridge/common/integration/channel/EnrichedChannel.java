package com.silenteight.payments.bridge.common.integration.channel;

import lombok.Getter;

import com.silenteight.payments.bridge.common.integration.TypedPublishSubscribeChannel;
import com.silenteight.payments.bridge.event.DomainEvent;

import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

class EnrichedChannel {

  @Getter
  private final TypedPublishSubscribeChannel channel;

  public EnrichedChannel(@Nullable List<ChannelInterceptor> channelInterceptors,
      Class<? extends DomainEvent> eventClass) {

    var interceptors = CollectionUtils.isEmpty(channelInterceptors) ?
                       Collections.<ChannelInterceptor>emptyList() : channelInterceptors;
    this.channel = new TypedPublishSubscribeChannel(eventClass, interceptors);
  }
}
