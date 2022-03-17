package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelLearningJobProperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseLearningJobsConsistencyHealthIndicatorTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;

  private ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;

  private static final String DB_RELATION = "dbRelationName";
  private static final String CBS_VIEW = "cbsView";

  @BeforeEach
  void setUp() throws SQLException {
    watchlistLevelLearningJobProperties = new ScbBridgeWatchlistLevelLearningJobProperties();
    watchlistLevelLearningJobProperties.setDbRelationName(DB_RELATION);
    watchlistLevelLearningJobProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);

    alertLevelLearningJobProperties = new ScbBridgeAlertLevelLearningJobProperties();
    alertLevelLearningJobProperties.setDbRelationName(DB_RELATION);
    alertLevelLearningJobProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);

    ecmBridgeLearningJobProperties = new EcmBridgeLearningJobProperties();
    ecmBridgeLearningJobProperties.setDbRelationName(DB_RELATION);
    ecmBridgeLearningJobProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
  }

  @Test
  void shouldVerifyWatchlistLearningViewsWithStatusUp() throws SQLException {
    //given
    watchlistLevelLearningJobProperties.setEnabled(true);

    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  void shouldVerifyWatchlistLearningViewsWithStatusDown() throws SQLException {
    //given
    watchlistLevelLearningJobProperties.setEnabled(true);

    when(resultSet.next())
        .thenReturn(true)
        .thenThrow(new SQLException("ORA-00904"));

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  void shouldVerifyAlertLearningViewsWithStatusUp() throws SQLException {
    //given
    alertLevelLearningJobProperties.setEnabled(true);

    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  void shouldVerifyAlertLearningViewsWithStatusDown() throws SQLException {
    //given
    alertLevelLearningJobProperties.setEnabled(true);

    when(resultSet.next())
        .thenReturn(true)
        .thenThrow(new SQLException("ORA-00904"));

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.DOWN);
  }

  private DatabaseLearningJobsConsistencyHealthIndicator createHealthIndicator() {
    return new DatabaseLearningJobsConsistencyHealthIndicator(
        dataSource,
        watchlistLevelLearningJobProperties,
        alertLevelLearningJobProperties,
        ecmBridgeLearningJobProperties);
  }
}
