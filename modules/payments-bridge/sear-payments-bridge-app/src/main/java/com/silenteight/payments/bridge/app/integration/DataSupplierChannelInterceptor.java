package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.event.DomainEvent;
import com.silenteight.payments.bridge.event.data.AlertDataIdentifier;
import com.silenteight.payments.bridge.event.data.AlertDtoIdentifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Qualifier(CommonChannels.CHANNEL_INTERCEPTOR_QUALIFIER)
@RequiredArgsConstructor
@Slf4j
class DataSupplierChannelInterceptor implements ChannelInterceptor {

  private final DataSuppliersProvider suppliers;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    if (!(message.getPayload() instanceof DomainEvent)) {
      log.warn("Enrichment skipped because the message payload is of the different type [{}] "
          + "than expected [{}]", message.getPayload().getClass().getName(),
          DomainEvent.class.getName());
      return message;
    }

    DomainEvent domainEvent = (DomainEvent) message.getPayload();

    if (message.getPayload() instanceof AlertDataIdentifier) {
      String alertId = ((AlertDataIdentifier)message.getPayload()).getAlertId();
      domainEvent.registerCollector(
          AlertData.class, suppliers.alertDataSupplierPrototype(alertId));
    }
    if (message.getPayload() instanceof AlertDtoIdentifier) {
      String alertId = ((AlertDtoIdentifier)message.getPayload()).getAlertId();
      domainEvent.registerCollector(
          AlertMessageDto.class, suppliers.alertDtoSupplierPrototype(alertId));
    }

    return message;
  }
}
