package com.silenteight.payments.bridge.svb.learning.step.composite;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class AlertCompositeReaderFactory {

  private final AlertCompositeFetcher alertCompositeFetcher;
  private final DataSource dataSource;

  public AlertCompositeReader createAlertCompositeReader(
      String alertsQuery, String fileName, int chunkSize) {
    var cursorReader = new BetterJdbcCursorItemReader<Long>();
    cursorReader.setSql(alertsQuery);
    if (StringUtils.isNotBlank(fileName)) {
      cursorReader.setPreparedStatementSetter(ps -> ps.setString(1, fileName));
    }
    cursorReader.setRowMapper((rs, rowNum) -> rs.getLong(1));
    cursorReader.setDataSource(dataSource);

    return new AlertCompositeReader(alertCompositeFetcher, cursorReader, chunkSize);
  }
}
