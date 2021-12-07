package com.silenteight.universaldatasource.api.library.country.v1;

import com.silenteight.datasource.api.country.v1.*;
import com.silenteight.datasource.api.country.v1.CountryInputServiceGrpc.CountryInputServiceImplBase;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class CountryInputClientGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private CountryInputClientGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedCountryInputGrpcServer());

    var stub = CountryInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new CountryInputClientGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchCountryInputs() {
    //when
    var response = underTest.getBatchGetMatchCountryInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedCountryInputGrpcServer extends CountryInputServiceImplBase {

    @Override
    public void batchGetMatchCountryInputs(
        BatchGetMatchCountryInputsRequest request,
        StreamObserver<BatchGetMatchCountryInputsResponse> responseObserver) {
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
    public static final List<String> ALERTED_PARTY_COUNTRIES = List.of("party_one", "party_two");
    public static final List<String> WATCHLIST_COUNTRIES = List.of("watch_one", "watch_two");

    static final BatchGetMatchCountryInputsIn REQUEST = BatchGetMatchCountryInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<CountryFeatureInput> COUNTRY_FEATURE_INPUTS = List.of(
        CountryFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyCountries(ALERTED_PARTY_COUNTRIES)
            .addAllWatchlistCountries(WATCHLIST_COUNTRIES)
            .build()
    );

    static final List<CountryInput> COUNTRY_INPUTS = List.of(
        CountryInput.newBuilder()
            .setMatch(MATCH)
            .addAllCountryFeatureInputs(COUNTRY_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchCountryInputsResponse GRPC_RESPONSE =
        BatchGetMatchCountryInputsResponse.newBuilder()
            .addAllCountryMatches(COUNTRY_INPUTS)
            .build();

    static final List<BatchGetMatchCountryInputsOut> RESPONSE =
        List.of(BatchGetMatchCountryInputsOut.createFrom(GRPC_RESPONSE));
  }
}
