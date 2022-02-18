package com.silenteight.payments.bridge.firco.dto.input;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HittedEntityDtoTest {

  private static final List<String> PAKISTAN_COUNTRY_NAMES =
      List.of("PAKISTAN", "ISLAMI JUMHURI YE PAKISTAN", "ISLAMIC REPUBLIC OF PAKISTAN", "PAKISTAN");
  private static final List<String> CHINA_COUNTRY_NAMES =
      List.of("CHINA", "CHINA", "CHINE", "CINA", "PEOPLE S REPUBLIC OF CHINA");
  private static final List<String> IRAN_COUNTRY_NAMES =
      List.of("IRAN ISLAMIC REPUBLIC OF", "IRAN", "IRAN (ISLAMIC REPUBLIC OF)",
          "IRAN ISLAMIC REPUBLIC OF", "IRAO");
  private static final List<String> TEHRAN_NAMES = List.of("TEHRAN", "TEHERAN");

  @ParameterizedTest
  @MethodSource("findCitiesFunctionDataProvider")
  void findCitiesFunctionTests(
      List<String> cities, List<String> states, List<String> countries,
      List<String> expectedListOfCities) {

    HittedEntityDto hittedEntityDto = HittedEntityDto.builder()
        .addresses(List.of(RequestAddressDto.builder()
            .address(createAddressDto(cities, states, countries, null))
            .build()))
        .build();

    assertEquals(expectedListOfCities, hittedEntityDto.findCities());
  }

  @ParameterizedTest
  @MethodSource("findStatesFunctionDataProvider")
  void findStatesFunctionTests(
      List<String> cities, List<String> states, List<String> countries,
      List<String> expectedListOfStates) {

    HittedEntityDto hittedEntityDto = HittedEntityDto.builder()
        .addresses(List.of(RequestAddressDto.builder()
            .address(createAddressDto(cities, states, countries, null))
            .build()))
        .build();

    assertEquals(expectedListOfStates, hittedEntityDto.findStates());
  }

  @ParameterizedTest
  @MethodSource("findCountriesFunctionDataProvider")
  void findCountriesFunctionTests(
      List<String> cities, List<String> states, List<String> countries,
      List<String> expectedListOfCountries) {

    HittedEntityDto hittedEntityDto = HittedEntityDto.builder()
        .addresses(List.of(RequestAddressDto.builder()
            .address(createAddressDto(cities, states, countries, null))
            .build()))
        .build();

    assertEquals(expectedListOfCountries, hittedEntityDto.findCountries());
  }

  @ParameterizedTest
  @MethodSource("findIsMainAddressFunctionDataProvider")
  void findIsMainAddressFunctionTests(
      List<String> cities, List<String> states, List<String> countries, String mainAddress,
      boolean isMainAddress) {

    HittedEntityDto hittedEntityDto = HittedEntityDto.builder()
        .addresses(List.of(RequestAddressDto.builder()
            .address(createAddressDto(cities, states, countries, mainAddress))
            .build()))
        .build();

    assertEquals(isMainAddress, hittedEntityDto.findIsMainAddress());
  }

  @Nonnull
  private static Stream<Arguments> findCitiesFunctionDataProvider() {

    return Stream.of(
        Arguments.of(TEHRAN_NAMES, List.of(), IRAN_COUNTRY_NAMES, TEHRAN_NAMES),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, List.of("SHANGHAI")),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, List.of("SHANGHAI")),
        Arguments.of(List.of(""), List.of(), CHINA_COUNTRY_NAMES, List.of("")),
        Arguments.of(null, List.of(), CHINA_COUNTRY_NAMES, List.of()),
        Arguments.of(null, null, null, List.of()));

  }

  @Nonnull
  private static Stream<Arguments> findStatesFunctionDataProvider() {
    return Stream.of(
        Arguments.of(
            List.of("LAHORE CITY"), List.of("LAHORE DISTRICT"), PAKISTAN_COUNTRY_NAMES,
            List.of("LAHORE DISTRICT")),
        Arguments.of(
            List.of("SHENZHEN"), List.of("GUANGDONG"), CHINA_COUNTRY_NAMES, List.of("GUANGDONG")),
        Arguments.of(
            List.of("SHENZHEN"), List.of("GUANGDONG"), CHINA_COUNTRY_NAMES, List.of("GUANGDONG")),
        Arguments.of(List.of("SHENZHEN"), List.of(""), CHINA_COUNTRY_NAMES, List.of("")),
        Arguments.of(List.of("SHENZHEN"), List.of(), CHINA_COUNTRY_NAMES, List.of()),
        Arguments.of(null, null, null, List.of()));
  }

  @Nonnull
  private static Stream<Arguments> findCountriesFunctionDataProvider() {
    return Stream.of(
        Arguments.of(TEHRAN_NAMES, List.of(), IRAN_COUNTRY_NAMES, IRAN_COUNTRY_NAMES),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, CHINA_COUNTRY_NAMES),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, CHINA_COUNTRY_NAMES),
        Arguments.of(List.of("SHANGHAI"), List.of(), List.of(), List.of()),
        Arguments.of(List.of(), List.of(), CHINA_COUNTRY_NAMES, CHINA_COUNTRY_NAMES),
        Arguments.of(null, null, null, List.of()));
  }

  @Nonnull
  private static Stream<Arguments> findIsMainAddressFunctionDataProvider() {
    return Stream.of(
        Arguments.of(TEHRAN_NAMES, List.of(), IRAN_COUNTRY_NAMES, "1", true),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, "1", true),
        Arguments.of(List.of("SHANGHAI"), List.of(), CHINA_COUNTRY_NAMES, "1", true),
        Arguments.of(List.of("SHANGHAI"), List.of(), List.of(), "0", false),
        Arguments.of(List.of(), List.of(), CHINA_COUNTRY_NAMES, "1", true),
        Arguments.of(null, null, null, null, false)
    );
  }

  private AddressDto createAddressDto(
      List<String> cities, List<String> states, List<String> countries, String mainAddress) {
    List<AddressCityDto> addressCityDtos = Optional.ofNullable(cities)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(AddressCityDto::new)
        .collect(Collectors.toList());

    List<AddressStateDto> addressStatesDtos = Optional.ofNullable(states)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(AddressStateDto::new)
        .collect(Collectors.toList());

    List<AddressCountryDto> addressCountryDtos = Optional.ofNullable(countries)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(AddressCountryDto::new)
        .collect(Collectors.toList());

    AddressDto addressDto = new AddressDto();
    addressDto.setIsMain(mainAddress);
    addressDto.setCities(addressCityDtos);
    addressDto.setStates(addressStatesDtos);
    addressDto.setCountries(addressCountryDtos);
    return addressDto;
  }
}
