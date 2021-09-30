package com.silenteight.payments.bridge.firco.alertmessage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;

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

}
