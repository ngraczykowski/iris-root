package com.silenteight.hsbc.datasource.feature.country;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.datasource.datamodel.*;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
public class IncorporationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {

    log.debug("Datasource start retrieve data for {} feature.", getFeature());

    var builder = CountryFeatureInputDto.builder();

    if (matchData.isEntity()) {
      var apIncorporationCountries = customerEntitiesIncorporationCountries(matchData.getCustomerEntities());
      var mpIncorporationCountries = getWatchlistEntitiesIncorporationCountries(matchData);

      builder.alertedPartyCountries(toDistinctList(apIncorporationCountries));
      builder.watchlistCountries(toDistinctList(mpIncorporationCountries));
    } else {
      builder.watchlistCountries(emptyList());
      builder.alertedPartyCountries(emptyList());
    }

    var result = builder
        .feature(getFeatureName())
        .build();

    log.debug(
        "Datasource response for feature: {} with alerted party size {} and watchlist party size {}.",
        result.getFeature(),
        result.getAlertedPartyCountries().size(),
        result.getWatchlistCountries().size());

    return result;
  }

  public static Stream<String> worldCheckEntitiesIncorporationCountries(
      List<WorldCheckEntity> worldCheckEntities) {
    return worldCheckEntities.stream()
        .flatMap(wce -> Stream.of(
            wce.getCountryCodesAll(),
            wce.getCountriesAll()
        )).filter(StringUtils::isNotBlank);
  }

  public static Stream<String> privateListEntitiesIncorporationCountries(
      List<PrivateListEntity> privateListEntities) {
    return privateListEntities.stream()
        .flatMap(ple -> Stream.of(
            ple.getCountryCodesAll(),
            ple.getCountriesAll()
        )).filter(StringUtils::isNotBlank);
  }

  public static Stream<String> ctrpScreeningEntitiesIncorporationCountries(
      List<CtrpScreening> ctrpScreeningEntities) {
    return ctrpScreeningEntities.stream()
        .flatMap(cse -> Stream.of(
            cse.getCountryName(),
            cse.getCountryCode(),
            cse.getCtrpValue()
        )).filter(StringUtils::isNotBlank);
  }

  private Stream<String> getWatchlistEntitiesIncorporationCountries(EntityComposite entity) {
    var worldCheckEntitiesIncorporationCountries =
        worldCheckEntitiesIncorporationCountries(entity.getWorldCheckEntities());
    var privateListEntitiesIncorporationCountries =
        privateListEntitiesIncorporationCountries(entity.getPrivateListEntities());
    var ctrpScreeningEntitiesIncorporationCountries =
        ctrpScreeningEntitiesIncorporationCountries(entity.getCtrpScreeningEntities());

    return StreamEx.of(worldCheckEntitiesIncorporationCountries)
        .append(privateListEntitiesIncorporationCountries)
        .append(ctrpScreeningEntitiesIncorporationCountries);
  }

  private static Stream<String> customerEntitiesIncorporationCountries(
      List<CustomerEntity> customerEntities) {
    return customerEntities.stream()
        .flatMap(customerEntity -> Stream.of(
            customerEntity.getCountriesOfIncorporation(),
            customerEntity.getEdqIncorporationCountries(),
            customerEntity.getEdqIncorporationCountriesCodes()))
        .filter(StringUtils::isNotBlank);
  }

  @Override
  public Feature getFeature() {
    return Feature.INCORPORATION_COUNTRY;
  }
}
