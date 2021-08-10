package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;
import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsResponse;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.ModelType;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalInputResponse;
import com.silenteight.hsbc.datasource.dto.historical.HistoricalSolutionInputDto;

import com.google.protobuf.Struct;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
public class HistoricalDecisionsGrpcService extends HistoricalDecisionsInputServiceImplBase {

  private final DataSourceInputProvider<HistoricalInputResponse> inputProvider;

  @Override
  public void batchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsRequest request,
      StreamObserver<BatchGetMatchHistoricalDecisionsInputsResponse> responseObserver) {
    var inputRequest = DataSourceInputRequest.builder()
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    responseObserver.onNext(provideInputResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchHistoricalDecisionsInputsResponse provideInputResponse(
      DataSourceInputRequest request) {
    var input = inputProvider.provideInput(request);

    return BatchGetMatchHistoricalDecisionsInputsResponse.newBuilder()
        .addAllHistoricalDecisionsInputs(mapInputs(input.getInputs()))
        .build();
  }

  private List<HistoricalDecisionsInput> mapInputs(
      List<HistoricalSolutionInputDto> inputs) {
    return inputs.stream()
        .map(input -> HistoricalDecisionsInput.newBuilder()
            .setMatch(input.getMatch())
            .addAllFeatureInputs(mapFeatures(input.getFeatures()))
            .build())
        .collect(toList());
  }

  private List<HistoricalDecisionsFeatureInput> mapFeatures(
      List<HistoricalFeatureInputDto> inputs) {
    return inputs.stream()
        .map(input -> HistoricalDecisionsFeatureInput.newBuilder()
            .setFeature(input.getFeature())
            .setTruePositiveCount(input.getTruePositiveCount())
            .setModelType(ModelType.valueOf(input.getModelKeyType().name()))
            .setReason(Struct.newBuilder().build())
            .build())
        .collect(toList());
  }
}
