package com.silenteight.universaldatasource.api.library.gender.v1;

import com.silenteight.datasource.api.gender.v1.*;
import com.silenteight.datasource.api.gender.v1.GenderInputServiceGrpc.GenderInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class GenderInputServiceGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private GenderInputServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedGenderInputServiceGrpcServer());

    var stub = GenderInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new GenderInputServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchGenderInputs() {
    //when
    var response = underTest.getBatchGetMatchGenderInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedGenderInputServiceGrpcServer extends GenderInputServiceImplBase {

    @Override
    public void batchGetMatchGenderInputs(
        BatchGetMatchGenderInputsRequest request,
        StreamObserver<BatchGetMatchGenderInputsResponse> responseObserver) {
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
    public static final List<String> ALERTED_PARTY_GENDERS = List.of("party_one", "party_two");
    public static final List<String> WATCHLIST_GENDERS = List.of("watch_one", "watch_two");

    static final BatchGetMatchGenderInputsIn REQUEST = BatchGetMatchGenderInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<GenderFeatureInput> GENDER_FEATURE_INPUTS = List.of(
        GenderFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyGenders(ALERTED_PARTY_GENDERS)
            .addAllWatchlistGenders(WATCHLIST_GENDERS)
            .build()
    );

    static final List<GenderInput> GENDER_INPUTS = List.of(
        GenderInput.newBuilder()
            .setMatch(MATCH)
            .addAllGenderFeatureInputs(GENDER_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchGenderInputsResponse GRPC_RESPONSE =
        BatchGetMatchGenderInputsResponse.newBuilder()
            .addAllGenderInputs(GENDER_INPUTS)
            .build();

    static final List<BatchGetMatchGenderInputsOut> RESPONSE =
        List.of(BatchGetMatchGenderInputsOut.createFrom(GRPC_RESPONSE));
  }
}
