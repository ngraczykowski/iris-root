package com.silenteight.hsbc.datasource.feature.name;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.name.AlertedPartyNameDto;
import com.silenteight.hsbc.datasource.dto.name.EntityType;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.extractors.name.Party;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.NameFeatureClientValuesRetriever;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class NameFeature implements NameFeatureClientValuesRetriever<NameFeatureInputDto> {

  private final NameQuery.Factory nameQueryFactory;

  @Override
  public NameFeatureInputDto retrieve(MatchData matchData, NameInformationServiceClient client) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = nameQueryFactory.create(matchData, client);
    var inputBuilder = NameFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      var party = query.applyOriginalScriptEnhancementsForIndividualNames();
      var watchListIndividual = getWatchListIndividual(query, party);

      inputBuilder.alertedPartyNames(mapToAlertedPartyNames(party.getAlertedPartyIndividuals()));
      inputBuilder.watchlistNames(watchListIndividual);
      inputBuilder.alertedPartyType(EntityType.INDIVIDUAL);
    } else {
      var watchListEntities = getWatchListEntities(query);

      inputBuilder.alertedPartyNames(
          mapToAlertedPartyNames(
              StreamUtils.toDistinctList(
                  query.apEntityExtractEntityNameOriginal(), query.apEntityExtractOtherNames())));
      inputBuilder.watchlistNames(watchListEntities);
      inputBuilder.alertedPartyType(EntityType.ORGANIZATION);
    }
    inputBuilder.matchingTexts(Collections.emptyList());

    var result = inputBuilder.feature(getFeatureName()).build();

    log.debug(
        "Datasource response for feature: {} with party type {}, alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyType(),
        result.getAlertedPartyNames().size(),
        result.getWatchlistNames().size());

    return result;
  }

  @Override
  public Feature getFeature() {
    return Feature.NAME;
  }

  private List<WatchlistNameDto> getWatchListIndividual(NameQuery query, Party party) {
    var wlIndividualWithoutAliases =
        mapToWatchlistNames(party.getWatchlistPartyIndividuals(), NameType.REGULAR);

    var nnsIndividualWithoutAliases =
        mapToWatchlistNames(party.getNnsIndividuals(), NameType.REGULAR);

    var wlIndividualWithAliases =
        mapToWatchlistNames(
            query.applyOriginalScriptEnhancementsForIndividualNamesWithAliases(), NameType.ALIAS);

    return Stream.of(
            wlIndividualWithoutAliases,
            wlIndividualWithAliases,
            nnsIndividualWithoutAliases)
        .flatMap(stream -> stream)
        .collect(Collectors.toList());
  }

  private List<WatchlistNameDto> getWatchListEntities(NameQuery query) {
    var wlEntitiesWithAliases =
        mapToWatchlistNames(
            StreamUtils.toDistinctList(
                query.mpWorldCheckEntitiesExtractOtherNames(),
                query.mpWorldCheckEntitiesExtractXmlNamesWithCountries()),
            NameType.ALIAS);

    var nnsEntitiesWithAliases =
        mapToWatchlistNames(
            StreamUtils.toDistinctList(
                query.nnsEntitiesExtractOtherNames(),
                query.nnsEntitiesExtractXmlNamesWithCountries()),
            NameType.ALIAS);

    var wlEntitiesWithoutAliases =
        mapToWatchlistNames(
            StreamUtils.toDistinctList(
                query.mpWorldCheckEntitiesExtractNames(),
                query.mpPrivateListEntitiesExtractNames()),
            NameType.REGULAR);

    var nnsEntitiesWithoutAliases =
        mapToWatchlistNames(
            StreamUtils.toDistinctList(query.nnsEntitiesExtractNames()), NameType.REGULAR);

    return Stream.of(
            wlEntitiesWithoutAliases,
            wlEntitiesWithAliases,
            nnsEntitiesWithAliases,
            nnsEntitiesWithoutAliases)
        .flatMap(stream -> stream)
        .collect(Collectors.toList());
  }

  private List<AlertedPartyNameDto> mapToAlertedPartyNames(List<String> names) {
    return names.stream()
        .map(name -> AlertedPartyNameDto.builder().name(name).build())
        .collect(Collectors.toList());
  }

  private Stream<WatchlistNameDto> mapToWatchlistNames(Collection<String> names, NameType type) {
    return names.stream().map(name -> WatchlistNameDto.builder().name(name).type(type).build());
  }
}
