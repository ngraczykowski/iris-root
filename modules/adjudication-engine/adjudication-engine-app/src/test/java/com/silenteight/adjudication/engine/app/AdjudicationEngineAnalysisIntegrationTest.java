package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static com.silenteight.adjudication.engine.app.IntegrationTestFixture.*;
import static com.silenteight.adjudication.engine.app.MatchSolutionTestDataAccess.solvedMatchesCount;
import static com.silenteight.adjudication.engine.app.RecommendationTestDataAccess.generatedRecommendationCount;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@SpringBootTest(
    classes = AdjudicationEngineApplication.class,
    properties = "debug=true")
@EnableConfigurationProperties
@ActiveProfiles({ "mockagents", "rabbitdeclare", "mockgovernance", "mockdatasource", "test" })
@Tag("longrunning")
class AdjudicationEngineAnalysisIntegrationTest {

  @GrpcClient("ae")
  private AnalysisServiceBlockingStub analysisService;

  @GrpcClient("ae")
  private DatasetServiceBlockingStub datasetService;

  @GrpcClient("ae")
  private AlertServiceBlockingStub alertService;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Analysis analysis;

  private Analysis secondAnalysis;

  private Analysis savedAnalysis;

  private Dataset savedDataset;
  private Dataset dataset;

  @BeforeEach
  public void setUp() {
    var alert = createAlert(alertService, "alert1");
    var alert2 = createAlert(alertService, "alert2");
    createMatch(alertService, alert.getName(), "match1");
    createMatch(alertService, alert2.getName(), "match2");
    dataset = createDataset(datasetService, List.of(alert.getName(), alert2.getName()));

    var analysisFixture = Analysis.newBuilder()
        .setStrategy("strategies/back_test")
        .setPolicy("policies/af85189b-fa0d-437b-9009-ebb5e5bd5028")
        .addCategories("categories/source_system")
        .addCategories("categories/country")
        .addCategories("categories/customer_type")
        .addCategories("categories/hit_category")
        .addFeatures(Feature.newBuilder()
            .setFeature("features/name")
            .setAgentConfig("agents/name/versions/1.0.0/configs/1")
            .build())
        .addFeatures(Feature.newBuilder()
            .setFeature("features/dateOfBirth")
            .setAgentConfig("agents/date/versions/1.0.0/configs/1")
            .build())
        .putLabels("SIMULATION", "2021-03")
        .build();

    analysis = createAnalysis(analysisService, analysisFixture);
    addDataset(analysisService, analysis.getName(), dataset.getName());

    savedAnalysis = getAnalysis(analysisService, analysis.getName());
    savedDataset = getDataset(datasetService, dataset.getName());
  }

  @Test
  void shouldSolveAlerts() {
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertSolvedAlerts(analysisId, 2);
  }

  @Test
  void shouldSaveRecommendations() {
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertGeneratedRecommendation(analysisId, 2);
  }

  @Test
  void shouldSolveOneAlert() {
    var alert = createAlert(alertService, "alert3");
    createMatch(alertService, alert.getName(), "match3");
    var analysis = givenSecondAnalysis();
    addAlert(analysisService, secondAnalysis.getName(), alert.getName());

    assertThat(getAnalysis(analysisService, secondAnalysis.getName()).getAlertCount()).isEqualTo(3);

    assertSolvedAlerts(analysis, 3);
  }

  @Test
  void shouldStreamRecommendations() {
    assertGeneratedRecommendation("analysis/1/recommendations/1");
  }

  @Test
  void shouldSolveAlertsWhenSecondAnalysisAdded() {
    var analysisId = givenSecondAnalysis();

    assertSolvedAlerts(analysisId, 2);
  }

  @Test
  void shouldSaveRecommendationsWhenSecondAnalysisAdded() {
    var analysisId = givenSecondAnalysis();

    assertGeneratedRecommendation(analysisId, 2);
  }

  @Test
  void shouldStreamRecommendationsWhenSecondAnalysisAdded() {
    givenSecondAnalysis();

    assertGeneratedRecommendation("analysis/2/recommendations/3");
  }

  private void assertSolvedAlerts(long analysisId, int solvedCount) {
    await()
        .atMost(Duration.ofSeconds(10))
        .until(() -> solvedMatchesCount(jdbcTemplate, analysisId) >= solvedCount);

    assertThat(solvedMatchesCount(jdbcTemplate, analysisId))
        .isEqualTo(solvedCount);
  }

