package com.silenteight.payments.bridge.firco.core.alertmessage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.proto.payments.bridge.internal.v1.event.MessageStored;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FircoAlertMessage {

  private UUID id;

  private OffsetDateTime receivedAt;

  private AlertMessageDto alertMessage;

  private String dataCenter;

  private String decisionUrl;

  public MessageStored toRequest() {
    var id = "alert-messages/" + getId();
    return MessageStored.newBuilder().setAlert(id).build();
  }

}
