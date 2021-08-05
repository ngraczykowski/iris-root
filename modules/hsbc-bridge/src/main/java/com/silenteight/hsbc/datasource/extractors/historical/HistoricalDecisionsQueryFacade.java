package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

import java.util.List;

@RequiredArgsConstructor
class HistoricalDecisionsQueryFacade implements HistoricalDecisionsQuery {

  private final MatchData matchData;
  private final HistoricalDecisionsServiceClient serviceClient;

  @Override
  public List<ModelCountsDto> getIsApTpMarkedSolution() {
    var alertedPartyId = matchData.getCaseInformation().getExternalId();
    var request =
        new AlertedPartyRequestCreator(alertedPartyId).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  @Override
  public List<ModelCountsDto> getIsTpMarkedSolution() {
    var request =
        new WatchlistPartyRequestCreator(matchData).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  @Override
  public List<ModelCountsDto> getCaseTpMarkedSolution() {
    var request =
        new MatchRequestCreator(matchData).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }
}
