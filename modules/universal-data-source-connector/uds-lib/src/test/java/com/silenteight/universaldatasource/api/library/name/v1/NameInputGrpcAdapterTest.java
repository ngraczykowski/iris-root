package com.silenteight.universaldatasource.api.library.name.v1;

import com.silenteight.datasource.api.name.v1.*;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class NameInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();
  private NameInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new NameInputGrpcServer());

    var stub = NameInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new NameInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchNameInputsOut() {
    //when
    var response = underTest.batchGetMatchNameInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class NameInputGrpcServer extends NameInputServiceImplBase {

    @Override
    public void batchGetMatchNameInputs(
        BatchGetMatchNameInputsRequest request,
        StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {
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
    public static final String WATCHLIST_NAME = "someWatchlistName";
    public static final String ALERTED_PARTY_NAME = "someAlertedPartyName";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> MATCHING_TEXTS = List.of("text1", "text1");

    static final List<WatchlistName> WATCHLIST_NAMES =
        List.of(WatchlistName.newBuilder()
            .setName(WATCHLIST_NAME)
            .setType(NameType.REGULAR)
            .build());

    static final List<AlertedPartyName> ALERTED_PARTY_NAMES =
        List.of(AlertedPartyName.newBuilder()
            .setName(ALERTED_PARTY_NAME)
            .build());

    static final List<NameFeatureInput> NAME_FEATURE_INPUTS = List.of(
        NameFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyNames(ALERTED_PARTY_NAMES)
            .addAllWatchlistNames(WATCHLIST_NAMES)
            .addAllWatchlistNames(WATCHLIST_NAMES)
            .addAllMatchingTexts(MATCHING_TEXTS)
            .setAlertedPartyType(EntityType.INDIVIDUAL)
            .build());

    static final List<NameInput> NAME_INPUTS = List.of(
        NameInput.newBuilder()
            .setMatch(MATCH)
            .addAllNameFeatureInputs(NAME_FEATURE_INPUTS)
            .build());

    static final BatchGetMatchNameInputsResponse GRPC_RESPONSE =
        BatchGetMatchNameInputsResponse.newBuilder()
            .addAllNameInputs(NAME_INPUTS)
            .build();

    static final BatchGetMatchNameInputsIn REQUEST =
        BatchGetMatchNameInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BatchGetMatchNameInputsOut> RESPONSE =
        List.of(BatchGetMatchNameInputsOut.createFrom(GRPC_RESPONSE));
  }
}
