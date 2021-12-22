package com.silenteight.warehouse.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;
import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.indexer.query.streaming.FieldDefinition;
import com.silenteight.warehouse.indexer.query.streaming.ReportFieldDefinitions;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys.RECOMMENDATION_KEY;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.RECOMMENDATION_FP;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.LocalDate.of;
import static java.time.LocalTime.MAX;
import static java.time.LocalTime.MIDNIGHT;
import static java.time.OffsetDateTime.of;
import static java.time.ZoneOffset.UTC;
import static java.util.List.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportFixture {

  public static final LocalDate LOCAL_DATE_FROM = of(2020, 10, 15);
  public static final LocalDate LOCAL_DATE_TO = of(2021, 10, 15);
  public static final OffsetDateTime OFFSET_DATE_TIME_FROM = of(LOCAL_DATE_FROM, MIDNIGHT, UTC);
  public static final OffsetDateTime OFFSET_DATE_TIME_TO =
      of(LOCAL_DATE_TO, MAX.minusSeconds(1), UTC);

  public static final String FILE_NAME = "report-test.csv";
  public static final QueryFilter QUERY_FILTER =
      new QueryFilter(RECOMMENDATION_KEY, of(RECOMMENDATION_FP));

  public static final String ALERT_COUNTRY_FIELD_LABEL = "Country LoB";
  public static final String ALERT_RECOMMENDATION_LABEL = "Alert Recommendation";
  public static final String ALERT_S8_ID_LABEL = "Alert S8 ID";
  public static final FieldDefinition FIELDS_DEFINITION_1 = FieldDefinition.builder()
      .name(COUNTRY_KEY)
      .label(ALERT_COUNTRY_FIELD_LABEL)
      .build();

  public static final FieldDefinition FIELDS_DEFINITION_2 = FieldDefinition.builder()
      .name(RECOMMENDATION_KEY)
      .label(ALERT_RECOMMENDATION_LABEL)
      .build();

  public static final FieldDefinition FIELDS_DEFINITION_3 = FieldDefinition.builder()
      .name(DISCRIMINATOR)
      .label(ALERT_S8_ID_LABEL)
      .build();

  public static final ReportFieldDefinitions REPORT_FIELDS_DEFINITION =
      ReportFieldDefinitions.builder()
          .fieldDefinitions(of(FIELDS_DEFINITION_1, FIELDS_DEFINITION_2, FIELDS_DEFINITION_3))
          .build();

  public static final FetchDataRequest FETCH_DATA_REQUEST = FetchDataRequest.builder()
      .fieldsDefinitions(REPORT_FIELDS_DEFINITION)
      .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
      .queryFilters(of(QUERY_FILTER))
      .dateField(INDEX_TIMESTAMP)
      .from(OFFSET_DATE_TIME_FROM)
      .to(OFFSET_DATE_TIME_TO)
      .name(FILE_NAME)
      .build();

  public static final FetchDataRequest FETCH_DATA_REQUEST_SQL = FetchDataRequest.builder()
      .fieldsDefinitions(REPORT_FIELDS_DEFINITION)
      .indexes(of(PRODUCTION_ELASTIC_INDEX_NAME))
      .queryFilters(of(QUERY_FILTER))
      .dateField(INDEX_TIMESTAMP)
      .from(OFFSET_DATE_TIME_FROM)
      .to(OFFSET_DATE_TIME_TO)
      .name(FILE_NAME)
      .useSqlReports(true)
      .selectSqlQuery("select count(*) from warehouse_alert")
      .sqlTemplates(Collections.emptyList())
      .build();

  public static final String PAYLOAD_DATA = "{"
      + "\"alert_id\": \"TEST[ANO][CUSTOMER][AAAGLOBAL186R1038]:ID:GG-ISCION-EX:20200212004\", "
      + "\"alert_step\": \"\", "
      + "\"s8_country\": \"MX\""
      + "}";
}
