package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;

@RequiredArgsConstructor
public class OtherCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  private final OtherCountryQuery.Factory queryFactory;

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var query = queryFactory.create(matchData);
    var inputBuilder = CountryFeatureInputDto.builder();

    if (matchData.isIndividual()) {
      inputBuilder.alertedPartyCountries(toDistinctList(
          query.apCustomerIndividualOtherCountries()));
      inputBuilder.watchlistCountries(toDistinctList(
          query.mpWorldCheckIndividualsOtherCountries(),
          query.mpPrivateListIndividualsOtherCountries(),
          query.mpCtrpScreeningIndividualsOtherCountries()));
    } else {
      inputBuilder.alertedPartyCountries(toDistinctList(
          query.apCustomerEntityOtherCountries()));
      inputBuilder.watchlistCountries(toDistinctList(
          query.mpWorldCheckEntitiesOtherCountries(),
          query.mpPrivateListEntitiesOtherCountries(),
          query.mpCtrpScreeningEntitiesOtherCountries()));
    }

    return inputBuilder
        .feature(getFeature().getName())
        .build();
  }

  @Override
  public Feature getFeature() {
    return Feature.OTHER_COUNTRY;
  }
}
