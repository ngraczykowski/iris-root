package com.silenteight.warehouse.test.flows.piiexpired;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.warehouse.test.client.gateway.PersonalInformationExpiredClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class PersonalInformationDataExpiredClient {

  private final PersonalInformationExpiredClientGateway personalInformationExpiredClientGateway;
  private final MessageGenerator messageGenerator;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();

    PersonalInformationExpired personalInformationExpired
        = messageGenerator.generatePersonalInformationExpired(generateAlertNames());

    personalInformationExpiredClientGateway.indexRequest(personalInformationExpired);
    log.info("PersonalInformationExpired msg sent, requestId={} alerts count: {}", requestId,
        personalInformationExpired.getAlertsCount());
  }

  private static List<String> generateAlertNames() {
    return IntStream
        .range(0, 6000)
        .boxed()
        .map(i -> "alerts/" + i)
        .collect(toList());
  }
}
