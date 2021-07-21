package com.silenteight.warehouse.indexer.query;

import com.silenteight.warehouse.indexer.alert.AlertMapperConstants;
import com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.MappedKeys;
import com.silenteight.warehouse.indexer.query.SqlBuilder;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.indexer.alert.MappedAlertFixtures.Values.PROCESSING_TIMESTAMP;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class SqlBuilderTest {

  private final SqlBuilder underTest = new SqlBuilder();

  private final OffsetDateTime timestamp = parse(PROCESSING_TIMESTAMP);

  @Test
  void shouldCreateSqlQuery() {
    List<String> fields = of(MappedKeys.COUNTRY_KEY, MappedKeys.RISK_TYPE_KEY);
    String query = underTest.groupByBetweenDates(of(PRODUCTION_ELASTIC_INDEX_NAME), fields,
        AlertMapperConstants.INDEX_TIMESTAMP, timestamp, timestamp.plusHours(1));

    assertThat(query).isEqualToIgnoringWhitespace(""
        + "select `alert_lob_country`, `alert_risk_type`, count(*) "
        + "from itest_production "
        + "where (index_timestamp >= timestamp('2021-04-15 12:17:37.098') "
        + "  and  index_timestamp < timestamp('2021-04-15 13:17:37.098')) "
        + "group by `alert_lob_country`, `alert_risk_type`");
  }
}
