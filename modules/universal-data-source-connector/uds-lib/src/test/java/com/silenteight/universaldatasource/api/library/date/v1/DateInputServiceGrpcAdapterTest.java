package com.silenteight.universaldatasource.api.library.date.v1;

import com.silenteight.datasource.api.date.v1.*;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.EntityType;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.SeverityMode;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static com.silenteight.datasource.api.date.v1.DateInputServiceGrpc.DateInputServiceImplBase;

class DateInputServiceGrpcAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private DateInputServiceGrpcAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedDateInputGrpcServer());

    var stub = DateInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new DateInputServiceGrpcAdapter(stub, 1L);
  }

  @Test
  void shouldGetBatchGetMatchDateInputs() {
    //when
    var response = underTest.getBatchGetMatchDateInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedDateInputGrpcServer extends DateInputServiceImplBase {

    @Override
    public void batchGetMatchDateInputs(
        BatchGetMatchDateInputsRequest request,
        StreamObserver<BatchGetMatchDateInputsResponse> responseObserver) {
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
    public static final List<String> ALERTED_PARTY_DATES = List.of("party_one", "party_two");
    public static final List<String> WATCHLIST_DATES = List.of("watch_one", "watch_two");
    public static final EntityType ENTITY_TYPE = EntityType.ORGANIZATION;
    public static final SeverityMode SEVERITY_MODE = SeverityMode.STRICT;

    static final BatchGetMatchDateInputsIn REQUEST = BatchGetMatchDateInputsIn.builder()
        .features(FEATURES)
        .matches(MATCHES)
        .build();

    static final List<DateFeatureInput> DATE_FEATURE_INPUTS = List.of(
        DateFeatureInput.newBuilder()
            .setFeature(FEATURE_ONE)
            .addAllAlertedPartyDates(ALERTED_PARTY_DATES)
            .addAllWatchlistDates(WATCHLIST_DATES)
            .setAlertedPartyType(ENTITY_TYPE)
            .setMode(SEVERITY_MODE)
            .build()
    );

    static final List<DateInput> DATE_INPUTS = List.of(
        DateInput.newBuilder()
            .setMatch(MATCH)
            .addAllDateFeatureInputs(DATE_FEATURE_INPUTS)
            .build()
    );

    static final BatchGetMatchDateInputsResponse GRPC_RESPONSE =
        BatchGetMatchDateInputsResponse.newBuilder()
            .addAllDateInputs(DATE_INPUTS)
            .build();

    static final List<BatchGetMatchDateInputsOut> RESPONSE =
        List.of(BatchGetMatchDateInputsOut.createFrom(GRPC_RESPONSE));
  }

}
