package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.extractors.historical.*;
import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;
import com.silenteight.proto.historicaldecisions.model.v1.api.*;
import com.silenteight.proto.historicaldecisions.model.v1.api.HistoricalDecisionsModelServiceGrpc.HistoricalDecisionsModelServiceBlockingStub;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class HistoricalDecisionsGrpcAdapter implements HistoricalDecisionsServiceClient {

  private final HistoricalDecisionsModelServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public GetHistoricalDecisionsResponseDto getHistoricalDecisions(
      GetHistoricalDecisionsRequestDto request) {
    var grpcRequest = BatchGetHistoricalDecisionsModelCountsRequest.newBuilder()
        .addAllModelKeys(requestMapToModelKeys(request.getModelKeys()))
        .build();

    log.debug("Datasource sending HistoricalDecisions grpc request: {}.", grpcRequest);

    var response = getBlockingStub().batchGetHistoricalDecisionsModelCounts(grpcRequest);

    log.debug(
        "Datasource received HistoricalDecisions grpc response: modelCountsList={}.", response.getModelCountsList());

    return GetHistoricalDecisionsResponseDto.builder()
        .modelCounts(mapToModelCountsDto(response.getModelCountsList()))
        .build();
  }

  private HistoricalDecisionsModelServiceBlockingStub getBlockingStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }

  private List<ModelKey> requestMapToModelKeys(List<ModelKeyDto> modelKeys) {
    return modelKeys.stream()
        .map(this::mapToModelKey)
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
        return ModelKey.newBuilder()
            .build();
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

  private List<ModelCountsDto> mapToModelCountsDto(List<ModelCounts> modelCounts) {
    return modelCounts.stream()
        .map(modelCount -> ModelCountsDto.builder()
            .modelKey(mapToModelKeyDto(modelCount.getModelKey()))
            .truePositivesCount(modelCount.getTruePositivesCount())
            .build())
        .collect(Collectors.toList());
  }

  private ModelKeyDto mapToModelKeyDto(ModelKey modelKey) {
    var typeCase = modelKey.getTypeCase();
    switch (typeCase) {
      case ALERTED_PARTY:
        return ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.ALERTED_PARTY)
            .modelKeyValue(new AlertedPartyDto(modelKey.getAlertedParty().getId()))
            .build();
      case WATCHLIST_PARTY:
        return ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.WATCHLIST_PARTY)
            .modelKeyValue(mapToWatchlistPartyDto(modelKey.getWatchlistParty()))
            .build();
      case MATCH:
        return ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.MATCH)
            .modelKeyValue(mapToAMatchDto(modelKey.getMatch()))
            .build();
      default: {
        return ModelKeyDto.builder()
            .build();
      }
    }
  }

  private WatchlistPartyDto mapToWatchlistPartyDto(WatchlistParty watchlistParty) {
    return WatchlistPartyDto.builder()
        .id(watchlistParty.getId())
        .type(watchlistParty.getType())
        .build();
  }

  private MatchDto mapToAMatchDto(Match match) {
    return MatchDto.builder()
        .alertedParty(new AlertedPartyDto(match.getAlertedParty().getId()))
        .watchlistParty(mapToWatchlistPartyDto(match.getWatchlistParty()))
        .build();
  }
}
