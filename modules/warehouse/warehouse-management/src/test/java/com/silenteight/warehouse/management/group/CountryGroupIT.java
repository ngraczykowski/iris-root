package com.silenteight.warehouse.management.group;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.alert.rest.AlertRestController;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.management.country.get.GetCountriesRestController;
import com.silenteight.warehouse.management.country.update.UpdateCountriesRestController;
import com.silenteight.warehouse.management.group.create.CreateCountryGroupRestController;
import com.silenteight.warehouse.management.group.delete.DeleteCountryGroupRestController;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_NAME_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static java.util.Collections.emptyMap;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = CountryGroupTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class CountryGroupIT {

  @Autowired
  private CreateCountryGroupRestController createCountryGroupRestController;

  @Autowired
  private UpdateCountriesRestController updateCountriesController;

  @Autowired
  private GetCountriesRestController getCountriesRestController;

  @Autowired
  private DeleteCountryGroupRestController deleteCountryGroupRestController;

  @Autowired
  private AlertRestController alertRestController;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String COUNTRY = "UK";
  private static final String OTHER_COUNTRY = "PL";
  private static final UUID NEW_COUNTRY_GROUP_ID =
      fromString("e11f9680-fb3b-4776-8044-571026290a65");

  private static final CountryGroupDto COUNTRY_GROUP = CountryGroupDto.builder()
      .id(NEW_COUNTRY_GROUP_ID)
      .name("new country group")
      .build();

  @Test
  @WithMockUser(username = "USERNAME", authorities = "e11f9680-fb3b-4776-8044-571026290a65")
  void shouldAccessAlert() {
    Collection<String> allowedCountries = of(COUNTRY);
    saveCountryGroup(COUNTRY_GROUP);
    updateCountriesController.update(NEW_COUNTRY_GROUP_ID, allowedCountries);

    insertData();

    assertThat(getData()).containsAllEntriesOf(Map.of(
        "id", "12345",
        "s8_lobCountryCode", "UK"
    ));
  }

  @Test
  @WithMockUser(username = "USERNAME", authorities = "e11f9680-fb3b-4776-8044-571026290a63")
  void shouldNotAccessAlert() {
    Collection<String> allowedCountries = of(OTHER_COUNTRY);
    saveCountryGroup(COUNTRY_GROUP);
    updateCountriesController.update(NEW_COUNTRY_GROUP_ID, allowedCountries);

    insertData();

    assertThat(getData()).isEmpty();
  }

  @Test
  void shouldThrowExceptionIfCountryGroupDoesNotExists() {
    assertThatThrownBy(() -> getCountriesRestController.get(randomUUID()))
        .isInstanceOf(CountryGroupDoesNotExistException.class);
  }

  @Test
  void shouldReturnEmptyListOfCountriesWhenNewCountryGroup() {
    saveCountryGroup(COUNTRY_GROUP);

    List<String> countries = getCountriesRestController.get(NEW_COUNTRY_GROUP_ID).getBody();

    assertThat(countries).isEmpty();
  }

  @Test
  void shouldThrowExceptionWhenNewlyCreatedCountryGroupIsRemoved() {
    //given
    saveCountryGroup(COUNTRY_GROUP);

    //when
    deleteCountryGroupRestController.delete(NEW_COUNTRY_GROUP_ID);

    //then
    assertThatThrownBy(() -> getCountriesRestController.get(NEW_COUNTRY_GROUP_ID))
        .isInstanceOf(CountryGroupDoesNotExistException.class);
  }

  private void saveCountryGroup(CountryGroupDto countryGroupDto) {
    createCountryGroupRestController.create(countryGroupDto);
  }

  private void insertData() {
    jdbcTemplate.execute(
        "insert INTO warehouse_alert(discriminator, name, recommendation_date, payload) "
            + "VALUES ('" + DISCRIMINATOR_1 + "','" + ALERT_NAME_1 + "','"
            + Values.PROCESSING_TIMESTAMP
            + "','{\"id\": \"12345\", \"s8_lobCountryCode\": \"UK\"}'::jsonb)");
  }

  private Map<String, String> getData() {
    return alertRestController.getAlertDetails(
        of("id", "s8_lobCountryCode"), of(ALERT_NAME_1))
        .getBody().stream().findFirst().orElse(emptyMap());
  }
}

