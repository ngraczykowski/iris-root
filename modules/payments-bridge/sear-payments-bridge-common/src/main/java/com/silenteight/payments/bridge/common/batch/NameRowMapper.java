package com.silenteight.payments.bridge.common.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NameRowMapper implements RowMapper<String> {

  @Override
  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getString(1);
  }
}
