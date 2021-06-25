package com.silenteight.hsbc.datasource.feature.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.name.AlertedPartyNameDto;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.extractors.name.Party;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.NameFeatureClientValuesRetriever;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.dto.name.EntityType.INDIVIDUAL;
import static com.silenteight.hsbc.datasource.dto.name.EntityType.ORGANIZATION;
import static com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType.ALIAS;
import static com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType.REGULAR;
import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
public class NameFeature implements NameFeatureClientValuesRetriever<NameFeatureInputDto> {

  private final NameQuery.Factory nameQueryFactory;

  @Override
  public NameFeatureInputDto retrieve(MatchData matchData, NameInformationServiceClient client) {
    var query = nameQueryFactory.create(matchData, client);
    var inputBuilder = NameFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      var party = query.applyOriginalScriptEnhancementsForIndividualNames();
      var watchListIndividual = getWatchListIndividual(query, party);

      inputBuilder.alertedPartyNames(mapToAlertedPartyNames(party.getAlertedPartyIndividuals()));
      inputBuilder.watchlistNames(watchListIndividual);
      inputBuilder.alertedPartyType(INDIVIDUAL);
    } else {
      var watchListEntities = getWatchListEntities(query);

      inputBuilder.alertedPartyNames(mapToAlertedPartyNames(toDistinctList(
          query.apEntityExtractEntityNameOriginal(),
          query.apEntityExtractOtherNames())));
      inputBuilder.watchlistNames(watchListEntities);
      inputBuilder.alertedPartyType(ORGANIZATION);
    }
    inputBuilder.matchingTexts(emptyList());

    return inputBuilder
        .feature(getFeatureName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.NAME;
  }

  private List<WatchlistNameDto> getWatchListIndividual(NameQuery query, Party party) {
    var wlIndividualWithoutAliases =
        mapToWatchlistNames(party.getWatchlistPartyIndividuals(), REGULAR);

    var wlIndividualWithAliases = mapToWatchlistNames(
        query.applyOriginalScriptEnhancementsForIndividualNamesWithAliases(), ALIAS);

    return concat(wlIndividualWithAliases, wlIndividualWithoutAliases).collect(toList());
  }

  private List<WatchlistNameDto> getWatchListEntities(NameQuery query) {
    var wlEntitiesWithAliases = mapToWatchlistNames(
        toDistinctList(query.mpWorldCheckEntitiesExtractXmlNamesWithCountries()), ALIAS);

    var wlEntitiesWithoutAliases = mapToWatchlistNames(toDistinctList(
        query.mpWorldCheckEntitiesExtractNames(),
        query.mpPrivateListEntitiesExtractNames()), REGULAR);

    return concat(wlEntitiesWithAliases, wlEntitiesWithoutAliases).collect(toList());
  }

  private List<AlertedPartyNameDto> mapToAlertedPartyNames(List<String> names) {
    return names.stream()
        .map(name -> AlertedPartyNameDto.builder()
            .name(name)
            .build())
        .collect(toList());
  }

  private Stream<WatchlistNameDto> mapToWatchlistNames(Collection<String> names, NameType type) {
    return names.stream()
        .map(name -> WatchlistNameDto.builder()
            .name(name)
            .type(type)
            .build());
  }
}
