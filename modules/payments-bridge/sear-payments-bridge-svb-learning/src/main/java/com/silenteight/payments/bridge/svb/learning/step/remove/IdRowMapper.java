package com.silenteight.payments.bridge.svb.learning.step.remove;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class IdRowMapper implements RowMapper<Long> {

  @Override
  public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getLong(1);
  }
}
