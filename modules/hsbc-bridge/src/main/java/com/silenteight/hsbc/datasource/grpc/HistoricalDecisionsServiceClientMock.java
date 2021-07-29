package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.historical.*;
import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.List;

import static com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType.ALERTED_PARTY;
import static com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType.MATCH;
import static com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType.WATCHLIST_PARTY;

public class HistoricalDecisionsServiceClientMock implements HistoricalDecisionsServiceClient {

  private static final int TRUE_POSITIVES_COUNT = 1;
  private static final ModelKeyType TYPE = MATCH;

  @Override
  public GetHistoricalDecisionsResponseDto getHistoricalDecisions(
      GetHistoricalDecisionsRequestDto request) {
    return GetHistoricalDecisionsResponseDto.builder()
        .modelCounts(getModelCounts())
        .build();
  }

  private static List<ModelCountsDto> getModelCounts() {
    return List.of(ModelCountsDto.builder()
        .truePositivesCount(TRUE_POSITIVES_COUNT)
        .modelKey(getModelKey())
        .build());
  }

  private static ModelKeyDto getModelKey() {
    switch (TYPE) {
      case ALERTED_PARTY:
        return ModelKeyDto.builder()
            .modelKeyType(ALERTED_PARTY)
            .modelKeyValue(new AlertedPartyDto("alertedParty_id"))
            .build();
      case WATCHLIST_PARTY:
        return ModelKeyDto.builder()
            .modelKeyType(WATCHLIST_PARTY)
            .modelKeyValue(getWatchlistParty())
            .build();
      case MATCH:
        return ModelKeyDto.builder()
            .modelKeyType(MATCH)
            .modelKeyValue(getMatch())
            .build();
      default: {
        return ModelKeyDto.builder()
            .build();
      }
    }
  }

  private static WatchlistPartyDto getWatchlistParty() {
    return WatchlistPartyDto.builder()
        .id("watchListParty_id")
        .type("watchListParty_type")
        .build();
  }

  private static MatchDto getMatch() {
    return MatchDto.builder()
        .alertedParty(new AlertedPartyDto("alertedParty_id"))
        .watchlistParty(getWatchlistParty())
        .build();
  }
}
