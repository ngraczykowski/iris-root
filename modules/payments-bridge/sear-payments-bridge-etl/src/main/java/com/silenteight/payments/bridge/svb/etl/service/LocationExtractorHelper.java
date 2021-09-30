package com.silenteight.payments.bridge.svb.etl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.common.dto.input.AddressCityDto;
import com.silenteight.payments.bridge.common.dto.input.AddressCountryDto;
import com.silenteight.payments.bridge.common.dto.input.AddressStateDto;
import com.silenteight.payments.bridge.common.dto.input.RequestHitDto;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationExtractorHelper {

  @NotNull
  public static List<String> extractListOfCities(RequestHitDto requestHitDto) {
    return requestHitDto
        .getHit()
        .getHittedEntity()
        .getAddresses()
        .stream()
        .flatMap(a -> a.getAddress().getCities()
            .stream())
        .map(AddressCityDto::getCity)
        .collect(Collectors.toList());
  }

  @NotNull
  public static List<String> extractListOfStates(RequestHitDto requestHitDto) {
    return requestHitDto
        .getHit()
        .getHittedEntity()
        .getAddresses()
        .stream()
        .flatMap(a -> a.getAddress().getStates()
            .stream())
        .map(AddressStateDto::getState)
        .collect(Collectors.toList());
  }

  @NotNull
  public static List<String> extractListOfCountries(RequestHitDto requestHitDto) {
    return requestHitDto
        .getHit()
        .getHittedEntity()
        .getAddresses()
        .stream()
        .flatMap(a -> a.getAddress().getCountries()
            .stream())
        .map(AddressCountryDto::getCountry)
        .collect(Collectors.toList());
  }

  public static boolean extractIsMainAddress(RequestHitDto requestHitDto) {
    return requestHitDto
        .getHit()
        .getHittedEntity()
        .getAddresses()
        .stream()
        .map(a -> a.getAddress().getIsMain())
        .anyMatch("1"::equals);
  }

  public static List<String> extractPostalAddresses(RequestHitDto requestHitDto) {
    return requestHitDto
        .getHit()
        .getHittedEntity()
        .getAddresses()
        .stream()
        .map(rad -> rad.getAddress().getPostalAddress())
        .collect(Collectors.toList());
  }
}
