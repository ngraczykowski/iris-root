package com.silenteight.universaldatasource.app.feature;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.api.document.v1.DocumentInput;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;
import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsResponse;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextInputServiceGrpc.FreeTextInputServiceBlockingStub;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsRequest;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse.Feature;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse.FeatureSolution;
import com.silenteight.datasource.api.ispep.v1.IsPepInputServiceGrpc.IsPepInputServiceBlockingStub;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceBlockingStub;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceBlockingStub;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.universaldatasource.app.UniversalDataSourceApplication;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

import static com.silenteight.universaldatasource.app.feature.FeatureTestDataAccess.streamedFeaturesCount;
import static com.silenteight.universaldatasource.app.feature.IntegrationFixture.getBatchCreateAgentInputsRequest;
import static com.silenteight.universaldatasource.app.feature.IntegrationFixture.getIsPepReason;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = UniversalDataSourceApplication.class,
    properties = { "debug=true" })
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
class AgentInputServiceIT {

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

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() throws InvalidProtocolBufferException {
    Map<String, Map<String, Message>> mapOfFeatureRequestsOne = Map.of(
        "alerts/alertOne/matches/nameMatch",
        Map.of(
            "features/name",
            NameFeatureInput.newBuilder()
                .setFeature("name")
                .addAlertedPartyNames(AlertedPartyName.newBuilder().setName("JohnDoe").build())
                .addWatchlistNames(WatchlistName.newBuilder().setName("Jane Doe").build())
                .setAlertedPartyType(EntityType.INDIVIDUAL)
                .addMatchingTexts("Doe")
                .build()),
        "alerts/alertOne/matches/locationMatchOne",
        Map.of(
            "features/country",
            LocationFeatureInput.newBuilder()
                .setFeature("country")
                .setAlertedPartyLocation("UK")
                .setWatchlistLocation("United Kingdom")
                .build(),
            "features/address",
            LocationFeatureInput.newBuilder()
                .setFeature("address")
                .setAlertedPartyLocation(
                    "Jerozolimskie 2/50, 01-200 Warszawa, mazowieckie, Poland")
                .setWatchlistLocation("Jerozolimskie Wawa, Poland")
                .build(),
            "features/state",
            LocationFeatureInput.newBuilder()
                .setFeature("state")
                .setAlertedPartyLocation("Cambridge TerraceWellington 6011, New Zealand")
                .setWatchlistLocation("Cambridge 6011, New Zealand")
                .build()),

        "alerts/alertTwo/matches/locationMatchTwo",
        Map.of(
            "features/city",
            LocationFeatureInput.newBuilder()
                .setFeature("city")
                .setAlertedPartyLocation("HongKong")
                .setWatchlistLocation("Singapore")
                .build()),

        "alerts/alertThree/matches/freeTextMatchThree",
        Map.of(
            "features/freetext",
            FreeTextFeatureInput.newBuilder()
                .setFeature("freetext")
                .setMatchedName("Joanna")
                .setMatchedNameSynonym("Joe")
                .setMatchedType("name")
                .addMatchingTexts("Joe")
                .setFreetext("Joe")
                .build()),

        "alerts/alertFour/matches/isPepMatchFour",
        Map.of(
            "features/isPep",
            Feature.newBuilder()
                .setFeature("pep")
                .addFeatureSolutions(
                    FeatureSolution.newBuilder()
                        .setSolution("isPepSolution")
                        .setReason(getIsPepReason())
                        .build())
                .build())
    );

    var addMatchFeatureRequestsOne =
        getBatchCreateAgentInputsRequest(mapOfFeatureRequestsOne);

    agentInputServiceBlockingStub.batchCreateAgentInputs(addMatchFeatureRequestsOne);
  }


