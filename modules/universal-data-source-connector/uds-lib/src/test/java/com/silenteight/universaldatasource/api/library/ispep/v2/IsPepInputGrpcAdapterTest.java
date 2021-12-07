package com.silenteight.universaldatasource.api.library.ispep.v2;

import com.silenteight.datasource.api.ispep.v2.*;
import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class IsPepInputGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();
  private IsPepInputGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new IsPepInputGrpcServer());

    var stub = IsPepInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new IsPepInputGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchIsPepInputsOut() {
    //when
    var response = underTest.batchGetMatchIsPepInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class IsPepInputGrpcServer extends IsPepInputServiceImplBase {

    @Override
    public void batchGetMatchIsPepInputs(
        BatchGetMatchIsPepInputsRequest request,
        StreamObserver<BatchGetMatchIsPepInputsResponse> responseObserver) {
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
    public static final String COUNTRY = "someCountry";
    public static final String ID = "someId";
    public static final String TYPE = "someType";
    public static final String FURTHER_INFORMATION = "someFurtherInformation";

    public static final List<String> MATCHES = List.of(MATCH_ONE, MATCH_TWO);
    public static final List<String> FEATURES = List.of(FEATURE_ONE, FEATURE_TWO);
    public static final List<String> COUNTRIES = List.of(COUNTRY);
    public static final List<String> LINKED_PEP_UIDS =
        List.of("pep_uid_1", "pep_uid_2", "pep_uid_3");

    static final AlertedPartyItem ALERTED_PARTY_ITEM =
        AlertedPartyItem.newBuilder()
            .setCountry(COUNTRY)
            .build();

    static final WatchlistItem WATCHLIST_ITEM =
        WatchlistItem.newBuilder()
            .setId(ID)
            .setType(TYPE)
            .addAllCountries(COUNTRIES)
            .setFurtherInformation(FURTHER_INFORMATION)
            .addAllLinkedPepsUids(LINKED_PEP_UIDS)
            .build();

    static final IsPepFeatureInput IS_PEP_FEATURE_INPUT =
        IsPepFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .setWatchlistItem(WATCHLIST_ITEM)
            .setAlertedPartyItem(ALERTED_PARTY_ITEM)
            .build();

    static final List<IsPepInput> IS_PEP_INPUTS = List.of(
        IsPepInput.newBuilder()
            .setMatch(MATCH)
            .setIsPepFeatureInput(IS_PEP_FEATURE_INPUT)
            .build());

    static final BatchGetMatchIsPepInputsResponse GRPC_RESPONSE =
        BatchGetMatchIsPepInputsResponse.newBuilder()
            .addAllIsPepInputs(IS_PEP_INPUTS)
            .build();

    static final BatchGetMatchIsPepInputsIn REQUEST =
        BatchGetMatchIsPepInputsIn.builder()
            .features(FEATURES)
            .matches(MATCHES)
            .build();

    static final List<BatchGetMatchIsPepInputsOut> RESPONSE =
        List.of(BatchGetMatchIsPepInputsOut.createFrom(GRPC_RESPONSE));
  }
}
