package com.silenteight.warehouse.management.group.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupAlreadyExistsException;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static com.silenteight.warehouse.management.group.CountryGroupFixtures.*;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = { CountryGroupTestConfiguration.class })
class CountryGroupServiceTest extends BaseDataJpaTest {

  private static final UUID MISSING_UUID = fromString("3fa85f64-5717-4562-b3fc-111111111111");

  @Autowired
  CountryGroupService underTest;

  @Autowired
  CountryGroupRepository countryGroupRepository;

  @Test
  void shouldCreateCountryGroup() {
    // when
    underTest.create(COUNTRY_GROUP_DTO);

    // then
    Optional<CountryGroupEntity> countryGroup = countryGroupRepository.findByCountryGroupId(UUID);
    assertThat(countryGroup).isPresent();
    CountryGroupEntity result = countryGroup.get();
    assertThat(result.getCountryGroupId()).isEqualTo(UUID);
    assertThat(result.getName()).isEqualTo(NAME);
  }

  @Test
  void shouldThrowCountryGroupDoesNotExistException() {
    assertThatThrownBy(() -> underTest.update(MISSING_UUID, UPDATE_COUNTRY_GROUP_REQUEST))
        .isInstanceOf(CountryGroupDoesNotExistException.class)
        .hasMessageContaining(format("Country Group with UUID %s does not exist.", MISSING_UUID));
  }

  @Test
  void shouldUpdateCountryGroup() {
    //given
    underTest.create(COUNTRY_GROUP_DTO);

    // when
    underTest.update(UUID, UPDATE_COUNTRY_GROUP_REQUEST);

    // then
    Optional<CountryGroupEntity> countryGroup = countryGroupRepository.findByCountryGroupId(UUID);
    assertThat(countryGroup).isPresent();
    CountryGroupEntity result = countryGroup.get();
    assertThat(result.getCountryGroupId()).isEqualTo(UUID);
    assertThat(result.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  void shouldThrowCountryGroupAlreadyExistsException() {
    // given
    underTest.create(COUNTRY_GROUP_DTO);

    // then
    assertThatThrownBy(() -> underTest.create(COUNTRY_GROUP_DTO))
        .isInstanceOf(CountryGroupAlreadyExistsException.class)
        .hasMessageContaining(format("Country Group with UUID %s already exists.", UUID));
  }
}
