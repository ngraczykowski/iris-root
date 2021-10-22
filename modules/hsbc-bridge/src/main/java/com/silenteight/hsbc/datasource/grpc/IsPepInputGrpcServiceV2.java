package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;
import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsResponse;
import com.silenteight.datasource.api.ispep.v2.IsPepInput;
import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceImplBase;
import com.silenteight.datasource.api.ispep.v2.WatchlistItem;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputDto;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputRequest;
import com.silenteight.hsbc.datasource.dto.ispep.WatchListItemDto;
import com.silenteight.hsbc.datasource.provider.IsPepInputProviderV2;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class IsPepInputGrpcServiceV2 extends IsPepInputServiceImplBase {

  private final IsPepInputProviderV2 inputProvider;

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
            .setFeature(input.getFeature())
            .setMatch(input.getMatch())
            .setWatchlistItem(mapWatchlistItem(input.getWatchListItem()))
            .build())
        .collect(toList());
  }

  private WatchlistItem mapWatchlistItem(WatchListItemDto watchListItem) {
    return WatchlistItem.newBuilder()
        .setId(watchListItem.getId())
        .setType(watchListItem.getType())
        .setFurtherInformation(watchListItem.getFurtherInformation())
        .addAllCountries(watchListItem.getCountries())
        .addAllLinkedPepsUids(watchListItem.getLinkedPepsUids())
        .build();
  }
}
