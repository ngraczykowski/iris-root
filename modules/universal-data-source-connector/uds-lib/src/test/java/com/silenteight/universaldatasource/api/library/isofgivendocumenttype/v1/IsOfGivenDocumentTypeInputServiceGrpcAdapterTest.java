package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.*;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeInputServiceGrpc.IsOfGivenDocumentTypeInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class IsOfGivenDocumentTypeInputServiceGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private IsOfGivenDocumentTypeInputServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedIsOfGivenDocumentTypeInputServiceGrpcServer());

    var stub =
        IsOfGivenDocumentTypeInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new IsOfGivenDocumentTypeInputServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchDocumentInputs() {
    //when
    var response = underTest.getBatchGetIsOfGivenDocumentTypeInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedIsOfGivenDocumentTypeInputServiceGrpcServer extends
      IsOfGivenDocumentTypeInputServiceImplBase {

    @Override
    public void batchGetIsOfGivenDocumentTypeInputs(
        BatchGetIsOfGivenDocumentTypeInputsRequest request,
        StreamObserver<BatchGetIsOfGivenDocumentTypeInputsResponse> responseObserver) {
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
    public static final String DOCUMENT_NUMBER = "document";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> DOCUMENT_TYPES = List.of("type_one", "type_two");

    static final BatchGetIsOfGivenDocumentTypeInputsIn REQUEST =
        BatchGetIsOfGivenDocumentTypeInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<IsOfGivenDocumentTypeFeatureInput> IS_OF_GIVEN_DOCUMENT_TYPE_FEATURE_INPUTS =
        List.of(
            IsOfGivenDocumentTypeFeatureInput.newBuilder()
                .setFeature(FEATURE_ONE)
                .setDocumentNumber(DOCUMENT_NUMBER)
                .addAllDocumentTypes(DOCUMENT_TYPES)
                .build()
        );
    static final List<IsOfGivenDocumentTypeInput> IS_OF_GIVEN_DOCUMENT_TYPE_INPUTS = List.of(
        IsOfGivenDocumentTypeInput.newBuilder()
            .setMatch(MATCH)
            .addAllIsOfGivenDocumentTypeFeaturesInput(IS_OF_GIVEN_DOCUMENT_TYPE_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetIsOfGivenDocumentTypeInputsResponse GRPC_RESPONSE =
        BatchGetIsOfGivenDocumentTypeInputsResponse.newBuilder()
            .addAllIsOfGivenDocumentTypeInputs(IS_OF_GIVEN_DOCUMENT_TYPE_INPUTS)
            .build();

    static final List<BatchGetIsOfGivenDocumentTypeInputsOut> RESPONSE =
        List.of(BatchGetIsOfGivenDocumentTypeInputsOut.createFrom(GRPC_RESPONSE));
  }
}