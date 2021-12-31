package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static com.silenteight.universaldatasource.common.resource.AlertName.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("populate_feature_inputs.sql")
@Sql(scripts = "truncate_feature_inputs.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Import({
    JdbcFeatureDataAccess.class,
    StreamFeaturesQuery.class,
    FeatureDataAccessConfiguration.class,
    UpdateAgentInputTypeQuery.class,
    SelectOldAgentInputTypeQuery.class,
    DeleteFeatureQuery.class
})
class JdbcFeatureDataAccessIT extends BaseJdbcTest {

  @Autowired
  private JdbcFeatureDataAccess featureDataAccess;

  @Test
  void shouldDeleteFeatureInputs() {

    var alerts = getListOfAlerts(1, 2, 3, 4, 5);

    assertEquals(5, featureDataAccess.delete(alerts));
    assertEquals(6, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM uds_feature_input", Integer.class));
  }
}
