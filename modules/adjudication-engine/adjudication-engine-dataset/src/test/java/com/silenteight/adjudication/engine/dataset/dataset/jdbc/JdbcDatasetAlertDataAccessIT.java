package com.silenteight.adjudication.engine.dataset.dataset.jdbc;

import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = {
    JdbcDatasetAlertDataAccess.class,
    SelectDatasetsByAlertsQuery.class,
    CreateSingleMatchFilteredDatasetQuery.class
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

  @Test
  void shouldCreateSingleMatchDataset() {

    dataAccess.createSingleMatchFilteredDataset(
        3, List.of("solvingCMAPI"), OffsetDateTime.MIN, OffsetDateTime.MAX);

    var datasetIds = dataAccess.selectDatasetsByAlerts(List.of(4L, 5L));

    assertThat(datasetIds.size()).isEqualTo(1);
    assertThat(datasetIds.get(0)).isEqualTo(3);
  }

  @Test
  void shouldCreateSingleMatchDatasetWithoutLabels() {

    dataAccess.createSingleMatchFilteredDataset(
        3, Collections.emptyList(), OffsetDateTime.MIN, OffsetDateTime.MAX);

    var datasetIds = dataAccess.selectDatasetsByAlerts(List.of(4L, 5L));

    assertThat(datasetIds.size()).isEqualTo(1);
    assertThat(datasetIds.get(0)).isEqualTo(3);
  }
}
