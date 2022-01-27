package com.silenteight.warehouse.indexer.query.grouping;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_READ_ALIAS_NAME;
import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.STATUS_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP_4;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.STATUS_COMPLETED;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.STATUS_ERROR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static com.silenteight.warehouse.indexer.query.grouping.GroupingQueryService.EMPTY_VALUE_PLACEHOLDER;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = GroupingQueryTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class, PostgresTestInitializer.class })
@ActiveProfiles({ "jpa-test" })
class GroupingQueryTest {

  private static final String NOT_EXISTING_KEY = "NOT_EXISTING_KEY";
  private static final String NOT_EXISTING_VALUE = "NOT_EXISTING_VALUE";

  @Autowired
  SimpleElasticTestClient testClient;

  @Autowired
  GroupingQueryService underTest;

  @BeforeEach
  void init() {
    testClient.createDefaultIndexTemplate(
        PRODUCTION_ELASTIC_WRITE_INDEX_NAME, PRODUCTION_ELASTIC_READ_ALIAS_NAME);
  }

  @AfterEach
  void cleanup() {
    testClient.removeDefaultIndexTemplate();
    removeData();
  }

  private void removeData() {
    testClient.removeIndex(PRODUCTION_ELASTIC_WRITE_INDEX_NAME);
  }

  @Test
  void shouldReturnGroupingByResult() {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_4);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_5);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
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
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
        .fields(of(MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP).plusSeconds(1))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    Row row = response.getRows().get(0);
    assertThat(row
        .getValueOrDefault(MappedKeys.RISK_TYPE_KEY, EMPTY_VALUE_PLACEHOLDER))
        .isEqualTo(EMPTY_VALUE_PLACEHOLDER);
  }

  @Test
  void shouldHandleNonExistingValue() {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
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
  void shouldReturnDataThatMatchesFilters() {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
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
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_6);
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
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
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_6);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_READ_ALIAS_NAME))
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED))))
        .build();

    FetchGroupedDataResponse response = underTest.generate(request);

    assertThat(response.getRows()).isEmpty();
  }

  @Test
  void shouldHandleNullOrMissingValuesConsistently() {
    testClient.storeData(PRODUCTION_ELASTIC_WRITE_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_1);

    Map<String, String> data = new HashMap<>();
    data.put("alert_qa.level-0.state", null);
    data.put("alert_analyst_decision", "existing value");

    var resp = FetchGroupedDataResponse.builder()
        .rows(List.of(
            Row.builder()
                .count(2)
                .data(data)
                .build()))
        .build();

    assertThat(resp.getRows().get(0)
        .getValueOrDefault("alert_qa.level-0.state", NOT_EXISTING_VALUE))
        .isEqualTo(NOT_EXISTING_VALUE);
    assertThat(resp.getRows().get(0)
        .getValueOrDefault(NOT_EXISTING_KEY, NOT_EXISTING_VALUE))
        .isEqualTo(NOT_EXISTING_VALUE);
    assertThat(resp.getRows().get(0)
        .getValueOrDefault("alert_analyst_decision", NOT_EXISTING_VALUE))
        .isEqualTo("existing value");
  }
}