  @Test
  void shouldGetNameFeature() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() ->
            streamedFeaturesCount(jdbcTemplate,
                "alerts/alertOne/matches/nameMatch", "features/name"
            ) > 0);

    var batchGetMatchNameInputsResponses =
        nameInputServiceBlockingStub.batchGetMatchNameInputs(
            BatchGetMatchNameInputsRequest.newBuilder()
                .addMatches("alerts/alertOne/matches/nameMatch")
                .addFeatures("features/name")
                .build());

    var matchNameInputsResponse = batchGetMatchNameInputsResponses.next();
    assertThat(matchNameInputsResponse.getNameInputs(0).getMatch()).isEqualTo(
        "alerts/alertOne/matches/nameMatch");
    assertThat(matchNameInputsResponse.getNameInputsCount()).isEqualTo(1);
  }

  @Test
  void shouldGetLocationFeature() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() ->
            streamedFeaturesCount(
                jdbcTemplate, "alerts/alertOne/matches/locationMatchOne", "features/state"
            ) > 0);

    var batchGetMatchLocationInputs =
        locationInputServiceBlockingStub.batchGetMatchLocationInputs(
            BatchGetMatchLocationInputsRequest.newBuilder()
                .addMatches("alerts/alertOne/matches/locationMatchOne")
                .addMatches("alerts/alertTwo/matches/locationMatchTwo")
                .addFeatures("features/state")
                .addFeatures("features/city")
                .build());

    assertLocationFeature(batchGetMatchLocationInputs);
  }

  private static void assertLocationFeature(
      Iterator<BatchGetMatchLocationInputsResponse> batchGetMatchLocationInputs) {

    var matchLocationInputsResponse = batchGetMatchLocationInputs.next();
    assertThat(matchLocationInputsResponse.getLocationInputsCount()).isEqualTo(2);
    assertThat(matchLocationInputsResponse.getLocationInputs(0).getMatch()).isEqualTo(
        "alerts/alertOne/matches/locationMatchOne");
    assertThat(matchLocationInputsResponse
        .getLocationInputs(0)
        .getLocationFeatureInputsList()
        .get(0)
        .getAlertedPartyLocation()).isEqualTo("Cambridge TerraceWellington 6011, New Zealand");
  }

  @Test
  void shouldGetFreeTextFeature() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() ->
            streamedFeaturesCount(
                jdbcTemplate, "alerts/alertThree/matches/freeTextMatchThree", "features/freetext"
            ) > 0);

    var batchGetMatchFreeTextInputs =
        freeTextInputServiceBlockingStub.batchGetMatchFreeTextInputs(
            BatchGetMatchFreeTextInputsRequest.newBuilder()
                .addMatches("alerts/alertThree/matches/freeTextMatchThree")
                .addFeatures("features/freetext")
                .build()
        );

    assertFreeTextFeature(batchGetMatchFreeTextInputs);
  }


  private static void assertFreeTextFeature(
      Iterator<BatchGetMatchFreeTextInputsResponse> batchGetMatchFreeTextInputs) {

    var matchFreeTextInputsResponse = batchGetMatchFreeTextInputs.next();
    assertThat(matchFreeTextInputsResponse.getFreetextInputsCount()).isEqualTo(1);

    assertThat(matchFreeTextInputsResponse.getFreetextInputsList().get(0)
        .getFreetextFeatureInputsList().stream()
        .filter(f -> f.getFeature().equals("freetext"))
        .filter(f -> f.getMatchedType().equals("name"))
        .filter(f -> f.getFreetext().equals("Joe"))
        .count()).isEqualTo(1);
  }

  @Test
  void shouldGetIsPepFeature() {

    var batchGetMatchIsPepSolutionsResponseIterator =
        isPepInputServiceBlockingStub.batchGetMatchIsPepSolutions(
            BatchGetMatchIsPepSolutionsRequest.newBuilder()
                .addMatches("alerts/alertFour/matches/isPepMatchFour")
                .addFeatures("features/isPep")
                .build());

    assertIsPepFeature(batchGetMatchIsPepSolutionsResponseIterator);
  }

  private static void assertIsPepFeature(
      Iterator<BatchGetMatchIsPepSolutionsResponse> batchGetMatchFreeTextInputs) {

    var matchFreeTextInputsResponse = batchGetMatchFreeTextInputs.next();
    assertThat(matchFreeTextInputsResponse.getFeaturesCount()).isEqualTo(1);

    assertThat(matchFreeTextInputsResponse.getFeaturesList().get(0)
        .getFeatureSolutionsList().stream()
        .filter(f -> f.getSolution().equals("isPepSolution"))
        .count()).isEqualTo(1);
  }

  @Test
  void addFeatureForNonExistingMapper() {
    Map<String, Map<String, Message>> mapOfFeatureRequestsOne = Map.of(
        "alerts/alertOne/matches/locationMatchOne",
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
