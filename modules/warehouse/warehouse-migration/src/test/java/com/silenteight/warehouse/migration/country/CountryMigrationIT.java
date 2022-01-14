package com.silenteight.warehouse.migration.country;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;
import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = CountryMigrationTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class CountryMigrationIT {

  private static final List<String> COUNTRIES = of("UK", "PL", "DE");
  private static final CountryGroupEntity COUNTRY_GROUP = CountryGroupEntity.builder()
      .countryGroupId(ELASTIC_ALLOWED_ROLE)
      .name("new country group")
      .build();


  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private CountryMigration countryMigration;

  @Autowired
  private RoleService roleService;

  @Autowired
  private CountryGroupRepository countryGroupRepository;

  @Test
  @WithElasticAccessCredentials
  void shouldMigrateCountries() {

    // given
    countryGroupRepository.save(COUNTRY_GROUP);
    roleService.setCountries(ELASTIC_ALLOWED_ROLE, COUNTRIES, List.of());
    assertThat(isCountryGroupMigrated()).isFalse();

    // when
    countryMigration.migration();

    // then
    List<String> countries = getCountries();

    assertThat(countries).containsExactlyElementsOf(COUNTRIES);
    assertThat(isCountryGroupMigrated()).isTrue();
  }

  private List<String> getCountries() {
    return jdbcTemplate.queryForList(
        "SELECT country FROM warehouse_country WHERE country_group_id = ?",
        String.class,
        ELASTIC_ALLOWED_ROLE);
  }

  private Boolean isCountryGroupMigrated() {
    return jdbcTemplate.queryForObject(
        "SELECT migrated FROM warehouse_country_group WHERE country_group_id = ?",
        Boolean.class,
        ELASTIC_ALLOWED_ROLE);
  }
}