  private void assertGeneratedRecommendation(long analysisId, int recommendationCount) {
    await()
        .atMost(Duration.ofSeconds(10000))
        .until(() -> generatedRecommendationCount(jdbcTemplate, analysisId) > 0);

    assertThat(generatedRecommendationCount(jdbcTemplate, analysisId))
        .isEqualTo(recommendationCount);
  }

  private void assertGeneratedRecommendation(String analysisName) {
    await()
        .atMost(Duration.ofSeconds(10))
        .until(() -> generatedRecommendationCount(
            jdbcTemplate,
            ResourceName.create(analysisName).getLong("analysis")) > 0);

    var recommendations = analysisService.streamRecommendations(
        StreamRecommendationsRequest.newBuilder().setAnalysis(analysisName).build());
    var recommendation = recommendations.next();

    assertThat(recommendation.getRecommendationComment())
        .contains("NOTE: This is the default alert comment template!");
    assertThat(recommendation.getName()).isEqualTo(analysisName);
    assertThat(recommendation.getRecommendedAction()).isEqualTo("MATCH");
  }


  /*
    FIRST APPROACH:

    - as in shouldSolveAlerts wait until count of recommendations

    SECOND APPROACH:

    - wait for a message on queue
    - prerequisite: bind the queue to exchange where RecommendationsGenerated gets published

      //rabbitTemplate.receiveAndConvert(
      //    "dont_know_yet", ParameterizedTypeReference.forType(RecommendationsGenerated.class));
   */
  @Test
  void shouldSaveOneAlertToDataSet() {
    assertThat(savedDataset.getAlertCount()).isEqualTo(2);
  }

  @Test
  void checkLabelCountInAnalysisWithSingleAlert() {
    assertThat(savedAnalysis.getLabelsCount()).isEqualTo(1);
  }

  @Test
  void checkCategoriesCountInAnalysisWithSingleAlert() {
    assertThat(savedAnalysis.getCategoriesCount()).isEqualTo(4);
  }

  @Test
  void checkFeatureCountInAnalysisWithSingleAlert() {
    assertThat(savedAnalysis.getFeaturesCount()).isEqualTo(2);
  }


  @Test
  void checkAlertsCountInAnalysisWithSingleAlert() {
    assertThat(savedAnalysis.getAlertCount()).isEqualTo(2);
    assertThat(savedAnalysis.getPendingAlerts()).isEqualTo(2);
  }

  List<Map<String, Object>> custom(String sql) {
    return jdbcTemplate.queryForList(sql);
  }

  @Test
  void checkAlertsCountInAnalysisWithSameAlertInTwoDatasets() {
    var analysis = createAnalysis(analysisService, this.analysis);
    var alert = createAlert(alertService, "1");
    createMatch(alertService, alert.getName(), "1");
    var dataset1 = createDataset(datasetService, List.of(alert.getName()));
    var dataset2 = createDataset(datasetService, List.of(alert.getName()));
    addDataset(analysisService, analysis.getName(), dataset1.getName());
    addDataset(analysisService, analysis.getName(), dataset2.getName());

    savedAnalysis = getAnalysis(analysisService, analysis.getName());
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");
    assertGeneratedRecommendation(analysisId, 1);
    savedAnalysis = getAnalysis(analysisService, analysis.getName());

    assertThat(savedAnalysis.getAlertCount()).isEqualTo(1);
    assertThat(savedAnalysis.getPendingAlerts()).isEqualTo(0);
  }

  private long givenSecondAnalysis() {
    var secondAnalysisFixture = Analysis.newBuilder()
        .setStrategy("strategies/back_test")
        .setPolicy("policies/af85189b-fa0d-437b-9009-ebb5e5bd5028")
        .addCategories("categories/source_system")
        .addCategories("categories/country")
        .addCategories("categories/hit_type")
        .addFeatures(Feature.newBuilder()
            .setFeature("features/name")
            .setAgentConfig("agents/name/versions/1.0.0/configs/1")
            .build())
        .addFeatures(Feature.newBuilder()
            .setFeature("features/nationalIdDocument")
            .setAgentConfig("agents/nationalId/versions/1.0.0/configs/1")
            .build())
        .putLabels("SIMULATION", "2021-03")
        .build();
    secondAnalysis = createAnalysis(analysisService, secondAnalysisFixture);
    addDataset(analysisService, secondAnalysis.getName(), dataset.getName());
    return ResourceName.create(secondAnalysis.getName()).getLong("analysis");
  }
}
