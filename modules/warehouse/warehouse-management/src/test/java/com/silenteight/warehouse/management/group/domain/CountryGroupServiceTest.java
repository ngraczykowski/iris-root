package com.silenteight.warehouse.management.group.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupAlreadyExistsException;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.COUNTRY_GROUP_DTO;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.NAME;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.UPDATED_NAME;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.UPDATE_COUNTRY_GROUP_REQUEST;
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
    Optional<CountryGroupEntity> countryGroup = countryGroupRepository.findByCountryGroupId(
        COUNTRY_GROUP_ID);
    assertThat(countryGroup).isPresent();
    CountryGroupEntity result = countryGroup.get();
    assertThat(result.getCountryGroupId()).isEqualTo(COUNTRY_GROUP_ID);
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
    underTest.update(COUNTRY_GROUP_ID, UPDATE_COUNTRY_GROUP_REQUEST);

    // then
    Optional<CountryGroupEntity> countryGroup = countryGroupRepository.findByCountryGroupId(
        COUNTRY_GROUP_ID);
    assertThat(countryGroup).isPresent();
    CountryGroupEntity result = countryGroup.get();
    assertThat(result.getCountryGroupId()).isEqualTo(COUNTRY_GROUP_ID);
    assertThat(result.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  void shouldThrowCountryGroupAlreadyExistsException() {
    // given
    underTest.create(COUNTRY_GROUP_DTO);

    // then
    assertThatThrownBy(() -> underTest.create(COUNTRY_GROUP_DTO))
        .isInstanceOf(CountryGroupAlreadyExistsException.class)
        .hasMessageContaining(format(
            "Country Group with UUID %s already exists.",
            COUNTRY_GROUP_ID));
  }

  @Test
  void shouldDeleteCountryGroup() {
    //given
    underTest.create(COUNTRY_GROUP_DTO);

    //when
    underTest.delete(COUNTRY_GROUP_ID);

    //then
    Optional<CountryGroupEntity> countryGroupEntity =
        countryGroupRepository.findByCountryGroupId(COUNTRY_GROUP_ID);
    assertThat(countryGroupEntity).isNotPresent();
  }

  @Test
  void shouldThrowExceptionWhenCountryGroupAlreadyDoesNotExist() {
    assertThatThrownBy(() -> underTest.delete(MISSING_UUID))
        .isInstanceOf(CountryGroupDoesNotExistException.class)
        .hasMessageContaining(format("Country Group with UUID %s does not exist.", MISSING_UUID));
  }
}
