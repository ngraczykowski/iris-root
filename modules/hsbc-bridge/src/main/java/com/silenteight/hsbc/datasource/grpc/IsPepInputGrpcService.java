package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v2.*;
import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceImplBase;
import com.silenteight.hsbc.datasource.dto.ispep.*;
import com.silenteight.hsbc.datasource.provider.IsPepInputProvider;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class IsPepInputGrpcService extends IsPepInputServiceImplBase {

  private final IsPepInputProvider inputProvider;

  @Override
  public void batchGetMatchIsPepInputs(
      BatchGetMatchIsPepInputsRequest request,
      StreamObserver<BatchGetMatchIsPepInputsResponse> responseObserver) {

    var inputRequest = IsPepInputRequest.builder()
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    responseObserver.onNext(prepareInputs(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchIsPepInputsResponse prepareInputs(IsPepInputRequest request) {
    var isPepInputs = inputProvider.provideInput(request);

    return BatchGetMatchIsPepInputsResponse.newBuilder()
        .addAllIsPepInputs(mapPepInputs(isPepInputs))
        .build();
  }

  private List<IsPepInput> mapPepInputs(List<IsPepInputDto> isPepInputs) {
    return isPepInputs.stream()
        .map(input -> IsPepInput.newBuilder()
            .setMatch(input.getMatch())
            .setIsPepFeatureInput(mapIsPepFeatureInput(input.getIsPepFeatureInput()))
            .build())
        .collect(Collectors.toList());
  }

  private IsPepFeatureInput mapIsPepFeatureInput(IsPepFeatureInputDto isPepFeatureInput) {
    return IsPepFeatureInput.newBuilder()
        .setFeature(isPepFeatureInput.getFeature())
        .setWatchlistItem(mapWatchlistItem(isPepFeatureInput.getWatchListItem()))
        .setAlertedPartyItem(mapAlertedPartyItem(isPepFeatureInput.getAlertedPartyItem()))
        .build();
  }

  private WatchlistItem mapWatchlistItem(WatchListItemDto watchListItem) {
    return WatchlistItem.newBuilder()
        .setId(watchListItem.getId())
        .setType(watchListItem.getType())
        .addAllCountries(watchListItem.getCountries())
        .setFurtherInformation(watchListItem.getFurtherInformation())
        .addAllLinkedPepsUids(watchListItem.getLinkedPepsUids())
        .build();
  }

  private AlertedPartyItem mapAlertedPartyItem(AlertedPartyItemDto alertedPartyItem) {
    return AlertedPartyItem.newBuilder()
        .setCountry(alertedPartyItem.getCountry())
        .build();
  }
}
