package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import com.silenteight.datasource.api.bankidentificationcodes.v1.*;
import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesInputServiceGrpc.BankIdentificationCodesInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class BankIdentificationCodesInputServiceClientGrpcAdapterTest {


  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private BankIdentificationCodesInputServiceClientGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedBankIdentificationCodesInputGrpcServer());

    var stub =
        BankIdentificationCodesInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new BankIdentificationCodesInputServiceClientGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchBankIdentificationCodesInputs() {
    //when
    var response = underTest.getBatchGetMatchBankIdentificationCodesInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedBankIdentificationCodesInputGrpcServer
      extends BankIdentificationCodesInputServiceImplBase {

    @Override
    public void batchGetMatchBankIdentificationCodesInputs(
        BatchGetMatchBankIdentificationCodesInputsRequest request,
        StreamObserver<BatchGetMatchBankIdentificationCodesInputsResponse> responseObserver) {
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

    public static final String ALERTED_PARTY_MATCHING_FIELD = "matching_field";
    public static final String WATCHLIST_MATCHING_TEXT = "matching_text";
    public static final String WATCHLIST_TYPE = "watchlist_type";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);

    public static final List<String> SEARCH_CODES = List.of("search_code_one", "search_code_two");
    public static final List<String> BIC_CODES = List.of("bic_code_one", "bic_code_two");

    static final BatchGetMatchBankIdentificationCodesInputsIn REQUEST =
        BatchGetMatchBankIdentificationCodesInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BankIdentificationCodesFeatureInput>
        BANK_IDENTIFICATION_CODES_FEATURE_INPUTS = List.of(
        BankIdentificationCodesFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .setAlertedPartyMatchingField(ALERTED_PARTY_MATCHING_FIELD)
            .setWatchlistMatchingText(WATCHLIST_MATCHING_TEXT)
            .setWatchlistType(WATCHLIST_TYPE)
            .addAllWatchlistSearchCodes(SEARCH_CODES)
            .addAllWatchlistBicCodes(BIC_CODES)
            .build()
    );

    static final List<BankIdentificationCodesInput> BANK_IDENTIFICATION_CODES_INPUTS = List.of(
        BankIdentificationCodesInput.newBuilder()
            .setMatch(MATCH)
            .addAllBankIdentificationCodesFeatureInputs(
                BANK_IDENTIFICATION_CODES_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchBankIdentificationCodesInputsResponse GRPC_RESPONSE =
        BatchGetMatchBankIdentificationCodesInputsResponse.newBuilder()
            .addAllBankIdentificationCodesInputs(BANK_IDENTIFICATION_CODES_INPUTS)
            .build();

    static final List<BatchGetMatchBankIdentificationCodesInputsOut> RESPONSE =
        List.of(BatchGetMatchBankIdentificationCodesInputsOut.createFrom(GRPC_RESPONSE));
  }
}
