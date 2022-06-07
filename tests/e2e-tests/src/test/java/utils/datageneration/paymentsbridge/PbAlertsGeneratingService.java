/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.SneakyThrows;

import java.util.Map;

import static java.util.UUID.randomUUID;
import static utils.datageneration.CommonUtils.getJsonTemplate;
import static utils.datageneration.CommonUtils.templateObject;

public class PbAlertsGeneratingService {

  @SneakyThrows
  public PaymentsBridgeAlert generateAlertWithHit() {

    final int alertNumber = 1;
    final String alertId = "Alert-ID-" + alertNumber;
    final int hitNumber = 1;
    final String hitId = "Hit-ID-" + hitNumber;
    final String messageId = randomUUID().toString();

    var template = getJsonTemplate("alertTemplates/sierra", "pbAlertTemplate");
    var payload =
        templateObject(
            template,
            Map.of(
                "alertId",
                alertId,
                "messageId",
                messageId,
                "messageData",
                String.format("Message %s data", messageId),
                "hitId",
                hitId));
    return PaymentsBridgeAlert.builder().id(alertId).payload(payload).build();
  }
}
