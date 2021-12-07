package com.silenteight.universaldatasource.api.library.freetext.v1;

import com.silenteight.datasource.api.freetext.v1.*;
import com.silenteight.datasource.api.freetext.v1.FreeTextInputServiceGrpc.FreeTextInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class FreeTextInputGrpcServiceAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private FreeTextInputGrpcServiceAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedFreeTextInputServiceGrpcServer());

    var stub = FreeTextInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new FreeTextInputGrpcServiceAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchFreeTextInputs() {
    //when
    var response = underTest.getBatchGetMatchFreeTextInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedFreeTextInputServiceGrpcServer extends FreeTextInputServiceImplBase {

    @Override
    public void batchGetMatchFreeTextInputs(
        BatchGetMatchFreeTextInputsRequest request,
        StreamObserver<BatchGetMatchFreeTextInputsResponse> responseObserver) {
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
    public static final String MATCHED_NAME = "machted_name";
    public static final String MATCHED_NAME_SYNONYM = "machted_name_synonym";
    public static final String MATCHED_NAME_TYPE = "machted_name_synonym";
    public static final String MATCHED_FREE_TEXT = "machted_free_text";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> ALERTED_PARTY_DATES = List.of("party_one", "party_two");

    static final BatchGetMatchFreeTextInputsIn REQUEST = BatchGetMatchFreeTextInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<FreeTextFeatureInput> FREE_TEXT_FEATURE_INPUTS = List.of(
        FreeTextFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .setMatchedName(MATCHED_NAME)
            .setMatchedNameSynonym(MATCHED_NAME_SYNONYM)
            .setMatchedType(MATCHED_NAME_TYPE)
            .addAllMatchingTexts(ALERTED_PARTY_DATES)
            .setFreetext(MATCHED_FREE_TEXT)
            .build()
    );

    static final List<FreeTextInput> FREE_TEXT_INPUTS = List.of(
        FreeTextInput.newBuilder()
            .setMatch(MATCH)
            .addAllFreetextFeatureInputs(FREE_TEXT_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchFreeTextInputsResponse GRPC_RESPONSE =
        BatchGetMatchFreeTextInputsResponse.newBuilder()
            .addAllFreetextInputs(FREE_TEXT_INPUTS)
            .build();

    static final List<BatchGetMatchFreeTextInputsOut> RESPONSE =
        List.of(BatchGetMatchFreeTextInputsOut.createFrom(GRPC_RESPONSE));
  }
}
