package com.silenteight.warehouse.indexer.alert;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import org.elasticsearch.ElasticsearchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERTS_WITH_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_SEARCH_CRITERIA;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.ResourceName.ALERT_NAME_1;
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
      ResourceName.ALERT_NAME_1,
      ResourceName.ALERT_NAME_2);

  static final List<String> ALERT_FIELDS = of(
      SourceAlertKeys.RECOMMENDATION_KEY,
      SourceAlertKeys.RISK_TYPE_KEY,
      SourceAlertKeys.COUNTRY_KEY);

  @Autowired
  private AlertQueryService queryUnderTest;

  @Autowired
  private AlertService underTest;

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

    AlertsAttributesListDto alertAttributesMapDto =
        queryUnderTest.getMultipleAlertsAttributes(ALERT_FIELDS, LIST_OF_ID);

    assertThat(alertAttributesMapDto.getAlerts().size()).isEqualTo(2);
    AlertAttributes alertAttributes = alertAttributesMapDto.getAlerts().get(1);
    assertThat(alertAttributes.getAttributes())
        .containsEntry(SourceAlertKeys.COUNTRY_KEY, Values.COUNTRY_UK);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnSingleAlertAttributes() {
    storeData();

    AlertAttributes singleAlertMapDto =
        queryUnderTest.getSingleAlertAttributes(ALERT_FIELDS, ALERT_NAME_1);

    assertThat(singleAlertMapDto.getAttributes())
        .containsEntry(SourceAlertKeys.COUNTRY_KEY, Values.COUNTRY_UK);
    assertThat(singleAlertMapDto.getAttributes())
        .containsEntry(SourceAlertKeys.RECOMMENDATION_KEY, Values.RECOMMENDATION_FP);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnEmptyFieldWhenRequestedAttributeDoesNotExistsInKibana() {
    storeData();

    AlertAttributes singleAlertMapDto =
        queryUnderTest.getSingleAlertAttributes(ALERT_FIELDS, ALERT_NAME_1);

    assertThat(singleAlertMapDto.getAttributes().get(SourceAlertKeys.RISK_TYPE_KEY)).isNull();
  }

  @ParameterizedTest
  @MethodSource("getInvalidAlertIds")
  @WithElasticAccessCredentials
  void shouldThrowExceptionWhenAlertIsNotFound(String alertId) {
    storeData();

    assertThrows(
        AlertNotFoundException.class,
        () -> queryUnderTest.getSingleAlertAttributes(
            ALERT_FIELDS,
            alertId));
  }

  @Test
  void shouldThrowExceptionWhenAlertHasZeroMatches() {
    assertThrows(
        ZeroMatchesException.class,
        () -> underTest.indexAlerts(of(ALERT_WITHOUT_MATCHES), PRODUCTION_ELASTIC_INDEX_NAME));
  }

  @Test
  void shouldCreateSingleDocumentForEachMatch() {
    underTest.indexAlerts(ALERTS_WITH_MATCHES, PRODUCTION_ELASTIC_INDEX_NAME);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(PRODUCTION_ELASTIC_INDEX_NAME);
    assertThat(documentCount).isEqualTo(4);
  }

  @Test
  void shouldReturnAlertsId() {
    storeData();

    List<String> alertsIds =
        randomAlertQueryService.getRandomAlertIdByCriteria(ALERT_SEARCH_CRITERIA);

    assertThat(alertsIds).containsAnyElementsOf(of(ALERT_ID_2, ALERT_ID_3));
  }

  private void storeData() {
    saveAlert(DOCUMENT_ID, MAPPED_ALERT_WITH_MATCHES_1);
    saveAlert(DOCUMENT_ID_2, MAPPED_ALERT_WITH_MATCHES_2);
    saveAlert(DOCUMENT_ID_3, MAPPED_ALERT_WITH_MATCHES_3);
    saveAlert(DOCUMENT_ID_4, MAPPED_ALERT_WITH_MATCHES_4);
    saveAlert(DOCUMENT_ID_5, MAPPED_ALERT_WITH_MATCHES_5);
    saveAlert(DOCUMENT_ID_6, MAPPED_ALERT_WITH_MATCHES_6);
  }

  private void saveAlert(String alertId, Map<String, Object> alert) {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, alertId, alert);
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

  private static Stream<Arguments> getInvalidAlertIds() {
    return Stream.of(
        Arguments.of(ALERT_ID_2.substring(0, 8)),
        Arguments.of(ALERT_ID_2 + "invalid"),
        Arguments.of(ALERT_ID_2.replaceFirst("f", "x"))
    );
  }
}
