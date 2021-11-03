package com.silenteight.hsbc.datasource.feature.geolocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.location.LocationFeatureInputDto;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.LocationFeatureClientValuesRetriever;
import com.silenteight.hsbc.datasource.feature.country.CountryDiscoverer;
import com.silenteight.hsbc.datasource.feature.name.NameQuery;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.feature.Feature.GEO_RESIDENCIES;

@Slf4j
@RequiredArgsConstructor
public class GeoResidencyFeature implements LocationFeatureClientValuesRetriever {

  private final GeoResidencyFeatureQuery.Factory queryFactory;
  private final NameQuery.Factory nameQueryFactory;

  @Override
  public LocationFeatureInputDto retrieve(MatchData matchData, CountryDiscoverer countryDiscoverer, NameInformationServiceClient nameInformationServiceClient) {
    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var query = queryFactory.create(matchData, countryDiscoverer);
    var nameQuery = nameQueryFactory.create(matchData, nameInformationServiceClient);

    var result = Optional.ofNullable(matchData)
        .filter(MatchData::isIndividual)
        .map(md -> this.retrieveForIndividual(query, nameQuery))
        .orElseGet(() -> this.retrieveForEntities(query, nameQuery));

    log.debug(
        "Datasource response for feature: {} with alerted party {} and watchlist party {}.",
        result.getFeature(),
        result.getAlertedPartyLocation(),
        result.getWatchlistLocation());

    return result;
  }

  private LocationFeatureInputDto retrieveForEntities(GeoResidencyFeatureQuery query, NameQuery nameQuery) {
    var entitiesAlertedPartyNames = Stream.concat(nameQuery.apEntityExtractEntityNameOriginal(), nameQuery.apEntityExtractOtherNames()).collect(Collectors.toList());

    var inputBuilder = LocationFeatureInputDto.builder();
    inputBuilder.alertedPartyLocation(query.getApEntitiesGeoResidencies(entitiesAlertedPartyNames));
    inputBuilder.watchlistLocation(query.getMpEntitiesGeoResidencies());
    return inputBuilder.feature(getFeatureName()).build();
  }


  private LocationFeatureInputDto retrieveForIndividual(GeoResidencyFeatureQuery query, NameQuery nameQuery) {
    var party = nameQuery.applyOriginalScriptEnhancementsForIndividualNamesAll();

    var inputBuilder = LocationFeatureInputDto.builder();
    inputBuilder.alertedPartyLocation(query.getApIndividualsGeoResidencies(party));
    inputBuilder.watchlistLocation(query.getMpIndividualsGeoResidencies());
    return inputBuilder.feature(getFeatureName()).build();
  }

  @Override
  public Feature getFeature() {
    return GEO_RESIDENCIES;
  }
}
