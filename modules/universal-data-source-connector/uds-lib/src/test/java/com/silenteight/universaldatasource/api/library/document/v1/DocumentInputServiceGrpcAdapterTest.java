package com.silenteight.universaldatasource.api.library.document.v1;

import com.silenteight.datasource.api.document.v1.*;
import com.silenteight.datasource.api.document.v1.DocumentInputServiceGrpc.DocumentInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class DocumentInputServiceGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private DocumentInputServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedDocumentInputServiceGrpcServer());

    var stub = DocumentInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new DocumentInputServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchDocumentInputs() {
    //when
    var response = underTest.getBatchGetMatchDocumentInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedDocumentInputServiceGrpcServer extends DocumentInputServiceImplBase {

    @Override
    public void batchGetMatchDocumentInputs(
        BatchGetMatchDocumentInputsRequest request,
        StreamObserver<BatchGetMatchDocumentInputsResponse> responseObserver) {
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
    public static final List<String> ALERTED_PARTY_DOCUMENTS = List.of("party_one", "party_one");
    public static final List<String> WATCHLIST_DOCUMENTS = List.of("watch_one", "watch_two");

    static final BatchGetMatchDocumentInputsIn REQUEST = BatchGetMatchDocumentInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<DocumentFeatureInput> DOCUMENT_FEATURE_INPUTS = List.of(
        DocumentFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyDocuments(ALERTED_PARTY_DOCUMENTS)
            .addAllWatchlistDocuments(WATCHLIST_DOCUMENTS)
            .build()
    );
    static final List<DocumentInput> DOCUMENT_INPUTS = List.of(
        DocumentInput.newBuilder()
            .setMatch(MATCH)
            .addAllDocumentFeaturesInput(DOCUMENT_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchDocumentInputsResponse GRPC_RESPONSE =
        BatchGetMatchDocumentInputsResponse.newBuilder()
            .addAllDocumentInputs(DOCUMENT_INPUTS)
            .build();

    static final List<BatchGetMatchDocumentInputsOut> RESPONSE =
        List.of(BatchGetMatchDocumentInputsOut.createFrom(GRPC_RESPONSE));
  }
}
