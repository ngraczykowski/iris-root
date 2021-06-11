package com.silenteight.warehouse.indexer.alert;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.indexer.alert.AlertsAttributesListDto.AlertAttributes;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERTS_WITH_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.ALERT_WITHOUT_MATCHES;
import static com.silenteight.warehouse.indexer.alert.DataIndexFixtures.INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AlertTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
})
class AlertIT {

  private static final String ELASTIC_SIMULATION_INDEX_NAME =
      "itest_simulation_" + SIMULATION_ANALYSIS_ID;

  @Autowired
  private AlertService underTest;

  @Autowired
  private RestHighLevelClient restHighLevelClient;

  @BeforeEach
  void init() {
    storeData();
  }

  @Test
  void shouldReturnAlertsAttributesListDto() {
    //when
    AlertsAttributesListDto alertAttributesMapDto =
        underTest.getMultipleAlertsAttributes(ALERT_FIELDS, LIST_OF_ID);
    //then
    assertThat(alertAttributesMapDto.getAlerts().size()).isEqualTo(2);
    AlertAttributes alertAttributes = alertAttributesMapDto.getAlerts().get(1);
    assertThat(alertAttributes.getAttributes()).containsEntry(ALERT_COUNTRY_KEY, "UK");
  }

  @Test
  void shouldReturnSingleAlertAttributes() {
    //when
    AlertAttributes singleAlertMapDto =
        underTest.getSingleAlertAttributes(
            ALERT_FIELDS, "alerts/457b1498-e348-4a81-8093-6079c1173010");
    //then
    assertThat(singleAlertMapDto.getAttributes()).containsEntry(ALERT_COUNTRY_KEY, "UK");
    assertThat(singleAlertMapDto.getAttributes())
        .containsEntry(ALERT_RECOMMENDATION_KEY, "FALSE_POSITIVE");
  }

  @Test
  void shouldReturnEmptyFieldWhenRequestedAttributeDoesNotExistsInKibana() {
    //when
    AlertAttributes singleAlertMapDto =
        underTest.getSingleAlertAttributes(
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

  @SneakyThrows
  @Test
  @Disabled("WEB-1106")
  void shouldCreateSingleDocumentForEachMatch() {
    ArgumentCaptor<BulkRequest> argumentCaptor = forClass(BulkRequest.class);
    doNothing().when(restHighLevelClient).bulk(argumentCaptor.capture(), eq(DEFAULT));

    underTest.indexAlerts(ALERTS_WITH_MATCHES, INDEX_NAME);

    int operationsCount = argumentCaptor.getValue().numberOfActions();
    assertThat(operationsCount).isEqualTo(4);
  }

  @SneakyThrows
  private void storeData() {
    saveAlert(DOCUMENT_ID_1, ALERT_WITH_MATCHES_1_MAP);
    saveAlert(DOCUMENT_ID_2, ALERT_WITH_MATCHES_2_MAP);
    saveAlert(DOCUMENT_ID_3, ALERT_WITH_MATCHES_3_MAP);
  }

  private void saveAlert(String alertId, Map<String, Object> alert) throws IOException {
    IndexRequest indexRequest = new IndexRequest(ELASTIC_SIMULATION_INDEX_NAME);
    indexRequest.id(alertId);
    indexRequest.source(alert);

    indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
  }
}
