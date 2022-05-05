package com.silenteight.adjudication.api.library.v1.alert;

import com.silenteight.adjudication.api.library.v1.GrpcServerExtension;
import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceImplBase;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AlertGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private AlertGrpcAdapter underTest;

  @BeforeEach
  public void setup() {
    grpcServerExtension.addService(new AlertGrpcAdapterTest.MockedAlertServiceGrpcServer());
    var stub = AlertServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());
    underTest = new AlertGrpcAdapter(stub, 1L);
  }

  @Test
  @DisplayName("should register alerts and matches")
  void shouldRegisterAlertsAndMatches() {
    //given
    var alertsWithMatches = List.of(
        BatchRegisterAlertMatchesIn.builder()
            .alertId("1")
            .alertPriority(5)
            .matchIds(List.of("1"))
            .build(),
        BatchRegisterAlertMatchesIn.builder()
            .alertId("2")
            .alertPriority(1)
            .matchIds(List.of("2", "3"))
            .build());

    var request = RegisterAlertsAndMatchesIn.builder()
        .alertsWithMatches(alertsWithMatches)
        .build();

    //when
    var response = underTest.registerAlertsAndMatches(request).getAlertWithMatches();

    //then
    assertThat(response.size()).isEqualTo(2);
    assertThat(response.get(0).getAlertId()).isEqualTo("1");
    assertThat(response.get(0).getAlertName()).isEqualTo("alert_1");
    assertThat(response.get(0).getMatches().get(0).getMatchId()).isEqualTo("1");
    assertThat(response.get(0).getMatches().get(0).getName()).isEqualTo("match_1");

    assertThat(response.get(1).getAlertId()).isEqualTo("2");
    assertThat(response.get(1).getAlertName()).isEqualTo("alert_2");
    assertThat(response.get(1).getMatches().get(0).getMatchId()).isEqualTo("2");
    assertThat(response.get(1).getMatches().get(0).getName()).isEqualTo("match_2");
    assertThat(response.get(1).getMatches().get(1).getMatchId()).isEqualTo("3");
    assertThat(response.get(1).getMatches().get(1).getName()).isEqualTo("match_3");
  }

  static class MockedAlertServiceGrpcServer extends AlertServiceImplBase {

    @Override
    public void batchCreateAlerts(
        BatchCreateAlertsRequest request,
        StreamObserver<BatchCreateAlertsResponse> responseObserver) {
      responseObserver.onNext(
          BatchCreateAlertsResponse.newBuilder().addAllAlerts(
                  List.of(
                      Alert.newBuilder()
                          .setName("alert_1")
                          .setAlertId("1")
                          .addAllMatches(
                              List.of(
                                  Match.newBuilder()
                                      .setName("match_1")
                                      .setMatchId("1")
                                      .build()
                              )
                          )
                          .build(),
                      Alert.newBuilder()
                          .setName("alert_2")
                          .setAlertId("2")
                          .addAllMatches(
                              List.of(
                                  Match.newBuilder()
                                      .setName("match_2")
                                      .setMatchId("2")
                                      .build(),
                                  Match.newBuilder()
                                      .setName("match_3")
                                      .setMatchId("3")
                                      .build()
                              ))
                          .build()
                  )
              )
              .build());
      responseObserver.onCompleted();
    }

    @Override
    public void batchCreateAlertMatches(
        BatchCreateAlertMatchesRequest request,
        StreamObserver<BatchCreateAlertMatchesResponse> responseObserver) {
      responseObserver.onNext(
          BatchCreateAlertMatchesResponse.newBuilder().addAllMatches(
                  List.of(
                      Match.newBuilder()
                          .setName("match_1")
                          .setMatchId("1")
                          .build(),
                      Match.newBuilder()
                          .setName("match_2")
                          .setMatchId("2")
                          .build()
                  )
              )
              .build());
      responseObserver.onCompleted();
    }
  }
}
