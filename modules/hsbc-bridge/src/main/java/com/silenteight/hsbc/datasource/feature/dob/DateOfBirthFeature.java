package com.silenteight.hsbc.datasource.feature.dob;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto.DateFeatureInputDtoBuilder;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class DateOfBirthFeature implements FeatureValuesRetriever<DateFeatureInputDto> {

  @Override
  public DateFeatureInputDto retrieve(MatchData matchData) {

    var featureBuilder = featureInputBuilder();
    if (matchData.isEntity()) {
      return featureBuilder.build();
    }

    var apDates = new ApDateExtractor(matchData.getCustomerIndividual()).extract();
    var mpDobsPrivateWatchlistDates = new PrivateWatchlistDateExtractor(
        matchData.getPrivateListIndividuals()).extract();
    var mpDobsWorldCheckIndividuals = new WorldCheckDateExtractor(
        matchData.getWorldCheckIndividuals()).extract();

    return featureBuilder
        .alertedPartyDates(apDates.collect(toList()))
        .watchlistDates(
            concat(mpDobsWorldCheckIndividuals, mpDobsPrivateWatchlistDates).collect(toList()))
        .build();
  }

  private DateFeatureInputDtoBuilder featureInputBuilder() {
    return DateFeatureInputDto.builder().feature(getFeatureName());
  }

  @Override
  public Feature getFeature() {
    return Feature.DATE_OF_BIRTH;
  }
}
