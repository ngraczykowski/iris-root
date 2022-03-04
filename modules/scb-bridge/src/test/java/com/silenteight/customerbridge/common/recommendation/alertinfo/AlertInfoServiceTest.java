package com.silenteight.customerbridge.common.recommendation.alertinfo;

import com.silenteight.customerbridge.common.messaging.OutboundProtoMessage;
import com.silenteight.customerbridge.common.messaging.ReactiveMessageSender;
import com.silenteight.proto.serp.scb.v1.ScbAlertInfo;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.reporter.AlertInfo;
import com.silenteight.sep.base.common.messaging.MessageSender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_REPORT_DATA;
import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ROUTE_INFO_FROM_SCB_BRIDGE;
import static com.silenteight.customerbridge.common.messaging.MessagingConstants.ROUTE_SCB_INFO_FROM_SYNC;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertInfoServiceTest {

  private final Fixtures fixtures = new Fixtures();

  @Mock
  private MessageSender messageSender;

  @Mock
  private ReactiveMessageSender sender;
  @Mock
  private AlertInfoMapper mapper;

  @Captor
  private ArgumentCaptor<Flux<OutboundProtoMessage>> captor;

  private AlertInfoService underTest;

  @BeforeEach
  void setUp() {
    var broadcaster = new AlertInfoBroadcaster(messageSender, sender);
    underTest = new AlertInfoService(mapper, broadcaster);
  }

  @Test
  void shouldReturnMonoFromSender() {
    doReturn(fixtures.mono).when(sender).send(any());

    assertThat(send(fixtures.alert1)).isEqualTo(fixtures.mono);
  }

  private Mono<Void> send(Alert... alerts) {
    return underTest.sendAlertInfo(of(alerts));
  }

  @Test
  void shouldSendAllInfos() {
    doReturn(fixtures.mono).when(sender).send(captor.capture());

    mockMapper(fixtures.alert1, fixtures.alertInfo1, fixtures.scbAlertInfo1);
    mockMapper(fixtures.alert2, fixtures.alertInfo2, fixtures.scbAlertInfo2);

    send(fixtures.alert1, fixtures.alert2);

    StepVerifier.create(captor.getValue())
        .assertNext(v -> assertInfoMessageAsExpected(v, fixtures.alertInfo1))
        .assertNext(v -> assertInfoMessageAsExpected(v, fixtures.scbAlertInfo1))
        .assertNext(v -> assertInfoMessageAsExpected(v, fixtures.alertInfo2))
        .assertNext(v -> assertInfoMessageAsExpected(v, fixtures.scbAlertInfo2))
        .verifyComplete();
  }

  private void mockMapper(Alert alert, AlertInfo alertInfo, ScbAlertInfo scbAlertInfo) {
    doReturn(new AlertInfoMapResult(alertInfo, scbAlertInfo))
        .when(mapper).map(alert);
  }

  private static void assertInfoMessageAsExpected(OutboundProtoMessage v, AlertInfo info) {
    assertThat(v.getExchange()).hasValue(EXCHANGE_REPORT_DATA);
    assertThat(v.getRoutingKey()).hasValue(ROUTE_INFO_FROM_SCB_BRIDGE);
    assertThat(v.getMessage()).isEqualTo(info);
  }

  private static void assertInfoMessageAsExpected(OutboundProtoMessage v, ScbAlertInfo info) {
    assertThat(v.getExchange()).hasValue(EXCHANGE_REPORT_DATA);
    assertThat(v.getRoutingKey()).hasValue(ROUTE_SCB_INFO_FROM_SYNC);
    assertThat(v.getMessage()).isEqualTo(info);
  }

  private static class Fixtures {

    String id1 = "sourceId1";
    String id2 = "sourceId2";

    Alert alert1 = createAlert(id1);
    Alert alert2 = createAlert(id2);

    AlertInfo alertInfo1 = createAlertInfo(id1);
    AlertInfo alertInfo2 = createAlertInfo(id2);

    ScbAlertInfo scbAlertInfo1 = createScbAlertInfo(id1);
    ScbAlertInfo scbAlertInfo2 = createScbAlertInfo(id2);

    Mono<Void> mono = Mono.empty();

    private static Alert createAlert(String id) {
      return Alert.newBuilder()
          .setId(createId(id))
          .build();
    }

    private static ObjectId createId(String id) {
      return ObjectId.newBuilder()
          .setSourceId(id)
          .setDiscriminator("discriminator")
          .build();
    }

    private static AlertInfo createAlertInfo(String id) {
      return AlertInfo.newBuilder()
          .setAlertId(createId(id))
          .build();
    }

    private static ScbAlertInfo createScbAlertInfo(String id) {
      return ScbAlertInfo.newBuilder()
          .setAlertId(createId(id))
          .build();
    }
  }
}
