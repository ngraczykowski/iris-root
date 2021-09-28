package com.silenteight.warehouse.indexer.query.streaming;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;
import com.silenteight.warehouse.common.testing.rest.WithElasticAccessCredentials;
import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.ImmutableList.of;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.INDEX_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.*;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.ANALYST_DECISION_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.RECOMMENDATION_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.*;
import static com.silenteight.warehouse.indexer.query.streaming.StreamingFixtures.RESOURCE_NAME;
import static java.time.OffsetDateTime.parse;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = ScrollSearchTestConfiguration.class)
@ContextConfiguration(initializers = OpendistroElasticContainerInitializer.class)
class ScrollSearchQueryServiceTest {

  @Autowired
  private SimpleElasticTestClient testClient;

  @Autowired
  private ScrollSearchStreamingService underTest;

  @AfterEach
  public void cleanup() {
    removeData();
  }

  @Test
  @WithElasticAccessCredentials
  void shouldFeedConsumerWith3Hits() {
    // given
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_4);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_5);
    List<String> rows = new ArrayList<>();
    Consumer<Collection<String>> consumer = rows::addAll;

    FetchDataRequest request = FetchDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(RECOMMENDATION_KEY))
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_3))
        .dateField(INDEX_TIMESTAMP)
        .name(RESOURCE_NAME)
        .build();

    // when
    underTest.fetchData(request, consumer);

    // then
    List<String> normalizedRows = normalizeRows(rows);
    assertThat(normalizedRows)
        .hasSize(4)
        .contains(RECOMMENDATION_FP, RECOMMENDATION_MI);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldNotReturnRowsWhenDateRangeDontMatchHits() {
    // given
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_3);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_4);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_5);
    List<String> rows = new ArrayList<>();
    Consumer<Collection<String>> consumer = rows::addAll;

    FetchDataRequest request = FetchDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(RECOMMENDATION_KEY))
        .from(parse(PROCESSING_TIMESTAMP_5))
        .to(parse(PROCESSING_TIMESTAMP_6))
        .dateField(INDEX_TIMESTAMP)
        .name(RESOURCE_NAME)
        .build();

    // when
    underTest.fetchData(request, consumer);

    // then
    List<String> normalizedRows = normalizeRows(rows);
    assertThat(normalizedRows)
        .hasSize(1)
        .doesNotContain(RECOMMENDATION_FP, RECOMMENDATION_MI);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldFilterOnlyAnalystDecisionAllowedValuesRows() {
    // given
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_8);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_9);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_10);
    List<String> rows = new ArrayList<>();
    Consumer<Collection<String>> consumer = rows::addAll;

    FetchDataRequest request = FetchDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(ANALYST_DECISION_KEY))
        .queryFilters(of(toQueryFilter()))
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .dateField(INDEX_TIMESTAMP)
        .name(RESOURCE_NAME)
        .build();

    // when
    underTest.fetchData(request, consumer);

    // then
    List<String> normalizedRows = normalizeRows(rows);
    assertThat(normalizedRows)
        .hasSize(3)
        .contains(ANALYST_DECISION_FP, ANALYST_DECISION_TP)
        .doesNotContain(ANALYST_DECISION_UNKNOWN);
  }

  @Test
  @WithElasticAccessCredentials
  void shouldReturnEmptyReportWhenFieldsAreMissing() {
    // given
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID, MAPPED_ALERT_8);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_2, MAPPED_ALERT_9);
    testClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, DOCUMENT_ID_3, MAPPED_ALERT_10);
    List<String> rows = new ArrayList<>();
    Consumer<Collection<String>> consumer = rows::addAll;

    FetchDataRequest request = FetchDataRequest.builder()
        .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
        .fields(of(COUNTRY_KEY))
        .from(parse(PROCESSING_TIMESTAMP))
        .to(parse(PROCESSING_TIMESTAMP_4))
        .dateField(INDEX_TIMESTAMP)
        .name(RESOURCE_NAME)
        .build();

    // when
    underTest.fetchData(request, consumer);

    // then
    assertThat(rows).hasSize(1);
  }

  private List<String> normalizeRows(List<String> rows) {
    return rows.stream()
        .map(StringUtils::normalizeSpace)
        .collect(toList());
  }

  private QueryFilter toQueryFilter() {
    return new QueryFilter(ANALYST_DECISION_KEY, of(ANALYST_DECISION_FP, ANALYST_DECISION_TP));
  }

  private void removeData() {
    testClient.removeIndex(PRODUCTION_ELASTIC_INDEX_NAME);
  }
}