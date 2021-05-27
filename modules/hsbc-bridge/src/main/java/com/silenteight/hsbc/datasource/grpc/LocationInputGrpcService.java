package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;
import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsResponse;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.datasource.api.location.v1.LocationInput;
import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.location.LocationInputDto;
import com.silenteight.hsbc.datasource.dto.location.LocationInputResponse;
import com.silenteight.hsbc.datasource.provider.FeatureNotAllowedException;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

import static io.grpc.Status.INVALID_ARGUMENT;

@GRpcService
@RequiredArgsConstructor
class LocationInputGrpcService extends LocationInputServiceImplBase {

  private final DataSourceInputProvider<LocationInputResponse> locationInputProvider;

  @Override
  public void batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsRequest request,
      StreamObserver<BatchGetMatchLocationInputsResponse> responseObserver) {

    try {
      responseObserver.onNext(provideInput(
          DataSourceInputRequest.builder()
              .features(request.getFeaturesList())
              .matches(request.getMatchesList())
              .build()
      ));
      responseObserver.onCompleted();
    } catch (
        FeatureNotAllowedException e) {
      responseObserver.onError(
          new StatusRuntimeException(INVALID_ARGUMENT.withDescription(e.getMessage())));
    }
  }

  private BatchGetMatchLocationInputsResponse provideInput(DataSourceInputRequest request) {
    var input = locationInputProvider.provideInput(request);

    return BatchGetMatchLocationInputsResponse.newBuilder()
        .addAllLocationInputs(mapLocationInputs(input.getInputs()))
        .build();
  }

  private List<LocationInput> mapLocationInputs(List<LocationInputDto> inputs) {
    return inputs.stream()
        .map(i -> LocationInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllLcoationFeatureInputs(mapFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<LocationFeatureInput> mapFeatureInputs(List<LocationFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> LocationFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyLocations(i.getAlertedPartyLocations())
            .addAllWatchlistLocations(i.getWatchlistLocations())
            .build())
        .collect(Collectors.toList());
  }
}
