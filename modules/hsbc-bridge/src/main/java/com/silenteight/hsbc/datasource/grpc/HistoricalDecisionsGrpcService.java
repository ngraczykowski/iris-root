package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.historicaldecisions.v2.*;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.historical.*;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class HistoricalDecisionsGrpcService extends HistoricalDecisionsInputServiceImplBase {

  private final DataSourceInputProvider<HistoricalInputResponse> inputProvider;

  @Override
  public void batchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsRequest request,
      StreamObserver<BatchGetMatchHistoricalDecisionsInputsResponse> responseObserver) {
    var inputRequest = DataSourceInputRequest.builder()
        .matches(request.getMatchesList())
        .features(request.getFeaturesList())
        .build();

    responseObserver.onNext(provideInputResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchHistoricalDecisionsInputsResponse provideInputResponse(
      DataSourceInputRequest request) {
    var input = inputProvider.provideInput(request);

    return BatchGetMatchHistoricalDecisionsInputsResponse.newBuilder()
        .addAllHistoricalDecisionsInputs(mapInputs(input.getInputs()))
        .build();
  }

  private List<HistoricalDecisionsInput> mapInputs(List<HistoricalDecisionsInputDto> inputs) {
    return inputs.stream()
        .map(input -> HistoricalDecisionsInput.newBuilder()
            .setMatch(input.getMatch())
            .addAllHistoricalDecisionsFeatureInput(mapFeatures(input.getFeatures()))
            .build())
        .collect(Collectors.toList());
  }

  private List<HistoricalDecisionsFeatureInput> mapFeatures(
      List<HistoricalDecisionsFeatureInputDto> features) {
    return features.stream()
        .map(feature -> HistoricalDecisionsFeatureInput.newBuilder()
            .setFeature(feature.getFeature())
            .setModelKey(mapToModelKey(feature.getModelKey()))
            .setDiscriminator(mapToDiscriminator(feature.getDiscriminator()))
            .build())
        .collect(Collectors.toList());
  }

  private ModelKey mapToModelKey(ModelKeyDto modelKey) {
    var type = modelKey.getModelKeyType();
    switch (type) {
      case ALERTED_PARTY:
        return ModelKey.newBuilder()
            .setAlertedParty(mapToAlertedParty((AlertedPartyDto) modelKey.getModelKeyValue()))
            .build();
      case WATCHLIST_PARTY:
        return ModelKey.newBuilder()
            .setWatchlistParty(mapToWatchlistParty((WatchlistPartyDto) modelKey.getModelKeyValue()))
            .build();
      case MATCH:
        return ModelKey.newBuilder()
            .setMatch(mapToAMatch((MatchDto) modelKey.getModelKeyValue()))
            .build();
      default:
        return ModelKey.newBuilder().build();
    }
  }

  private WatchlistParty mapToWatchlistParty(WatchlistPartyDto watchlistParty) {
    return WatchlistParty.newBuilder()
        .setId(watchlistParty.getId())
        .setType(watchlistParty.getType())
        .build();
  }

  private AlertedParty mapToAlertedParty(AlertedPartyDto alertedParty) {
    return AlertedParty.newBuilder()
        .setId(alertedParty.getId())
        .build();
  }

  private Match mapToAMatch(MatchDto match) {
    return Match.newBuilder()
        .setAlertedParty(mapToAlertedParty(match.getAlertedParty()))
        .setWatchlistParty(mapToWatchlistParty(match.getWatchlistParty()))
        .build();
  }

  private Discriminator mapToDiscriminator(String discriminator) {
    return Discriminator.newBuilder().setValue(discriminator).build();
  }
}
