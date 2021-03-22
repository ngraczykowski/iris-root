package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.*;
import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceImplBase;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.name.*;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
@RequiredArgsConstructor
class NameInputGrpcService extends NameInputServiceImplBase {

  private final DataSourceInputProvider<NameInputResponse> nameInputProvider;

  @Override
  public void batchGetMatchNameInputs(
      BatchGetMatchNameInputsRequest request,
      StreamObserver<BatchGetMatchNameInputsResponse> responseObserver) {
    responseObserver.onNext(provideInput(DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build()));
    responseObserver.onCompleted();
  }

  private BatchGetMatchNameInputsResponse provideInput(DataSourceInputRequest request) {
    var nameInputs = nameInputProvider.provideInput(request);

    return BatchGetMatchNameInputsResponse.newBuilder()
        .addAllNameInputs(mapNameInputs(nameInputs.getInputs()))
        .build();
  }

  private List<NameInput> mapNameInputs(List<NameInputDto> inputs) {
    return inputs.stream()
        .map(i -> NameInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllNameFeatureInputs(mapNameFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<NameFeatureInput> mapNameFeatureInputs(List<NameFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> NameFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllMatchingTexts(i.getMatchingTexts())
            .addAllAlertedPartyNames(mapAlertedPartyNames(i.getAlertedPartyNames()))
            .addAllWatchlistNames(mapWatchlistNames(i.getWatchlistNames()))
            .build())
        .collect(Collectors.toList());
  }

  private List<WatchlistName> mapWatchlistNames(List<WatchlistNameDto> watchlistNames) {
    return watchlistNames.stream()
        .map(i -> WatchlistName.newBuilder()
            .setName(i.getName())
            .setType(NameType.valueOf(i.getType().name()))
            .build())
        .collect(Collectors.toList());
  }

  private List<AlertedPartyName> mapAlertedPartyNames(List<AlertedPartyNameDto> names) {
    return names.stream()
        .map(i -> AlertedPartyName.newBuilder()
            .setName(i.getName())
            .build())
        .collect(Collectors.toList());
  }
}
