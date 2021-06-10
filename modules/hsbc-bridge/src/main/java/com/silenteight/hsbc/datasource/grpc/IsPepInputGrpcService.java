package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsRequest;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse.Feature;
import com.silenteight.datasource.api.ispep.v1.BatchGetMatchIsPepSolutionsResponse.FeatureSolution;
import com.silenteight.datasource.api.ispep.v1.IsPepInputServiceGrpc.IsPepInputServiceImplBase;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepFeatureSolutionDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputRequest;
import com.silenteight.hsbc.datasource.provider.IsPepInputProvider;

import com.google.protobuf.Struct;
import com.google.protobuf.util.Values;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class IsPepInputGrpcService extends IsPepInputServiceImplBase {

  private final IsPepInputProvider inputProvider;

  @Override
  public void batchGetMatchIsPepSolutions(
      BatchGetMatchIsPepSolutionsRequest request,
      StreamObserver<BatchGetMatchIsPepSolutionsResponse> responseObserver) {

    var inputRequest = IsPepInputRequest.builder()
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    prepareInputs(inputRequest).forEach(responseObserver::onNext);
    responseObserver.onCompleted();
  }

  private List<BatchGetMatchIsPepSolutionsResponse> prepareInputs(IsPepInputRequest request) {
    return inputProvider.provideInput(request).stream()
        .map(input -> BatchGetMatchIsPepSolutionsResponse.newBuilder()
            .setMatch(input.getMatch())
            .addAllFeatures(mapFeatureInputs(input.getFeatureInputs()))
            .build()
        )
        .collect(Collectors.toList());
  }

  private List<Feature> mapFeatureInputs(List<IsPepFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> Feature.newBuilder()
            .setFeature(i.getFeature())
            .addAllFeatureSolutions(mapFeatureSolutionInputs(i.getFeatureSolutions()))
            .build())
        .collect(Collectors.toList());
  }

  private List<FeatureSolution> mapFeatureSolutionInputs(List<IsPepFeatureSolutionDto> inputs) {
    return inputs.stream()
        .map(i -> FeatureSolution.newBuilder()
            .setSolution(i.getSolution())
            .setReason(mapToStruct(i.getReason()))
            .build())
        .collect(Collectors.toList());
  }

  private Struct mapToStruct(Map<String, String> reason) {
    var builder = Struct.newBuilder();
    reason.forEach((key, val) -> builder.putFields(key, Values.of(val)));
    return builder.build();
  }
}
