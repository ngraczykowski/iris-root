package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;
import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsResponse;
import com.silenteight.datasource.api.gender.v1.GenderFeatureInput;
import com.silenteight.datasource.api.gender.v1.GenderInput;
import com.silenteight.datasource.api.gender.v1.GenderInputServiceGrpc.GenderInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.gender.GenderFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputDto;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class GenderInputGrpcService extends GenderInputServiceImplBase {

  private final DataSourceInputProvider<GenderInputResponse> genderInputProvider;

  @Override
  public void batchGetMatchGenderInputs(
      BatchGetMatchGenderInputsRequest request,
      StreamObserver<BatchGetMatchGenderInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(prepareInput(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchGenderInputsResponse prepareInput(DataSourceInputRequest request) {
    var genderInputs = genderInputProvider.provideInput(request);

    return BatchGetMatchGenderInputsResponse.newBuilder()
        .addAllGenderInputs(mapGenderInputs(genderInputs.getInputs()))
        .build();
  }

  private List<GenderInput> mapGenderInputs(List<GenderInputDto> inputs) {
    return inputs.stream()
        .map(i -> GenderInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllGenderFeatureInputs(mapFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<GenderFeatureInput> mapFeatureInputs(List<GenderFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> GenderFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyGenders(i.getAlertedPartyGenders())
            .addAllWatchlistGenders(i.getWatchlistGenders())
            .build())
        .collect(Collectors.toList());
  }
}
