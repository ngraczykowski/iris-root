package com.silenteight.universaldatasource.api.library.location.v1;

import com.silenteight.datasource.api.location.v1.*;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class LocationInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();
  private LocationInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new LocationInputGrpcServer());

    var stub = LocationInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new LocationInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchLocationInputsOut() {
    //when
    var response = underTest.batchGetMatchLocationInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class LocationInputGrpcServer extends LocationInputServiceImplBase {

    @Override
    public void batchGetMatchLocationInputs(
        BatchGetMatchLocationInputsRequest request,
        StreamObserver<BatchGetMatchLocationInputsResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String MATCH = "match";
    public static final String FEATURE_ONE = "featureOne";
    public static final String FEATURE_TWO = "featureTwo";
    public static final String MATCH_ONE = "matchOne";
    public static final String MATCH_TWO = "matchTwo";
    public static final String WATCHLIST_LOCATION = "someWatchlistLocation";
    public static final String ALERTED_PARTY_LOCATION = "someAlertedPartyLocation";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);

    static final List<LocationFeatureInput> LOCATION_FEATURE_INPUTS = List.of(
        LocationFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .setAlertedPartyLocation(ALERTED_PARTY_LOCATION)
            .setWatchlistLocation(WATCHLIST_LOCATION)
            .build());

    static final List<LocationInput> LOCATION_INPUTS = List.of(
        LocationInput.newBuilder()
            .setMatch(MATCH)
            .addAllLocationFeatureInputs(LOCATION_FEATURE_INPUTS)
            .build());

    static final BatchGetMatchLocationInputsResponse GRPC_RESPONSE =
        BatchGetMatchLocationInputsResponse.newBuilder()
            .addAllLocationInputs(LOCATION_INPUTS)
            .build();

    static final BatchGetMatchLocationInputsIn REQUEST =
        BatchGetMatchLocationInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BatchGetMatchLocationInputsOut> RESPONSE =
        List.of(BatchGetMatchLocationInputsOut.createFrom(GRPC_RESPONSE));
  }
}
