package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;

import io.vavr.control.Try;

import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
@Slf4j
public class AnalysisGrpcAdapter implements AnalysisServiceClient {

  private static final String CANNOT_ADD_ALERTS_TO_ANALYSIS = "Cannot add alerts to analysis ";
  private static final String CANNOT_GET_ANALYSIS = "Cannot get analysis";
  private static final String CANNOT_CREATE_ANALYSIS = "Cannot create analysis";
  private static final String CANNOT_ADD_DATASET = "Cannot add dataset";
  private final AnalysisServiceBlockingStub analysisServiceBlockingStub;
  private final long deadlineInSeconds;

  @Override
  public AnalysisDatasetOut addDataset(AddDatasetIn request) {
    var grpcRequest = AddDatasetRequest.newBuilder()
        .setAnalysis(request.getAnalysis())
        .setDataset(request.getDataset())
        .build();

    return Try.of(() -> getStub().addDataset(grpcRequest))
        .map(AnalysisGrpcMapper::mapToAnalysisDatasetOut)
        .onFailure(e -> log.error(CANNOT_ADD_DATASET, e))
        .onSuccess(result -> log.debug("Dataset was added successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_ADD_DATASET, e));
  }

  @Override
  public CreateAnalysisOut createAnalysis(CreateAnalysisIn request) {
    var grpcRequest = CreateAnalysisRequest.newBuilder()
        .setAnalysis(Analysis.newBuilder()
            .setStrategy(request.getStrategy())
            .setPolicy(request.getPolicy())
            .addAllCategories(request.getCategories())
            .addAllFeatures(AnalysisGrpcMapper.mapFeatures(request.getFeatures()))
            .setNotificationFlags(
                AnalysisGrpcMapper.mapToNotificationFlags(request.getNotificationFlags()))
            .build())
        .build();

    return Try.of(() -> getStub().createAnalysis(grpcRequest))
        .map(AnalysisGrpcMapper::mapToCreateAnalysisOut)
        .onFailure(e -> log.error(CANNOT_CREATE_ANALYSIS, e))
        .onSuccess(result -> log.debug("Analysis was created successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_CREATE_ANALYSIS, e));
  }

  @Override
  public GetAnalysisOut getAnalysis(String analysis) {
    var grpcRequest = GetAnalysisRequest.newBuilder()
        .setAnalysis(analysis)
        .build();

    return Try.of(() -> getStub().getAnalysis(grpcRequest))
        .map(AnalysisGrpcMapper::mapToGetAnalysisOut)
        .onFailure(e -> log.error(CANNOT_GET_ANALYSIS, e))
        .onSuccess(result -> log.debug("Analysis was got successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_GET_ANALYSIS, e));
  }

  @Override
  public AddAlertsToAnalysisOut addAlertsToAnalysis(AddAlertsToAnalysisIn request) {
    var analysisAlerts = request.getAlerts().stream()
        .map(AnalysisGrpcMapper::mapToAnalysisAlert)
        .collect(Collectors.toUnmodifiableList());

    var addAlertsRequest = BatchAddAlertsRequest.newBuilder()
        .setAnalysis(request.getAnalysisName())
        .addAllAnalysisAlerts(analysisAlerts)
        .build();

    return Try.of(() -> getStub().batchAddAlerts(addAlertsRequest))
        .map(AnalysisGrpcMapper::mapToAddAlertsToAnalysisOut)
        .onFailure(e -> log.error(CANNOT_ADD_ALERTS_TO_ANALYSIS + request.getAnalysisName(), e))
        .onSuccess(result -> log.debug("Analysis was added alerts to analysis successfully"))
        .getOrElseThrow(
            e -> new AdjudicationEngineLibraryRuntimeException(CANNOT_ADD_ALERTS_TO_ANALYSIS, e));
  }

  private AnalysisServiceBlockingStub getStub() {
    return analysisServiceBlockingStub.withDeadlineAfter(deadlineInSeconds, SECONDS);
  }
}
