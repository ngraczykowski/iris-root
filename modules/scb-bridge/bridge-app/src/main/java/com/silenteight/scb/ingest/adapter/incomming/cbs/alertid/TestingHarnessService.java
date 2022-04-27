package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@AllArgsConstructor
@Slf4j
public class TestingHarnessService {

  private static final String QUERY_PREFIX = "SELECT SYSTEM_ID, BATCH_ID FROM ";

  private final AlertIdPublisher alertIdPublisher;
  private final JdbcTemplate jdbcTemplate;

  public void queueAlert(AlertIdContext context, String systemId) {
    log.info("Queueing alert: {} with context: {}", systemId, context);

    var query = prepareQuery(context.getRecordsView()) + " WHERE SYSTEM_ID = ?";

    var result = jdbcTemplate.query(
        query,
        ps -> ps.setString(1, systemId),
        new AlertIdRowMapper());

    log.info("Result: {}", result);

    var processor = new CbsAlertIdProcessor(context, 1024, alertIdPublisher);
    result.forEach(processor::process);
    processor.processRemaining();
  }

  private static String prepareQuery(String viewName) {
    return QUERY_PREFIX + viewName;
  }

  private static class AlertIdRowMapper implements RowMapper<AlertId> {

    @Override
    public AlertId mapRow(ResultSet resultSet, int rowNum) throws SQLException {
      return new AlertId(
          resultSet.getString("SYSTEM_ID"),
          resultSet.getString("BATCH_ID"));
    }
  }
}
