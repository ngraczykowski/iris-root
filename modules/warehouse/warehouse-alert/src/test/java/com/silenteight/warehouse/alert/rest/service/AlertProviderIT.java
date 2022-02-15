package com.silenteight.warehouse.alert.rest.service;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.domain.group.CountryGroupEntity;
import com.silenteight.warehouse.common.domain.group.CountryGroupRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Sql
@SpringBootTest(classes = AlertProviderTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class AlertProviderIT {

  @Autowired
  private AlertProvider alertProvider;

  @Autowired
  private CountryGroupRepository countryGroupRepository;

  @Autowired
  private CountryPermissionService countryPermissionService;

  private final UUID testGroup = UUID.fromString("d87be316-eb23-43c1-8c5c-dc2285339783");
  private final UUID emptyTestGroup = UUID.fromString("d87be316-eb23-43c1-8c5c-dc2285339782");

  @BeforeEach
  public void setUp() {
    if (countryGroupRepository.findByCountryGroupId(testGroup).isEmpty()) {
      countryGroupRepository.saveAll(List.of(CountryGroupEntity.builder()
          .countryGroupId(testGroup).name("my test group").migrated(true).build()));
      countryPermissionService.setCountries(testGroup, List.of("UK", "PL"));
      countryGroupRepository.saveAll(List.of(CountryGroupEntity.builder()
          .countryGroupId(emptyTestGroup).name("empty test group").migrated(true).build()));
      countryPermissionService.setCountries(emptyTestGroup, List.of());
    }
  }

  @Test
  @WithMockUser(username = "USERNAME", authorities = "d87be316-eb23-43c1-8c5c-dc2285339783")
  void shouldReturnListOfMapsForAlerts() {

    Collection<Map<String, String>>
        result = alertProvider.getMultipleAlertsAttributes(List.of("discriminator", "step"),
        List.of("21dd1cd2-42b9-43c0-9d97-8ac379b364d1", "21dd1cd2-42b9-43c0-9d97-8ac379b364d2"));

    assertThat(result).hasSize(2);
  }

  @Test
  @WithMockUser(username = "USERNAME", authorities = "d87be316-eb23-43c1-8c5c-dc2285339782")
  void shouldReturnEmptyMapWhenUserDoesNotHaveGroup() {
    Collection<Map<String, String>>
        result = alertProvider.getMultipleAlertsAttributes(List.of("discriminator", "step"),
        List.of("21dd1cd2-42b9-43c0-9d97-8ac379b364d1", "21dd1cd2-42b9-43c0-9d97-8ac379b364d2"));

    assertThat(result).isEmpty();
  }
}
