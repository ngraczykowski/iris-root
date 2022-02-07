package com.silenteight.warehouse.indexer.query.grouping;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.grouping.FetchGroupedDataResponse.Row;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.STATUS_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.*;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = GroupingQueryTestConfiguration.class)
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class })
@ActiveProfiles({ "jpa-test" })
@Transactional
abstract class GroupingQueryServiceTest {

  private static final String NOT_EXISTING_KEY = "NOT_EXISTING_KEY";
  private static final String NOT_EXISTING_VALUE = "NOT_EXISTING_VALUE";
  private static final String EMPTY_VALUE_PLACEHOLDER = "";

  @Test
  void shouldReturnGroupingByResult() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_3, PROCESSING_TIMESTAMP, ALERT_NAME_3);
    insertRow(DOCUMENT_ID_2, MAPPED_ALERT_4, PROCESSING_TIMESTAMP_2, ALERT_NAME_4);
    insertRow(DOCUMENT_ID_3, MAPPED_ALERT_5, PROCESSING_TIMESTAMP_3, ALERT_NAME_5);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(COUNTRY_KEY, MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    assertThat(response.getRows()).contains(
        Row
            .builder()
            .count(2)
            .data(Map.of(COUNTRY_KEY, COUNTRY_UK, MappedKeys.RISK_TYPE_KEY, RISK_TYPE_PEP))
            .build(),
        Row
            .builder()
            .count(1)
            .data(Map.of(COUNTRY_KEY, COUNTRY_PL, MappedKeys.RISK_TYPE_KEY, RISK_TYPE_PEP))
            .build()
    );
  }

  protected abstract GroupingQueryService provideServiceForTest();

  protected abstract void insertRow(
      String id, Map<String, Object> alertMapping, String processingTimeStamp, String alertName);

  @Test
  void shouldReplaceNullValuesWithPlaceholder() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_1, PROCESSING_TIMESTAMP, ALERT_NAME_1);
    insertRow(DOCUMENT_ID_3, MAPPED_ALERT_3, PROCESSING_TIMESTAMP, ALERT_NAME_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(MappedKeys.RISK_TYPE_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP).plusSeconds(1))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    Row row = response.getRows().get(0);
    assertThat(row
        .getValueOrDefault(MappedKeys.RISK_TYPE_KEY, EMPTY_VALUE_PLACEHOLDER))
        .isEqualTo(EMPTY_VALUE_PLACEHOLDER);
  }

  @Test
  void shouldHandleNonExistingValue() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_3, PROCESSING_TIMESTAMP, ALERT_NAME_3);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(MappedKeys.RISK_TYPE_KEY, NOT_EXISTING_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    assertThat(response.getRows()).hasSize(1);
    Row row = response.getRows().get(0);
    String notExistingVey = row.getValueOrDefault(NOT_EXISTING_KEY, NOT_EXISTING_VALUE);
    assertThat(notExistingVey).isEqualTo(NOT_EXISTING_VALUE);
  }

  @Test
  void shouldReturnDataThatMatchesFilters() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_3, PROCESSING_TIMESTAMP, ALERT_NAME_3);
    insertRow(DOCUMENT_ID_2, MAPPED_ALERT_7, PROCESSING_TIMESTAMP, ALERT_NAME_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED))))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    assertThat(response.getRows()).hasSize(1);
    Row row = response.getRows().get(0);
    assertThat(row.getValue(STATUS_KEY)).isEqualTo(STATUS_COMPLETED);
  }

  @Test
  void shouldSupportMultipleValuesInFilters() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_1, PROCESSING_TIMESTAMP, ALERT_NAME_1);
    insertRow(DOCUMENT_ID_2, MAPPED_ALERT_6, PROCESSING_TIMESTAMP_4, ALERT_NAME_6);
    insertRow(DOCUMENT_ID_3, MAPPED_ALERT_7, PROCESSING_TIMESTAMP, ALERT_NAME_7);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED, STATUS_ERROR))))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    assertThat(response.getRows()).hasSize(2);
  }

  @Test
  void shouldReturnEmptyResultIfAtLeastOneFilterFieldNotPresentInIndex() {
    insertRow(DOCUMENT_ID_2, MAPPED_ALERT_6, PROCESSING_TIMESTAMP_4, ALERT_NAME_6);

    FetchGroupedTimeRangedDataRequest request = FetchGroupedTimeRangedDataRequest.builder()
        .fields(of(STATUS_KEY))
        .dateField(INDEX_TIMESTAMP)
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .queryFilters(of(new QueryFilter(STATUS_KEY, of(STATUS_COMPLETED))))
        .build();

    FetchGroupedDataResponse response = provideServiceForTest().generate(request);

    assertThat(response.getRows()).isEmpty();
  }

  @Test
  void shouldHandleNullOrMissingValuesConsistently() {
    insertRow(DOCUMENT_ID, MAPPED_ALERT_1, PROCESSING_TIMESTAMP, ALERT_NAME_1);

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
