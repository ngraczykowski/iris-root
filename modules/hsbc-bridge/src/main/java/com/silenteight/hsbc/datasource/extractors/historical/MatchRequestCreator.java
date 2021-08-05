package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.List;

@RequiredArgsConstructor
class MatchRequestCreator extends HistoricalDecisionsRequestCreator {

  private final MatchData matchData;

  @Override
  GetHistoricalDecisionsRequestDto createRequest() {
    return GetHistoricalDecisionsRequestDto.builder()
        .modelKeys(getModelKeys())
        .build();
  }

  private List<ModelKeyDto> getModelKeys() {
    return List.of(
        ModelKeyDto.builder()
            .modelKeyType(ModelKeyType.MATCH)
            .modelKeyValue(getMatch())
            .build()
    );
  }

  private MatchDto getMatch() {
    var alertedPartyId = matchData.getCaseInformation().getExternalId();
    return MatchDto.builder()
        .alertedParty(new AlertedPartyDto(alertedPartyId))
        .watchlistParty(getWatchlist(matchData))
        .build();
  }
}
