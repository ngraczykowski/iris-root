package com.silenteight.warehouse.management.group;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.alert.AlertNotFoundException;
import com.silenteight.warehouse.indexer.alert.AlertRestController;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;
import com.silenteight.warehouse.management.country.update.UpdateCountriesRestController;
import com.silenteight.warehouse.management.group.create.CreateCountryGroupRestController;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

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

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MAPPED_ALERT_WITH_MATCHES_1;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = CountryGroupsConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class,
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
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private AlertRestController alertRestController;

  private static final String COUNTRY = "UK";
  private static final String OTHER_COUNTRY = "PL";

  @BeforeEach
  void init() {
    storeData();

    CountryGroupDto countryGroupDto = CountryGroupDto.builder()
        .id(ELASTIC_ALLOWED_ROLE)
        .name("Country group")
        .build();
    createCountryGroupRestController.create(countryGroupDto);
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

    AlertAttributes allowedAlert =
        alertRestController.getSingleAlertDto(ALERT_ID_1, of()).getBody();
    assertThat(allowedAlert).isNotNull();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldNotAccessAlert() {
    Collection<String> allowedCountries = of(OTHER_COUNTRY);
    updateCountriesController.update(ELASTIC_ALLOWED_ROLE, allowedCountries);

    assertThatThrownBy(
        () -> alertRestController.getSingleAlertDto(ALERT_ID_1, of()))
        .isInstanceOf(AlertNotFoundException.class);
  }

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, ALERT_ID_1, MAPPED_ALERT_WITH_MATCHES_1);
  }

  private void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}

