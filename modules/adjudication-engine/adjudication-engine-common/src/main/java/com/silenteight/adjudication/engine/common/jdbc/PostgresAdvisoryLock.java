package com.silenteight.adjudication.engine.common.jdbc;

import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.empty;

@RequiredArgsConstructor
public final class PostgresAdvisoryLock {

  private static final String SQL_TRY_LOCK = "SELECT pg_try_advisory_lock(?)";
  private static final String SQL_UNLOCK = "SELECT pg_advisory_unlock(?)";

  private final Connection con;
  private final long key;

  private boolean locked;

  /**
   * @see "<a href='https://www.postgresql.org/docs/10/functions-admin.html#FUNCTIONS-ADVISORY-LOCKS'>
   *     <code>pg_try_advisory_lock</code></a> (Advisory Lock Functions)"
   */
  public Optional<AcquiredLock> acquire() throws SQLException {
    try (PreparedStatement stmt = con.prepareStatement(SQL_TRY_LOCK)) {
      stmt.setLong(1, key);

      locked = Boolean.TRUE.equals(getBooleanResult(stmt));

      return locked ? Optional.of(new AcquiredLock()) : empty();
    }
  }

  /**
   * @see "<a href='https://www.postgresql.org/docs/10/functions-admin.html#FUNCTIONS-ADVISORY-LOCKS'>
   *     <code>pg_try_advisory_lock</code></a> (Advisory Lock Functions)"
   */
  public void release() throws SQLException {
    try (PreparedStatement stmt = con.prepareStatement(SQL_UNLOCK)) {
      stmt.setLong(1, key);

      Boolean unlocked = getBooleanResult(stmt);
      if (!Boolean.TRUE.equals(unlocked)) {
        throw new LockException("pg_advisory_unlock() returned " + unlocked);
      }

      locked = false;
    }
  }

  private static Boolean getBooleanResult(PreparedStatement stmt) throws SQLException {
    try (ResultSet rs = stmt.executeQuery()) {
      rs.next();
      return (Boolean) rs.getObject(1);
    }
  }

  public final class AcquiredLock implements AutoCloseable {

    private AcquiredLock() {
    }

    @Override
    public void close() throws SQLException {
      release();
    }
  }


  private static final class LockException extends RuntimeException {

    private static final long serialVersionUID = 5278499553118533562L;

    LockException(String message) {
      super(message);
    }
  }
}
