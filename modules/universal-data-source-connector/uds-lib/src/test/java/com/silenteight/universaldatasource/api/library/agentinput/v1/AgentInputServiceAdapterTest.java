package com.silenteight.universaldatasource.api.library.agentinput.v1;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceImplBase;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.universaldatasource.api.library.GrpcServerExtension;
import com.silenteight.universaldatasource.api.library.country.v1.CountryFeatureInputOut;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

class AgentInputServiceAdapterTest {

  @RegisterExtension
  GrpcServerExtension grpcServerExtension = new GrpcServerExtension();

  private AgentInputServiceAdapter underTest;

  @BeforeEach
  void setup() {
    grpcServerExtension.addService(new MockedAgentInputGrpcServer());

    var stub = AgentInputServiceGrpc.newBlockingStub(grpcServerExtension.getChannel());

    underTest = new AgentInputServiceAdapter(stub, 1L);
  }

  @Test
  void shouldCreateBatchCreateAgentInputs() {
    //when
    var response = underTest.createBatchCreateAgentInputs(Fixtures.REQUEST);

    //then
    Assertions.assertEquals(response.getCreatedAgentInputs().size(), 1);
    Assertions.assertEquals(response, Fixtures.RESPONSE);
  }

  static class MockedAgentInputGrpcServer extends AgentInputServiceImplBase {

    @Override
    public void batchCreateAgentInputs(
        BatchCreateAgentInputsRequest request,
        StreamObserver<BatchCreateAgentInputsResponse> responseObserver) {
      responseObserver.onNext(Fixtures.GRPC_RESPONSE);
      responseObserver.onCompleted();
    }
  }

  static class Fixtures {

    public static final String MATCH = "match";
    public static final String NAME = "name";
    public static final String ALERT = "alert";
    public static final String FEATURE_ONE = "featureone";

    public static final List<String> ALERTED_PARTY_COUNTRIES = List.of("part_one", "part_two");
    public static final List<String> WATCHLIST_COUNTRIES = List.of("country_one", "country_two");

    static final List<CountryFeatureInputOut> COUNTRY_FEATURE_INPUT_OUT = List.of(
        CountryFeatureInputOut.builder()
            .feature(FEATURE_ONE)
            .alertedPartyCountries(ALERTED_PARTY_COUNTRIES)
            .watchlistCountries(WATCHLIST_COUNTRIES)
            .build()
    );

    static final List<AgentInputIn<CountryFeatureInputOut>> AGENT_INPUT_INS = List.of(
        AgentInputIn.<CountryFeatureInputOut>builder()
            .alert(ALERT)
            .match(MATCH)
            .featureInputs(COUNTRY_FEATURE_INPUT_OUT)
            .build()
    );

    static final BatchCreateAgentInputsIn<CountryFeatureInputOut> REQUEST =
        BatchCreateAgentInputsIn.<CountryFeatureInputOut>builder()
            .agentInputs(AGENT_INPUT_INS)
            .build();

    static final List<CreatedAgentInput> CREATED_AGENT_INPUTS = List.of(
        CreatedAgentInput.newBuilder()
            .setMatch(MATCH)
            .setName(NAME)
            .build()
    );

    static final BatchCreateAgentInputsResponse GRPC_RESPONSE =
        BatchCreateAgentInputsResponse.newBuilder()
            .addAllCreatedAgentInputs(CREATED_AGENT_INPUTS)
            .build();

    static final BatchCreateAgentInputsOut RESPONSE =
        BatchCreateAgentInputsOut.createFrom(GRPC_RESPONSE);
  }

}
