package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.StreamRecommendationsRequest;
import com.silenteight.adjudication.engine.alerts.alert.AlertFacade;
import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeDataAccess;
import com.silenteight.adjudication.engine.analysis.agentexchange.AgentExchangeFeatureQueryRepository;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisCancelledUseCase;
import com.silenteight.adjudication.engine.analysis.pii.PiiFacade;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.silenteight.adjudication.engine.app.IntegrationTestFixture.*;
import static com.silenteight.adjudication.engine.app.MatchSolutionTestDataAccess.solvedMatchesCount;
import static com.silenteight.adjudication.engine.app.RecommendationTestDataAccess.generatedMatchRecommendationCount;
import static com.silenteight.adjudication.engine.app.RecommendationTestDataAccess.generatedRecommendationCount;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@SpringBootTest(
    classes = AdjudicationEngineApplication.class,
    properties = "debug=true",
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@EnableConfigurationProperties
@ActiveProfiles({ "mockagents", "rabbitdeclare", "mockgovernance", "mockdatasource", "test" })
@Tag("longrunning")
class AdjudicationEngineAnalysisIntegrationTest {

  private static final UUID REQUEST_ID_1 = UUID.fromString("7e465bc0-b661-11ec-b909-0242ac120002");
  private static final UUID REQUEST_ID_2 = UUID.fromString("9d2b4866-b661-11ec-b909-0242ac120002");
  private static final UUID REQUEST_ID_3 = UUID.fromString("a73e486c-b661-11ec-b909-0242ac120002");

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

  @Autowired
  private AlertFacade alertFacade;

  @Autowired
  private PiiFacade piiFacade;

  private static final int SOLVING_AWAIT_TIME = 20;


  @Autowired
  AnalysisCancelledUseCase analysisCancelledUseCase;

  @Autowired
  AgentExchangeFeatureQueryRepository repository;

  @Autowired
  AgentExchangeDataAccess agentExchangeDataAccess;

  @Test
  void shouldSolveAlerts() {
    var analysisDataset = createAnalysisWithDataset(datasetService, analysisService, alertService);
    var savedAnalysis = analysisDataset.getAnalysis();
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertSolvedAlerts(analysisId, 2);
  }

  @Test
  void shouldSaveRecommendations() {
    var analysisDataset = createAnalysisWithDataset(datasetService, analysisService, alertService);
    var savedAnalysis = analysisDataset.getAnalysis();
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertGeneratedRecommendation(analysisId, 2);
  }

  @Test
  void shouldSaveMatchRecommendations() {
    var analysisDataset = createAnalysisWithDataset(datasetService, analysisService, alertService);
    var savedAnalysis = analysisDataset.getAnalysis();
    var analysisId = ResourceName.create(savedAnalysis.getName()).getLong("analysis");

    assertGeneratedMatchRecommendation(analysisId, 2);
  }

  @Test
  void shouldSolveOneAlert() {
    solveAlert();
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

  @Test
  void shouldRemoveExpiredAlerts() {
    long alertId = solveAlert();
    alertFacade.deleteAlerts(List.of(alertId));

    assertThat(
        jdbcTemplate.queryForObject(
            "SELECT count(*) FROM ae_alert WHERE alert_id = " + alertId,
            Integer.class)).isEqualTo(0);
  }

  @Test
  void shouldRemovePiiAlerts() {
    long alertId = solveAlert();
    piiFacade.removePii(List.of("alerts/" + alertId));

    assertThat(
        jdbcTemplate.queryForObject(
            "SELECT match_contexts FROM ae_recommendation WHERE alert_id = " + alertId,
            String.class)).isEqualTo("[]");

    assertThat(
        jdbcTemplate.queryForObject(
            "SELECT value FROM ae_alert_comment_input WHERE alert_id = " + alertId,
            String.class)).isEqualTo("{}");

    assertThat(
        jdbcTemplate.queryForObject(
            "SELECT comment FROM ae_recommendation WHERE alert_id = " + alertId,
            String.class)).isEqualTo("");
  }

  private long solveAlert() {
    var analysis =
        createAnalysisWithDataset(datasetService, analysisService, alertService).getAnalysis();
    var alert = createAlert(alertService, "alert3");
    createMatch(alertService, alert.getName(), "match3");
    addAlert(analysisService, analysis.getName(), alert.getName());

    assertThat(getAnalysis(analysisService, analysis.getName()).getAlertCount()).isEqualTo(3);

    var analysisId = ResourceName.create(analysis.getName()).getLong("analysis");
    assertSolvedAlerts(analysisId, 3);

    assertGeneratedRecommendation(analysisId, 3);

    return ResourceName.create(alert.getName()).getLong("alerts");
  }

  @Test
  @Sql(scripts = "populate_analysis_items.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "truncate_analysis_items.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  void deleteItemsConnectedWithAnalysis() {
    analysisCancelledUseCase.cancelAnalysis(1001L);

    assertThat(
        agentExchangeDataAccess
            .selectAgentExchangeMatchFeatureIdsByAlertIds(
                List.of(10001L, 10002L, 10003L)).size())
        .isEqualTo(0);
  }

  private void assertSolvedAlerts(long analysisId, int solvedCount) {
    await()
        .atMost(Duration.ofSeconds(SOLVING_AWAIT_TIME))
        .until(() -> solvedMatchesCount(jdbcTemplate, analysisId) >= solvedCount);

    assertThat(solvedMatchesCount(jdbcTemplate, analysisId))
        .isEqualTo(solvedCount);
  }

  private void assertGeneratedRecommendation(long analysisId, int recommendationCount) {
    await()
        .atMost(Duration.ofSeconds(SOLVING_AWAIT_TIME))
        .until(() -> generatedRecommendationCount(jdbcTemplate, analysisId) >= recommendationCount);

    assertThat(generatedRecommendationCount(jdbcTemplate, analysisId))
        .isEqualTo(recommendationCount);
  }

  private void assertGeneratedMatchRecommendation(long analysisId, int recommendationCount) {
    await()
        .atMost(Duration.ofSeconds(SOLVING_AWAIT_TIME))
        .until(() -> generatedMatchRecommendationCount(jdbcTemplate, analysisId) > 0);

    assertThat(generatedMatchRecommendationCount(jdbcTemplate, analysisId))
        .isEqualTo(recommendationCount);
  }

  private void assertGeneratedRecommendation(String analysisName) {
    var analysisId = ResourceName.create(analysisName).getLong("analysis");
    await()
        .atMost(Duration.ofSeconds(SOLVING_AWAIT_TIME))
        .until(() -> generatedRecommendationCount(
            jdbcTemplate,
            analysisId) > 0);

    var recommendations = analysisService.streamRecommendations(
        StreamRecommendationsRequest.newBuilder().setAnalysis(analysisName).build());
    var recommendation = recommendations.next();

    assertThat(recommendation.getRecommendationComment())
        .contains("NOTE: This is the default alert comment template!");
    assertThat(
        ResourceName.create(recommendation.getName()).getLong("analysis")).isEqualTo(analysisId);
    assertThat(recommendation.getRecommendedAction()).isEqualTo("Manual Investigation");
  }
}
