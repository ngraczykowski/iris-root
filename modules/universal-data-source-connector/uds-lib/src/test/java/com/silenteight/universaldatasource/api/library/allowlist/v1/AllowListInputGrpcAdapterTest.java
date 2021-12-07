package com.silenteight.universaldatasource.api.library.allowlist.v1;

import com.silenteight.datasource.api.allowlist.v1.*;
import com.silenteight.datasource.api.allowlist.v1.AllowListInputServiceGrpc.AllowListInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class AllowListInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private AllowListInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedAllowedInputGrpcServer());

    var stub = AllowListInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new AllowListInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchAllowListInputs() {
    //when
    var response = underTest.getBatchGetMatchAllowListInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedAllowedInputGrpcServer extends AllowListInputServiceImplBase {

    @Override
    public void batchGetMatchAllowListInputs(
        BatchGetMatchAllowListInputsRequest request,
        StreamObserver<BatchGetMatchAllowListInputsResponse> responseObserver) {
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
    public static final List<String> ALLOW_LIST_NAME = List.of("nameone", "nametwo");
    public static final List<String> CHARACTERISTICS_VALUE = List.of("valueone", "valuetwo");

    static final BatchGetMatchAllowListInputsIn REQUEST = BatchGetMatchAllowListInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<AllowListFeatureInput> ALLOW_LIST_FEATURE_INPUTS = List.of(
        AllowListFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAllowListName(ALLOW_LIST_NAME)
            .addAllCharacteristicsValues(CHARACTERISTICS_VALUE)
            .build()
    );

    static final List<AllowListInput> ALLOW_LIST_INPUTS = List.of(
        AllowListInput.newBuilder()
            .setMatch(MATCH)
            .addAllAllowListFeatureInputs(ALLOW_LIST_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchAllowListInputsResponse GRPC_RESPONSE =
        BatchGetMatchAllowListInputsResponse.newBuilder()
            .addAllAllowListInputs(ALLOW_LIST_INPUTS)
            .build();

    static final List<BatchGetMatchAllowListInputsOut> RESPONSE =
        List.of(BatchGetMatchAllowListInputsOut.createFrom(GRPC_RESPONSE));
  }
}
