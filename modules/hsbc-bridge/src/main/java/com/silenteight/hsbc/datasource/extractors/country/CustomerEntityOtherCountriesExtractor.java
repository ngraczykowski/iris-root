package com.silenteight.hsbc.datasource.extractors.country;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerEntity;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class CustomerEntityOtherCountriesExtractor {

  private final CustomerEntity customerEntity;

  public Stream<String> extract() {
    return of(
        customerEntity.getTradesWithCountries(),
        customerEntity.getSubsidiariesOperatesInCountries(),
        customerEntity.getCountriesOfBusiness(),
        customerEntity.getCountriesOfHeadOffice(),
        customerEntity.getAddressCountry(),
        customerEntity.getEdqAddressCountryCode(),
        customerEntity.getEdqBusinessCountries(),
        customerEntity.getCountriesAll(),
        customerEntity.getCountriesAllCodes(),
        customerEntity.getSourceAddressCountry(),
        customerEntity.getEdqTradesWithCountries(),
        customerEntity.getEdqHeadOfficeCountries(),
        customerEntity.getOperatingCountries(),
        customerEntity.getEdqOperatingCountriesCodes()
    ).filter(StringUtils::isNotBlank);
  }
}
