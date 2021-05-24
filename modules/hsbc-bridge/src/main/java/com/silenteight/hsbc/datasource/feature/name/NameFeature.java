package com.silenteight.hsbc.datasource.feature.name;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.name.AlertedPartyNameDto;
import com.silenteight.hsbc.datasource.dto.name.NameFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto;
import com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureClientValuesRetriever;

import java.util.List;

import static com.silenteight.hsbc.datasource.dto.name.EntityType.INDIVIDUAL;
import static com.silenteight.hsbc.datasource.dto.name.EntityType.ORGANIZATION;
import static com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType.ALIAS;
import static com.silenteight.hsbc.datasource.dto.name.WatchlistNameDto.NameType.REGULAR;
import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.empty;

@RequiredArgsConstructor
public class NameFeature implements FeatureClientValuesRetriever<NameFeatureInputDto> {

  private final NameQuery.Factory nameQueryFactory;

  @Override
  public NameFeatureInputDto retrieve(MatchData matchData, NameInformationServiceClient client) {
    var query = nameQueryFactory.create(matchData, client);
    var inputBuilder = NameFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      extractIndividual(query, inputBuilder);
    } else {
      extractEntity(query, inputBuilder);
    }

    return inputBuilder
        .feature(getFeature().getName())
        .build();
  }

  private void extractEntity(
      NameQuery query, NameFeatureInputDto.NameFeatureInputDtoBuilder inputBuilder) {
    inputBuilder.alertedPartyNames(mapToAlertedPartyNames(toDistinctList(
        query.apEntityExtractEntityNameOriginal(),
        query.apEntityExtractOtherNames())));
    inputBuilder.watchlistNames(mapToWatchlistNames(toDistinctList(
        query.mpWorldCheckEntitiesExtractNames(),
        query.mpWorldCheckEntitiesExtractXmlNamesWithCountries(),
        query.mpPrivateListEntitiesExtractNames()), REGULAR));
    inputBuilder.alertedPartyType(ORGANIZATION);
    inputBuilder.matchingTexts(toDistinctList(empty()));
  }

  private void extractIndividual(
      NameQuery query, NameFeatureInputDto.NameFeatureInputDtoBuilder inputBuilder) {
    var party = query.applyOriginalScriptEnhancementsForIndividualNames();
    inputBuilder.alertedPartyNames(mapToAlertedPartyNames(party.getAlertedPartyIndividuals()));
    inputBuilder.watchlistNames(mapToWatchlistNames(party.getWatchlistPartyIndividuals(), ALIAS));
    inputBuilder.alertedPartyType(INDIVIDUAL);
    inputBuilder.matchingTexts(toDistinctList(empty()));
  }

  @Override
  public Feature getFeature() {
    return Feature.NAME;
  }

  private static List<AlertedPartyNameDto> mapToAlertedPartyNames(List<String> names) {
    return names.stream()
        .map(name -> AlertedPartyNameDto.builder()
            .name(name)
            .build())
        .collect(toList());
  }

  private static List<WatchlistNameDto> mapToWatchlistNames(List<String> names, NameType nameType) {
    return names.stream()
        .map(name -> WatchlistNameDto.builder()
            .name(name)
            .type(nameType)
            .build())
        .collect(toList());
  }
}
