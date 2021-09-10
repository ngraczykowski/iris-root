package com.silenteight.payments.bridge.firco.core.alertmessage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FircoAlertMessage {

  private UUID id;

  OffsetDateTime receivedAt;

  private AlertMessageDto alertMessage;

  private String dataCenter;

  private String decisionUrl;
}
