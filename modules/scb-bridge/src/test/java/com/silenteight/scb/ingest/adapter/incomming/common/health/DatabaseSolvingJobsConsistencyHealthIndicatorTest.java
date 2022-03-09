package com.silenteight.scb.ingest.adapter.incomming.common.health;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelSolvingJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelSolvingJobProperties;

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
class DatabaseSolvingJobsConsistencyHealthIndicatorTest {

  @Mock
  private DataSource dataSource;
  @Mock
  private Connection connection;
  @Mock
  private PreparedStatement preparedStatement;
  @Mock
  private ResultSet resultSet;

  private CbsConfigProperties cbsConfigProperties;
  private ScbBridgeAlertOrderProperties scbBridgeAlertOrderProperties;
  private ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingProperties;
  private ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;

  private static final String DB_RELATION = "dbRelationName";
  private static final String CBS_VIEW = "cbsView";
  private static final String ACK_FUNCTION = "ackFunction";
  private static final String RECOM_FUNCTION = "recomFunction";

  @BeforeEach
  void setUp() throws SQLException {
    scbBridgeAlertOrderProperties = new ScbBridgeAlertOrderProperties();
    scbBridgeAlertOrderProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);
    scbBridgeAlertOrderProperties.setDbRelationName(DB_RELATION);

    cbsConfigProperties = new CbsConfigProperties();

    setupAlertLevelSolvingProperties();
    setupCbsConfigProperties();
    setupWatchlistLevelSolvingProperties();

    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
  }

  @Test
  void shouldVerifyAlertOrderViewsAndFunctionsWithStatusUp() throws SQLException {
    //given
    alertLevelSolvingProperties.setEnabled(false);
    watchlistLevelSolvingJobProperties.setEnabled(false);

    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  void shouldVerifyAlertOrderCbsViewWithStatusUp() throws SQLException {
    //given
    scbBridgeAlertOrderProperties.setCbsHitsDetailsHelperViewName("");
    alertLevelSolvingProperties.setEnabled(false);
    watchlistLevelSolvingJobProperties.setEnabled(false);

    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(1)).next();
    assertThat(health.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  void shouldVerifyAlertOrderViewsAndFunctionsWithStatusDown() throws SQLException {
    //given
    alertLevelSolvingProperties.setEnabled(false);
    watchlistLevelSolvingJobProperties.setEnabled(false);
    when(resultSet.next())
        .thenReturn(true)
        .thenThrow(new SQLException());

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(2)).next();
    assertThat(health.getStatus()).isEqualTo(Status.DOWN);
  }

  @Test
  void shouldVerifySolvingViewsAndFunctionsWithStatusUpViewPresent() throws SQLException {
    //given
    alertLevelSolvingProperties.setEnabled(true);
    watchlistLevelSolvingJobProperties.setEnabled(true);

    // Used for function check
    when(preparedStatement.execute())
        .thenReturn(true)
        .thenThrow(new SQLException("ORA-06553"))
        .thenReturn(true);

    // Used for table or view check
    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(6)).next();
    verify(preparedStatement, times(3)).execute();
    assertThat(health.getStatus()).isEqualTo(Status.UP);
  }

  @Test
  void shouldVerifySolvingViewsAndFunctionsWithStatusDownFunctionMissing() throws SQLException {
    //given
    alertLevelSolvingProperties.setEnabled(true);
    watchlistLevelSolvingJobProperties.setEnabled(true);

    // Used for function check
    when(preparedStatement.execute())
        .thenReturn(true)
        .thenReturn(true)
        .thenThrow(new SQLException("ORA-00904"));

    // Used for table or view check
    when(resultSet.next()).thenReturn(true);

    //when
    final Health health = createHealthIndicator().health();

    //then
    verify(resultSet, times(6)).next();
    verify(preparedStatement, times(3)).execute();
    assertThat(health.getStatus()).isEqualTo(Status.DOWN);
  }

  private DatabaseSolvingJobsConsistencyHealthIndicator createHealthIndicator() {
    return new DatabaseSolvingJobsConsistencyHealthIndicator(
        cbsConfigProperties,
        dataSource,
        scbBridgeAlertOrderProperties,
        alertLevelSolvingProperties,
        watchlistLevelSolvingJobProperties);
  }

  private void setupAlertLevelSolvingProperties() {
    alertLevelSolvingProperties = new ScbBridgeAlertLevelSolvingJobProperties();
    alertLevelSolvingProperties.setDbRelationName(DB_RELATION);
    alertLevelSolvingProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);
  }

  private void setupCbsConfigProperties() {
    cbsConfigProperties.setAckFunctionName(ACK_FUNCTION);
    cbsConfigProperties.setRecomFunctionName(RECOM_FUNCTION);
  }

  private void setupWatchlistLevelSolvingProperties() {
    watchlistLevelSolvingJobProperties = new ScbBridgeWatchlistLevelSolvingJobProperties();
    watchlistLevelSolvingJobProperties.setDbRelationName(DB_RELATION);
    watchlistLevelSolvingJobProperties.setCbsHitsDetailsHelperViewName(CBS_VIEW);
  }
}
