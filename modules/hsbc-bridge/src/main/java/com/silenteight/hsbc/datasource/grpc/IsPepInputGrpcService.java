package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v1.*;
import com.silenteight.datasource.api.ispep.v1.IsPepInputServiceGrpc.IsPepInputServiceImplBase;
import com.silenteight.hsbc.datasource.dto.ispep.*;
import com.silenteight.hsbc.datasource.provider.IsPepInputProvider;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class IsPepInputGrpcService extends IsPepInputServiceImplBase {

  private final IsPepInputProvider inputProvider;

  @Override
  public void batchGetMatchIsPepInputs(
      BatchGetMatchIsPepInputsRequest request,
      StreamObserver<BatchGetMatchIsPepInputsResponse> responseObserver) {

    var inputRequest = IsPepInputRequest.builder()
        .matches(request.getMatchesList())
        .regionModelFields(map(request.getModelConfigurationList()))
        .build();

    responseObserver.onNext(prepareInputs(inputRequest));
    responseObserver.onCompleted();
  }

  private List<RegionModelFieldDto> map(List<RegionModelFields> modelConfigurations) {
    return modelConfigurations.stream()
        .map(c -> RegionModelFieldDto.builder()
            .region(c.getRegion())
            .requiredFields(c.getRequiredFieldsList())
            .build())
        .collect(Collectors.toList());
  }

  private BatchGetMatchIsPepInputsResponse prepareInputs(IsPepInputRequest request) {
    var isPepInputs = inputProvider.provideInput(request);

    return BatchGetMatchIsPepInputsResponse.newBuilder()
        .addAllIsPepInputs(mapInputs(isPepInputs.getInputs()))
        .build();
  }

  private List<IsPepInput> mapInputs(List<IsPepInputDto> inputs) {
    return inputs.stream()
        .map(i -> IsPepInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllIsPepFeatureInputs(mapFeatureInput(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<IsPepFeatureInput> mapFeatureInput(List<IsPepFeatureInputDto> featureInputs) {
    return featureInputs.stream()
        .map(i -> IsPepFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllModelFieldValues(mapModelFieldValues(i.getModelFieldValues()))
            .build())
        .collect(Collectors.toList());
  }

  private List<ModelFieldValue> mapModelFieldValues(List<ModelFieldValueDto> modelFieldValues) {
    return modelFieldValues.stream()
        .map(i -> ModelFieldValue.newBuilder()
            .setFieldName(i.getFieldName())
            .setValue(i.getValue())
            .build())
        .collect(Collectors.toList());
  }
}
