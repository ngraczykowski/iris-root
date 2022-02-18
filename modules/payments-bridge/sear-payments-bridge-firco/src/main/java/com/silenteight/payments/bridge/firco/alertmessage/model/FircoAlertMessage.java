package com.silenteight.payments.bridge.firco.alertmessage.model;

import lombok.*;

import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

  public Map<String, Long> countHitTagsByType() {
    return this
        .getAlertMessage()
        .getHits()
        .stream()
        .collect(Collectors.groupingBy(
            dto -> dto.getHit().getTag(),
            Collectors.counting()));
  }

}
