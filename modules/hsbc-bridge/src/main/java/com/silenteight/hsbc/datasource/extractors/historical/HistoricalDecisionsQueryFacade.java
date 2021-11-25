package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class HistoricalDecisionsQueryFacade implements HistoricalDecisionsQuery {

  private final MatchData matchData;
  private final HistoricalDecisionsServiceClient serviceClient;

  @Override
  public List<ModelCountsDto> getIsApTpMarkedInput() {
    return getExternalProfileIds()
        .map(AlertedPartyRequestCreator::new)
        .map(AlertedPartyRequestCreator::createRequest)
        .map(serviceClient::getHistoricalDecisions)
        .map(GetHistoricalDecisionsResponseDto::getModelCounts)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<ModelCountsDto> getIsTpMarkedInput() {
    var request =
        new WatchlistPartyRequestCreator(matchData).createRequest();

    return serviceClient.getHistoricalDecisions(request).getModelCounts();
  }

  @Override
  public List<ModelCountsDto> getCaseTpMarkedInput() {
    return getExternalProfileIds()
        .map(id -> new MatchRequestCreator(matchData, id))
        .map(MatchRequestCreator::createRequest)
        .map(serviceClient::getHistoricalDecisions)
        .map(GetHistoricalDecisionsResponseDto::getModelCounts)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private Stream<String> getExternalProfileIds() {
    return (matchData.isIndividual()) ?
           Optional.of(matchData
                   .getCustomerIndividuals()
                   .stream()
                   .flatMap(customerIndividual -> Stream.of(customerIndividual.getExternalProfileId()))
                   .distinct())
               .orElse(Stream.empty()) :
           Optional.of(matchData
                   .getCustomerEntities()
                   .stream()
                   .flatMap(customerEntity -> Stream.of(customerEntity.getExternalProfileId()))
                   .distinct())
               .orElse(Stream.empty());
  }
}
