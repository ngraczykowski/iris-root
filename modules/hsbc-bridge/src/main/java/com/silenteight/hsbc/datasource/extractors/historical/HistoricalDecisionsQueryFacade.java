package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

@RequiredArgsConstructor
class HistoricalDecisionsQueryFacade implements HistoricalDecisionsQuery {

  private final MatchData matchData;
  private final HistoricalDecisionsServiceClient serviceClient;

  @Override
  public List<ModelCountsDto> getIsApTpMarkedInput() {
    return getExternalProfileIds().map(AlertedPartyRequestCreator::new)
        .map(AlertedPartyRequestCreator::createRequest)
        .map(serviceClient::getHistoricalDecisions)
        .map(GetHistoricalDecisionsResponseDto::getModelCounts)
        .flatMap(Collection::stream)
        .collect(toList());
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

  private Stream<String> getExternalProfileIds() {
    return (matchData.isIndividual()) ?
           of(matchData
               .getCustomerIndividuals()
               .stream()
               .flatMap(customerEntity -> Stream.of(customerEntity.getExternalProfileId()))
               .distinct())
               .orElse(empty()) :
           of(matchData
               .getCustomerEntities()
               .stream()
               .flatMap(customerEntity -> Stream.of(customerEntity.getExternalProfileId()))
               .distinct())
               .orElse(empty());
  }
}
