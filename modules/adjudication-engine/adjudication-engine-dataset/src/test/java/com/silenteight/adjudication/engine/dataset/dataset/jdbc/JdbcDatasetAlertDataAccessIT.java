package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
    JdbcDatasetAlertDataAccess.class,
    SelectDatasetsByAlertsQuery.class
})
@Sql
class JdbcDatasetAlertDataAccessIT extends BaseJdbcTest {

  @Autowired
  JdbcDatasetAlertDataAccess dataAccess;

  @Test
  void shouldSelectDataset() {
    var datasetIds = dataAccess.selectDatasetsByAlerts(List.of(1L, 2L));
    assertThat(datasetIds.size()).isEqualTo(1);
  }
}
