package com.silenteight.warehouse.management.group;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.query.single.AlertNotFoundException;
import com.silenteight.warehouse.indexer.query.single.AlertRestController;
import com.silenteight.warehouse.management.country.get.GetCountriesRestController;
import com.silenteight.warehouse.management.country.update.UpdateCountriesRestController;
import com.silenteight.warehouse.management.group.create.CreateCountryGroupRestController;
import com.silenteight.warehouse.management.group.delete.DeleteCountryGroupRestController;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;
import com.silenteight.warehouse.management.group.domain.exception.CountryGroupDoesNotExistException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.DISCRIMINATOR_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_1;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = CountryGroupsConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
@Transactional
class CountryGroupsIT {

  @Autowired
  private CreateCountryGroupRestController createCountryGroupRestController;

  @Autowired
  private UpdateCountriesRestController updateCountriesController;

  @Autowired
  private GetCountriesRestController getCountriesRestController;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private DeleteCountryGroupRestController deleteCountryGroupRestController;

  @Autowired
  private AlertRestController alertRestController;

  private static final String COUNTRY = "UK";
  private static final String OTHER_COUNTRY = "PL";
  private static final UUID NEW_COUNTRY_GROUP_ID =
      fromString("e11f9680-fb3b-4776-8044-571026290a65");

  private static final CountryGroupDto COUNTRY_GROUP = CountryGroupDto.builder()
      .id(NEW_COUNTRY_GROUP_ID)
      .name("new country group")
      .build();

  @BeforeEach
  void init() {
    storeData();
  }

  @AfterEach
  void cleanup() {
    removeData();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldAccessAlert() {
    Collection<String> allowedCountries = of(COUNTRY);
    updateCountriesController.update(ELASTIC_ALLOWED_ROLE, allowedCountries);

    Map<String, String> allowedAlert =
        alertRestController.getSingleAlert(DISCRIMINATOR_1, of()).getBody();

    assertThat(allowedAlert).isEmpty();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldNotAccessAlert() {
    Collection<String> allowedCountries = of(OTHER_COUNTRY);
    updateCountriesController.update(ELASTIC_ALLOWED_ROLE, allowedCountries);

    assertThatThrownBy(
        () -> alertRestController.getSingleAlert(DISCRIMINATOR_1, of()))
        .isInstanceOf(AlertNotFoundException.class);
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

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, DISCRIMINATOR_1, MAPPED_ALERT_1);
  }

  private void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}

