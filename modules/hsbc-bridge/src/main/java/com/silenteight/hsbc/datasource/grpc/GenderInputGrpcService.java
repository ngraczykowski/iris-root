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
import com.silenteight.hsbc.datasource.provider.FeatureNotAllowedException;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

import static io.grpc.Status.INVALID_ARGUMENT;

@GRpcService
@RequiredArgsConstructor
class GenderInputGrpcService extends GenderInputServiceImplBase {

  private final DataSourceInputProvider<GenderInputResponse> genderInputProvider;

  @Override
  public void batchGetMatchGenderInputs(
      BatchGetMatchGenderInputsRequest request,
      StreamObserver<BatchGetMatchGenderInputsResponse> responseObserver) {

    try {
      responseObserver.onNext(prepareInput(DataSourceInputRequest.builder()
          .features(request.getFeaturesList())
          .matches(request.getMatchesList())
          .build()));
      responseObserver.onCompleted();
    } catch (
        FeatureNotAllowedException e) {
      responseObserver.onError(
          new StatusRuntimeException(INVALID_ARGUMENT.withDescription(e.getMessage())));
    }
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
