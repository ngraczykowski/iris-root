package com.silenteight.universaldatasource.api.library.nationalid.v1;

import com.silenteight.datasource.api.nationalid.v1.*;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInputServiceGrpc.NationalIdInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class NationalIdInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();
  private NationalIdInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new NationalIdInputGrpcServer());

    var stub = NationalIdInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new NationalIdInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchNationalIdInputsOut() {
    //when
    var response = underTest.batchGetMatchTransactionInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class NationalIdInputGrpcServer extends NationalIdInputServiceImplBase {

    @Override
    public void batchGetMatchNationalIdInputs(
        BatchGetMatchNationalIdInputsRequest request,
        StreamObserver<BatchGetMatchNationalIdInputsResponse> responseObserver) {
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

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> ALERTED_PARTY_DOCUMENT_NUMBERS =
        List.of("apDocNum1", "apDocNum2");
    public static final List<String> WATCHLIST_DOCUMENT_NUMBERS = List.of("wlDocNum1", "wlDocNum2");
    public static final List<String> ALERTED_PARTY_COUNTRIES = List.of("apCountry1", "apCountry2");
    public static final List<String> WATCHLIST_COUNTRIES = List.of("wlCountry1", "wlCountry2");

    static final List<NationalIdFeatureInput> NATIONAL_ID_FEATURE_INPUTS = List.of(
        NationalIdFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyDocumentNumbers(ALERTED_PARTY_DOCUMENT_NUMBERS)
            .addAllWatchlistDocumentNumbers(WATCHLIST_DOCUMENT_NUMBERS)
            .addAllAlertedPartyCountries(ALERTED_PARTY_COUNTRIES)
            .addAllWatchlistCountries(WATCHLIST_COUNTRIES)
            .build());

    static final List<NationalIdInput> NATIONAL_ID_INPUTS = List.of(
        NationalIdInput.newBuilder()
            .setMatch(MATCH)
            .addAllNationalIdFeatureInputs(NATIONAL_ID_FEATURE_INPUTS)
            .build());

    static final BatchGetMatchNationalIdInputsResponse GRPC_RESPONSE =
        BatchGetMatchNationalIdInputsResponse.newBuilder()
            .addAllNationalIdInputs(NATIONAL_ID_INPUTS)
            .build();

    static final BatchGetMatchNationalIdInputsIn REQUEST =
        BatchGetMatchNationalIdInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BatchGetMatchNationalIdInputsOut> RESPONSE =
        List.of(BatchGetMatchNationalIdInputsOut.createFrom(GRPC_RESPONSE));
  }
}
