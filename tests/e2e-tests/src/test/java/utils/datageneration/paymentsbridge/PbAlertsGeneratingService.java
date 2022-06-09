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
    final String messageData =
        "{1:F01SVBKUS60XXXX0000000000}{2:I199SVBKUS60XXXXN}{3:{108:ECSAMT0000418871}{121:f299c3d8-0121-12c5-b352-1d16b7017419}}{4:\\n:20:ABCOFP1123\\n:23B:CRED BP00000009LK\\n:32A:10606CAD5431,26\\n:50K:\\/3300565427\\nSINA BANK\\nABCD LTD\\nSADDAM HUSSAIN CUBA Clara\\n:59A:nosccatt\\nSCOTT PLAZA 40 king street West\\nsuite 5800 PO Box 1011\\nTORONTO ON MSH 351 Canada\\n:71A:SHA\\n-}{5:{CHK:370453F95D8E}{TNG:}}";

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
                String.format("Message %s data", messageData),
                "hitId",
                hitId));
    return PaymentsBridgeAlert.builder().id(alertId).payload(payload).build();
  }
}
