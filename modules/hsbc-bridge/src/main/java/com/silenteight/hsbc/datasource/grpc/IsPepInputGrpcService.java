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
import com.silenteight.hsbc.datasource.dto.ispep.ReasonDto;
import com.silenteight.hsbc.datasource.provider.IsPepInputProvider;

import com.google.protobuf.Struct;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
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

  private Struct mapToStruct(ReasonDto reason) {
    var builder = Struct.newBuilder()
        .putFields("message", StructHelper.buildStringValue(reason.getMessage()))
        .putFields("noPepPositions", StructHelper.buildListValues(reason.getNoPepPositions()))
        .putFields("notMatchedPositions", StructHelper.buildListValues(reason.getNotMatchedPositions()))
        .putFields("pepPositions", StructHelper.buildListValues(reason.getPepPositions()))
        .putFields("linkedPepsUids", StructHelper.buildListValues(reason.getLinkedPepsUids()))
        .putFields("numberOfNotPepDecisions", StructHelper.buildNumberValue(reason.getNumberOfNotPepDecisions()))
        .putFields("numberOfPepDecisions", StructHelper.buildNumberValue(reason.getNumberOfPepDecisions()))
        .putFields("version", StructHelper.buildStringValue(reason.getVersion()))
        .putFields("regionName", StructHelper.buildStringValue(reason.getRegionName()));
    return builder.build();
  }
}
