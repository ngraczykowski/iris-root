package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.silenteight.universaldatasource.common.resource.AlertName.getListOfAlerts;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("populate_feature_inputs.sql")
@ContextConfiguration(classes = {
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

    var alerts = getListOfAlerts(List.of(1, 2, 3, 4, 5));

    assertEquals(5, featureDataAccess.delete(alerts));
    assertEquals(5, jdbcTemplate.queryForObject(
        "SELECT count(*) FROM uds_feature_input", Integer.class));
  }
}
