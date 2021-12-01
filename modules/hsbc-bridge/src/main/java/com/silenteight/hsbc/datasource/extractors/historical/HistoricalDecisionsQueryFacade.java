package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto;
import com.silenteight.hsbc.datasource.feature.historical.HistoricalDecisionsQuery;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
class HistoricalDecisionsQueryFacade implements HistoricalDecisionsQuery {

  private final MatchData matchData;

  @Override
  public Optional<ModelKeyDto> getIsApTpMarkedInput() {
    return getExternalProfileIds()
        .map(AlertedPartyCreator::new)
        .map(AlertedPartyCreator::create)
        .findFirst();
  }

  @Override
  public Optional<ModelKeyDto> getIsTpMarkedInput() {
    return Optional.of(new WatchlistPartyRequestCreator(matchData).create());
  }

  @Override
  public Optional<ModelKeyDto> getCaseTpMarkedInput() {
    return getExternalProfileIds()
        .map(id -> new MatchRequestCreator(matchData, id))
        .map(MatchRequestCreator::create)
        .findFirst();
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
