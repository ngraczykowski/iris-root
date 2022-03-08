package com.silenteight.payments.bridge.svb.learning.step.composite;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class AlertCompositeReaderFactory {

  private final AlertCompositeFetcher alertCompositeFetcher;
  private final DataSource dataSource;


  public AlertCompositeReader createAlertCompositeReader(
      final String alertsQuery, final long jobId, final int chunkSize
  ) {
    return createAlertCompositeReader(alertsQuery, jobId, chunkSize, "");
  }

  public AlertCompositeReader createAlertCompositeReader(
      final String alertsQuery, final long jobId, final int chunkSize, final String filename
  ) {
    var cursorReader = new BetterJdbcCursorItemReader<Long>();
    cursorReader.setSql(alertsQuery);
    cursorReader.setPreparedStatementSetter(ps -> ps.setLong(1, jobId));
    cursorReader.setRowMapper((rs, rowNum) -> rs.getLong(1));
    cursorReader.setDataSource(dataSource);

    this.alertCompositeFetcher.setFileName(filename);
    return new AlertCompositeReader(alertCompositeFetcher, cursorReader, chunkSize);
  }
}
