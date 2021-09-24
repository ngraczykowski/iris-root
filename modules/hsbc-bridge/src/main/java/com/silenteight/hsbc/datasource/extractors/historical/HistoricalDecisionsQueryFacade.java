package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

import java.util.List;

import static java.util.Optional.of;

@RequiredArgsConstructor
class HistoricalDecisionsQueryFacade implements HistoricalDecisionsQuery {

  private final MatchData matchData;
  private final HistoricalDecisionsServiceClient serviceClient;

  @Override
  public List<ModelCountsDto> getIsApTpMarkedInput() {
    var alertedPartyId = getExternalProfileId();

    var request =
        new AlertedPartyRequestCreator(alertedPartyId).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  @Override
  public List<ModelCountsDto> getIsTpMarkedInput() {
    var request =
        new WatchlistPartyRequestCreator(matchData).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  @Override
  public List<ModelCountsDto> getCaseTpMarkedInput() {
    var request =
        new MatchRequestCreator(matchData).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  private String getExternalProfileId() {
    return (matchData.isIndividual()) ?
           of(matchData.getCustomerIndividual().getExternalProfileId()).orElse("") :
           of(matchData.getCustomerEntity().getExternalProfileId()).orElse("");
  }
}
