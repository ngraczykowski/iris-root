package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
@ContextConfiguration(initializers = { RabbitTestInitializer.class, PostgresTestInitializer.class })
@SpringBootTest(
    classes = AdjudicationEngineApplication.class,
    properties = "debug=true")
@EnableConfigurationProperties
@ActiveProfiles({ "mockagents", "rabbitdeclare", "mockgovernance", "mockdatasource", "test" })
class AdjudicationEngineAnalysisIntegrationTest {

  @GrpcClient("ae")
  private AnalysisServiceBlockingStub analysisService;

  @GrpcClient("ae")
  private DatasetServiceBlockingStub datasetService;

  @GrpcClient("ae")
  private AlertServiceBlockingStub alertService;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Analysis analysisFixture;

  private Analysis savedAnalysis;
  private Dataset savedDataset;

  @BeforeEach
  public void before() {
    analysisFixture = Analysis.newBuilder()
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

    var analysis = analysisService.createAnalysis(
        CreateAnalysisRequest.newBuilder()
            .setAnalysis(analysisFixture)
            .build());

    var alert = alertService.createAlert(
        CreateAlertRequest.newBuilder()
            .setAlert(Alert.newBuilder().setAlertId("alert1").build())
            .build());

    alertService.createMatch(
        CreateMatchRequest.newBuilder()
            .setAlert(alert.getName())
            .setMatch(Match.newBuilder()
                .setMatchId("match1")
                .build())
            .build());

    var dataset = datasetService.createDataset(
        CreateDatasetRequest.newBuilder()
            .setNamedAlerts(NamedAlerts.newBuilder().addAlerts(alert.getName()).build())
            .build());

    analysisService.addDataset(
        AddDatasetRequest.newBuilder()
            .setAnalysis(analysis.getName())
            .setDataset(dataset.getName())
            .build());

    savedAnalysis = analysisService.getAnalysis(
        GetAnalysisRequest.newBuilder()
            .setAnalysis(analysis.getName())
            .build());

    savedDataset = datasetService.getDataset(
        GetDatasetRequest.newBuilder().setName(dataset.getName()).build());
  }

  @Test
  void shouldSaveOneAlertToDataSet() {
    assertThat(savedDataset.getAlertCount()).isEqualTo(1);
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
    assertThat(savedAnalysis.getAlertCount()).isEqualTo(1);
    assertThat(savedAnalysis.getPendingAlerts()).isEqualTo(1);
  }

  @Test
  void checkAlertsCountInAnalysisWithSameAlertInTwoDatasets() {
    var analysis = analysisService.createAnalysis(
        CreateAnalysisRequest.newBuilder()
            .setAnalysis(analysisFixture)
            .build());

    var alert = alertService.createAlert(
        CreateAlertRequest.newBuilder()
            .setAlert(Alert.newBuilder().setAlertId("alert1").build())
            .build());

    alertService.createMatch(CreateMatchRequest.newBuilder()
        .setAlert(alert.getName())
        .setMatch(Match.newBuilder()
            .setMatchId("match1")
            .build())
        .build());

    var dataset1 = datasetService.createDataset(
        CreateDatasetRequest.newBuilder()
            .setNamedAlerts(NamedAlerts.newBuilder().addAlerts(alert.getName()).build())
            .build());

    var dataset2 = datasetService.createDataset(
        CreateDatasetRequest.newBuilder()
            .setNamedAlerts(NamedAlerts.newBuilder().addAlerts(alert.getName()).build())
            .build());

    analysisService.addDataset(
        AddDatasetRequest.newBuilder()
            .setAnalysis(analysis.getName()).setDataset(dataset1.getName())
            .build());
    analysisService.addDataset(
        AddDatasetRequest.newBuilder()
            .setAnalysis(analysis.getName()).setDataset(dataset2.getName())
            .build());

    var savedAnalysis = analysisService.getAnalysis(
        GetAnalysisRequest.newBuilder()
            .setAnalysis(analysis.getName())
            .build());

    assertThat(savedAnalysis.getAlertCount()).isEqualTo(1);
    assertThat(savedAnalysis.getPendingAlerts()).isEqualTo(1);
  }
}
