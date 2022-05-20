package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import java.util.List;

public class IntegrationTestFixture {

  static Analysis createAnalysis(
      AnalysisServiceBlockingStub analysisService, Analysis analysisFixture) {
    return analysisService.createAnalysis(
        CreateAnalysisRequest.newBuilder()
            .setAnalysis(analysisFixture)

            .build());
  }

  static Alert createAlert(AlertServiceBlockingStub alertService, String alertId) {
    return alertService.createAlert(
        CreateAlertRequest.newBuilder()
            .setAlert(Alert.newBuilder().setAlertId(alertId).build())
            .build());
  }

  static Match createMatch(
      AlertServiceBlockingStub alertService, String alertName, String matchId) {
    return alertService.createMatch(
        CreateMatchRequest.newBuilder()
            .setAlert(alertName)
            .setMatch(Match.newBuilder()
                .setMatchId(matchId)
                .build())
            .build());
  }

  static Dataset createDataset(DatasetServiceBlockingStub datasetService, List<String> alerts) {
    return datasetService.createDataset(
        CreateDatasetRequest.newBuilder()
            .setNamedAlerts(NamedAlerts.newBuilder().addAllAlerts(alerts).build())
            .build());
  }

  static com.silenteight.adjudication.api.v1.AnalysisDataset addDataset(
      AnalysisServiceBlockingStub analysisService, String analysisName, String datasetName) {
    return analysisService.addDataset(
        AddDatasetRequest.newBuilder()
            .setAnalysis(analysisName)
            .setDataset(datasetName)
            .build());
  }

  static AnalysisAlert addAlert(
      AnalysisServiceBlockingStub analysisService, String analysisName, String alertName) {
    return analysisService.addAlert(
        AddAlertRequest.newBuilder().setAnalysis(analysisName).setAnalysisAlert(
            AnalysisAlert.newBuilder().setAlert(alertName).buildPartial()).build());
  }

  static Analysis getAnalysis(AnalysisServiceBlockingStub analysisService, String analysisName) {
    return analysisService.getAnalysis(
        GetAnalysisRequest.newBuilder()
            .setAnalysis(analysisName)
            .build());
  }

  static Dataset getDataset(DatasetServiceBlockingStub datasetService, String datasetName) {
    return datasetService.getDataset(
        GetDatasetRequest.newBuilder().setName(datasetName).build());
  }

  static Analysis createAnalysisFixture() {
    return Analysis.newBuilder()
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
        .setNotificationFlags(Analysis.NotificationFlags
            .newBuilder()
            .setAttachRecommendation(true)
            .setAttachMetadata(true)
            .build())
        .build();
  }

  static AnalysisDataset createAnalysisWithDataset(
      DatasetServiceBlockingStub datasetService, AnalysisServiceBlockingStub analysisService,
      AlertServiceBlockingStub alertService) {
    var alert = createAlert(alertService, "alert1");
    var alert2 = createAlert(alertService, "alert2");
    createMatch(alertService, alert.getName(), "match1");
    createMatch(alertService, alert2.getName(), "match2");
    var dataset = createDataset(datasetService, List.of(alert.getName(), alert2.getName()));

    var analysisFixture = createAnalysisFixture();

    var analysis = createAnalysis(analysisService, analysisFixture);
    addDataset(analysisService, analysis.getName(), dataset.getName());

    var savedAnalysis = getAnalysis(analysisService, analysis.getName());
    var savedDataset = getDataset(datasetService, dataset.getName());

    return AnalysisDataset.builder().analysis(savedAnalysis).dataset(savedDataset).build();
  }
}
