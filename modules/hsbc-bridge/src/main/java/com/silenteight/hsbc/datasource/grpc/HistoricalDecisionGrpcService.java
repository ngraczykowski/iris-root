package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsSolutionsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsSolutionsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsSolution;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsSolution.Feature;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsSolution.FeatureSolution;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.historical.*;

import com.google.protobuf.Struct;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;

import static com.silenteight.hsbc.datasource.grpc.StructHelper.buildStringValue;
import static java.util.stream.Collectors.toList;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
public class HistoricalDecisionGrpcService extends HistoricalDecisionsInputServiceImplBase {

  private final DataSourceInputProvider<HistoricalInputResponse> inputProvider;

  @Override
  public void batchGetMatchHistoricalDecisionsSolutions(
      BatchGetMatchHistoricalDecisionsSolutionsRequest request,
      StreamObserver<BatchGetMatchHistoricalDecisionsSolutionsResponse> responseObserver) {
    var inputRequest = DataSourceInputRequest.builder()
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    responseObserver.onNext(provideInputResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchHistoricalDecisionsSolutionsResponse provideInputResponse(
      DataSourceInputRequest request) {
    var input = inputProvider.provideInput(request);

    return BatchGetMatchHistoricalDecisionsSolutionsResponse.newBuilder()
        .addAllHistoricalDecisionsSolutions(mapSolutionInputs(input.getSolutions()))
        .build();
  }

  private List<HistoricalDecisionsSolution> mapSolutionInputs(
      List<HistoricalSolutionInputDto> inputs) {
    return inputs.stream()
        .map(input -> HistoricalDecisionsSolution.newBuilder()
            .setMatch(input.getMatch())
            .addAllFeatures(mapFeatures(input.getFeatures()))
            .build())
        .collect(toList());
  }

  private List<Feature> mapFeatures(
      List<HistoricalFeatureInputDto> inputs) {
    return inputs.stream()
        .map(input -> Feature.newBuilder()
            .setFeature(input.getFeature())
            .addAllFeatureSolutions(mapFeatureSolutions(input.getFeatureSolutions()))
            .build())
        .collect(toList());
  }

  private List<FeatureSolution> mapFeatureSolutions(
      List<HistoricalFeatureSolutionInputDto> inputs) {
    return inputs.stream()
        .map(input -> FeatureSolution.newBuilder()
            .setSolution(input.getSolution())
            .setReason(mapToStruct(input.getReason()))
            .build())
        .collect(toList());
  }

  private Struct mapToStruct(Map<String, String> reason) {
    var builder = Struct.newBuilder();
    reason.entrySet().stream()
        .forEach(r -> builder.putFields(r.getKey(), buildStringValue(r.getValue())));
    return builder.build();
  }
}
