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
  @DisplayName("should create batch of alerts")
  void shouldCreateBatchAlerts() {
    //given
    var alerts = List.of(
        AlertIn.builder().name("alert_1").alertId("1").build(),
        AlertIn.builder().name("alert_2").alertId("2").build());

    //when
    var response = underTest.batchCreateAlerts(alerts);

    //then
    assertThat(response.getAlerts().size()).isEqualTo(2);
    assertThat(response.getAlerts().get(0).getName()).isEqualTo("alert_1");
    assertThat(response.getAlerts().get(0).getAlertId()).isEqualTo("1");
    assertThat(response.getAlerts().get(1).getName()).isEqualTo("alert_2");
    assertThat(response.getAlerts().get(1).getAlertId()).isEqualTo("2");
  }

  @Test
  @DisplayName("should create batch of alert matches")
  void shouldCreateBatchAlertMatches() {
    //given
    var request = BatchCreateAlertMatchesIn.builder()
        .alert("alert")
        .matchIds(List.of("matchId_1", "matchId_2"))
        .build();

    //when
    var response = underTest.batchCreateAlertMatches(request);

    //then
    assertThat(response.getAlertMatches().size()).isEqualTo(2);
    assertThat(response.getAlertMatches().get(0).getName()).isEqualTo("match_1");
    assertThat(response.getAlertMatches().get(0).getMatchId()).isEqualTo("1");
    assertThat(response.getAlertMatches().get(1).getName()).isEqualTo("match_2");
    assertThat(response.getAlertMatches().get(1).getMatchId()).isEqualTo("2");
  }

  static class MockedAlertServiceGrpcServer extends AlertServiceImplBase {

    @Override
    public void batchCreateAlerts(
        BatchCreateAlertsRequest request,
        StreamObserver<BatchCreateAlertsResponse> responseObserver) {
      responseObserver.onNext(
          BatchCreateAlertsResponse.newBuilder().addAllAlerts(
                  List.of(
                      Alert.newBuilder().setName("alert_1").setAlertId("1").build(),
                      Alert.newBuilder().setName("alert_2").setAlertId("2").build()))
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
                      Match.newBuilder().setName("match_1").setMatchId("1").build(),
                      Match.newBuilder().setName("match_2").setMatchId("2").build()))
              .build());
      responseObserver.onCompleted();
    }
  }
}
