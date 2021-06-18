package com.silenteight.warehouse.indexer.alert;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;
import java.util.stream.Stream;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERTS_WITH_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.INDEX_NAME;
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
class AlertIT {

  @Autowired
  private AlertQueryService queryUnderTest;

  @Autowired
  private AlertService underTest;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private ElasticsearchProperties properties;

  @BeforeEach
  void init() {
    storeData();
  }

  @AfterEach
  void cleanup() {
    cleanData();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnAlertsAttributesListDto() {
    //when
    AlertsAttributesListDto alertAttributesMapDto =
        queryUnderTest.getMultipleAlertsAttributes(ALERT_FIELDS, LIST_OF_ID);

    //then
    assertThat(alertAttributesMapDto.getAlerts().size()).isEqualTo(2);
    AlertAttributes alertAttributes = alertAttributesMapDto.getAlerts().get(1);
    assertThat(alertAttributes.getAttributes()).containsEntry(ALERT_COUNTRY_KEY, "UK");
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnSingleAlertAttributes() {
    //when
    AlertAttributes singleAlertMapDto =
        queryUnderTest.getSingleAlertAttributes(
            ALERT_FIELDS, "alerts/457b1498-e348-4a81-8093-6079c1173010");

    //then
    assertThat(singleAlertMapDto.getAttributes()).containsEntry(ALERT_COUNTRY_KEY, "UK");
    assertThat(singleAlertMapDto.getAttributes())
        .containsEntry(ALERT_RECOMMENDATION_KEY, "FALSE_POSITIVE");
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnEmptyFieldWhenRequestedAttributeDoesNotExistsInKibana() {
    //when
    AlertAttributes singleAlertMapDto =
        queryUnderTest.getSingleAlertAttributes(
            ALERT_FIELDS_WITH_ONE_NON_EXISTING, "alerts/457b1498-e348-4a81-8093-6079c1173010");

    //then
    assertThat(singleAlertMapDto.getAttributes().get(ALERT_RISK_TYPE_KEY)).isNull();
  }

  @Test
  void shouldThrowExceptionWhenAlertHasZeroMatches() {
    assertThrows(
        ZeroMatchesException.class,
        () -> underTest.indexAlerts(of(ALERT_WITHOUT_MATCHES), INDEX_NAME));
  }

  @Test
  void shouldCreateSingleDocumentForEachMatch() {
    underTest.indexAlerts(ALERTS_WITH_MATCHES, INDEX_NAME);

    await()
        .atMost(5, SECONDS)
        .until(() -> simpleElasticTestClient.getDocumentCount(INDEX_NAME) > 0);

    long documentCount = simpleElasticTestClient.getDocumentCount(INDEX_NAME);
    assertThat(documentCount).isEqualTo(4);
  }

  @ParameterizedTest
  @MethodSource("getInvalidAlertIds")
  @WithElasticAccessCredentials
  void shouldThrowExceptionWhenAlertIsNotFound(String alertId) {
    assertThrows(
        AlertNotFoundException.class,
        () -> queryUnderTest.getSingleAlertAttributes(
            ALERT_FIELDS,
            alertId));
  }

  private void storeData() {
    saveAlert(DOCUMENT_ID_1, ALERT_WITH_MATCHES_1_MAP);
    saveAlert(DOCUMENT_ID_2, ALERT_WITH_MATCHES_2_MAP);
    saveAlert(DOCUMENT_ID_3, ALERT_WITH_MATCHES_3_MAP);
  }

  private void saveAlert(String alertId, Map<String, Object> alert) {
    simpleElasticTestClient.storeData(properties.getProductionQueryIndex(), alertId, alert);
  }

  private void cleanData() {
    simpleElasticTestClient.removeIndex(properties.getProductionQueryIndex());
  }

  private static Stream<Arguments> getInvalidAlertIds() {
    return Stream.of(
        Arguments.of(ALERT_ID_2.substring(0, 8)),
        Arguments.of(ALERT_ID_2 + "invalid"),
        Arguments.of(ALERT_ID_2.replaceFirst("f", "x"))
    );
  }
}
