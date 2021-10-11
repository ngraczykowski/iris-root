package com.silenteight.payments.bridge.firco.alertmessage.model;

import lombok.*;

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

  private String userLogin;

  @ToString.Exclude
  private String userPassword;

}
