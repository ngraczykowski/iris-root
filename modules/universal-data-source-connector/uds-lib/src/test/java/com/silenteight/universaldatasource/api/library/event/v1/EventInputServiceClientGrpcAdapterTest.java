package com.silenteight.universaldatasource.api.library.event.v1;

import com.silenteight.datasource.api.event.v1.*;
import com.silenteight.datasource.api.event.v1.EventInputServiceGrpc.EventInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class EventInputServiceClientGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private EventInputServiceClientGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedEventInputServiceGrpcServer());

    var stub = EventInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new EventInputServiceClientGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchEventInputs() {
    //when
    var response = underTest.getBatchGetMatchEventInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedEventInputServiceGrpcServer extends EventInputServiceImplBase {

    @Override
    public void batchGetMatchEventInputs(
        BatchGetMatchEventInputsRequest request,
        StreamObserver<BatchGetMatchEventInputsResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String MATCH = "match";
    public static final String FEATURE_ONE = "featureone";
    public static final String FEATURE_TWO = "featuretwo";
    public static final String MATCH_ONE = "matchone";
    public static final String MATCH_TWO = "matchtwo";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> ALERTED_PARTY_DATES = List.of("party_one", "party_two");
    public static final List<String> WATCHLIST_EVENTS = List.of("event_one", "event_two");

    static final BatchGetMatchEventInputsIn REQUEST = BatchGetMatchEventInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<EventFeatureInput> EVENT_FEATURE_INPUTS = List.of(
        EventFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyDates(ALERTED_PARTY_DATES)
            .addAllWatchlistEvents(WATCHLIST_EVENTS)
            .build()
    );

    static final List<EventInput> EVENT_INPUTS = List.of(
        EventInput.newBuilder()
            .setMatch(MATCH)
            .addAllEventFeatureInputs(EVENT_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchEventInputsResponse GRPC_RESPONSE =
        BatchGetMatchEventInputsResponse.newBuilder()
            .addAllEventInputs(EVENT_INPUTS)
            .build();

    static final List<BatchGetMatchEventInputsOut> RESPONSE =
        List.of(BatchGetMatchEventInputsOut.createFrom(GRPC_RESPONSE));
  }
}
