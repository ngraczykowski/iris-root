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

import static com.silenteight.hsbc.datasource.grpc.StructHelper.buildListValues;
import static com.silenteight.hsbc.datasource.grpc.StructHelper.buildNumberValue;
import static com.silenteight.hsbc.datasource.grpc.StructHelper.buildStringValue;
import static java.util.stream.Collectors.toList;

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
        .collect(toList());
  }

  private List<Feature> mapFeatureInputs(List<IsPepFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> Feature.newBuilder()
            .setFeature(i.getFeature())
            .addAllFeatureSolutions(mapFeatureSolutionInputs(i.getFeatureSolutions()))
            .build())
        .collect(toList());
  }

  private List<FeatureSolution> mapFeatureSolutionInputs(List<IsPepFeatureSolutionDto> inputs) {
    return inputs.stream()
        .map(i -> FeatureSolution.newBuilder()
            .setSolution(i.getSolution())
            .setReason(mapToStruct(i.getReason()))
            .build())
        .collect(toList());
  }

  private Struct mapToStruct(ReasonDto reason) {
    var builder = Struct.newBuilder()
        .putFields("message", buildStringValue(reason.getMessage()))
        .putFields("noPepPositions", buildListValues(reason.getNoPepPositions()))
        .putFields("notMatchedPositions", buildListValues(reason.getNotMatchedPositions()))
        .putFields("pepPositions", buildListValues(reason.getPepPositions()))
        .putFields("linkedPepsUids", buildListValues(reason.getLinkedPepsUids()))
        .putFields("numberOfNotPepDecisions", buildNumberValue(reason.getNumberOfNotPepDecisions()))
        .putFields("numberOfPepDecisions", buildNumberValue(reason.getNumberOfPepDecisions()))
        .putFields("version", buildStringValue(reason.getVersion()))
        .putFields("regionName", buildStringValue(reason.getRegionName()));
    return builder.build();
  }
}
