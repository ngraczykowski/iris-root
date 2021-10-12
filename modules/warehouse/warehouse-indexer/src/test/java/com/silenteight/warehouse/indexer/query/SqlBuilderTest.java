package com.silenteight.warehouse.indexer.query;

import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values;
import com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants;
import com.silenteight.warehouse.indexer.query.sql.MultiValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SingleValueCondition;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.indexer.IndexerFixtures.PRODUCTION_ELASTIC_WRITE_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.OffsetDateTime.parse;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class SqlBuilderTest {

  private final SqlBuilder underTest = new SqlBuilder();

  private static final OffsetDateTime TIMESTAMP = parse(PROCESSING_TIMESTAMP);
  private static final SingleValueCondition FROM =
      new SingleValueCondition(AlertMapperConstants.INDEX_TIMESTAMP, TIMESTAMP);
  private static final SingleValueCondition TO =
      new SingleValueCondition(AlertMapperConstants.INDEX_TIMESTAMP, TIMESTAMP.plusHours(1));

  @Test
  void shouldCreateSqlQueryForValuesMatchingConditions() {
    var conditions = of(new MultiValueCondition(
        MappedKeys.STATUS_KEY, Values.STATUS_COMPLETED, Values.STATUS_ERROR));
    String query = underTest.groupByBetweenDates(
        of(PRODUCTION_ELASTIC_WRITE_INDEX_NAME),
        of(MappedKeys.COUNTRY_KEY),
        conditions,
        FROM,
        TO);

    assertThat(query).isEqualToIgnoringWhitespace(""
        + "select `alert_lob_country`, count(*) "
        + "from `itest_production.2021-04-15` "
        + "where (index_timestamp >= timestamp('2021-04-15 12:17:37.098') "
        + "  and  index_timestamp < timestamp('2021-04-15 13:17:37.098') "
        + "and (alert_status = 'COMPLETED' or alert_status = 'ERROR')) "
        + "group by `alert_lob_country`");
  }

  @Test
  void shouldCreateSqlQueryForAllReportStatus() {
    String query = underTest.groupByBetweenDates(
        of(PRODUCTION_ELASTIC_WRITE_INDEX_NAME),
        of(MappedKeys.COUNTRY_KEY, MappedKeys.RISK_TYPE_KEY),
        emptyList(),
        FROM,
        TO);

    assertThat(query).isEqualToIgnoringWhitespace(""
        + "select `alert_lob_country`, `alert_risk_type`, count(*) "
        + "from `itest_production.2021-04-15` "
        + "where (index_timestamp >= timestamp('2021-04-15 12:17:37.098') "
        + "  and  index_timestamp < timestamp('2021-04-15 13:17:37.098')) "
        + "group by `alert_lob_country`, `alert_risk_type`");
  }
}
