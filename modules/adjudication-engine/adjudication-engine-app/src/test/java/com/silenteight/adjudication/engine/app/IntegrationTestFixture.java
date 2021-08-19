package com.silenteight.adjudication.engine.app;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

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

  static Dataset createDataset(DatasetServiceBlockingStub datasetService, String alerts) {
    return datasetService.createDataset(
        CreateDatasetRequest.newBuilder()
            .setNamedAlerts(NamedAlerts.newBuilder().addAlerts(alerts).build())
            .build());
  }

  static AnalysisDataset addDataset(
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
}
