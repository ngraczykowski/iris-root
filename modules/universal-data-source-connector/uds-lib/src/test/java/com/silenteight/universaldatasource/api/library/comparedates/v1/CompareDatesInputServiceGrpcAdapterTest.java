package com.silenteight.universaldatasource.api.library.comparedates.v1;

import com.silenteight.datasource.api.compareDates.v1.*;
import com.silenteight.datasource.api.compareDates.v1.CompareDatesInputServiceGrpc.CompareDatesInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class CompareDatesInputServiceGrpcAdapterTest {

  @RegisterExtension GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private CompareDatesInputServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedCompareDatesInputServiceGrpcServer());

    var stub = CompareDatesInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new CompareDatesInputServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchDocumentInputs() {
    //when
    var response = underTest.getBatchGetCompareDatesInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedCompareDatesInputServiceGrpcServer extends CompareDatesInputServiceImplBase {

    @Override
    public void batchGetCompareDatesInputs(
        BatchGetCompareDatesInputsRequest request,
        StreamObserver<BatchGetCompareDatesInputsResponse> responseObserver) {
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
    public static final String UAE_NATIONAL_ID_DATE = "31/12/2021";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);

    static final BatchGetCompareDatesInputsIn REQUEST =
        BatchGetCompareDatesInputsIn.builder().features(FEATURES).matches(MATCHES).build();

    static final List<CompareDatesFeatureInput>
        UAE_NATIONAL_ID_DATE_NOT_AFTER_TODAY_FEATURE_INPUTS = List.of(CompareDatesFeatureInput
        .newBuilder()
        .setFeature(FEATURE_ONE)
        .setDateToCompare(UAE_NATIONAL_ID_DATE)
        .build());

    static final List<CompareDatesInput> UAE_NATIONAL_ID_DATE_NOT_AFTER_TODAY_INPUTS = List.of(
        CompareDatesInput
            .newBuilder()
            .setMatch(MATCH)
            .addAllCompareDatesFeaturesInput(UAE_NATIONAL_ID_DATE_NOT_AFTER_TODAY_FEATURE_INPUTS)
            .build());

    static final BatchGetCompareDatesInputsResponse GRPC_RESPONSE =
        BatchGetCompareDatesInputsResponse
            .newBuilder()
            .addAllCompareDatesInputs(UAE_NATIONAL_ID_DATE_NOT_AFTER_TODAY_INPUTS)
            .build();

    static final List<BatchGetCompareDatesInputsOut> RESPONSE =
        List.of(BatchGetCompareDatesInputsOut.createFrom(GRPC_RESPONSE));
  }
}
