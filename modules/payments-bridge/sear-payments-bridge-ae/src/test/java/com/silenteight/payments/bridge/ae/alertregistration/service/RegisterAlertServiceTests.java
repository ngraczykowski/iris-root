package com.silenteight.payments.bridge.ae.alertregistration.service;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AlertClientPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAlertServiceTests {

  private RegisterAlertService registerAlertService;

  @Mock
  private AlertClientPort alertClient;

  @BeforeEach
  void setup() {
    registerAlertService = new RegisterAlertService(alertClient);
  }

  @Test
  void test() {
    final String alertId1 = "123-123-123";
    final String alertId2 = "321-321-312";
    final String alertName1 = "alerts/123";
    final String alertName2 = "alerts/321";

    List<RegisterAlertRequest> requests = new ArrayList<>();
    requests.add(
        RegisterAlertRequest.builder()
          .alertId(alertId1)
          .matchIds(List.of("1:ONE", "1:TWO"))
          .build());
    requests.add(
        RegisterAlertRequest.builder()
            .alertId(alertId2)
            .matchIds(List.of("2:ONE", "2:TWO"))
            .build());

    when(alertClient.batchCreateAlerts(argThat(arg ->
        alertId1.equals(arg.getAlertsList().get(0).getAlertId())
    ))).thenReturn(
        BatchCreateAlertsResponse.newBuilder()
            .addAlerts(
                Alert.newBuilder()
                .setAlertId(alertId1).setName(alertName1).build()
            )
            .addAlerts(
                Alert.newBuilder()
                    .setAlertId(alertId2).setName(alertName2).build()
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
