package com.silenteight.hsbc.datasource.feature.dob;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto.DateFeatureInputDtoBuilder;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.stream.Stream.concat;

@Slf4j
public class DateOfBirthFeature implements FeatureValuesRetriever<DateFeatureInputDto> {

  @Override
  public DateFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var featureBuilder = featureInputBuilder();

    if (matchData.isEntity()) {
      return featureBuilder.build();
    }

    var apDates = new ApDateExtractor(matchData.getCustomerIndividual()).extract();

    var mpDobsPrivateWatchlistDates = new PrivateListIndividualsDateExtractor(
        matchData.getPrivateListIndividuals()).extract();
    var mpDobsWorldCheckIndividuals = new WorldCheckIndividualsDateExtractor(
        matchData.getWorldCheckIndividuals()).extract();

    var wlDates = concat(mpDobsWorldCheckIndividuals, mpDobsPrivateWatchlistDates);

    var result = featureBuilder
        .alertedPartyDates(toDistinctList(apDates))
        .watchlistDates(toDistinctList(wlDates))
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
