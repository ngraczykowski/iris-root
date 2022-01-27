package com.silenteight.warehouse.sampling.alert;

import com.silenteight.model.api.v1.SampleAlertServiceProto.Alert;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleRequest;
import com.silenteight.model.api.v1.SampleAlertServiceProto.AlertsSampleResponse;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.sampling.configuration.SamplingProperties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.silenteight.warehouse.sampling.alert.SamplingTestFixtures.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = SamplingTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class,
    OpendistroElasticContainerInitializer.class
})
@ActiveProfiles({ "jpa-test" })
@Transactional
abstract class SamplingAlertIndexService {

  @Autowired
  protected SamplingAlertService underTest;
  @Autowired
  protected SamplingProperties samplingProperties;

  static Stream<Arguments> getAlertsSampleRequests() {
    return Stream.of(
        Arguments.of(ALERTS_SAMPLE_REQUEST_1, REQUESTED_ALERT_COUNT_2,
            of(DISCRIMINATOR_4, DISCRIMINATOR_5)),
        Arguments.of(ALERTS_SAMPLE_REQUEST_2, REQUESTED_ALERT_COUNT_4,
            of(DISCRIMINATOR_2, DISCRIMINATOR_3, DISCRIMINATOR_5, DISCRIMINATOR_4)),
        Arguments.of(ALERTS_SAMPLE_REQUEST_3, 1, of(DISCRIMINATOR_1))

    );
  }

  @AfterEach
  void turnDown() {
    removeData();
  }

  protected abstract void removeData();

  @ParameterizedTest
  @MethodSource("getAlertsSampleRequests")
  @WithElasticAccessCredentials
  void shouldReturnCorrectNumberOfAlertsForSampling(
      AlertsSampleRequest req, int alertsCount, List<String> expectedNames) {
    // Given
    initDateForCorrectSamplingNumber();

    // When
    AlertsSampleResponse randomAlertResponse =
        underTest.generateSamplingAlerts(req);

    // Then
    int idsCount = randomAlertResponse.getAlertsCount();
    assertThat(idsCount).isEqualTo(alertsCount);
    assertThat(randomAlertResponse.getAlertsList()).containsExactlyInAnyOrderElementsOf(
        buildAlerts(expectedNames));
  }

  private static List<Alert> buildAlerts(List<String> alertName) {
    return alertName.stream().map(SamplingAlertIndexService::buildAlert).collect(toImmutableList());
  }

  private static Alert buildAlert(String alertName) {
    return Alert.newBuilder().setName(alertName).build();
  }

  protected abstract void initDateForCorrectSamplingNumber();

  @Test
  void shouldIncludeFilterQueryWhenSamplingAlerts() {
    // Given
    initDateForIncludedFilters();

    // When
    AlertsSampleResponse randomAlertResponse =
        underTest.generateSamplingAlerts(ALERTS_SAMPLE_REQUEST_2);

    // Then
    assertThat(randomAlertResponse.getAlertsCount()).isEqualTo(2);
    assertThat(randomAlertResponse.getAlertsList()).containsExactlyInAnyOrder(
        buildAlert(ALERT_NAME_4), buildAlert(ALERT_NAME_5));
  }

  protected abstract void initDateForIncludedFilters();
}
