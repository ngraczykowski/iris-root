package com.silenteight.warehouse.sampling.alert;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

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
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.sampling.alert.SamplingTestFixtures.*;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = SamplingTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
class SamplingAlertServiceTest {

  @Autowired
  private SamplingAlertService underTest;

  @Autowired
  private SimpleElasticTestClient simpleElasticTestClient;

  @Autowired
  private SamplingProperties samplingProperties;

  @AfterEach
  void removeData() {
    simpleElasticTestClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }

  @ParameterizedTest
  @MethodSource("getAlertsSampleRequests")
  @WithElasticAccessCredentials
  void shouldReturnCorrectNumberOfAlertsForSampling(
      AlertsSampleRequest req, int alertsCount, List<String> expectedIds) {

    storeData();

    AlertsSampleResponse randomAlertResponse =
        underTest.generateSamplingAlerts(req);

    int idsCount = randomAlertResponse.getAlertsCount();
    assertThat(idsCount).isEqualTo(alertsCount);
    List<String> idsList = convertResponseToIdsList(randomAlertResponse);
    assertThat(idsList).containsExactlyInAnyOrder(expectedIds.toArray(String[]::new));
  }

  @Test
  void shouldIncludeFilterQueryWhenSamplingAlerts() {
    saveAlert(DOCUMENT_ID_4, ALERT_4_MAP);
    saveAlert(DOCUMENT_ID_5, ALERT_5_MAP);
    saveAlert(DOCUMENT_ID_6, ALERT_6_MAP);

    AlertsSampleResponse randomAlertResponse =
        underTest.generateSamplingAlerts(ALERTS_SAMPLE_REQUEST_2);

    var allowedValues = samplingProperties.getQueryFilters().get(0).getAllowedValues();
    assertThat(ALERT_4_MAP.get(ALERT_PREFIX + ALERT_STATUS_KEY)).isIn(allowedValues);
    assertThat(ALERT_5_MAP.get(ALERT_PREFIX + ALERT_STATUS_KEY)).isIn(allowedValues);
    assertThat(ALERT_6_MAP.get(ALERT_PREFIX + ALERT_STATUS_KEY)).isNotIn(allowedValues);
    assertThat(randomAlertResponse.getAlertsCount()).isEqualTo(2);
  }

  private List<String> convertResponseToIdsList(AlertsSampleResponse randomAlertResponse) {
    return randomAlertResponse
        .getAlertsList()
        .stream()
        .map(Alert::getName)
        .collect(toList());
  }

  private void storeData() {
    saveAlert(DOCUMENT_ID_1, ALERT_1_MAP);
    saveAlert(DOCUMENT_ID_2, ALERT_2_MAP);
    saveAlert(DOCUMENT_ID_3, ALERT_3_MAP);
    saveAlert(DOCUMENT_ID_4, ALERT_4_MAP);
    saveAlert(DOCUMENT_ID_5, ALERT_5_MAP);
  }

  private void saveAlert(String discriminator, Map<String, Object> alert) {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, discriminator, alert);
  }

  private static Stream<Arguments> getAlertsSampleRequests() {
    return Stream.of(
        Arguments.of(ALERTS_SAMPLE_REQUEST_1, REQUESTED_ALERT_COUNT_2,
            of(DISCRIMINATOR_4, DISCRIMINATOR_5)),
        Arguments.of(ALERTS_SAMPLE_REQUEST_2, REQUESTED_ALERT_COUNT_4,
            of(DISCRIMINATOR_2, DISCRIMINATOR_3, DISCRIMINATOR_5, DISCRIMINATOR_4))
    );
  }
}
