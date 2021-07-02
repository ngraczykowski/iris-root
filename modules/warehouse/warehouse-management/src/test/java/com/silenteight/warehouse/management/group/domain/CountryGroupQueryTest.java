package com.silenteight.warehouse.management.group.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.management.group.CountryGroupFixtures.UPDATED_NAME;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = { CountryGroupTestConfiguration.class })
class CountryGroupQueryTest extends BaseDataJpaTest {

  @Autowired
  CountryGroupQuery underTest;

  @Autowired
  CountryGroupRepository countryGroupRepository;

  @Test
  void shouldGetCountryGroup() {
    // given
    persistCountryGroup();

    // when
    CountryGroupDto result = underTest.get(COUNTRY_GROUP_ID);

    // then
    assertThat(result.getId()).isEqualTo(COUNTRY_GROUP_ID);
    assertThat(result.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  void shouldListCountryGroups() {
    // given
    persistCountryGroup();

    // when
    List<CountryGroupDto> result = underTest.listAll();

    // then
    assertThat(result).hasSize(1);
    CountryGroupDto dto = result.get(0);
    assertThat(dto.getId()).isEqualTo(COUNTRY_GROUP_ID);
    assertThat(dto.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  void shouldThrowCountryGroupDoesNotExistException() {
    assertThatThrownBy(() -> underTest.get(COUNTRY_GROUP_ID))
        .isInstanceOf(CountryGroupDoesNotExistException.class)
        .hasMessageContaining(
            format("Country Group with UUID %s does not exist.", COUNTRY_GROUP_ID));
  }

  @Test
  void shouldReturnEmptyList() {
    // when
    List<CountryGroupDto> result = underTest.listAll();

    // then
    assertThat(result).isEmpty();
  }

  private void persistCountryGroup() {
    CountryGroupEntity entity = CountryGroupEntity
        .builder()
        .countryGroupId(COUNTRY_GROUP_ID)
        .name(UPDATED_NAME)
        .build();

    countryGroupRepository.save(entity);
  }
}
