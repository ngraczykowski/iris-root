package com.silenteight.universaldatasource.app.feature;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.api.document.v1.DocumentInput;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextInputServiceGrpc.FreeTextInputServiceBlockingStub;
import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v2.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceBlockingStub;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceBlockingStub;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceBlockingStub;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceBlockingStub;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.universaldatasource.app.UniversalDataSourceApplication;

import com.google.protobuf.Message;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Iterator;
import java.util.Map;

import static com.silenteight.universaldatasource.app.feature.IntegrationFixture.getBatchCreateAgentInputsRequest;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = UniversalDataSourceApplication.class,
    properties = { "debug=true" })
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
class FeatureInputServiceIT {

  @GrpcClient("uds")
  private AgentInputServiceBlockingStub agentInputServiceBlockingStub;

  @GrpcClient("uds")
  private NameInputServiceBlockingStub nameInputServiceBlockingStub;

  @GrpcClient("uds")
  private LocationInputServiceBlockingStub locationInputServiceBlockingStub;

  @GrpcClient("uds")
  private FreeTextInputServiceBlockingStub freeTextInputServiceBlockingStub;

  @GrpcClient("uds")
  private IsPepInputServiceBlockingStub isPepInputServiceBlockingStub;

  @GrpcClient("uds")
  private HistoricalDecisionsInputServiceBlockingStub historicalDecisionsInputServiceBlockingStub;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetNameFeature() {

    var batchGetMatchNameInputsResponses =
        nameInputServiceBlockingStub.batchGetMatchNameInputs(
            BatchGetMatchNameInputsRequest.newBuilder()
                .addMatches("alerts/1/matches/0")
                .addFeatures("features/name")
                .build());

    var matchNameInputsResponse = batchGetMatchNameInputsResponses.next();
    assertThat(matchNameInputsResponse.getNameInputs(0).getMatch()).isEqualTo(
        "alerts/1/matches/0");
    assertThat(matchNameInputsResponse.getNameInputsCount()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetLocationFeature() {

    var batchGetMatchLocationInputs =
        locationInputServiceBlockingStub.batchGetMatchLocationInputs(
            BatchGetMatchLocationInputsRequest.newBuilder()
                .addMatches("alerts/3/matches/3")
                .addMatches("alerts/4/matches/4")
                .addFeatures("features/state")
                .addFeatures("features/city")
                .build());

    var matchLocationInputsResponse = batchGetMatchLocationInputs.next();
    assertLocationFeature(matchLocationInputsResponse);
  }


  private static void assertLocationFeature(
      BatchGetMatchLocationInputsResponse response) {

    assertThat(response.getLocationInputsCount()).isEqualTo(2);
    assertThat(response.getLocationInputsList().stream()
        .filter(l -> l.getMatch().equals("alerts/3/matches/3"))
        .count())
        .isEqualTo(1);

    assertThat(response
        .getLocationInputs(0)
        .getLocationFeatureInputsList()
        .get(0)
        .getAlertedPartyLocation()).isEqualTo("Cambridge TerraceWellington 6011, New Zealand");
  }

  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetLocationFeatureAfterMappingFeatureState() {

    var batchGetMatchLocationInputs =
        locationInputServiceBlockingStub.batchGetMatchLocationInputs(
            BatchGetMatchLocationInputsRequest.newBuilder()
                .addMatches("alerts/3/matches/3")
                .addFeatures("features/state2")
                .build());

    var matchLocationInputsResponse = batchGetMatchLocationInputs.next();
    assertLocationFeatureState(matchLocationInputsResponse);
  }


  private static void assertLocationFeatureState(
      BatchGetMatchLocationInputsResponse response) {

    assertThat(response.getLocationInputsCount()).isEqualTo(1);
    assertThat(response.getLocationInputsList().stream()
        .filter(l -> l.getMatch().equals("alerts/3/matches/3"))
        .count())
        .isEqualTo(1);

    assertThat(response
        .getLocationInputs(0)
        .getLocationFeatureInputsList()
        .get(0)
        .getAlertedPartyLocation()).isEqualTo("Cambridge TerraceWellington 6011, New Zealand");
  }


  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetFreeTextFeature() {

    var batchGetMatchFreeTextInputs =
        freeTextInputServiceBlockingStub.batchGetMatchFreeTextInputs(
            BatchGetMatchFreeTextInputsRequest.newBuilder()
                .addMatches("alerts/6/matches/6")
                .addFeatures("features/freetext")
                .build()
        );

    var matchFreeTextInputsResponse = batchGetMatchFreeTextInputs.next();
    assertFreeTextFeature(matchFreeTextInputsResponse);
  }


  private static void assertFreeTextFeature(
      BatchGetMatchFreeTextInputsResponse response) {

    assertThat(response.getFreetextInputsCount()).isEqualTo(1);

    assertThat(response.getFreetextInputsList().get(0)
        .getFreetextFeatureInputsList().stream()
        .filter(f -> f.getFeature().equals("features/freetext"))
        .filter(f -> f.getMatchedType().equals("name"))
        .filter(f -> f.getFreetext().equals("Joe"))
        .count()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetIsPepFeature() {

    var batchGetMatchIsPepSolutionsResponseIterator =
        isPepInputServiceBlockingStub.batchGetMatchIsPepInputs(
            BatchGetMatchIsPepInputsRequest.newBuilder()
                .addMatches("alerts/5/matches/5")
                .addFeatures("features/pep")
                .build());

    assertIsPepFeature(batchGetMatchIsPepSolutionsResponseIterator);
  }

  private static void assertIsPepFeature(
      Iterator<BatchGetMatchIsPepInputsResponse> batchGetMatchFreeTextInputs) {

    var matchFreeTextInputsResponse = batchGetMatchFreeTextInputs.next();
    assertThat(matchFreeTextInputsResponse.getIsPepInputsCount()).isEqualTo(1);

    assertThat(matchFreeTextInputsResponse.getIsPepInputsList().stream()
        .filter(f -> f.getMatch().equals("alerts/5/matches/5"))
        .filter(f -> f.getIsPepFeatureInput().getFeature().equals("features/pep"))
        .count()).isEqualTo(1);
  }

  @Test
  @Sql(scripts = {
      "adapter/outgoing/jdbc/populate_feature_inputs.sql",
      "adapter/outgoing/jdbc/populate_feature_mapper_inputs.sql" })
  @Sql(scripts = "adapter/outgoing/jdbc/truncate_feature_inputs.sql",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldGetHistoricalDecisionFeature() {

    var response =
        historicalDecisionsInputServiceBlockingStub.batchGetMatchHistoricalDecisionsInputs(
            BatchGetMatchHistoricalDecisionsInputsRequest.newBuilder()
                .addMatches("alerts/11/matches/11")
                .addFeatures("features/historicalRiskAccountNumber")
                .build());

    assertHistoricalDecisionFeature(response);
  }

  private static void assertHistoricalDecisionFeature(
      Iterator<BatchGetMatchHistoricalDecisionsInputsResponse> batchGetMatchFreeTextInputs) {

    var historicalDecisionsInputsResponse = batchGetMatchFreeTextInputs.next();
    assertThat(historicalDecisionsInputsResponse.getHistoricalDecisionsInputsCount()).isEqualTo(1);

    assertThat(historicalDecisionsInputsResponse.getHistoricalDecisionsInputsList().stream()
        .filter(f -> f.getMatch().equals("alerts/11/matches/11"))
        .count()).isEqualTo(1);

    var historicalDecisionsFeatureInputList =
        historicalDecisionsInputsResponse
            .getHistoricalDecisionsInputsList()
            .get(0)
            .getHistoricalDecisionsFeatureInputList();

    assertThat(historicalDecisionsFeatureInputList.stream()
        .filter(featureInput -> featureInput.getModelKey().getAlertedParty().getId().equals("123"))
        .filter(featureInput -> featureInput.getDiscriminator().getValue().equals("1234"))
        .count()).isEqualTo(1);
  }

  @Test
  void addFeatureForNonExistingMapper() {
    Map<String, Map<String, Message>> mapOfFeatureRequestsOne = Map.of(
        "alerts/1/matches/1",
        Map.of(
            "features/name",
            DocumentInput.newBuilder().build()));

    var addMatchFeatureRequestsOne =
        getBatchCreateAgentInputsRequest(mapOfFeatureRequestsOne);

    assertThrows(
        StatusRuntimeException.class,
        () -> agentInputServiceBlockingStub.batchCreateAgentInputs(addMatchFeatureRequestsOne));
  }
}
