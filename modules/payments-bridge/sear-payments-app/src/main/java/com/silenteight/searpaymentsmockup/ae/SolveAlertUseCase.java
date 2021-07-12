package com.silenteight.searpaymentsmockup.ae;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;
import com.silenteight.adjudication.api.v2.StreamRecommendationsRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
class SolveAlertUseCase {

  private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 24801)
      .usePlaintext()
      .build();

  private AlertServiceGrpc.AlertServiceBlockingStub alertStub
      = AlertServiceGrpc.newBlockingStub(channel);

  private DatasetServiceGrpc.DatasetServiceBlockingStub datasetStub =
      DatasetServiceGrpc.newBlockingStub(channel);

  private AnalysisServiceGrpc.AnalysisServiceBlockingStub analysisStub =
      AnalysisServiceGrpc.newBlockingStub(channel);

  private RecommendationServiceGrpc.RecommendationServiceBlockingStub recommendationStub =
      RecommendationServiceGrpc.newBlockingStub(channel);


  void solveAlert(long alertId) {
    var alertResponse = alertStub.batchCreateAlerts(BatchCreateAlertsRequest
        .newBuilder()
        .addAlerts(
            Alert.newBuilder().setAlertId(String.valueOf(alertId)).setName("alert/"+alertId).build())
        .build());

    var matches = List.of(
        Match.newBuilder().setMatchId(String.valueOf(1)).setName("Match").build(),
        Match.newBuilder().setMatchId(String.valueOf(2)).setName("Match2").build());
    var matchesResponse =
        alertStub.batchCreateMatches(BatchCreateMatchesRequest.newBuilder().addAllAlertMatches(
            List.of(
                BatchCreateAlertMatchesRequest
                    .newBuilder()
                    .setAlert(alertResponse.getAlerts(0).getName())
                    .addAllMatches(matches)
                    .build())).build());

    var datasetResponse = datasetStub.createDataset(CreateDatasetRequest.newBuilder().setNamedAlerts(
        NamedAlerts
            .newBuilder()
            .addAlerts(alertResponse.getAlerts(0).getName())
            .build()).build());

    var analysisResponse = analysisStub.createAnalysis(CreateAnalysisRequest
        .newBuilder()
        .setAnalysis(Analysis.newBuilder().setName("analysis/1").build())
        .build());
    var addDataSetResponse = analysisStub.addDataset(
        AddDatasetRequest.newBuilder().setAnalysis(analysisResponse.getName()).setDataset(datasetResponse.getName()).build());
  }
}
