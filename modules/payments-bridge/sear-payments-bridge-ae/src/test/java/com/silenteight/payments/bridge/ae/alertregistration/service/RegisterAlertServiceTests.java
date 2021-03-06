package com.silenteight.payments.bridge.ae.alertregistration.service;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisteredAlertDataAccessPort;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAlertServiceTests {

  private RegisterAlertService registerAlertService;

  @Mock
  private AlertClientPort alertClient;
  @Mock
  private RegisteredAlertDataAccessPort registeredAlertDataAccessPort;
  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @BeforeEach
  void setup() {
    registerAlertService = new RegisterAlertService(alertClient, registeredAlertDataAccessPort,
        applicationEventPublisher);
  }

  @Test
  void test() {
    final String fkcoSystemId1 = "87AB4899-BE5B-5E4F-E053-150A6C0A7A84";
    final String fkcoSystemId2 = "883252A2-C860-7D0A-E053-150A6C0A0D90";

    final UUID alertMessageId1 = UUID.randomUUID();
    final UUID alertMessageId2 = UUID.randomUUID();

    final String alertName1 = "alerts/123";
    final String alertName2 = "alerts/321";

    List<RegisterAlertRequest> requests = new ArrayList<>();
    requests.add(
        RegisterAlertRequest.builder()
            .alertMessageId(alertMessageId1)
            .fkcoSystemId(fkcoSystemId1)
            .matchIds(List.of("1:ONE", "1:TWO"))
            .alertTime(Timestamp.getDefaultInstance())
            .build());
    requests.add(
        RegisterAlertRequest.builder()
            .alertMessageId(alertMessageId2)
            .fkcoSystemId(fkcoSystemId2)
            .matchIds(List.of("2:ONE", "2:TWO"))
            .alertTime(Timestamp.getDefaultInstance())
            .build());

    when(alertClient.batchCreateAlerts(argThat(arg ->
        alertMessageId1.toString().equals(arg.getAlertsList().get(0).getAlertId())
    ))).thenReturn(
        BatchCreateAlertsResponse.newBuilder()
            .addAlerts(
                Alert.newBuilder()
                    .setAlertId(alertMessageId1.toString()).setName(alertName1).build()
            )
            .addAlerts(
                Alert.newBuilder()
                    .setAlertId(alertMessageId2.toString()).setName(alertName2).build()
            )
            .build());

    when(alertClient.batchCreateMatches(
        argThat(arg -> {
          var alertCondition = arg.getAlertMatchesList().stream()
              .map(BatchCreateAlertMatchesRequest::getAlert)
              .collect(Collectors.toSet()).containsAll(Set.of(alertName1, alertName2));

          var matchCondition = arg.getAlertMatchesList().stream().map(
                  BatchCreateAlertMatchesRequest::getMatchesList)
              .flatMap(Collection::stream)
              .map(Match::getMatchId)
              .collect(Collectors.toSet())
              .containsAll(Set.of("1:ONE", "1:TWO", "2:ONE", "2:TWO"));
          return alertCondition && matchCondition;
        }))).thenReturn(
        BatchCreateMatchesResponse.newBuilder()
            .addMatches(
                Match.newBuilder()
                    .setName(alertName1 + "/matches/1:ONE").build())
            .addMatches(
                Match.newBuilder()
                    .setName(alertName1 + "/matches/1:TWO").build())
            .addMatches(
                Match.newBuilder()
                    .setName(alertName2 + "/matches/1:ONE").build())
            .addMatches(
                Match.newBuilder()
                    .setName(alertName2 + "/matches/2:TWO").build())
            .build());

    var response = registerAlertService.batchRegistration(requests);
    assertEquals(2, response.size());
  }

}
