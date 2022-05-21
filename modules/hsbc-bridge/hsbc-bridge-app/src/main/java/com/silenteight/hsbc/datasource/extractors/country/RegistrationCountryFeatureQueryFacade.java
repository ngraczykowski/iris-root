package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CtrpScreening;
import com.silenteight.hsbc.datasource.datamodel.EntityComposite;
import com.silenteight.hsbc.datasource.datamodel.PrivateListEntity;
import com.silenteight.hsbc.datasource.datamodel.WorldCheckEntity;
import com.silenteight.hsbc.datasource.feature.country.RegistrationCountryFeatureQuery;

import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class RegistrationCountryFeatureQueryFacade implements RegistrationCountryFeatureQuery {

  private final EntityComposite entityComposite;

  @Override
  public Stream<String> worldCheckEntitiesRegistrationCountries() {
    return entityComposite.getWorldCheckEntities()
        .stream()
        .flatMap(
            RegistrationCountryFeatureQueryFacade::extractWorldCheckEntitiesRegistrationCountries);
  }

  @Override
  public Stream<String> privateListEntitiesRegistrationCountries() {
    return entityComposite.getPrivateListEntities()
        .stream()
        .flatMap(
            RegistrationCountryFeatureQueryFacade::extractPrivateListEntitiesRegistrationCountries);
  }

  @Override
  public Stream<String> ctrpScreeningEntitiesRegistrationCountries() {
    return entityComposite.getCtrpScreeningEntities()
        .stream()
        .flatMap(
            RegistrationCountryFeatureQueryFacade::extractCtrpScreeningEntitiesRegistrationCountries);
  }

  @Override
  public Stream<String> getCustomerEntityRegistrationCountries() {
    return entityComposite.getCustomerEntities().stream()
        .flatMap(customerEntity -> Stream.of(
            customerEntity.getRegistrationCountry(),
            customerEntity.getCountriesOfRegistrationOriginal(),
            customerEntity.getEdqRegistrationCountriesCodes()))
        .filter(StringUtils::isNotBlank);
  }

  @Override
  public Stream<String> getWatchlistEntityRegistrationCountries() {
    var worldCheckEntitiesRegistrationCountries = worldCheckEntitiesRegistrationCountries();
    var privateListEntitiesRegistrationCountries = privateListEntitiesRegistrationCountries();
    var ctrpScreeningEntitiesRegistrationCountries = ctrpScreeningEntitiesRegistrationCountries();

    return StreamEx.of(worldCheckEntitiesRegistrationCountries)
        .append(privateListEntitiesRegistrationCountries)
        .append(ctrpScreeningEntitiesRegistrationCountries);
  }

  private static Stream<String> extractWorldCheckEntitiesRegistrationCountries(
      WorldCheckEntity worldCheckEntity) {
    return Stream.of(
        worldCheckEntity.getRegistrationCountry()
    ).filter(StringUtils::isNotBlank);
  }

  private static Stream<String> extractPrivateListEntitiesRegistrationCountries(
      PrivateListEntity privateListEntity) {
    return Stream.of(
        privateListEntity.getCountryCodesAll(),
        privateListEntity.getCountriesAll()
    ).filter(StringUtils::isNotBlank);
  }

  private static Stream<String> extractCtrpScreeningEntitiesRegistrationCountries(
      CtrpScreening ctrpScreening) {
    return Stream.of(
        ctrpScreening.getCountryName(),
        ctrpScreening.getCountryCode(),
        ctrpScreening.getCtrpValue()
    ).filter(StringUtils::isNotBlank);
  }
}
