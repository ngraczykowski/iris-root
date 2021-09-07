package com.silenteight.payments.bridge.app;

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
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchAddMatchFeaturesUseCase;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;

import com.google.protobuf.Message;
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
import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.app.FeatureTestDataAccess.streamedFeaturesCount;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@SpringBootTest(
    classes = PaymentsBridgeApplication.class,
    properties = "debug=true")
@ActiveProfiles({ "test" })
@EnableConfigurationProperties
class DataSourceServiceIT {

  @GrpcClient("pb")
  private NameInputServiceBlockingStub nameInputServiceBlockingStub;

  @GrpcClient("pb")
  private LocationInputServiceBlockingStub locationInputServiceBlockingStub;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private BatchAddMatchFeaturesUseCase batchAddMatchFeaturesUseCase;

  @BeforeEach
  void setUp() {
    Map<String, Map<String, Message>> mapOfFeatureRequestsOne = Map.of(
        "nameMatch",
        Map.of(
            "name",
            NameFeatureInput.newBuilder()
                .setFeature("name")
                .addAlertedPartyNames(AlertedPartyName.newBuilder().setName("JohnDoe").build())
                .addWatchlistNames(WatchlistName.newBuilder().setName("Jane Doe").build())
                .setAlertedPartyType(EntityType.INDIVIDUAL)
                .addMatchingTexts("Doe")
                .build()),
        "locationMatchOne",
        Map.of(
            "country",
            LocationFeatureInput.newBuilder()
                .setFeature("country")
                .setAlertedPartyLocation("UK")
                .setWatchlistLocation("United Kingdom")
                .build(),
            "address",
            LocationFeatureInput.newBuilder()
                .setFeature("address")
                .setAlertedPartyLocation(
                    "Jerozolimskie 2/50, 01-200 Warszawa, mazowieckie, Poland")
                .setWatchlistLocation("Jerozolimskie Wawa, Poland")
                .build(),
            "state",
            LocationFeatureInput.newBuilder()
                .setFeature("state")
                .setAlertedPartyLocation("Cambridge TerraceWellington 6011, New Zealand")
                .setWatchlistLocation("Cambridge 6011, New Zealand")
                .build()));

    Map<String, Map<String, Message>> mapOfFeatureRequestsTwo = Map.of(
        "locationMatchTwo",
        Map.of(
            "city",
            LocationFeatureInput.newBuilder()
                .setFeature("city")
                .setAlertedPartyLocation("HongKong")
                .setWatchlistLocation("Singapore")
                .build()));

    var addMatchFeatureRequestsOne =
        IntegrationFixture.createAddMatchFeatureRequests(mapOfFeatureRequestsOne);
    var addMatchFeatureRequestsTwo =
        IntegrationFixture.createAddMatchFeatureRequests(mapOfFeatureRequestsTwo);
    batchAddMatchFeaturesUseCase.batchAddMatchFeatures(
        List.of(addMatchFeatureRequestsOne, addMatchFeatureRequestsTwo));
  }

  @Test
  void shouldGetNameFeature() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() -> streamedFeaturesCount(jdbcTemplate, "nameMatch", "name"
        ) > 0);

    var batchGetMatchNameInputsResponses =
        nameInputServiceBlockingStub.batchGetMatchNameInputs(
            BatchGetMatchNameInputsRequest.newBuilder()
                .addMatches("alerts/1/matches/nameMatch")
                .addFeatures("features/name")
                .build());

    var matchNameInputsResponse = batchGetMatchNameInputsResponses.next();
    assertThat(matchNameInputsResponse.getNameInputs(0).getMatch()).isEqualTo("nameMatch");
    assertThat(matchNameInputsResponse.getNameInputsCount()).isEqualTo(1);
  }

  @Test
  void shouldGetLocationFeature() {
    await()
        .atMost(Duration.ofSeconds(5))
        .until(() -> streamedFeaturesCount(jdbcTemplate, "locationMatchOne", "state"
        ) > 0);

    var batchGetMatchLocationInputs =
        locationInputServiceBlockingStub.batchGetMatchLocationInputs(
            BatchGetMatchLocationInputsRequest.newBuilder()
                .addMatches("alerts/1/matches/locationMatchOne")
                .addMatches("alerts/1/matches/locationMatchTwo")
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
        "locationMatchOne");
    assertThat(matchLocationInputsResponse
        .getLocationInputs(0)
        .getLocationFeatureInputsList()
        .get(0)
        .getAlertedPartyLocation()).isEqualTo("Cambridge TerraceWellington 6011, New Zealand");
  }
}
