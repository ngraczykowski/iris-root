/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.SneakyThrows;

import utils.datageneration.CommonUtils;

import java.util.Map;

import static java.util.UUID.randomUUID;

public class PbAlertsGeneratingService {

  private final CommonUtils commonUtils = new CommonUtils();

  @SneakyThrows
  public PaymentsBridgeAlert generateAlertWithHit() {

    final int alertNumber = 1;
    final String alertId = "Alert-ID-" + alertNumber;
    final int hitNumber = 1;
    final String hitId = "Hit-ID-" + hitNumber;
    final String messageId = randomUUID().toString();

    String payload = commonUtils.templateObjectOfName(
        "pbAlertTemplate",
        Map.of(
            "alertId", alertId,
            "messageId", messageId,
            "messageData", String.format("Message %s data", messageId),
            "hitId", hitId
        ));
    return PaymentsBridgeAlert.builder()
        .id(alertId)
        .payload(payload)
        .build();
  }
}
