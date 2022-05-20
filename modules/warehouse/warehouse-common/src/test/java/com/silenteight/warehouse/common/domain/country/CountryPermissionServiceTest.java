package com.silenteight.warehouse.common.domain.country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryPermissionServiceTest {

  private static final String EXISTING_GROUP_1 = "123e4567-e89b-42d3-a456-556642441234";
  private static final String EXISTING_GROUP_2 = "123e4567-e89b-42d3-a456-556642441235";
  private static final String NOT_EXISTING_GROUP = "123e4567-e89b-42d3-a456-556642441111";
  private static final String INVALID_GROUP = "asdasfg adsasd";

  private static final UUID EXISTING_GROUP_UUID_1 = UUID.fromString(EXISTING_GROUP_1);
  private static final UUID EXISTING_GROUP_UUID_2 = UUID.fromString(EXISTING_GROUP_2);
  private static final UUID NOT_EXISTING_GROUP_UUID = UUID.fromString(NOT_EXISTING_GROUP);

  private static final CountryEntity COUNTRY_ENTITY_1 =
      new CountryEntity(1L, EXISTING_GROUP_UUID_1, "MX");
  private static final CountryEntity COUNTRY_ENTITY_2 =
      new CountryEntity(2L, EXISTING_GROUP_UUID_1, "UK");
  private static final CountryEntity COUNTRY_ENTITY_3 =
      new CountryEntity(3L, EXISTING_GROUP_UUID_1, "PL");

  private static final CountryEntity COUNTRY_ENTITY_4 =
      new CountryEntity(4L, EXISTING_GROUP_UUID_2, "UK");
  private static final CountryEntity COUNTRY_ENTITY_5 =
      new CountryEntity(5L, EXISTING_GROUP_UUID_2, "DE");

  private static final List<CountryEntity> COUNTRIES_1 = List.of(COUNTRY_ENTITY_1,
      COUNTRY_ENTITY_2, COUNTRY_ENTITY_3);
  private static final List<CountryEntity> COUNTRIES_2 = List.of(COUNTRY_ENTITY_4,
      COUNTRY_ENTITY_5);

  @Mock
  CountryRepository countryRepository;

  CountryPermissionService underTest;

  @BeforeEach
  public void setUp() {
    underTest = new CountryPermissionService(countryRepository);
    when(countryRepository.getCountryEntitiesByCountryGroupId(NOT_EXISTING_GROUP_UUID))
        .thenReturn(Collections.emptyList());
  }

  @Test
  public void shouldReturnSetOfGroupsForClient() {
    when(countryRepository.getCountryEntitiesByCountryGroupId(EXISTING_GROUP_UUID_1)).thenReturn(
        COUNTRIES_1);

    Set<String> result = underTest.getCountries(Set.of(EXISTING_GROUP_1,
        NOT_EXISTING_GROUP));

    assertThat(result).hasSize(3);
    assertThat(result).contains("MX", "UK", "PL");
  }

  @Test
  public void shouldReturnMergedSetOfGroupsForClient() {
    when(countryRepository.getCountryEntitiesByCountryGroupId(EXISTING_GROUP_UUID_1))
        .thenReturn(COUNTRIES_1);
    when(countryRepository.getCountryEntitiesByCountryGroupId(EXISTING_GROUP_UUID_2))
        .thenReturn(COUNTRIES_2);

    Set<String> result = underTest.getCountries(Set.of(EXISTING_GROUP_1,
        EXISTING_GROUP_2, NOT_EXISTING_GROUP));

    assertThat(result).hasSize(4);
    assertThat(result).contains("MX", "UK", "PL", "DE");
  }

  @Test
  public void shouldReturnEmptyOfGroupsForClient() {
    Set<String> result = underTest.getCountries(Set.of(NOT_EXISTING_GROUP));

    assertThat(result).isEmpty();
  }

  @Test
  public void shouldReturnEmptyOfGroupsForInvalidUuid() {
    Set<String> result = underTest.getCountries(Set.of(INVALID_GROUP, NOT_EXISTING_GROUP));

    assertThat(result).isEmpty();
  }
}
