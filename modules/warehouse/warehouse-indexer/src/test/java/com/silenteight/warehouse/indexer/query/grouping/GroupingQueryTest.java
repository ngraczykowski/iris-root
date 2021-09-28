package com.silenteight.warehouse.indexer.query.grouping;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClientException;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.INDEX_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.STATUS_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP_4;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.STATUS_COMPLETED;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.STATUS_ERROR;
import static com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService.EMPTY_VALUE_PLACEHOLDER;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = GroupingQueryTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
class GroupingQueryTest {

  private static final String NOT_EXISTING_KEY = "NOT_EXISTING_KEY";
  private static final String NOT_EXISTING_VALUE = "NOT_EXISTING_VALUE";

  @Autowired
  SimpleElasticTestClient testClient;

  @Autowired
  GroupingQueryService underTest;

  @AfterEach
  public void cleanup() {
    removeData();
  }

  @Test
  void shouldReturnGroupingByResult() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_4);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_5);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(COUNTRY_KEY, MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).hasSize(2);
    Row row = response.getRows().get(1);
    assertThat(row.getCount()).isEqualTo(2);
    assertThat(row.getValue(COUNTRY_KEY)).isEqualTo(Values.COUNTRY_UK);
    assertThat(row.getValue(MappedKeys.RISK_TYPE_KEY)).isEqualTo(Values.RISK_TYPE_PEP);
  }

  @Test
  void shouldReplaceNullValuesWithPlaceholder() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP).plusSeconds(1))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    Row row = response.getRows().get(0);
    assertThat(row.getValue(MappedKeys.RISK_TYPE_KEY)).isEqualTo(EMPTY_VALUE_PLACEHOLDER);
  }

  @Test
  void shouldHandleNonExistingValue() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(MappedKeys.RISK_TYPE_KEY, NOT_EXISTING_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).hasSize(1);
    Row row = response.getRows().get(0);
    String notExistingVey = row.getValueOrDefault(NOT_EXISTING_KEY, NOT_EXISTING_VALUE);
    assertThat(notExistingVey).isEqualTo(NOT_EXISTING_VALUE);
  }

  @Test
  void shouldHandleEmptyGroupValue() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(NOT_EXISTING_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).hasSize(1);
    Row row = response.getRows().get(0);
    assertThat(row.getCount()).isEqualTo(1);
  }

  @Test
  void shouldHandleException() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);

    FetchGroupedTimeRangedDataRequest invalidRequest = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of())
        .fields(of())
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP))
        .build();

    assertThatThrownBy(() -> underTest.generate(invalidRequest))
        .isInstanceOf(OpendistroElasticClientException.class)
        .extracting("statusCode")
        .isEqualTo(400);
  }

  @Test
  void shouldReturnDataThatMatchesFilters() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED))))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).hasSize(1);
    Row row = response.getRows().get(0);
    assertThat(row.getValue(STATUS_KEY)).isEqualTo(STATUS_COMPLETED);
  }

  @Test
  void shouldSupportMultipleValuesInFilters() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_6);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED, STATUS_ERROR))))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).hasSize(2);
  }

  @Test
  void shouldReturnEmptyResultIfAtLeastOneFilterFieldNotPresentInIndex() {
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_6);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED))))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).isEmpty();
  }

  private void removeData() {
    testClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}
