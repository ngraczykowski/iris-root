/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.datageneration.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;

public class AlertsGeneratingService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CommonUtils commonUtils = new CommonUtils();

  @SneakyThrows
  public String generateAlertWithHit() {

    final int alertNumber = 1;
    final String alertId = "Alert-ID-" + alertNumber;
    final int hitNumber = 1;
    final String hitId = "Hit-ID-" + hitNumber;
    final String messageId = randomUUID().toString();

    return commonUtils.templateObjectOfName(
        "pbAlertTemplate",
        Map.of(
            "alertId", alertId,
            "messageId", messageId,
            "messageData", String.format("Message %s data", messageId),
            "hitId", hitId
        ));
  }
}
