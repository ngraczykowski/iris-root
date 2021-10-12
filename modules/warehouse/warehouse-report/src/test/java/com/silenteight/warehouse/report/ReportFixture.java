package com.silenteight.warehouse.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.RECOMMENDATION_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP_3;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_FP;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportFixture {

  public static final OffsetDateTime FROM_TIMESTAMP = parse(PROCESSING_TIMESTAMP);
  public static final OffsetDateTime TO_TIMESTAMP = parse(PROCESSING_TIMESTAMP_3);
  public static final String FILE_NAME = "report-test.csv";

  public static final QueryFilter QUERY_FILTER =
      new QueryFilter(RECOMMENDATION_KEY, of(RECOMMENDATION_FP));

  public static final FetchDataRequest FETCH_DATA_REQUEST = FetchDataRequest.builder()
      .fields(of(COUNTRY_KEY, RECOMMENDATION_KEY, DISCRIMINATOR))
      .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
      .queryFilters(of(QUERY_FILTER))
      .dateField(INDEX_TIMESTAMP)
      .from(FROM_TIMESTAMP)
      .to(TO_TIMESTAMP)
      .name(FILE_NAME)
      .build();
}
