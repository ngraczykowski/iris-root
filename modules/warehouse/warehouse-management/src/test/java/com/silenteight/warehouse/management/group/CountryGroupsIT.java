package com.silenteight.warehouse.management.group;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.alert.AlertRestController;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;
import com.silenteight.warehouse.management.country.update.UpdateCountriesRestController;
import com.silenteight.warehouse.management.group.create.CreateCountryGroupRestController;
import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_ID_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_1;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ALERT_ID_2;
import static java.util.List.of;
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
class CountryGroupsIT {

  @Autowired
  private CreateCountryGroupRestController createCountryGroupRestController;

  @Autowired
  private UpdateCountriesRestController updateCountriesController;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private AlertRestController alertRestController;

  private static final Map<String, Object> ALERT_ALLOWED = Map.of(
      ALERT_ID_KEY, ALERT_ID_1,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, "DE");

  private static final Map<String, Object> ALERT_RESTRICTED = Map.of(
      ALERT_ID_KEY, ALERT_ID_2,
      ALERT_PREFIX + ALERT_COUNTRY_KEY, "PL");

  @SneakyThrows
  @Test
  @WithElasticAccessCredentials
  void createCountryGroup() {
    storeData();
    UUID countryGroupId = randomUUID();

    CountryGroupDto countryGroupDto = CountryGroupDto.builder()
        .id(countryGroupId)
        .name("Europe without Poland")
        .build();
    createCountryGroupRestController.create(countryGroupDto);

    Collection<String> countries = of("DE", "ES");
    updateCountriesController.update(countryGroupId, countries);

    AlertAttributes allowedAlert =
        alertRestController.getSingleAlertDto(ALERT_ID_1, of(ALERT_COUNTRY_KEY)).getBody();
    assertThat(allowedAlert.getAttributes()).containsEntry(ALERT_COUNTRY_KEY, "DE");

    // TODO: Add logic to apply document-level-security (WEB-1273)
  }

  @SneakyThrows
  private void storeData() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, ALERT_ID_1, ALERT_ALLOWED);
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME, ALERT_ID_2, ALERT_RESTRICTED);
  }
}

