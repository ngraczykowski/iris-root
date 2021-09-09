package com.silenteight.warehouse.indexer.alert;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_1;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_2;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_SEARCH_CRITERIA;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = AlertTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
@Slf4j
class AlertIT {

  static final List<String> LIST_OF_ID = of(
      DISCRIMINATOR_1,
      DISCRIMINATOR_2);

  static final List<String> ALERT_FIELDS = of(
      MappedKeys.RECOMMENDATION_KEY,
      MappedKeys.RISK_TYPE_KEY,
      MappedKeys.COUNTRY_KEY);

  @Autowired
  private AlertQueryService queryUnderTest;

  @Autowired
  private AlertIndexService underTest;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  AlertSearchService underSearchService;

  @Autowired
  RandomAlertQueryService randomAlertQueryService;

  @AfterEach
  void cleanup() {
    cleanData();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnAlertsAttributesListDto() {
    storeData();

    Collection<Map<String, String>> alertAttributes =
        queryUnderTest.getMultipleAlertsAttributes(ALERT_FIELDS, LIST_OF_ID);

    assertThat(alertAttributes).hasSize(2);
    Map<String, String> alert = alertAttributes.iterator().next();
    assertThat(alert)
        .containsEntry(MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnSingleAlertAttributes() {
    storeData();

    Map<String, String> singleAlertAttributes =
        queryUnderTest.getSingleAlertAttributes(ALERT_FIELDS, DISCRIMINATOR_1);

    assertThat(singleAlertAttributes)
        .containsEntry(MappedKeys.COUNTRY_KEY, Values.COUNTRY_UK)
        .containsEntry(MappedKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnEmptyFieldWhenRequestedAttributeDoesNotExistsInKibana() {
    storeData();

    Map<String, String> singleAlertAttributes =
        queryUnderTest.getSingleAlertAttributes(ALERT_FIELDS, DISCRIMINATOR_1);

    assertThat(singleAlertAttributes.get(MappedKeys.RISK_TYPE_KEY)).isNull();
  }

  @ParameterizedTest
  @MethodSource("getInvalidDiscriminators")
  @WithElasticAccessCredentials
  void shouldThrowExceptionWhenAlertIsNotFound(String discriminatorId) {
    storeData();

    assertThrows(
        AlertNotFoundException.class,
        () -> queryUnderTest.getSingleAlertAttributes(
            ALERT_FIELDS,
            discriminatorId));
  }

  @Test
  void shouldIgnoreMissingMatches() {
    underTest.indexAlerts(of(ALERT_WITHOUT_MATCHES), PRODUCTION_ELASTIC_INDEX_NAME);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME);
    assertThat(documentCount).isEqualTo(1);
  }

  @Test
  void shouldCreateSingleDocumentForEachAlert() {
    List<Alert> alerts = of(ALERT_1, ALERT_2);
    underTest.indexAlerts(alerts, PRODUCTION_ELASTIC_INDEX_NAME);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME);
    assertThat(documentCount).isEqualTo(alerts.size());
  }

  @Test
  void shouldReturnDiscriminators() {
    storeData();

    List<String> alertsIds =
        randomAlertQueryService.getRandomDiscriminatorByCriteria(ALERT_SEARCH_CRITERIA);

    assertThat(alertsIds).containsAnyElementsOf(of(DISCRIMINATOR_2, DISCRIMINATOR_3));
  }

  private void storeData() {
    saveAlert(DOCUMENT_ID, MAPPED_ALERT_1);
    saveAlert(DOCUMENT_ID_2, MAPPED_ALERT_2);
    saveAlert(DOCUMENT_ID_3, MAPPED_ALERT_3);
    saveAlert(DOCUMENT_ID_4, MAPPED_ALERT_4);
    saveAlert(DOCUMENT_ID_5, MAPPED_ALERT_5);
    saveAlert(DOCUMENT_ID_6, MAPPED_ALERT_6);
  }

  private void saveAlert(String discriminator, Map<String, Object> alert) {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, discriminator, alert);
  }

  private void cleanData() {
    safeDeleteIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }

  private void safeDeleteIndex(String index) {
    try {
      simpleElasticTestClient.removeIndex(index);
    } catch (ElasticsearchException e) {
      log.debug("index not present index={}", index);
    }
  }

  private static Stream<Arguments> getInvalidDiscriminators() {
    return Stream.of(
        Arguments.of(DISCRIMINATOR_2.substring(0, 8)),
        Arguments.of(DISCRIMINATOR_2 + "invalid"),
        Arguments.of(DISCRIMINATOR_2.replaceFirst("f", "x"))
    );
  }
}
