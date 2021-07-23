package com.silenteight.searpayments.scb.util;

import com.silenteight.searpayments.scb.domain.Alert;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class MessagingUtil {

  private MessagingUtil() {

  }

  public static Message<Long> toPayload(Alert alert) {
    return MessageBuilder
        .withPayload(alert.getId())
        .setHeader("alertStatus", alert.getStatus().name())
        .setHeader("alertId", alert.getId())
        .setHeader("systemId", alert.getSystemId())
        .setHeader("hitCount", alert.getHitCount())
        .build();
  }
}
