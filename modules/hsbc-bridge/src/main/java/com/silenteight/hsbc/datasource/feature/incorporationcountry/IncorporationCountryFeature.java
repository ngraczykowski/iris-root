package com.silenteight.hsbc.datasource.feature.incorporationcountry;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.*;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.feature.Feature;
import com.silenteight.hsbc.datasource.feature.FeatureValuesRetriever;

import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.hsbc.datasource.util.StreamUtils.toDistinctList;
import static java.util.Collections.emptyList;

@RequiredArgsConstructor
public class IncorporationCountryFeature implements FeatureValuesRetriever<CountryFeatureInputDto> {

  @Override
  public CountryFeatureInputDto retrieve(MatchData matchData) {
    var builder = CountryFeatureInputDto.builder();

    if (matchData.isEntity()) {
      var apIncorporationCountries = customerEntitiesIncorporationCountries(
          matchData.getCustomerEntity());
      var mpIncorporationCountries = getWatchlistEntitiesIncorporationCountries(matchData);

      builder.alertedPartyCountries(toDistinctList(apIncorporationCountries));
      builder.watchlistCountries(toDistinctList(mpIncorporationCountries));
    } else {
      builder.watchlistCountries(emptyList());
      builder.alertedPartyCountries(emptyList());
    }

    return builder
        .feature(getFeatureName())
        .build();
  }

  public static Stream<String> worldCheckEntitiesIncorporationCountries(
      List<WorldCheckEntity> worldCheckEntities) {
    return worldCheckEntities.stream()
        .flatMap(wce -> Stream.of(
            wce.getCountryCodesAll(),
            wce.getCountriesAll()
        ));
  }

  public static Stream<String> privateListEntitiesIncorporationCountries(
      List<PrivateListEntity> privateListEntities) {
    return privateListEntities.stream()
        .flatMap(ple -> Stream.of(
            ple.getCountryCodesAll(),
            ple.getCountriesAll()
        ));
  }

  public static Stream<String> ctrpScreeningEntitiesIncorporationCountries(
      List<CtrpScreening> ctrpScreeningEntities) {
    return ctrpScreeningEntities.stream()
        .flatMap(cse -> Stream.of(
            cse.getCountryName(),
            cse.getCountryCode(),
            cse.getCtrpValue()
        ));
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
      CustomerEntity customerEntity) {
    return Stream.of(
        customerEntity.getCountriesOfIncorporation(),
        customerEntity.getEdqIncorporationCountries(),
        customerEntity.getEdqIncorporationCountriesCodes()
    );
  }

  @Override
  public Feature getFeature() {
    return Feature.INCORPORATION_COUNTRY;
  }
}
