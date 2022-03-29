package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Disabled;
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
@Disabled
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

  @Test
  void shouldSolveAlerts() {
    var analysisDataset = createAnalysisWithDataset(datasetService, analysisService, alertService);
    var savedAnalysis = analysisDataset.getAnalysis();
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertSolvedAlerts(analysisId, 2);
  }

  @Test
  @Disabled
  void shouldSaveRecommendations() {
    var analysisDataset = createAnalysisWithDataset(datasetService, analysisService, alertService);
    var savedAnalysis = analysisDataset.getAnalysis();
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertGeneratedRecommendation(analysisId, 2);
  }

  @Test
  void shouldSolveOneAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    var alert = createAlert(alertService, "alert3");
    createMatch(alertService, alert.getName(), "match3");
    addAlert(analysisService, analysis.getName(), alert.getName());

    assertThat(getAnalysis(analysisService, analysis.getName()).getAlertCount()).isEqualTo(3);

    var analysisId = ResourceName.create(analysis.getName()).getLong("analysis");
    assertSolvedAlerts(analysisId, 3);
  }

  @Test
  void shouldStreamRecommendations() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    assertGeneratedRecommendation(analysis.getName());
  }

  @Test
  void shouldSolveAlertsWhenSecondAnalysisAdded() {
    createAnalysisWithDataset(datasetService, analysisService, alertService);
    var second =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();

    var analysisId = ResourceName.create(second.getName()).getLong("analysis");
    assertSolvedAlerts(analysisId, 2);
  }

  @Test
  void shouldSaveRecommendationsWhenSecondAnalysisAdded() {
    createAnalysisWithDataset(datasetService, analysisService, alertService);
    var second =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();

    var analysisId = ResourceName.create(second.getName()).getLong("analysis");

    assertGeneratedRecommendation(analysisId, 2);
  }

  @Test
  void shouldSaveOneAlertToDataSet() {
    var dataset =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getDataset();

    assertThat(dataset.getAlertCount()).isEqualTo(2);
  }

  @Test
  void checkLabelCountInAnalysisWithSingleAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    assertThat(analysis.getLabelsCount()).isEqualTo(1);
  }

  @Test
  void checkCategoriesCountInAnalysisWithSingleAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    assertThat(analysis.getCategoriesCount()).isEqualTo(4);
  }

  @Test
  void checkFeatureCountInAnalysisWithSingleAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    assertThat(analysis.getFeaturesCount()).isEqualTo(2);
  }


  @Test
  void checkAlertsCountInAnalysisWithSingleAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    assertThat(analysis.getAlertCount()).isEqualTo(2);
    assertThat(analysis.getPendingAlerts()).isEqualTo(2);
  }

  @Test
  @Disabled
  void checkAlertsCountInAnalysisWithSameAlertInTwoDatasets() {
    var analysis = createAnalysis(analysisService, createAnalysisFixture());
    var alert = createAlert(alertService, "1");
    createMatch(alertService, alert.getName(), "1");
    var dataset1 = createDataset(datasetService, List.of(alert.getName()));
    var dataset2 = createDataset(datasetService, List.of(alert.getName()));
    addDataset(analysisService, analysis.getName(), dataset1.getName());
    addDataset(analysisService, analysis.getName(), dataset2.getName());

    var savedAnalysis = getAnalysis(analysisService, analysis.getName());
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");
    assertGeneratedRecommendation(analysisId, 1);
    savedAnalysis = getAnalysis(analysisService, analysis.getName());

    assertThat(savedAnalysis.getAlertCount()).isEqualTo(1);
    assertThat(savedAnalysis.getPendingAlerts()).isEqualTo(0);
  }

  private void assertSolvedAlerts(long analysisId, int solvedCount) {
    await()
        .atMost(Duration.ofSeconds(180))
        .until(() -> solvedMatchesCount(jdbcTemplate, analysisId) >= solvedCount);

    assertThat(solvedMatchesCount(jdbcTemplate, analysisId))
        .isEqualTo(solvedCount);
  }

  private void assertGeneratedRecommendation(long analysisId, int recommendationCount) {
    await()
        .atMost(Duration.ofSeconds(180))
        .until(() -> generatedRecommendationCount(jdbcTemplate, analysisId) > 0);

    assertThat(generatedRecommendationCount(jdbcTemplate, analysisId))
        .isEqualTo(recommendationCount);
  }

  private void assertGeneratedRecommendation(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    await()
        .atMost(Duration.ofSeconds(180))
        .until(() -> generatedRecommendationCount(
            jdbcTemplate,
            analysisId) > 0);

    var recommendations = analysisService.streamRecommendations(
        StreamRecommendationsRequest.newBuilder().setAnalysis(analysisName).build());
    var recommendation = recommendations.next();

    assertThat(recommendation.getRecommendationComment())
        .contains("Manual Investigation");
    assertThat(
        ResourceName.create(recommendation.getName()).getLong("analysis")).isEqualTo(analysisId);
    assertThat(recommendation.getRecommendedAction()).isEqualTo("MATCH");
  }
}
