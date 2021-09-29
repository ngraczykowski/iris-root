package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;
import com.silenteight.payments.bridge.event.AlertStored;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.AMQP_OUTBOUND;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.STORED;
import static com.silenteight.payments.bridge.firco.alertmessage.service.IntChannels.INT_ALERT_STORED_ACCEPTED;

@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
public class AlertMessageStoredEndpoint {

  private final AlertMessageStatusService alertMessageStatusService;

  @ServiceActivator(inputChannel = INT_ALERT_STORED_ACCEPTED, outputChannel = AMQP_OUTBOUND)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public MessageStored send(AlertStored alertStored) {
    var alertModel = alertStored.getAlertModel();
    alertMessageStatusService
        .transitionAlertMessageStatus(alertModel.getId(), STORED);

    return buildMessageStore(alertModel);
  }

  private MessageStored buildMessageStore(AlertMessageModel model) {
    var id = "alert-messages/" + model.getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }


}
