/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;

public class AlertsGeneratingService {

  @SneakyThrows
  public String generateAlertWithHits(int numberOfHits) {

    final int alertNumber = 1;
    final String alertId = "Alert-ID-" + alertNumber;
    final String messageId = randomUUID().toString();

    SolvingAlert alert = SolvingAlert.builder()
        .body(AlertBody.builder()
            .sendMessage(AlertSendMessage.builder()
                .messages(List.of(
                    AlertMessage.builder()
                        .alertId(alertId)
                        .messageId(messageId)
                        .messageData(String.format("Message %s data", messageId))
                        .hits(generateHits(numberOfHits))
                        .build()
                )).build())
            .build()).build();
    return new ObjectMapper().writeValueAsString(alert);
  }

  private static List<AlertHit> generateHits(int numberOfHits) {
    return IntStream.range(0, numberOfHits)
        .mapToObj(i -> AlertHit.builder().id("Hit-" + i).build())
        .collect(Collectors.toList());
  }
}
