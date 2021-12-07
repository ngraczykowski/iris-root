package com.silenteight.universaldatasource.api.library.transaction.v1;

import com.silenteight.datasource.api.transaction.v1.*;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput.WatchlistType;
import com.silenteight.datasource.api.transaction.v1.TransactionInputServiceGrpc.TransactionInputServiceImplBase;
import com.silenteight.datasource.api.transaction.v1.WatchlistName.NameType;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class TransactionInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();
  private TransactionInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new TransactionInputGrpcServer());

    var stub = TransactionInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new TransactionInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchTransactionInputsOut() {
    //when
    var response = underTest.batchGetMatchTransactionInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class TransactionInputGrpcServer extends TransactionInputServiceImplBase {

    @Override
    public void batchGetMatchTransactionInputs(
        BatchGetMatchTransactionInputsRequest request,
        StreamObserver<BatchGetMatchTransactionInputsResponse> responseObserver) {
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

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> MATCHING_TEXTS = List.of("text1", "text1");
    public static final List<String> TRANSACTION_MESSAGES = List.of("msg1", "msg2");

    static final List<WatchlistName> WATCHLIST_NAMES =
        List.of(WatchlistName.newBuilder()
            .setName(WATCHLIST_NAME)
            .setType(NameType.REGULAR)
            .build());

    static final List<TransactionFeatureInput> TRANSACTION_FEATURE_INPUTS = List.of(
        TransactionFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllTransactionMessages(TRANSACTION_MESSAGES)
            .addAllWatchlistNames(WATCHLIST_NAMES)
            .setWatchlistType(WatchlistType.INDIVIDUAL)
            .addAllMatchingTexts(MATCHING_TEXTS)
            .build());

    static final List<TransactionInput> TRANSACTION_INPUTS = List.of(
        TransactionInput.newBuilder()
            .setMatch(MATCH)
            .addAllTransactionFeatureInputs(TRANSACTION_FEATURE_INPUTS)
            .build());

    static final BatchGetMatchTransactionInputsResponse GRPC_RESPONSE =
        BatchGetMatchTransactionInputsResponse.newBuilder()
            .addAllTransactionInputs(TRANSACTION_INPUTS)
            .build();

    static final BatchGetMatchTransactionInputsIn REQUEST =
        BatchGetMatchTransactionInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BatchGetMatchTransactionInputsOut> RESPONSE =
        List.of(BatchGetMatchTransactionInputsOut.createFrom(GRPC_RESPONSE));
  }
}
