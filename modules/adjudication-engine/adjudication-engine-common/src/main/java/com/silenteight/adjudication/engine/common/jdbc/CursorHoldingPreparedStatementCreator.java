package com.silenteight.adjudication.engine.common.jdbc;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

@RequiredArgsConstructor
@Builder
class CursorHoldingPreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

  private final String sql;

  @Nonnull
  @Override
  public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
    return con.prepareStatement(sql, TYPE_FORWARD_ONLY, CONCUR_READ_ONLY, HOLD_CURSORS_OVER_COMMIT);
  }

  @Override
  public String getSql() {
    return sql;
  }
}
