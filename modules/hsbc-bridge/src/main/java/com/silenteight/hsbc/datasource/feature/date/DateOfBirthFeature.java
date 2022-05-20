package com.silenteight.hsbc.datasource.feature.date;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto.DateFeatureInputDtoBuilder;
import com.silenteight.hsbc.datasource.dto.name.EntityType;
import com.silenteight.hsbc.datasource.feature.DateFeatureValuesRetriever;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.util.StreamUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class DateOfBirthFeature implements DateFeatureValuesRetriever<DateFeatureInputDto> {

  @Override
  public DateFeatureInputDto retrieve(MatchData matchData, Map<String, List<String>> watchlistTypes) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var severityResolver = new SeverityResolver(matchData, watchlistTypes);
    var featureBuilder = featureInputBuilder();

    if (matchData.isEntity()) {
      return featureBuilder
          .alertedPartyType(EntityType.ORGANIZATION)
          .mode(severityResolver.resolve())
          .build();
    }

    var apDates = new ApDateExtractor(matchData.getCustomerIndividuals()).extract();

    var mpDobsPrivateWatchlistDates = new PrivateListIndividualsDateExtractor(
        matchData.getPrivateListIndividuals()).extract();
    var mpDobsWorldCheckIndividuals = new WorldCheckIndividualsDateExtractor(
        matchData.getWorldCheckIndividuals()).extract();

    var wlDates = Stream.concat(mpDobsWorldCheckIndividuals, mpDobsPrivateWatchlistDates);

    var result = featureBuilder
        .alertedPartyDates(StreamUtils.toDistinctList(apDates))
        .watchlistDates(StreamUtils.toDistinctList(wlDates))
        .alertedPartyType(EntityType.INDIVIDUAL)
        .mode(severityResolver.resolve())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyDates().size(),
        result.getWatchlistDates().size());

    return result;
  }

  private DateFeatureInputDtoBuilder featureInputBuilder() {
    return DateFeatureInputDto.builder().feature(getFeatureName());
  }

  @Override
  public Feature getFeature() {
    return Feature.DATE_OF_BIRTH;
  }
}
