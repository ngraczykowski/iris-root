package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.ModelType;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;
import com.silenteight.universaldatasource.api.library.commentinput.v2.StructMapperUtil;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.Map;

class HistoricalDecisionsInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private HistoricalDecisionsInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedHistoricalDecisionsInputGrpcServer());

    var stub =
        HistoricalDecisionsInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new HistoricalDecisionsInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchHistoricalDecisionsInputs() {
    //when
    var response = underTest.getBatchGetMatchHistoricalDecisionsInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedHistoricalDecisionsInputGrpcServer
      extends HistoricalDecisionsInputServiceImplBase {

    @Override
    public void batchGetMatchHistoricalDecisionsInputs(
        BatchGetMatchHistoricalDecisionsInputsRequest request,
        StreamObserver<BatchGetMatchHistoricalDecisionsInputsResponse> responseObserver) {
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
    public static final int TRUE_POSITIVE_COUNT = 5;
    public static final Map<String, String> REASONS = Map.of("key_one", "value_one");

    static final BatchGetMatchHistoricalDecisionsInputsIn REQUEST =
        BatchGetMatchHistoricalDecisionsInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<HistoricalDecisionsFeatureInput> HISTORICAL_DECISIONS_FEATURE_INPUTS =
        List.of(
            HistoricalDecisionsFeatureInput.newBuilder()
                .setFeature(FEATURE_ONE)
                .setTruePositiveCount(TRUE_POSITIVE_COUNT)
                .setModelType(ModelType.ALERTED_PARTY)
                .setReason(StructMapperUtil.toStruct(REASONS))
                .build()
        );

    static final List<HistoricalDecisionsInput> HISTORICAL_DECISIONS_INPUTS = List.of(
        HistoricalDecisionsInput.newBuilder()
            .setMatch(MATCH)
            .addAllFeatureInputs(HISTORICAL_DECISIONS_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchHistoricalDecisionsInputsResponse GRPC_RESPONSE =
        BatchGetMatchHistoricalDecisionsInputsResponse.newBuilder()
            .addAllHistoricalDecisionsInputs(HISTORICAL_DECISIONS_INPUTS)
            .build();

    static final List<BatchGetMatchHistoricalDecisionsInputsOut> RESPONSE =
        List.of(BatchGetMatchHistoricalDecisionsInputsOut.createFrom(GRPC_RESPONSE));
  }
}
